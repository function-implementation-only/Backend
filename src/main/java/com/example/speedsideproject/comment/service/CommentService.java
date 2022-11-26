package com.example.speedsideproject.comment.service;


import com.example.speedsideproject.account.entity.Account;
import com.example.speedsideproject.comment.dto.CommentRequestDto;
import com.example.speedsideproject.comment.dto.CommentResponseDto;
import com.example.speedsideproject.comment.entity.Comment;
import com.example.speedsideproject.comment.repository.CommentRepository;
import com.example.speedsideproject.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.speedsideproject.error.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    //커멘트 생성
    public CommentResponseDto createComment(CommentRequestDto requestDto, Account account) {
        var r = new Comment(requestDto, account);
        commentRepository.save(r);
        return new CommentResponseDto(r);
    }

    // 커멘트 수정
    public CommentResponseDto updateComment(CommentRequestDto requestDto, Long id, Account account) {

        var r = commentRepository.findById(id).orElseThrow(
                () -> new RuntimeException("comment is not exist"));
        if (!account.getEmail().equals(r.getEmail())) {
            throw new CustomException(ONLY_CAN_UPDATE_COMMENT_WRITER);
        }
        r.update(requestDto);
        return new CommentResponseDto(r);
    }

    //커멘트 삭제
    public String deleteComment(Long Id, Account account) {
        var r = commentRepository.findById(Id).orElseThrow(
                () -> new CustomException(DOESNT_EXIST_COMMENT_FOR_READ));
        if (!account.getEmail().equals(r.getEmail())) {
            throw new CustomException(ONLY_CAN_DELETE_COMMENT_WRITER);}
        commentRepository.deleteById(Id);
        return "Delete success";
    }
    //모든 커멘트 읽기
    public List<CommentResponseDto> getAllMyComments(Account account) {
        var commentLists = commentRepository.findAllByEmail(account.getEmail());
        var commentResponseLists = new ArrayList<CommentResponseDto>();
        for(Comment commentList: commentLists){
            commentResponseLists.add(new CommentResponseDto(commentList));
        }
        return commentResponseLists;
    }
    //커멘트 1개 읽기
    public CommentResponseDto getOneComment(Long Id, Account account) {
        var r = commentRepository.findById(Id).orElseThrow(
                () -> new CustomException(DOESNT_EXIST_COMMENT_FOR_READ)
        );
        return new CommentResponseDto(r);
    }
}