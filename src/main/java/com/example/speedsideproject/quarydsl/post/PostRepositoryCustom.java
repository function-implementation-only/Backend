package com.example.speedsideproject.quarydsl.post;


import com.example.speedsideproject.post.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    //mypage에서 내가 like 한 postList  불러오기
//    List<Post> findAllMyLikes(Member member);

    //post get all
    Page<?> findAllMyPost(Pageable pageable);
    // post get all - search
//    Page<PostResponseDto> findAllMyPostWithSearch(Pageable pageable, String string);



    //post - category 정렬 + 내가 한 좋아요
//    Page<?> findAllPostWithCategory(Pageable pageable, String category);

    //post -category - search
//    Page<?> findAllPostWithCategoryWithSearch(Pageable pageable, String category, String search);

}
