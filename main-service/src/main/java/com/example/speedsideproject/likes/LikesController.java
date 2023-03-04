package com.example.speedsideproject.likes;


import com.example.speedsideproject.global.dto.ResponseDto;
import com.example.speedsideproject.security.user.UserDetailsImpl;
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
    @DeleteMapping("/likes/{postId}")
    public ResponseDto<?> createLikes(
            @AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails,
            @PathVariable Long postId) {
        return ResponseDto.success(likesService.createLikes(userDetails.getAccount(), postId));
    }
}
