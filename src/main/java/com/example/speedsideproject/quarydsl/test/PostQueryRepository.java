//package com.example.speedsideproject.quarydsl.test;
//
//import com.example.speedsideproject.post.Post;
//import com.example.speedsideproject.post.QPost;
//import com.example.speedsideproject.post.QTechs;
//import com.example.speedsideproject.post.enums.Tech;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Repository;
//
//import javax.persistence.EntityManager;
//import java.util.List;
//import java.util.Set;
//
//import static com.example.speedsideproject.post.QPost.post;
//import static com.example.speedsideproject.post.QTechs.techs;
//
//@Repository
//@Component
//public class PostQueryRepository {
//    private final JPAQueryFactory queryFactory;
//
//    public PostQueryRepository(EntityManager em) {
//        this.queryFactory = new JPAQueryFactory(em);
//    }
//
//    public List<?> testQuery1(long size, long offset){
//        QPost qPost= post;
//        QTechs qTechs= techs;
//
//       return queryFactory
//                .select(post)
//                .from(post)
//                .join(post.techs, techs)
//                .where(techs.tech.in(Tech.JAVA, Tech.REACT))
//                .distinct()
//                .offset(offset)
//                .limit(size)
//                .fetch();
//
//    }
//}
