package com.auctiononline.warbidrestful.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.auctiononline.warbidrestful.payload.response.UserInfoResponse;
import jakarta.validation.Valid;

import com.auctiononline.warbidrestful.exception.AppException;
import com.auctiononline.warbidrestful.models.ERole;
import com.auctiononline.warbidrestful.models.Role;
import com.auctiononline.warbidrestful.models.User;
import com.auctiononline.warbidrestful.payload.request.LoginRequest;
import com.auctiononline.warbidrestful.payload.request.SignupRequest;
import com.auctiononline.warbidrestful.payload.response.MessageResponse;
import com.auctiononline.warbidrestful.payload.response.UserInfo;
import com.auctiononline.warbidrestful.repository.RoleRepository;
import com.auctiononline.warbidrestful.repository.UserRepository;
import com.auctiononline.warbidrestful.security.jwt.JwtUtils;
import com.auctiononline.warbidrestful.security.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


//@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
    ResponseEntity<?> invalidResponse = UserController.invalidResponse(bindingResult);
    if (invalidResponse != null) {
      return invalidResponse;
    }

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    if(userDetails.getDeleted()){
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
              .body(new MessageResponse(401, HttpStatus.UNAUTHORIZED, "Bad credentials"));
    }

    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    UserInfo userInfo = new UserInfo(userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getEmail(),
            userDetails.getPhone(),
            userDetails.getAddress(),
            roles);

    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
        .body(new UserInfoResponse(200,"OK","Get user information successfully!", userInfo));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest, BindingResult bindingResult) {
    ResponseEntity<?> invalidResponse = UserController.invalidResponse(bindingResult);
    if (invalidResponse != null) {
      return invalidResponse;
    }

    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest().body(new MessageResponse(400, HttpStatus.BAD_REQUEST,"Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new MessageResponse(400, HttpStatus.BAD_REQUEST,"Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(),
                         signUpRequest.getEmail(),
                         encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
        case "admin":
          Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
              .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Error: Role is not found."));
          roles.add(adminRole);
          break;
        case "mod":
          Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
              .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Error: Role is not found."));
          roles.add(modRole);
          break;
        default:
          Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Error: Role is not found."));
          roles.add(userRole);
        }
      });
    }

    user.setRoles(roles);
    user.setDeleted(false);
    userRepository.save(user);

    return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(200,HttpStatus.OK,"User registered successfully!"));
  }

  @PostMapping("/signout")
  public ResponseEntity<?> logoutUser() {
    ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(new MessageResponse(200,HttpStatus.OK,"You've been signed out!"));
  }
}
