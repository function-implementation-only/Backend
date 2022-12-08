package com.example.speedsideproject.quarydsl;

import com.example.speedsideproject.post.PostRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<PostRequestDto> findAllMyPost(Pageable pageable);
}
