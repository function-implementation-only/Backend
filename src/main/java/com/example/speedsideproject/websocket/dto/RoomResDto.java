package com.example.speedsideproject.websocket.dto;

import com.example.speedsideproject.post.Post;
import com.example.speedsideproject.websocket.domain.Chat;
import com.example.speedsideproject.websocket.domain.Room;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoomResDto {
    private Long postId;
    private Long roomId;
    private String title;
    private List<Chat> chatList;
    private Long postUserId;
    private String postUserImg;
    private String postUserNickname;
    private Long joinUserId;
    private String joinUserImg;
    private String joinUserNickname;


    public RoomResDto(Room room){
        this.postId = room.getPost().getId();
        this.roomId = room.getId();
        this.title = room.getTitle();
        this.chatList = room.getChats();
        this.postUserId = room.getPostUser().getId();
        this.postUserImg = room.getPostUser().getImgUrl();
        this.postUserNickname = room.getPostUser().getNickname();
        this.joinUserId = room.getJoinUser().getId();
        this.joinUserImg = room.getJoinUser().getImgUrl();
        this.joinUserNickname = room.getJoinUser().getNickname();
    }

    public RoomResDto(Room room, Post post){
    }
}
