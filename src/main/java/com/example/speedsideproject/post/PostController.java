package com.example.speedsideproject.post;

import com.example.speedsideproject.global.dto.ResponseDto;
import com.example.speedsideproject.security.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    //모든 글 읽어 오기
    @GetMapping("/all")
    public ResponseDto<?> getAllPost() {
        return ResponseDto.success(postService.getAllpost());
    }

    //글쓰기 + img 업로드
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseDto<?> createPost(@RequestPart(name = "data", required = false) PostRequestDto postRequestDto,
                                     @RequestPart(name = "image", required = false) MultipartFile imgFile,
                                     @AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails) throws IOException {
        return ResponseDto.success(postService.createPost(postRequestDto, imgFile, userDetails.getAccount()));
    }

    //글 수정
    @PutMapping(name = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseDto<?> updatePost(@RequestPart(name = "data", required = false) PostRequestDto postRequestDto,
                                     @RequestPart(name = "image", required = false) MultipartFile imgFile,
                                     @PathVariable Long id,
                                     @AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails) throws IOException{
        return ResponseDto.success(postService.updatePost(postRequestDto, imgFile, id, userDetails.getAccount()));
    }

    //글 삭제
    @DeleteMapping("{id}")
    public ResponseDto<?> deletePost(@PathVariable Long id, @AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails) {
        return ResponseDto.success(postService.deletePost(id, userDetails.getAccount()));
    }

    //글 1개 읽기
    @GetMapping("/{id}")
    public ResponseDto<?> getOnePost(@AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails) {
        return ResponseDto.success(postService.getOnePost(userDetails.getAccount()));
    }
}
