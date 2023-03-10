package com.example.speedsideproject.post;

import com.example.speedsideproject.post.enums.Tech;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Techs {
    @Id
    @Column(name = "techs_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column
    @Enumerated(EnumType.STRING)
    private Tech tech;


    public Techs(Tech tech, Post post) {
        this.tech = tech;
        this.post = post;
        post.addTechs(this);
    }

    public Techs(Tech tech) {
        this.tech = tech;
    }

    @Data
    static class TechsResponseDto {
        private Long id;
        private Tech tech;
        public TechsResponseDto(Techs techs){
            this.id = techs.getId();
            this.tech = techs.getTech();
        }
    }
}
