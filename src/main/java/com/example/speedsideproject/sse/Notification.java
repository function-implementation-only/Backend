package com.example.speedsideproject.sse;

import com.example.speedsideproject.account.entity.Account;
import com.example.speedsideproject.global.Timestamped;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;
    @Column(nullable = true)
    private String message;
    @Column(nullable = true)
    private Boolean readState;
    @Column(nullable = true)
    private Long url;
    @Column
    private String title;
    @ManyToOne
    @JsonBackReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "receiver_account_id")
    private Account account;

    @Builder
    public Notification(AlarmType alarmType, String message, Boolean readState,
                        Long articlesId, Account receiver, String title) {
        this.alarmType = alarmType;
        this.message = message;
        this.readState = readState;
        this.url = articlesId;
        this.account = receiver;
        this.title = title;
    }

    public void changeState() {
        readState = true;
    }
}
