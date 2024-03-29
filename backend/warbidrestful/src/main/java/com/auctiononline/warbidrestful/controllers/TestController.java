package com.auctiononline.warbidrestful.controllers;

import com.auctiononline.warbidrestful.payload.request.*;
import com.auctiononline.warbidrestful.payload.response.GetAllResponse;
import com.auctiononline.warbidrestful.payload.response.MessageResponse;
import com.auctiononline.warbidrestful.services.ilterface.EmailService;
import com.auctiononline.warbidrestful.services.ilterface.PostService;
import com.auctiononline.warbidrestful.services.ilterface.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

//@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

  @Autowired
  private UserService userService;

  @Autowired
  private EmailService emailService;

  @Autowired
  private PostService postService;

  @GetMapping("/all")
  public String allAccess() {
    return "Public Content.";
  }

  @GetMapping("/user")
  @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
  public String userAccess() {
    return "User Content.";
  }

  @GetMapping("/mod")
  @PreAuthorize("hasRole('MODERATOR')")
  public String moderatorAccess() {
    return "Moderator Board.";
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public String adminAccess() {
    return "Admin Board.";
  }

  @PostMapping("/sendmail")
  public void allAccess(@RequestBody EmailRequest emailRequest) {
    emailService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getText());
  }

  @GetMapping("/get-all-label")
  public ResponseEntity<?> getAllLabel() {
    GetAllResponse postLabelResponse = postService.getAllPostLabel();
    return ResponseEntity.status(HttpStatus.OK).body(postLabelResponse);
  }
}
