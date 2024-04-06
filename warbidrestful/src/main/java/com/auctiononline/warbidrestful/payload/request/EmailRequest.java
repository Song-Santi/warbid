package com.auctiononline.warbidrestful.payload.request;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmailRequest {
    private String to;
    private String subject;
    private String text;
}
