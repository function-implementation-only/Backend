package com.example.speedsideproject.quarydsl.post;


import com.example.speedsideproject.account.entity.QAccount;
import com.example.speedsideproject.post.PostResponseDto;
import com.example.speedsideproject.post.QPost;
import com.example.speedsideproject.post.QPostResponseDto;
import com.example.speedsideproject.post.QTechs;
import com.example.speedsideproject.post.enums.Category;
import com.example.speedsideproject.post.enums.Place;
import com.example.speedsideproject.post.enums.Tech;
import com.querydsl.core.FilteredClause;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.speedsideproject.account.entity.QAccount.account;
import static com.example.speedsideproject.post.QPost.post;
import static com.example.speedsideproject.post.QTechs.techs;


@Slf4j
@Component
//@Primary
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PostRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

//    @Override
//    public List<Post> findAllMyLikes(Member mem) {
//        QPost qPost = post;
//        QLike qLike = like;
//
//        return queryFactory
//                .select(post)
//                .from(post)
//                .join(post.likes, like).on(like.member.eq(mem))
//                .fetch();
//    }

    //전체글 + 정렬
    @Override
    public Page<?> findAllPost(Pageable pageable) {
        //Qclass 인스턴스
        QPost qPost = QPost.post;
        QAccount qAccount = QAccount.account;

        JPAQuery<PostResponseDto> query = queryFactory
                .select(new QPostResponseDto(post))
                .from(post)
                .leftJoin(post.account, account).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());


        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post);


        // sorting
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(post.getType(), post.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<PostResponseDto> list = query.fetch();
        log.info("query 끝");
        return PageableExecutionUtils.getPage(list, pageable, countQuery::fetchOne);
    }


    //전체글 + 정렬 2
//    @Override
//    public Page<?> findAllMyPost2(Pageable pageable) {
//        //Qclass 인스턴스
//        QPost qPost = QPost.post;
//        QAccount qAccount = QAccount.account;
//
//        JPAQuery<PostResponseDto> query = queryFactory
//                .select(new QPostResponseDto(post))
//                .from(post)
//                .leftJoin(post.account, account).fetchJoin()
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize());
//
//
//        JPAQuery<Long> countQuery = queryFactory
//                .select(post.count())
//                .from(post);
//
//
//        // sorting
//        for (Sort.Order o : pageable.getSort()) {
//            PathBuilder pathBuilder = new PathBuilder(post.getType(), post.getMetadata());
//            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
//                    pathBuilder.get(o.getProperty())));
//        }
//
//        List<PostResponseDto> list = query.fetch();
//        log.info("query 끝");
//        return PageableExecutionUtils.getPage(list, pageable, countQuery::fetchOne);
//    }


    //카테고리 + 정렬 + 동적처리
    @Override
    public Page<?> findAllPostWithCategory(Pageable pageable, List<Tech> techList) {


        JPAQuery<PostResponseDto> query = queryFactory.
                select(new QPostResponseDto(post))
                .from(post)
                .leftJoin(post.account, account).fetchJoin()
                .leftJoin(post.techs, techs)
                .where(checkTechList(techList))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());


        // sorting
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(post.getType(), post.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }
        List<PostResponseDto> list = query.fetch();
        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post)
                .where(checkTechList(techList));
        return PageableExecutionUtils.getPage(list, pageable, countQuery::fetchOne);
    }

    //카테고리 + 정렬 + 동적처리 v3
    @Override
    public Page<?> findAllPostWithCategory3(Pageable pageable, List<Tech> techList, Category category, Place place) {


        JPAQuery<PostResponseDto> query = queryFactory.
                select(new QPostResponseDto(post))
                .from(post)
                .leftJoin(post.account, account).fetchJoin()
                .leftJoin(post.techs, techs)
                .where(checkTechList(techList))
                .where(checkCategory(category))
                .where(checkPlace(place))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());


        // sorting
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(post.getType(), post.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }
        List<PostResponseDto> list = query.fetch();
        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post)
                .where(checkTechList(techList));
        return PageableExecutionUtils.getPage(list, pageable, countQuery::fetchOne);
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
        return place == null ? null :post.place.eq(place);
    }


}

//    @Override
//    public Page<?> findAllPostWithCategory(Pageable pageable, String categoryReceived, Member mem) {
//        List<Tuple> list = queryFactory.
//                select(post, like.likeId)
//                .from(post)
//                .where(post.category.eq(categoryReceived))
//                .leftJoin(post.likes, like).on(like.member.eq(mem))
//                .orderBy(post.postId.desc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//
//        //제 1안
////        List<PostResponseDto> collect = list.stream().map(r -> new PostResponseDto(r.get(post)) {
////                    public Long myLong = r.get(like.likeId);
////                }
////        ).collect(Collectors.toList());
//
//        //제 2안
////        List<PostResponseDto> collect = list.stream().map(r -> new PostResponseDto(r.get(post))).collect(Collectors.toList());
////        list.stream().map(r-> {return (r.get(like.likeId)==null) ? false: true;}).collect(Collectors.toList());
//
//        //제 3안
//        List<PostResponseDto> collect = list.stream().map(r -> new PostResponseDto(r.get(post)) {
//            public Boolean myLikeCheck = r.get(like.likeId) != null;
//        }).collect(Collectors.toList());
////        list.stream().map(r-> {return r.get(like.likeId) != null;}).collect(Collectors.toList());
//
//        JPAQuery<Long> countQuery = queryFactory
//                .select(post.count())
//                .from(post);
//
//        return PageableExecutionUtils.getPage(collect, pageable, countQuery::fetchOne);
//    }

