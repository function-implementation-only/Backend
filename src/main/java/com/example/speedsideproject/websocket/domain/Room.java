package com.example.speedsideproject.websocket.domain;

import com.example.speedsideproject.account.entity.Account;
import com.example.speedsideproject.post.Post;
import com.example.speedsideproject.websocket.dto.RoomReqDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @JsonManagedReference
    @OneToMany(mappedBy = "room", cascade = CascadeType.REMOVE ,fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Chat> chats;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Account postUser;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Account joinUser;

    public Room(Account postUser, Account joinUser, Post post){
        this.postUser = postUser;
        this.joinUser = joinUser;
        this.post = post;

    }
}
