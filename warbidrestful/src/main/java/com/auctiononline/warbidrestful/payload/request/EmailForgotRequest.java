package com.auctiononline.warbidrestful.payload.request;

import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmailForgotRequest {
    @Email(message = "Invalid email!")
    private String email;
}
