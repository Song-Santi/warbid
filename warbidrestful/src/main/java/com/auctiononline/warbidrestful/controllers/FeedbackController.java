package com.auctiononline.warbidrestful.controllers;

import com.auctiononline.warbidrestful.payload.response.GetAllResponse;
import com.auctiononline.warbidrestful.services.ilterface.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @GetMapping("/get-all-product")
    public ResponseEntity<?> getAllProduct() {
        GetAllResponse postLabelResponse = feedbackService.getAllFeedbackProduct();
        return ResponseEntity.status(HttpStatus.OK).body(postLabelResponse);
    }

    @GetMapping("/get-all-post")
    public ResponseEntity<?> getAllPost() {
        GetAllResponse postLabelResponse = feedbackService.getAllFeedbackPost();
        return ResponseEntity.status(HttpStatus.OK).body(postLabelResponse);
    }

    @GetMapping("/get-all-contact")
    public ResponseEntity<?> getAllContact() {
        GetAllResponse postLabelResponse = feedbackService.getAllFeedbackContact();
        return ResponseEntity.status(HttpStatus.OK).body(postLabelResponse);
    }

    @GetMapping("/get-feedback-product-by-id/{id}")
    public ResponseEntity<?> getFeedbackProductById(@PathVariable Long id) {
        GetAllResponse postLabelResponse = feedbackService.getFeedbackProductById(id);
        return ResponseEntity.status(HttpStatus.OK).body(postLabelResponse);
    }

    @GetMapping("/get-feedback-post-by-id/{id}")
    public ResponseEntity<?> getFeedbackPostById(@PathVariable Long id) {
        GetAllResponse postLabelResponse = feedbackService.getFeedbackPostById(id);
        return ResponseEntity.status(HttpStatus.OK).body(postLabelResponse);
    }

    @GetMapping("/get-feedback-contact-by-id/{id}")
    public ResponseEntity<?> getFeedbackContactById(@PathVariable Long id) {
        GetAllResponse postLabelResponse = feedbackService.getFeedbackContactById(id);
        return ResponseEntity.status(HttpStatus.OK).body(postLabelResponse);
    }

}
