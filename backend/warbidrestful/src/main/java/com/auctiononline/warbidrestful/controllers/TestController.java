package com.auctiononline.warbidrestful.controllers;

import com.auctiononline.warbidrestful.payload.request.EmailForgotRequest;
import com.auctiononline.warbidrestful.payload.request.EmailRequest;
import com.auctiononline.warbidrestful.payload.request.PasswordRequest;
import com.auctiononline.warbidrestful.payload.request.TokenRequest;
import com.auctiononline.warbidrestful.payload.response.MessageResponse;
import com.auctiononline.warbidrestful.services.ilterface.EmailService;
import com.auctiononline.warbidrestful.services.ilterface.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
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

  @PostMapping("/send-token")
  public ResponseEntity<?> allAccess(@RequestBody EmailForgotRequest emailForgotRequest) {
    MessageResponse messageResponse = userService.emailSendToken(emailForgotRequest);
    return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
  }

  @PostMapping("/check-token")
  public ResponseEntity<?> checkToken(@RequestBody @Valid TokenRequest tokenRequest, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      Map<String, String> errors = bindingResult.getFieldErrors().stream()
              .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

      MessageResponse errorResponse = new MessageResponse(400, HttpStatus.BAD_REQUEST, "Invalid", errors);
      return ResponseEntity.badRequest().body(errorResponse);
    }

    MessageResponse messageResponse = userService.checkToken(tokenRequest);
    return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
  }

  @PostMapping("/change-password")
  public ResponseEntity<?> changePassword(@RequestBody @Valid PasswordRequest passwordRequest, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      Map<String, String> errors = bindingResult.getFieldErrors().stream()
              .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

      MessageResponse errorResponse = new MessageResponse(400, HttpStatus.BAD_REQUEST, "Invalid", errors);
      return ResponseEntity.badRequest().body(errorResponse);
    }

    MessageResponse messageResponse = userService.changePassword(passwordRequest);
    return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
  }

}
