package com.example.speedsideproject.post;

import com.example.speedsideproject.global.dto.ResponseDto;
import com.example.speedsideproject.post.enums.Tech;
import com.example.speedsideproject.security.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.util.List;

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
                                     @RequestPart(name = "image", required = false) List<MultipartFile> imgFiles,
                                     @RequestPart(name = "techList", required = false) List<Tech> techList,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        //List<Tech> techList 사용
//        System.out.println("=============================================");
//        System.out.println(techList.get(0));
//        System.out.println(techList.get(1));
        return ResponseDto.success(postService.createPost(postRequestDto, imgFiles, techList, userDetails.getAccount()));
    }

    //글 수정
    @PutMapping(name = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseDto<?> updatePost(@RequestPart(name = "data", required = false) PostRequestDto postRequestDto,
                                     @RequestPart(name = "image", required = false) List<MultipartFile> imgFiles,
                                     @PathVariable Long id,
                                     @RequestPart(name = "techList", required = false) List<Tech> techList,
                                     @AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails) throws IOException{
        return ResponseDto.success(postService.updatePost(postRequestDto, imgFiles, techList, id, userDetails.getAccount()));
    }

    //글 삭제
    @DeleteMapping("/{id}")
    public ResponseDto<?> deletePost(@PathVariable Long id,
                                     @AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails) {
        return ResponseDto.success(postService.deletePost(id, userDetails.getAccount()));
    }

    //글 1개 읽기
    @GetMapping("/{id}")
    public ResponseDto<?> getOnePost(@PathVariable Long id) {
        return ResponseDto.success(postService.getOnePost(id));
    }
}
