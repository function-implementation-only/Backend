package com.example.speedsideproject.websocket.domain;

import com.example.speedsideproject.post.Post;
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

    private String name;

    @JsonManagedReference
    @OneToMany(mappedBy = "room", cascade = CascadeType.REMOVE ,fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Chat> chats;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public Room(String name){
        this.name = name;
    }
    /*
      * 채팅방 생성
      * @param name 방 이름
      * @return Room Entity
     */
    public static Room createRoom(String name){
        return Room.builder()
                .name(name)
                .build();
    }
}
