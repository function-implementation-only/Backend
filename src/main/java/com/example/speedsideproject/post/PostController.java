package com.example.speedsideproject.post;

import com.example.speedsideproject.global.dto.ResponseDto;
import com.example.speedsideproject.post.enums.Category;
import com.example.speedsideproject.post.enums.Place;
import com.example.speedsideproject.post.enums.Tech;
import com.example.speedsideproject.security.user.UserDetailsImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
    @ApiOperation(value = "전체 게시글 조회", notes = "예시 page,size,sort기능 [api/posts/all?page=0&size=3&sort=postId,DESC]")
    @GetMapping("/all")
    public ResponseDto<?> getAllPost(Pageable pageable) {
        return ResponseDto.success(postService.getAllPost(pageable));
    }

    //카테고리별 읽어오기
    @ApiOperation(value = "카테고리별 게시글 조회", notes = "예시 page,size,sort기능 [api/posts/v2/all?page=0&size=3&sort=postId,DESC]")
    @GetMapping("/v2/all")
    public ResponseDto<?> getAllPostWithCategory(Pageable pageable, @RequestParam(name = "techList", required = false) List<Tech> techList) {
        return ResponseDto.success(postService.getAllPostWithCategory(pageable, techList));
    }

    //카테고리별 읽어오기
    @ApiOperation(value = "카테고리별 게시글 조회", notes = "예시 page,size,sort기능 [api/posts/v2/all?page=0&size=3&sort=postId,DESC]")
    @GetMapping("/v3/all")
    public ResponseDto<?> getAllPostWithCategory3(Pageable pageable,
                                                  @RequestParam(name = "techList", required = false) List<Tech> techList,
                                                  @RequestParam(name ="category", required = false) Category category,
                                                  @RequestParam(name = "place", required = false)Place place) {

        //체크한 techList 체크
        System.out.println(techList);
        System.out.println(category);
        System.out.println(place);

        return ResponseDto.success(postService.getAllPostWithCategory3(pageable, techList, category,place));
    }


    //글쓰기 + img 업로드
    @ApiOperation(value = "게시글 작성", notes = "게시글을 작성합니다.(토큰필요)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "category", value = "모집구분(STUDY/PROJECT)"),
            @ApiImplicitParam(name = "Duration", value = "예상기간(UNDEFINED/ONE/TWO/THREE/FOUR/FIVE/SIX)"),
            @ApiImplicitParam(name = "Place", value = "진행방식(ONLINE/OFFLINE)"),
            @ApiImplicitParam(name = "Tech", value = "사용언어(...)")
    })
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseDto<?> createPost(@RequestPart(name = "data", required = false) PostRequestDto postRequestDto,
                                     @RequestPart(name = "image", required = false) List<MultipartFile> imgFiles,
                                     @RequestPart(name = "techList", required = false) List<Tech> techList,
                                     @AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails) throws IOException {
        //List<Tech> techList 사용
//        System.out.println("=============================================");
//        System.out.println(techList.get(0));
//        System.out.println(techList.get(1));
        return ResponseDto.success(postService.createPost(postRequestDto, imgFiles, techList, userDetails.getAccount()));
    }

    //글 수정
    @ApiOperation(value = "게시글 수정", notes = "자신의 글을 수정합니다.(토큰필요)")
    @PatchMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseDto<?> updatePost(@RequestPart(name = "data", required = false) PostRequestDto postRequestDto,
                                     @RequestPart(name = "image", required = false) List<MultipartFile> imgFiles,
                                     @PathVariable Long id,
                                     @RequestPart(name = "techList", required = false) List<Tech> techList,
                                     @AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails) throws IOException {
        return ResponseDto.success(postService.updatePost(postRequestDto, imgFiles, techList, id, userDetails.getAccount()));
    }

    //글 삭제
    @ApiOperation(value = "게시글 삭제", notes = "자신의 글을 삭제합니다.(토큰필요)")
    @DeleteMapping("/{id}")
    public ResponseDto<?> deletePost(@PathVariable Long id,
                                     @AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails) {
        return ResponseDto.success(postService.deletePost(id, userDetails.getAccount()));
    }

    //글 1개 읽기
    @ApiOperation(value = "게시글 조회", notes = "BD에 저장된 하나의 게시글을 조회합니다")
    @GetMapping("/{id}")
    public ResponseDto<?> getOnePost(@PathVariable Long id,
                                     @AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails) {


        return ResponseDto.success(postService.getOnePost(id, userDetails));
    }
}
