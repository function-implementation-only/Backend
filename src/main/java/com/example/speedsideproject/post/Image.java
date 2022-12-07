package com.example.speedsideproject.post;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Map;

@Getter
@Entity
@NoArgsConstructor
public class Image {
    @Id
    @Column(name="image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob //image 형식의 저장을 위해
    private String imgUrl;
    private String imgKey;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public Image(Map<String, String> image){
        this.imgUrl = image.get("url");
        this.imgKey = image.get("key");
    }

    public void setPost(Post post) {
        this.post = post;
    }
}