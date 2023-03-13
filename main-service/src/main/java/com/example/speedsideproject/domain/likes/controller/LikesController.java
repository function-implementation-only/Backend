package com.example.speedsideproject.domain.likes.controller;


import com.example.speedsideproject.domain.likes.service.LikesService;
import com.example.speedsideproject.global.dto.ResponseDto;
import com.example.speedsideproject.global.security.user.UserDetailsImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RequestMapping
@RestController
public class LikesController {

    private final LikesService likesService;

    @Autowired
    LikesController(LikesService likesService) {
        this.likesService = likesService;
    }

    // 좋아요 등록
    @ApiOperation(value = "북마크 등록/취소", notes = "북마크 등록/취소, 단일 api로 구성하였습니다")
    @DeleteMapping("/likes/{postId}")
    public ResponseDto<?> createLikes(
            @AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails,
            @PathVariable Long postId) {
        return ResponseDto.success(likesService.createLikes(userDetails.getAccount(), postId));
    }
}
