package com.auctiononline.warbidrestful.controllers;

import com.auctiononline.warbidrestful.models.Category;
import com.auctiononline.warbidrestful.payload.response.GetAllResponse;
import com.auctiononline.warbidrestful.payload.response.MessageResponse;
import com.auctiononline.warbidrestful.services.ilterface.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/public/get-all")
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "10") int pageSize) {
        GetAllResponse postLabelResponse = categoryService.getAll(page, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(postLabelResponse);
    }

    @GetMapping("/public/get-by-id/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        GetAllResponse postLabelResponse = categoryService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(postLabelResponse);
    }

    @GetMapping("/public/get-by-name/{name}")
    public ResponseEntity<?> getByName(@PathVariable String name) {
        GetAllResponse postLabelResponse = categoryService.getByName(name);
        return ResponseEntity.status(HttpStatus.OK).body(postLabelResponse);
    }

    @PostMapping("/private/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addCategory(@RequestBody @Valid Category category, BindingResult bindingResult) {
        ResponseEntity<?> invalidResponse = UserController.invalidResponse(bindingResult);
        if (invalidResponse != null) {
            return invalidResponse;
        }
        MessageResponse addPostResponse = categoryService.add(category);
        return ResponseEntity.status(HttpStatus.OK).body(addPostResponse);
    }

    @PostMapping("/private/update")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateCategory(@RequestBody @Valid Category category, BindingResult bindingResult) {
        ResponseEntity<?> invalidResponse = UserController.invalidResponse(bindingResult);
        if (invalidResponse != null) {
            return invalidResponse;
        }
        MessageResponse addPostResponse = categoryService.update(category);
        return ResponseEntity.status(HttpStatus.OK).body(addPostResponse);
    }
}
