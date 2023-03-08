package com.example.speedsideproject.quarydsl.post;


import com.example.speedsideproject.account.entity.Account;
import com.example.speedsideproject.likes.LikesRepository;
import com.example.speedsideproject.likes.QLikes;
import com.example.speedsideproject.post.*;
import com.example.speedsideproject.post.enums.Category;
import com.example.speedsideproject.post.enums.Place;
import com.example.speedsideproject.post.enums.Tech;
import com.example.speedsideproject.security.user.UserDetailsImpl;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.speedsideproject.account.entity.QAccount.account;
import static com.example.speedsideproject.likes.QLikes.likes;
import static com.example.speedsideproject.post.QPost.post;
import static com.example.speedsideproject.post.QTechs.techs;


@Slf4j
@Component
//@Primary
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final LikesRepository likesRepository;

    @Autowired
    public PostRepositoryImpl(EntityManager em, LikesRepository likesRepository) {
        this.queryFactory = new JPAQueryFactory(em);
        this.likesRepository = likesRepository;
    }

    @Override
    public List<Post> findTop5ByMyLikes(Account account) {
        QPost qPost = post;
        QLikes qLike = likes;

        return queryFactory
                .select(post)
                .from(post)
                .join(post.likes, likes).on(likes.account.eq(account))
                .orderBy(post.id.desc())
                .limit(5)
                .fetch();
    }
    //카테고리 + 정렬 + 동적처리 v8
    @Override
    public Page<?> findAllPostWithCategory(Pageable pageable, String sort, List<Tech> techList, Category category, Place place, UserDetailsImpl userDetails) {
//        log.info("v8시작");
        /*main query*/
        List<?> list = queryFactory.
                select(new QPostQueryResponseDto(post, likes.likeCheck))
                .from(post)
                .leftJoin(post.account, account).fetchJoin()
                .where(checkCategory(category), checkPlace(place))
                .leftJoin(post.techs, techs)
                .where(checkTechList(techList))
                .distinct()
                .leftJoin(post.likes, likes).on(usernameEq(userDetails))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getSort(sort))
                .fetch();

        /*count query*/
        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post)
                .where(checkCategory(category), checkPlace(place))
                .leftJoin(post.techs, techs)
                .where(checkTechList(techList))
                .distinct();

        return PageableExecutionUtils.getPage(list, pageable, countQuery::fetchOne);
    }

    public Page<?> findAllPost(Pageable pageable) {
        QPost qPost = QPost.post;
        /*main-query*/
        JPAQuery<PostResponseDto> query = queryFactory
                .select(new QPostResponseDto(post))
                .from(post)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        /*정렬*/
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(post.getType(), post.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }
        List<PostResponseDto> fetch = query.fetch();
        /*count-query*/
        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post);
        return PageableExecutionUtils.getPage(fetch, pageable, countQuery::fetchOne);
    }

    //동적관리 method
    /*getSort*/
    private OrderSpecifier<Long> getSort(String sort) {
        return sort == null ? post.id.desc() : getSortValue(sort);
    }

    /*check userDetail*/
    private BooleanExpression usernameEq(UserDetailsImpl userDetails) {
        return userDetails == null ? likes.account.id.eq(-1L) : likes.account.id.eq(userDetails.getAccount().getId());
    }

    /*minor method*/
    private OrderSpecifier<Long> getSortValue(String sort) {
        if (sort.equals("postId")) {
            return post.id.desc();
        } else if (sort.equals("likesLength")) {
            return post.likesLength.desc();
        } else return null;
    }

    //동적 관리
    /*techList in query where*/
    private BooleanExpression checkTechList(List<Tech> techList) {
        return techList == null ? null : techs.tech.in(techList);
    }

    /*category in query where*/
    private BooleanExpression checkCategory(Category category) {
        return category == null ? null : post.category.eq(category);
    }

    /*place in query where*/
    private BooleanExpression checkPlace(Place place) {
        return place == null ? null : post.place.eq(place);
    }
}

