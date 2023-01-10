package com.example.speedsideproject.applyment.service;


import com.example.speedsideproject.account.entity.Account;
import com.example.speedsideproject.applyment.dto.ApplymentRequestDto;
import com.example.speedsideproject.applyment.dto.ApplymentResponseDto;
import com.example.speedsideproject.applyment.entity.Applyment;
import com.example.speedsideproject.applyment.repository.ApplymentRepository;
import com.example.speedsideproject.error.CustomException;
import com.example.speedsideproject.post.Post;
import com.example.speedsideproject.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.speedsideproject.error.ErrorCode.*;

@Transactional
@RequiredArgsConstructor
@Service
public class ApplymentService {
    private final ApplymentRepository applymentRepository;
    private final PostRepository postRepository;

    //지원 생성
    public ApplymentResponseDto createApplyment(ApplymentRequestDto requestDto, Account account) {
        var r = new Applyment(requestDto, account);
        Post post = postRepository.findById(requestDto.getPostId()).orElseThrow(()-> new CustomException(CANNOT_FIND_POST_NOT_EXIST));
        applymentRepository.save(r);
        r.setPost(post);
        return new ApplymentResponseDto(r,requestDto.getPostId());
    }

    // 지원 수정
    public ApplymentResponseDto updateApplyment(ApplymentRequestDto requestDto, Long id, Account account) {

        var r = applymentRepository.findById(id).orElseThrow(
                () -> new CustomException(DOESNT_EXIST_APPLYMENT_FOR_READ));
        if (!account.getId().equals(r.getAccount().getId())) {
            throw new CustomException(ONLY_CAN_UPDATE_APPLYMENT_WRITER);
        }
        r.update(requestDto);
        return new ApplymentResponseDto(r);
    }

    //지원 삭제
    public String deleteApplyment(Long Id, Account account) {
        var r = applymentRepository.findById(Id).orElseThrow(
                () -> new CustomException(DOESNT_EXIST_APPLYMENT_FOR_READ));
        if (!account.getId().equals(r.getAccount().getId())) {
            throw new CustomException(ONLY_CAN_DELETE_APPLYMENT_WRITER);
        }
        applymentRepository.deleteById(Id);
        return "Delete success";
    }

    //모든 지원 읽기
    public List<ApplymentResponseDto> getAllMyApplyments(Account account) {
        var commentLists = applymentRepository.findAllByAccount(account);
        var commentResponseLists = new ArrayList<ApplymentResponseDto>();
        for (Applyment applymentList : commentLists) {
            commentResponseLists.add(new ApplymentResponseDto(applymentList));
        }
        return commentResponseLists;
    }

    //지원1개 읽기
    public ApplymentResponseDto getOneApplyment(Long Id, Account account) {
        var r = applymentRepository.findById(Id).orElseThrow(
                () -> new CustomException(DOESNT_EXIST_APPLYMENT_FOR_READ)
        );
        return new ApplymentResponseDto(r);
    }
}