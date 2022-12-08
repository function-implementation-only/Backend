package com.example.speedsideproject.sse;

import com.example.speedsideproject.global.Timestamped;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Notification extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;
    @Column(nullable = true)
    private String alarType;
    @Column(nullable = true)
    private String message;
    @Column(nullable = true)
    private Boolean readState;
    @Column(nullable = true)
    private Long url;
    @Column
    private String title;
}
