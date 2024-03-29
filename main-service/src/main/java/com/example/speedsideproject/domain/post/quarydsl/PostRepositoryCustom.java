package com.example.speedsideproject.domain.post.quarydsl;


import com.example.speedsideproject.domain.account.entity.Account;
import com.example.speedsideproject.domain.post.dto.PostResponseDto;
import com.example.speedsideproject.domain.post.entity.Post;
import com.example.speedsideproject.domain.post.dto.PostDetailResponseDto;
import com.example.speedsideproject.domain.post.enums.Category;
import com.example.speedsideproject.domain.post.enums.Place;
import com.example.speedsideproject.domain.post.enums.Tech;
import com.example.speedsideproject.global.security.user.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {

    PostDetailResponseDto findOnePostWithOneQuery(Long id, UserDetailsImpl userDetails);

    //mypage에서 내가 like 한 postList  불러오기
    List<Post> findTop5ByMyLikes(Account account);

    //mypage에서 내가 지원 한 postList  불러오기
    List<?> findTop5ByMyApplyment(UserDetailsImpl userDetails);
    //post get all
    Page<?> findAllPost(Pageable pageable);
    Page<?> findAllPostWithCategory(Pageable pageable, String sort, List<Tech> techList, Category category, Place place, UserDetailsImpl userDetails);

    /*post 1개 get 한방쿼리*/
//    PostDetailResponseDto findOnePostWithQuery(Long id, UserDetailsImpl userDetails);
}
