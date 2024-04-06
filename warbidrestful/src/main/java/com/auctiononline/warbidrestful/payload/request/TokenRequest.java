package com.auctiononline.warbidrestful.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TokenRequest {
    @Email(message = "Invalid email!")
    private String email;

    @Size(min = 5, max = 5, message = "Invalid token!")
    private String token;
}
