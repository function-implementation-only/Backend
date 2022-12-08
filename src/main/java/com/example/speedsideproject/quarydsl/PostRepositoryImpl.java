package com.example.speedsideproject.quarydsl;

import com.example.speedsideproject.post.PostResponseDto;
import com.example.speedsideproject.post.QPost;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import java.util.List;

import static com.example.speedsideproject.post.QPost.post;

@Component
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    public PostRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }
    @Override
    public Page<PostResponseDto> findAllMyPost(Pageable pageable){
        QPost qPost = post;
        JPAQuery<PostResponseDto> query = queryFactory
                .select(new QPostResponseDto(post))
                .from(post)
                .offset(pageable.getOffset())
                .limit(pageable.getOffset());
        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post);
        List<PostResponseDto> list = query.fetch();
        return PageableExecutionUtils.getPage(list, pageable,countQuery::fetchOne);
    }
}
