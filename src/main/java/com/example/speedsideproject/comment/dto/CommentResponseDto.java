package com.example.speedsideproject.comment.dto;


import com.example.speedsideproject.comment.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private Long postId;
    private Long commentId;
    private String comments;
    private String email;

    public CommentResponseDto(Comment comment) {
        this.postId = comment.getPostId();
        this.commentId = comment.getId();
        this.comments = comment.getComments();
        this.email = comment.getEmail();


    }


}