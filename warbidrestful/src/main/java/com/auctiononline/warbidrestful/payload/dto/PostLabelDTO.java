package com.auctiononline.warbidrestful.payload.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostLabelDTO {
    private Long id;
    private String title;
    private String image;
}
