package com.example.speedsideproject.post;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.speedsideproject.post.QPost.post;


@Repository
public class PostQueryRepository {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public PostQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    //findAllMyPostWithQuery
    public List<Post> findAllMyPostWithQuery() {
        QPost qPost = post;

        return queryFactory
                .select(post)
                .from(post)
                .fetch();
    }
}
