package com.auctiononline.warbidrestful.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserRequest {
    private Long id;

    @NotBlank(message = "Username must not be blank")
    @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username must contain only alphanumeric characters")
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email(message = "Invalid email!")
    private String email;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    private String password;

    @Size(min = 9, max = 12, message = "Invalid Phone")
    private String phone;

    @Size(max = 255)
    @Size(min = 6, max = 254, message = "Invalid Address")
    private String address;

    private Set<String> role;

}
