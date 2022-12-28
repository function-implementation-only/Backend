package com.example.speedsideproject.quarydsl.post;


import com.example.speedsideproject.post.Post;
import com.example.speedsideproject.post.PostResponseDto;
import com.example.speedsideproject.post.QPost;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.List;



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
    public Page<PostResponseDto> findAllMyPost(Pageable pageable) {
        //Qclass 인스턴스
        QPost qPost = QPost.post;


        JPAQuery<PostResponseDto> query = queryFactory
                .select(new QPostResponseDto(post))
                .from(post)
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
        System.out.println("================================================");
        System.out.println(countQuery);
        return PageableExecutionUtils.getPage(list, pageable, countQuery::fetchOne);
    }

    //전체글 + 검색 + 정렬
    @Override
    public Page<PostResponseDto> findAllMyPostWithSearch(Pageable pageable, String searchReceived) {

        QPost qPost = post;

        JPAQuery<PostResponseDto> query = queryFactory
                .select(new QPostResponseDto(post))
                .from(post)
                .where(post.title.contains(searchReceived).or(post.content.contains(searchReceived)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());


        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post)
                .where(post.title.contains(searchReceived).or(post.content.contains(searchReceived)));

        // sorting
        Sort sort = pageable.getSort();
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(post.getType(), post.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }


        List<PostResponseDto> list = query.fetch();
        System.out.println("================================================");
        System.out.println(countQuery);
        return PageableExecutionUtils.getPage(list, pageable, countQuery::fetchOne);
    }

    //카테고리 + 정렬
    @Override
    public Page<?> findAllPostWithCategory(Pageable pageable, String categoryReceived) {
        JPAQuery<PostResponseDto> query = queryFactory.
                select(new QPostResponseDto(post))
                .from(post)
                .where(post.category.eq(categoryReceived))
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
                .where(post.category.eq(categoryReceived));
        System.out.println("================================================");
        System.out.println(countQuery);
        return PageableExecutionUtils.getPage(list, pageable, countQuery::fetchOne);
    }

    //카테고리 + 검색 + 정렬
    @Override
    public Page<?> findAllPostWithCategoryWithSearch(Pageable pageable, String categoryReceived, String searchReceived) {

        JPAQuery<PostResponseDto> query = queryFactory
                .select(new QPostResponseDto(post))
                .from(post)
                .where((post.category.eq(categoryReceived).and(post.title.contains(searchReceived)))
                        .or(post.category.eq(categoryReceived).and(post.content.contains(searchReceived)))
                )
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
                .where((post.category.eq(categoryReceived).and(post.title.contains(searchReceived)))
                        .or(post.category.eq(categoryReceived).and(post.content.contains(searchReceived)))
                );
        System.out.println("================================================");
        System.out.println(countQuery);
        return PageableExecutionUtils.getPage(list, pageable, countQuery::fetchOne);
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
}
