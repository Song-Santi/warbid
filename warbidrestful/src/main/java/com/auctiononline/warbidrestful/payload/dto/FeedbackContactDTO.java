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
public class FeedbackContactDTO {
    private Long id;
    private String fullName;
    private String email;
    private String title;
    private String content;
    private LocalDateTime updateTime;
}
