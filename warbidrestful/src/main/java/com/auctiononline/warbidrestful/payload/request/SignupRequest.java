package com.auctiononline.warbidrestful.payload.request;

import java.util.Set;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignupRequest {
    @NotBlank(message = "Username must not be blank")
    @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username must contain only alphanumeric characters")
    private String username;
 
    @NotBlank
    @Size(max = 50)
    @Email(message = "Invalid email!")
    private String email;
    
    private Set<String> role;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    private String password;

}
