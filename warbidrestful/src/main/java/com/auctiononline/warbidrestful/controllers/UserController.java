package com.auctiononline.warbidrestful.controllers;

import com.auctiononline.warbidrestful.models.Role;
import com.auctiononline.warbidrestful.models.User;
import com.auctiononline.warbidrestful.payload.request.*;
import com.auctiononline.warbidrestful.payload.response.GetAllResponse;
import com.auctiononline.warbidrestful.payload.response.MessageResponse;
import com.auctiononline.warbidrestful.repository.UserRepository;
import com.auctiononline.warbidrestful.security.jwt.JwtUtils;
import com.auctiononline.warbidrestful.services.ilterface.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

//@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {
   @Autowired
   private UserService userService;

   @Autowired
   private JwtUtils jwtUtils;

   @Autowired
   private UserRepository userRepository;

    @GetMapping("/private/get-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUser(@RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "10") int pageSize) {
        GetAllResponse getAllUserResponse = userService.getAllUser(page, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(getAllUserResponse);
    }

    @GetMapping("/private/get-user-by-search/{searchString}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserBySearch( @RequestParam(defaultValue = "1") int page,
                                              @RequestParam(defaultValue = "10") int pageSize,
                                              @PathVariable String searchString) {
        GetAllResponse getAllUserResponse = userService.getAllUserBySearch(page, pageSize,searchString);
        return ResponseEntity.status(HttpStatus.OK).body(getAllUserResponse);
    }

    @PostMapping("/private/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addUser(@Valid @RequestBody UserRequest userRequest, BindingResult bindingResult) {
        ResponseEntity<?> invalidResponse = invalidResponse(bindingResult);
        if (invalidResponse != null) {
            return invalidResponse;
        }
        MessageResponse messageResponse = userService.add(userRequest);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }

    @PostMapping("/private/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserRequest userRequest, BindingResult bindingResult) {
        ResponseEntity<?> invalidResponse = invalidResponse(bindingResult);
        if (invalidResponse != null) {
            return invalidResponse;
        }

        MessageResponse messageResponse = userService.update(userRequest);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }

    @PostMapping("/private/update-profile")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR')")
    public ResponseEntity<?> updateProfile(HttpServletRequest request, @Valid @RequestBody UserRequest userRequest, BindingResult bindingResult) {
        ResponseEntity<?> invalidResponse = invalidResponse(bindingResult);
        if (invalidResponse != null) {
            return invalidResponse;
        }

        String token = jwtUtils.getJwtFromCookies(request);
        String username = jwtUtils.getUserNameFromJwtToken(token);
        Optional<User>  apiRequest = userRepository.findByUsername(username);
        if (apiRequest.isPresent()) {
            Set<Role> roleSet = apiRequest.get().getRoles();

            MessageResponse messageResponse = userService.updateProfile(userRequest, roleSet);
            return ResponseEntity.status(HttpStatus.OK).body(messageResponse);

        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(404, HttpStatus.NOT_FOUND, "User not found!"));
        }
    }

    @DeleteMapping("/private/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        MessageResponse messageResponse = userService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }

    @DeleteMapping("/private/restore/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> restoreUser(@PathVariable Long id) {
        MessageResponse messageResponse = userService.restore(id);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }

    @PostMapping("/public/send-token")
    public ResponseEntity<?> sendToken(@RequestBody @Valid EmailForgotRequest emailForgotRequest, BindingResult bindingResult) {
        ResponseEntity<?> invalidResponse = invalidResponse(bindingResult);
        if (invalidResponse != null) {
            return invalidResponse;
        }
        MessageResponse messageResponse = userService.emailSendToken(emailForgotRequest);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }

    @PostMapping("/public/check-token")
    public ResponseEntity<?> checkToken(@RequestBody @Valid TokenRequest tokenRequest, BindingResult bindingResult) {
        ResponseEntity<?> invalidResponse = invalidResponse(bindingResult);
        if (invalidResponse != null) {
            return invalidResponse;
        }
        MessageResponse messageResponse = userService.checkToken(tokenRequest);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }

    @PostMapping("/public/change-pass-due-forgot")
    public ResponseEntity<?> changePassDueForgot(@RequestBody @Valid ChangePassDueForgotRequest changePassDueForgotRequest, BindingResult bindingResult) {
        ResponseEntity<?> invalidResponse = invalidResponse(bindingResult);
        if (invalidResponse != null) {
            return invalidResponse;
        }

        MessageResponse messageResponse = userService.changePassDueForgot(changePassDueForgotRequest);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }

    @PostMapping("/public/change-password")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> changePassword(@RequestBody @Valid PasswordRequest passwordRequest, BindingResult bindingResult) {
        ResponseEntity<?> invalidResponse = invalidResponse(bindingResult);
        if (invalidResponse != null) {
            return invalidResponse;
        }

        MessageResponse messageResponse = userService.changePassword(passwordRequest);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }

    public static ResponseEntity<?> invalidResponse(BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

            MessageResponse errorResponse = new MessageResponse(400, HttpStatus.BAD_REQUEST, "Invalid", errors);
            return ResponseEntity.status(HttpStatus.OK).body(errorResponse);
        }
        return null;
    }

}
