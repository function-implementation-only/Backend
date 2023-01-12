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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.speedsideproject.applyment.Position.*;
import static com.example.speedsideproject.error.ErrorCode.CANNOT_FIND_POST_NOT_EXIST;
import static com.example.speedsideproject.error.ErrorCode.NOT_FOUND_USER;

@Service
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final S3UploadUtil s3UploadUtil;
    private final ImageRepository imageRepository;
    private final TechsRepository techsRepository;
    private final LikesRepository likesRepository;
    private final ApplymentRepository applymentRepository;


    @Autowired
    public PostService(PostRepository postRepository, ImageRepository imageRepository, S3UploadUtil s3UploadUtil, TechsRepository techsRepository,
                       LikesRepository likesRepository, ApplymentRepository applymentRepository) {
        this.postRepository = postRepository;
        this.imageRepository = imageRepository;
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

    private boolean isLikedPost(Post post, List<Likes> likeList) {
        for (Likes like : likeList) {
            if (like.getPost() != null && like.getPost().getId().equals(post.getId())) {
                return true;
            }
        }
        return false;
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
    public PostResponseDto getOnePost(Long id, UserDetailsImpl userDetails) {

        Post post = postRepository.findById(id).orElseThrow(() -> new CustomException(CANNOT_FIND_POST_NOT_EXIST));

        //포지션별 카운트
        List<Long> countList = new ArrayList<>();

        countList.add(applymentRepository.countByPostAndPosition(post, BACKEND));
        countList.add(applymentRepository.countByPostAndPosition(post, FRONTEND));
        countList.add(applymentRepository.countByPostAndPosition(post, DESIGN));

        System.out.println("유저인증 시작");
        if (userDetails != null) {
            System.out.println("detail이 !null 입니다");
//            List<Likes> likeList = likesRepository.findLikesByAccount(userDetails.getAccount());
            return new PostResponseDto(post, likesRepository.findLikeCheckByAccountAndPost(userDetails.getAccount(), post), countList);
        }

        return new PostResponseDto(post, countList);

    }

    //V2 카테고리별 get
    @Transactional(readOnly = true)
    public Page<?> getAllPostWithCategory(Pageable pageable, List<Tech> techlist) {

        return postRepository.findAllPostWithCategory(pageable, techlist);
    }

    //V3 카테고리별 get
    public Page<?> getAllPostWithCategory3(Pageable pageable, List<Tech> techList, Category category, Place place) {

        return postRepository.findAllPostWithCategory3(pageable, techList, category, place);
    }

    //V4 카테고리별 get
    public List<?> getAllPostWithCategory4(Long offset, Long size, List<Tech> techList, Category category, Place place) {
        return postRepository.findAllPostWithCategory4(offset, size, techList, category, place);
    }

    //V5 카테고리별 get
    public Page<?> getAllPostWithCategory5(Pageable pageable, List<Tech> techList, Category category, Place place) {

        return postRepository.findAllPostWithCategory5(pageable, techList, category, place);
    }

    //V6 카테고리별 get
    public List<?> getAllPostWithCategory6(String sort, Long size, Long page, List<Tech> techList, Category category, Place place) {
        return postRepository.findAllPostWithCategory6(sort, size, page, techList, category, place);
    }

    //V7 카테고리별 get
    public Object getAllPostWithCategory7(Pageable pageable, String sort, List<Tech> techList, Category category, Place place) {
        return postRepository.findAllPostWithCategory7(pageable, sort, techList, category, place);
    }

    //V2 글쓰기
    @Transactional
    public PostResponseDto2 createPost2(PostRequestDto2 postRequestDto2, String contents, List<Tech> techList, Account account) throws IOException {
        Post post = new Post(postRequestDto2, account);
        //html 변환
        File htmlFile = new File("StringToHtml.html");
        FileOutputStream fos = new FileOutputStream(htmlFile);
        fos.write(contents.getBytes());
        fos.close();

        //html 저장
        var r = s3UploadUtil.upload(htmlFile, "html");
        post.setContent(r);
        System.out.println(r);
        //techs 추가
        List<Techs> techsList = techList.stream().map(te -> new Techs(te, post)).collect(Collectors.toList());
        postRepository.save(post);
        return new PostResponseDto2(post);
    }


    //글쓰기 v1
    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto, List<MultipartFile> imgFiles, List<Tech> techList, Account account) throws IOException {
        Post post = new Post(postRequestDto, account);

        List<Image> imageList = new ArrayList<>();
        if (imgFiles != null) {
            for (MultipartFile image : imgFiles) {
                Image image1 = new Image(s3UploadUtil.upload(image, "side-post"));
                imageList.add(image1);
                post.addImg(image1);
            }
            imageRepository.saveAll(imageList);
        }
        //techs 추가
        List<Techs> techsList = techList.stream().map(te -> new Techs(te, post)).collect(Collectors.toList());
        techsRepository.saveAll(techsList);
        postRepository.save(post);
        List<Likes> likeList = likesRepository.findLikesByAccount(account);
        return new PostResponseDto(post, isLikedPost(post, likeList));
    }

    //글 수정 v1
    @Transactional
    public PostResponseDto updatePost(PostRequestDto requestDto, List<MultipartFile> imgFiles, List<Tech> techList, Long id, Account account) throws IOException {

        Post post = postRepository.findByIdAndAccount(id, account);
        if (post == null) throw new CustomException(NOT_FOUND_USER);

        List<Image> imageList = imageRepository.findAllByPostId(post.getId());

        for (Image i : imageList) {
            s3UploadUtil.delete(i.getImgKey());
            imageRepository.delete(i);
        }

        List<Image> images = new ArrayList<>();

        if (imgFiles != null) {
            for (MultipartFile m : imgFiles) {
                Image i = imageRepository.save(new Image(s3UploadUtil.upload(m, "side-post")));
                images.add(i);
                post.addImg(i);
            }
        }

        //요거보고 이미지도 바꾸세여?
        List<Techs> techLists = techsRepository.findAllByPostId(post.getId());
        techsRepository.deleteAllInBatch(techLists);

        List<Techs> techsList = techList.stream().map(te -> new Techs(te, post)).collect(Collectors.toList());
        techsRepository.saveAll(techsList);
        post.update(requestDto);
        List<Likes> likeList = likesRepository.findLikesByAccount(account);
        return new PostResponseDto(post, isLikedPost(post, likeList));
    }

    //글수정 v2
    @Transactional
    public PostResponseDto2 updatePost2(PostRequestDto2 requestDto, String contents, List<Tech> techList, Long id, Account account) throws IOException {
        Post post = postRepository.findByIdAndAccount(id, account);
        if (post == null) throw new CustomException(NOT_FOUND_USER);

        // 기술 스택 제거
        techsRepository.deleteAllInBatch(post.getTechs());

        // 기술 스택 저장
        System.out.println("techlist 시작");
        List<Techs> techsList = techList.stream().map(tech -> new Techs(tech,post)).collect(Collectors.toList());
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

        List<Likes> likeList = likesRepository.findLikesByAccount(account);
        techsRepository.saveAll(techsList);
        return new PostResponseDto2(post, isLikedPost(post, likeList));
    }
}

