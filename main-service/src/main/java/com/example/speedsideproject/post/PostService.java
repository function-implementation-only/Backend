package com.example.speedsideproject.post;

import com.example.speedsideproject.account.entity.Account;
import com.example.speedsideproject.applyment.repository.ApplymentRepository;
import com.example.speedsideproject.aws_s3.S3UploadUtil;
import com.example.speedsideproject.error.CustomException;
import com.example.speedsideproject.likes.Likes;
import com.example.speedsideproject.likes.LikesRepository;
import com.example.speedsideproject.post.enums.Category;
import com.example.speedsideproject.post.enums.Place;
import com.example.speedsideproject.post.enums.Tech;
import com.example.speedsideproject.security.user.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.speedsideproject.applyment.Position.*;
import static com.example.speedsideproject.error.ErrorCode.CANNOT_FIND_POST_NOT_EXIST;
import static com.example.speedsideproject.error.ErrorCode.NOT_FOUND_USER;

@Service
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final S3UploadUtil s3UploadUtil;
    private final TechsRepository techsRepository;
    private final LikesRepository likesRepository;
    private final ApplymentRepository applymentRepository;


    @Autowired
    public PostService(PostRepository postRepository, S3UploadUtil s3UploadUtil, TechsRepository techsRepository,
                       LikesRepository likesRepository, ApplymentRepository applymentRepository) {
        this.postRepository = postRepository;
        this.s3UploadUtil = s3UploadUtil;
        this.techsRepository = techsRepository;
        this.likesRepository = likesRepository;
        this.applymentRepository = applymentRepository;
    }

    // 모든 글 읽어오기
    @Transactional(readOnly = true)
    public Page<?> getAllPost(Pageable pageable) {
        return postRepository.findAllPost(pageable);
    }

    //글 삭제
    @Transactional
    public String deletePost(Long id, Account account) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new CustomException(CANNOT_FIND_POST_NOT_EXIST)
        );
        if (!account.getEmail().equals(post.getAccount().getEmail())) {
            throw new CustomException(NOT_FOUND_USER);
        }
        postRepository.deleteById(id);
        // post image를 List 형태로 불러와서 삭제해야한다.
        // 추후 변경 해야 함.
//        if (!(post.getUrlKey() == null)) {
//            s3UploadUtil.delete(post.getUrlKey());
//        }
        return "delete success";
    }
    //글 1개 get
    @Transactional(readOnly = true)
    public PostResponseDto getOnePost(Long id, UserDetailsImpl userDetails) {
        Post post = postRepository.findById(id).orElseThrow(() -> new CustomException(CANNOT_FIND_POST_NOT_EXIST));
        //조회수
        post.setViewCount(post.getViewCount() + 1);
        //포지션별 카운트
        List<Long> countList = new ArrayList<>();
        countList.add(applymentRepository.countByPostAndPosition(post, BACKEND));
        countList.add(applymentRepository.countByPostAndPosition(post, FRONTEND));
        countList.add(applymentRepository.countByPostAndPosition(post, DESIGN));
        if (userDetails != null) {
            return new PostResponseDto(post, myLikeCheck(userDetails.getAccount(), post), countList);
        }
        return new PostResponseDto(post, countList);
    }

    //v8 카테고리별 get
    @Transactional(readOnly = true)
    public Page<?> getAllPostWithCategory(Pageable pageable, String sort, List<Tech> techList, Category category, Place place, UserDetailsImpl  userDetails) {

        return postRepository.findAllPostWithCategory(pageable, sort, techList, category, place, userDetails);
    }

    //V2 글쓰기
    @Transactional
    public PostResponseDto createPost2(PostRequestDto postRequestDto2, String contents, List<Tech> techList, Account account) throws IOException {
        //html 변환
        File htmlFile = new File("StringToHtml.html");
        FileOutputStream fos = new FileOutputStream(htmlFile);
        fos.write(contents.getBytes());
        fos.close();
        //html 저장
        var r = s3UploadUtil.upload(htmlFile, "html");
        Post post = new Post(postRequestDto2, account, r);
        //techs 추가
        List<Techs> techsList = techList.stream().map(te -> new Techs(te, post)).collect(Collectors.toList());
        postRepository.save(post);
        return new PostResponseDto(post);
    }

    //글수정 v2
    @Transactional
    public PostResponseDto updatePost2(PostRequestDto requestDto, String contents, List<Tech> techList, Long id, Account account) throws IOException {
        Post post = postRepository.findByIdAndAccount(id, account);
        if (post == null) throw new CustomException(NOT_FOUND_USER);
        // 기술 스택 제거
        techsRepository.deleteAllInBatch(post.getTechs());
        // 기술 스택 저장
        System.out.println("techlist 시작");
        List<Techs> techsList = techList.stream().map(tech -> new Techs(tech, post)).collect(Collectors.toList());
        System.out.println("techlist save all 시작");
        //post 변수 변경
        //html 변환
        File htmlFile = new File("StringToHtml.html");
        FileOutputStream fos = new FileOutputStream(htmlFile);
        fos.write(contents.getBytes());
        fos.close();
        //html 저장
        var r = s3UploadUtil.upload(htmlFile, "html");
        System.out.println("post update 시작");
        post.update2(requestDto, r);

        techsRepository.saveAll(techsList);
        return new PostResponseDto(post, myLikeCheck(account, post));
    }

    //method
    /*check my like at post*/
    private Boolean myLikeCheck(Account account, Post post) {
        Boolean result = null;
        Optional<Likes> likes = likesRepository.findByAccountAndPost(account, post);
        if (likes.isPresent()) result = likes.get().getLikeCheck();
        return result;
    }

}

