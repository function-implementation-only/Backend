package com.example.speedsideproject.post;

import com.example.speedsideproject.error.CustomException;
import com.example.speedsideproject.global.dto.ResponseDto;
import com.example.speedsideproject.post.enums.Category;
import com.example.speedsideproject.post.enums.Place;
import com.example.speedsideproject.post.enums.Tech;
import com.example.speedsideproject.security.user.UserDetailsImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/posts")
public class PostController {
    private final PostService postService;

    //모든 글 읽어 오기
    @ApiOperation(value = "전체 게시글 조회", notes = "예시 page,size,sort기능 [api/posts/all?page=0&size=3&sort=postId,DESC]")
    @GetMapping("/all")
    public ResponseDto<?> getAllPost(Pageable pageable) {
        return ResponseDto.success(postService.getAllPost(pageable));
    }

    /*v2 카테고리별 읽어오기*/
    @ApiOperation(value = "카테고리별 게시글 조회", notes = "예시 page,size,sort기능 [api/posts/v2/all?page=0&size=3&sort=postId,DESC]")
    @GetMapping("/v2/all")
    public ResponseDto<?> getAllPostWithCategory(Pageable pageable, @RequestParam(name = "techList", required = false) List<Tech> techList) {
        return ResponseDto.success(postService.getAllPostWithCategory(pageable, techList));
    }

    /*v3 카테고리별 읽어오기*/
    @ApiOperation(value = "카테고리별 게시글 조회", notes = "예시 page,size,sort기능 [api/posts/v3/all?page=0&size=3&sort=postId,DESC]")
    @GetMapping("/v3/all")
    public ResponseDto<?> getAllPostWithCategory3(Pageable pageable,
                                                  @RequestParam(name = "techList", required = false) List<Tech> techList,
                                                  @RequestParam(name = "category", required = false) Category category,
                                                  @RequestParam(name = "place", required = false) Place place) {

        //체크한 techList 체크
        System.out.println(techList);
        System.out.println(category);
        System.out.println(place);

        return ResponseDto.success(postService.getAllPostWithCategory3(pageable, techList, category, place));
    }

    /*v4 카테고리별 읽어오기*/
    @ApiOperation(value = "카테고리별 게시글 조회", notes = "예시 page,size,sort기능 [api/posts/v4/all?page=0&size=3&sort=postId,DESC]")
    @GetMapping("/v4/all")
    public ResponseDto<?> getAllPostWithCategory4(Long offset, Long size,
                                                  @RequestParam(name = "techList", required = false) List<Tech> techList,
                                                  @RequestParam(name = "category", required = false) Category category,
                                                  @RequestParam(name = "place", required = false) Place place) {

        //체크한 techList 체크
        System.out.println(techList);
        System.out.println(category);
        System.out.println(place);

        return ResponseDto.success(postService.getAllPostWithCategory4(offset, size, techList, category, place));
    }

    /*v5 카테고리별 읽어오기*/
    @ApiOperation(value = "카테고리별 게시글 조회", notes = "예시 page,size,sort기능 [api/posts/v5/all?page=0&size=3&sort=postId,DESC]")
    @GetMapping("/v5/all")
    public ResponseDto<?> getAllPostWithCategory5(Pageable pageable,
                                                  @RequestParam(name = "techList", required = false) List<Tech> techList,
                                                  @RequestParam(name = "category", required = false) Category category,
                                                  @RequestParam(name = "place", required = false) Place place) {

        //체크한 techList 체크
        System.out.println(techList);
        System.out.println(category);
        System.out.println(place);

        return ResponseDto.success(postService.getAllPostWithCategory5(pageable, techList, category, place));
    }

    /*v6 카테고리별 읽어오기*/
    @ApiOperation(value = "카테고리별 게시글 조회", notes = "예시 page,size,sort기능 [api/posts/v6/all?page=0&size=3&sort=postId,DESC]")
    @GetMapping("/v6/all")
    public ResponseDto<?> getAllPostWithCategory6(@RequestParam(name = "sort", required = false) String sort,
                                                  @RequestParam(name = "size", required = false) Long size,
                                                  @RequestParam(name = "page", required = false) Long page,
                                                  @RequestParam(name = "techList", required = false) List<Tech> techList,
                                                  @RequestParam(name = "category", required = false) Category category,
                                                  @RequestParam(name = "place", required = false) Place place){

        //params 체크
        log.info("sort: {}", sort);
        log.info("size: {}", size);
        log.info("page: {}", page);
        log.info("techList: {}", techList);
        log.info("category: {}", category);
        log.info("place: {}", place);

        return ResponseDto.success(postService.getAllPostWithCategory6(sort, size, page, techList, category, place));
    }

    /*v7 카테고리별 읽어오기*/
    @ApiOperation(value = "카테고리별 게시글 조회", notes = "예시 page,size,sort기능 [api/posts/v6/all?page=0&size=3&sort=postId,DESC]")
    @GetMapping("/v7/all")
    public ResponseDto<?> getAllPostWithCategory7(Pageable pageable,
                                                  @RequestParam(name = "sort", required = false) String sort,
                                                  @RequestParam(name = "techList", required = false) List<Tech> techList,
                                                  @RequestParam(name = "category", required = false) Category category,
                                                  @RequestParam(name = "place", required = false) Place place) throws CustomException {

        //params 체크
        log.info("sort: {}", sort);
        log.info("techList: {}", techList);
        log.info("category: {}", category);
        log.info("place: {}", place);

        return ResponseDto.success(postService.getAllPostWithCategory7(pageable, sort, techList, category, place));
    }


    //글쓰기 v2
    @ApiOperation(value = "게시글 작성", notes = "게시글을 작성합니다.(토큰필요)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "category", value = "모집구분(STUDY/PROJECT)"),
            @ApiImplicitParam(name = "Duration", value = "예상기간(Long 타입)"),
            @ApiImplicitParam(name = "Place", value = "진행방식(ONLINE/OFFLINE)"),
            @ApiImplicitParam(name = "Tech", value = "사용언어(...)")
    })
    @PostMapping(value = "/v2",
            produces = APPLICATION_JSON_VALUE)
    public ResponseDto<?> createPost2(@RequestPart(name = "data", required = false) PostRequestDto2 postRequestDto2,
                                      @RequestPart(name = "contents", required = false) String contents,
                                      @RequestPart(name = "techList", required = false) List<Tech> techList,

                                      @AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails,
                                      HttpServletRequest httpServletRequest) throws IOException {
        return ResponseDto.success(postService.createPost2(postRequestDto2, contents, techList, userDetails.getAccount()));
    }

    //글 수정 v2
    @ApiOperation(value = "게시글 수정", notes = "자신의 글을 수정합니다.(토큰필요)")
    @PatchMapping(value = "v2/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseDto<?> updatePost2(@RequestPart(name = "data", required = false) PostRequestDto2 postRequestDto2,
                                      @RequestPart(name = "contents", required = false) String contents,
                                      @PathVariable Long id,
                                      @RequestPart(name = "techList", required = false) List<Tech> techList,
                                      @AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails) throws IOException {
        return ResponseDto.success(postService.updatePost2(postRequestDto2, contents, techList, id, userDetails.getAccount()));
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


// v1 create 글쓰기 + img 업로드
/*
 @ApiOperation(value = "게시글 작성", notes = "게시글을 작성합니다.(토큰필요)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "category", value = "모집구분(STUDY/PROJECT)"),
            @ApiImplicitParam(name = "Duration", value = "예상기간(UNDEFINED/ONE/TWO/THREE/FOUR/FIVE/SIX)"),
            @ApiImplicitParam(name = "Place", value = "진행방식(ONLINE/OFFLINE)"),
            @ApiImplicitParam(name = "Tech", value = "사용언어(...)")})
    @PostMapping(value = "/v1", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseDto<?> createPost(@RequestPart(name = "data", required = false) PostRequestDto postRequestDto,
                                     @RequestPart(name = "image", required = false) List<MultipartFile> imgFiles,
                                     @RequestPart(name = "techList", required = false) List<Tech> techList,
                                     @AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails) throws IOException {
        return ResponseDto.success(postService.createPost(postRequestDto, imgFiles, techList, userDetails.getAccount()));
    }*/

//글 수정 v1 update
/*
    @ApiOperation(value = "게시글 수정", notes = "자신의 글을 수정합니다.(토큰필요)")
    @PatchMapping(value = "v1/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseDto<?> updatePost(@RequestPart(name = "data", required = false) PostRequestDto postRequestDto,
                                     @RequestPart(name = "image", required = false) List<MultipartFile> imgFiles,
                                     @PathVariable Long id,
                                     @RequestPart(name = "techList", required = false) List<Tech> techList,
                                     @AuthenticationPrincipal @ApiIgnore UserDetailsImpl userDetails) throws IOException {
        return ResponseDto.success(postService.updatePost(postRequestDto, imgFiles, techList, id, userDetails.getAccount()));
    }*/

}
