package com.auctiononline.warbidrestful.controllers;

import com.auctiononline.warbidrestful.payload.request.PostRequest;
import com.auctiononline.warbidrestful.payload.response.GetAllResponse;
import com.auctiononline.warbidrestful.payload.response.MessageResponse;
import com.auctiononline.warbidrestful.services.ilterface.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping("/get-all-label")
    public ResponseEntity<?> getAllLabel() {
        GetAllResponse postLabelResponse = postService.getAllPostLabel();
        return ResponseEntity.status(HttpStatus.OK).body(postLabelResponse);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAll() {
        GetAllResponse postLabelResponse = postService.getAllPost();
        return ResponseEntity.status(HttpStatus.OK).body(postLabelResponse);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        GetAllResponse postLabelResponse = postService.getPostById(id);
        return ResponseEntity.status(HttpStatus.OK).body(postLabelResponse);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addPost(@RequestBody @Valid PostRequest postRequest, BindingResult bindingResult) {
        ResponseEntity<?> invalidResponse = UserController.invalidResponse(bindingResult);
        if (invalidResponse != null) {
            return invalidResponse;
        }
        MessageResponse addPostResponse = postService.addPost(postRequest);
        return ResponseEntity.status(HttpStatus.OK).body(addPostResponse);
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updatePost(@RequestBody @Valid PostRequest postRequest, BindingResult bindingResult) {
        ResponseEntity<?> invalidResponse = UserController.invalidResponse(bindingResult);
        if (invalidResponse != null) {
            return invalidResponse;
        }
        MessageResponse updatePostResponse = postService.updatePost(postRequest);
        return ResponseEntity.status(HttpStatus.OK).body(updatePostResponse);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        MessageResponse messageResponse = postService.deletePost(id);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }
}
