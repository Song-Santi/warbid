package com.auctiononline.warbidrestful.controllers;


import com.auctiononline.warbidrestful.payload.request.UserRequest;
import com.auctiononline.warbidrestful.payload.response.GetAllUserResponse;
import com.auctiononline.warbidrestful.payload.response.MessageResponse;
import com.auctiononline.warbidrestful.services.ilterface.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {
   @Autowired
   private UserService userService;

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUser() {
        GetAllUserResponse getAllUserResponse = userService.getAllUser();
        return ResponseEntity.status(HttpStatus.OK).body(getAllUserResponse);
    }

    @PostMapping("/getUserBySearch/{searchString}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addUser( @PathVariable String searchString) {
        GetAllUserResponse getAllUserResponse = userService.getAllUserBySearch(searchString);
        return ResponseEntity.status(HttpStatus.OK).body(getAllUserResponse);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addUser(@Valid @RequestBody UserRequest userRequest) {
        MessageResponse messageResponse = userService.add(userRequest);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserRequest userRequest) {
        MessageResponse messageResponse = userService.update(userRequest);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        MessageResponse messageResponse = userService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }

    @PostMapping("/restore/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> restoreUser(@PathVariable Long id) {
        MessageResponse messageResponse = userService.restore(id);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }

}
