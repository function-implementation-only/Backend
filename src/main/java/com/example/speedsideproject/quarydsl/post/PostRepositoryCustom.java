package com.example.speedsideproject.quarydsl.post;


import com.example.speedsideproject.post.enums.Tech;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {

    //mypage에서 내가 like 한 postList  불러오기
//    List<Post> findAllMyLikes(Member member);

    //post get all
    Page<?> findAllPost(Pageable pageable);

    //post get all v2
//    Page<?> findAllMyPost2(Pageable pageable);
    // post get all - search
//    Page<PostResponseDto> findAllMyPostWithSearch(Pageable pageable, String string);


    //    post - category 정렬
    Page<?> findAllPostWithCategory(Pageable pageable, List<Tech> techList);

    //post -category - search
//    Page<?> findAllPostWithCategoryWithSearch(Pageable pageable, String category, String search);

}
