package com.example.speedsideproject.quarydsl.post;


import com.example.speedsideproject.account.entity.Account;
import com.example.speedsideproject.post.Post;
import com.example.speedsideproject.post.enums.Category;
import com.example.speedsideproject.post.enums.Place;
import com.example.speedsideproject.post.enums.Tech;
import com.example.speedsideproject.security.user.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {

    //mypage에서 내가 like 한 postList  불러오기
    List<Post> findTop5ByMyLikes(Account account);

    //post get all
    Page<?> findAllPost(Pageable pageable);
    Page<?> findAllPostWithCategory(Pageable pageable, String sort, List<Tech> techList, Category category, Place place, UserDetailsImpl userDetails);

}
