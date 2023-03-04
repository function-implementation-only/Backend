//package com.example.speedsideproject.applyment.service;
//
//import com.example.speedsideproject.account.entity.Account;
//import com.example.speedsideproject.account.repository.AccountRepository;
//import com.example.speedsideproject.applyment.Position;
//import com.example.speedsideproject.applyment.dto.ApplymentRequestDto;
//import com.example.speedsideproject.applyment.entity.Applyment;
//import com.example.speedsideproject.applyment.repository.ApplymentRepository;
//import com.example.speedsideproject.error.CustomException;
//import com.example.speedsideproject.post.Post;
//import com.example.speedsideproject.post.PostRepository;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//import lombok.RequiredArgsConstructor;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.PostConstruct;
//
//import static com.example.speedsideproject.error.ErrorCode.CANNOT_FIND_POST_NOT_EXIST;
//
//@SpringBootTest
//@Transactional
//@AllArgsConstructor
//class ApplymentServiceTest {
//
//    @Autowired private final ApplymentRepository applymentRepository;
//
//    @Autowired private final PostRepository postRepository;
//
//    @Autowired private final AccountRepository accountRepository;
//    @BeforeEach
//    void beforeTest(){
//
//        ApplymentRequestDto requestDto = new ApplymentRequestDto();
//        requestDto.setPostId(4L);
//        requestDto.setPosition(Position.BACKEND);
//        Account account3 = accountRepository.findById(4L).orElseThrow(() -> new RuntimeException("유저없음"));
//    }
//    @Test
//    void createApplyment() {
//
//        requestDto
//        var r = new Applyment(requestDto, account3);
//        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(() -> new CustomException(CANNOT_FIND_POST_NOT_EXIST));
//        applymentRepository.save(r);
//        r.setPost(post);
//
//        Assertions.assertThat(r.getPosition()).isEqualTo(Position.BACKEND);
////        Assertions.assertThat(r.getPost().getId()).isEqualTo(4L);
////        Assertions.assertThat(r.getPost().getBackendNum()).isNotEqualTo(0);
////        Assertions.assertThat(post.getAccount().getId()).isEqualTo(4L);
//    }
//}
