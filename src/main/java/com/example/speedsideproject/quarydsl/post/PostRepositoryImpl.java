package com.example.speedsideproject.quarydsl.post;


import com.example.speedsideproject.account.entity.Account;
import com.example.speedsideproject.account.entity.QAccount;
import com.example.speedsideproject.likes.Likes;
import com.example.speedsideproject.likes.LikesRepository;
import com.example.speedsideproject.likes.QLikes;
import com.example.speedsideproject.post.*;
import com.example.speedsideproject.post.enums.Category;
import com.example.speedsideproject.post.enums.Place;
import com.example.speedsideproject.post.enums.Tech;
import com.example.speedsideproject.security.user.UserDetailsImpl;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanPath;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public Object findAllPostWithCategory8(Pageable pageable, String sort, List<Tech> techList, Category category, Place place, UserDetailsImpl userDetails) {
        log.info("v8시작");


        List<?> list = queryFactory.
                select(new QPostResponseDto3(post, likes.likeCheck))
                .from(post)
                .leftJoin(post.account, account).fetchJoin()
                .where(checkCategory(category), checkPlace(place))
                .leftJoin(post.techs, techs)
                .where(checkTechList(techList))
                .distinct()
                .leftJoin(post.likes, likes)
                .where(usernameEq(userDetails))
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

    //카테고리 + 정렬 + 동적처리 v7
    @Override
    public Page<?> findAllPostWithCategory7(Pageable pageable, String sort, List<Tech> techList, Category category, Place place) {
        log.info("v7시작");

        List<PostResponseDto2> list = queryFactory.
                select(new QPostResponseDto2(post))
                .from(post)
                .leftJoin(post.account, account).fetchJoin()
                .where(checkCategory(category), checkPlace(place))
                .leftJoin(post.techs, techs)
                .where(checkTechList(techList))
                .distinct()
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

    //카테고리 + 정렬 + 동적처리 v6
    @Override
    public List<?> findAllPostWithCategory6(String sort, Long size, Long page, List<Tech> techList, Category category, Place place) {
        log.info("v6시작");

        List<PostResponseDto2> fetch = queryFactory.
                select(new QPostResponseDto2(post))
                .from(post)
                .leftJoin(post.account, account).fetchJoin()
                .where(checkCategory(category), checkPlace(place))
                .leftJoin(post.techs, techs)
                .where(checkTechList(techList))
                .distinct()
                .offset(getPage(page))
                .limit(getSize(size))
                .orderBy(getSort(sort))
                .fetch();
        /*count query*/
        List<Long> count = queryFactory
                .select(post.count())
                .from(post)
                .where(checkCategory(category), checkPlace(place))
                .leftJoin(post.techs, techs)
                .where(checkTechList(techList))
                .distinct()
                .fetch();


        Map<List<?>, List<?>> map = new HashMap<>();
        map.put(count, fetch);
//        System.out.println(map);

        return fetch;
    }

    //동적관리 method
    /*getSort*/
    private OrderSpecifier<Long> getSort(String sort) {
        return sort == null ? post.id.desc() : getSortValue(sort);
    }

    /*check userDetail*/
    private BooleanExpression usernameEq(UserDetailsImpl userDetails) {
        return userDetails == null ? null : likes.account.id.eq(userDetails.getAccount().getId());
    }

    /*minor method*/
    private OrderSpecifier<Long> getSortValue(String sort) {
        if (sort.equals("postId")) {
            return post.id.desc();
        } else if (sort.equals("likesLength")) {
            return post.likesLength.desc();
        } else return null;
    }

    /*getPage*/
    private Long getPage(Long page) {
        return page == null ? 0 : page;
    }

    /*get size*/
    private Long getSize(Long size) {
        return size == null ? 100 : size;
    }

    //카테고리 + 정렬 + 동적처리 v5
    @Override
    public Page<?> findAllPostWithCategory5(Pageable pageable, List<Tech> techList, Category category, Place place) {


        JPAQuery<PostResponseDto2> query = queryFactory.
                select(new QPostResponseDto2(post))
                .from(post)
                .leftJoin(post.account, account).fetchJoin()
                .where(checkCategory(category), checkPlace(place))
                .leftJoin(post.techs, techs)
                .where(checkTechList(techList))
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        // sorting
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(post.getType(), post.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<PostResponseDto2> list
                = query.fetch();
        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post)
                .where(checkCategory(category), checkPlace(place))
                .leftJoin(post.techs, techs)
                .where(checkTechList(techList))
                .distinct();
        return PageableExecutionUtils.getPage(list, pageable, countQuery::fetchOne);

    }

    //카테고리 + 정렬 + 동적처리 v4
    @Override
    public List<?> findAllPostWithCategory4(Long offset, Long size, List<Tech> techList, Category category, Place place) {


        List<PostResponseDto2> list = queryFactory.
                select(new QPostResponseDto2(post))
                .from(post)
                .leftJoin(post.account, account).fetchJoin()
                .where(checkCategory(category), checkPlace(place))
                .leftJoin(post.techs, techs)
                .where(checkTechList(techList))
                .distinct()
                .offset(offset)
                .limit(size)
                .fetch();

        // sorting
//        for (Sort.Order o : pageable.getSort()) {
//            PathBuilder pathBuilder = new PathBuilder(post.getType(), post.getMetadata());
//            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
//                    pathBuilder.get(o.getProperty())));
//        }
//        List<PostResponseDto> list = query.fetch();
//        JPAQuery<Long> countQuery = queryFactory
//                .select(post.count())
//                .from(post)
//                .where(checkTechList(techList));
//        return PageableExecutionUtils.getPage(list, pageable, countQuery::fetchOne);
        return list;
    }

    //    //카테고리 + 정렬 + 동적처리 v3
    @Override
    public Page<?> findAllPostWithCategory3(Pageable pageable, List<Tech> techList, Category category, Place place) {

        if (techList != null) {

            JPAQuery<PostResponseDto2> query = queryFactory.
                    select(new QPostResponseDto2(post))
                    .from(post)
                    .leftJoin(post.account, account).fetchJoin()
                    .where(checkCategory(category), checkPlace(place))
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
            List<PostResponseDto2> list = query.fetch();
            JPAQuery<Long> countQuery = queryFactory
                    .select(post.count())
                    .from(post)
                    .where(checkTechList(techList));
            return PageableExecutionUtils.getPage(list, pageable, countQuery::fetchOne);
        } else {
            JPAQuery<PostResponseDto2> query = queryFactory.
                    select(new QPostResponseDto2(post))
                    .from(post)
                    .leftJoin(post.account, account).fetchJoin()
                    .where(checkCategory(category), checkPlace(place))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize());

            // sorting
            for (Sort.Order o : pageable.getSort()) {
                PathBuilder pathBuilder = new PathBuilder(post.getType(), post.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }
            List<PostResponseDto2> list = query.fetch();
            JPAQuery<Long> countQuery = queryFactory
                    .select(post.count())
                    .from(post)
                    .where(checkTechList(techList));
            return PageableExecutionUtils.getPage(list, pageable, countQuery::fetchOne);
        }
    }

//}
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


        JPAQuery<PostResponseDto2> query = queryFactory.
                select(new QPostResponseDto2(post))
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
        List<PostResponseDto2> list = query.fetch();
        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post)
                .where(checkTechList(techList));
        return PageableExecutionUtils.getPage(list, pageable, countQuery::fetchOne);
    }

    //전체글 + 정렬
    @Override
    public Page<?> findAllPost(Pageable pageable) {
        //Qclass 인스턴스
        QPost qPost = QPost.post;
        QAccount qAccount = QAccount.account;

        JPAQuery<PostResponseDto2> query = queryFactory
                .select(new QPostResponseDto2(post))
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

        List<PostResponseDto2> list = query.fetch();
        log.info("query 끝");
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
        return place == null ? null : post.place.eq(place);
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

