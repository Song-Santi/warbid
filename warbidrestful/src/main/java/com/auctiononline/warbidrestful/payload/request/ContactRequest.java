package com.auctiononline.warbidrestful.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class ContactRequest {
    @Size(min = 4, max = 50, message = "Full name invalid!")
    String fullName;
    @Email(message = "Email invalid!")
    String email;
    @Size(max = 255, message = "Title is too long!")
    String title;
    @Size(max = 3000, message = "Content is to long, Please send us an email!")
    String content;
}
