package com.example.speedsideproject.sse;


import com.example.speedsideproject.applyment.entity.Applyment;
import com.example.speedsideproject.global.Timestamped;
import com.nimbusds.jose.shaded.json.annotate.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@Getter
public class Notification extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;
    private String sender;
    @Setter
    @Column(nullable = false)
    private Boolean isRead;
    private String receiver;

    //one notification to one applyment
    @OneToOne
    @JsonIgnore
    private Applyment applyment;


    @Builder
    public Notification(String receiver, String sender, Boolean isRead, Applyment applyment) {
        this.receiver = receiver;
        this.sender = sender;
        this.isRead = isRead;
        this.applyment = applyment;
    }

}