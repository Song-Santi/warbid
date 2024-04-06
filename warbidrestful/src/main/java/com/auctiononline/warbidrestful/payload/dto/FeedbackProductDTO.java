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
public class FeedbackProductDTO {
   private Long id;
   private Long productId;
   private String productTitle;
   private String userName;
   private String email;
   private String content;
   private LocalDateTime updateTime;

}
