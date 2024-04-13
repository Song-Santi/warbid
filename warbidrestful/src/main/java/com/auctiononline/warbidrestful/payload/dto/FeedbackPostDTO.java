package com.auctiononline.warbidrestful.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackPostDTO {
    private Long id;
    private Long postId;
    private String postTitle;
    private String userName;
    private String email;
    private String content;
    private LocalDateTime updateTime;
}
