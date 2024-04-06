package com.auctiononline.warbidrestful.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "tokens")
@Entity
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "expiry")
    private LocalDateTime expiry;

    @Column(name = "failed_count")
    private int failedCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    private Boolean deleted;

    public Token(String token, LocalDateTime expiry, User user){
        this.token = token;
        this.expiry = expiry;
        this.user = user;
    }

    public Token(Long id, String token, LocalDateTime expiry, int failedCount, User user,LocalDateTime createdTime, LocalDateTime updatedTime, boolean deleted){
        this.id = id;
        this.token = token;
        this.expiry = expiry;
        this.failedCount = failedCount;
        this.user = user;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
        this.deleted = deleted;
    }

}
