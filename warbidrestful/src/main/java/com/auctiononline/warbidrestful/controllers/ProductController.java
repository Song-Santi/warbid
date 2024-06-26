package com.auctiononline.warbidrestful.controllers;

import com.auctiononline.warbidrestful.payload.request.PostRequest;
import com.auctiononline.warbidrestful.payload.request.ProductRequest;
import com.auctiononline.warbidrestful.payload.response.GetAllResponse;
import com.auctiononline.warbidrestful.payload.response.MessageResponse;
import com.auctiononline.warbidrestful.services.ilterface.ProductService;
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
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/public/get-all")
    public ResponseEntity<?> getAllProduct(@RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "10") int pageSize) {
        GetAllResponse postLabelResponse = productService.getAll(page, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(postLabelResponse);
    }

    @GetMapping("/public/get-by-id/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        GetAllResponse postLabelResponse = productService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(postLabelResponse);
    }

    @GetMapping("/public/get-by-search/{search}")
    public ResponseEntity<?> getProductByKeyword(@RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "10") int pageSize,
                                                 @PathVariable String search) {
        GetAllResponse postLabelResponse = productService.getBySearch(page, pageSize, search);
        return ResponseEntity.status(HttpStatus.OK).body(postLabelResponse);
    }

    @GetMapping("/public/get-by-category-id/{categoryId}")
    public ResponseEntity<?> getProductByCategoryId(@PathVariable Integer categoryId) {
        GetAllResponse postLabelResponse = productService.getByCategory(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(postLabelResponse);
    }

    @GetMapping("/public/get-by-yet-auction")
    public ResponseEntity<?> getProductByYetAuction(@RequestParam(defaultValue = "1") int page,
                                                    @RequestParam(defaultValue = "10") int pageSize) {
        GetAllResponse postLabelResponse = productService.getByYetAuction(page, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(postLabelResponse);
    }

    @GetMapping("/public/get-by-auctionning")
    public ResponseEntity<?> getProductByAuctionning(@RequestParam(defaultValue = "1") int page,
                                                     @RequestParam(defaultValue = "10") int pageSize) {
        GetAllResponse postLabelResponse = productService.getByAuctioning(page, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(postLabelResponse);
    }

    @GetMapping("/public/get-by-auctioned")
    public ResponseEntity<?> getProductByAuctioned(@RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "10") int pageSize) {
        GetAllResponse postLabelResponse = productService.getByAuctioned(page, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(postLabelResponse);
    }

    @PostMapping("/private/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addProduct(@RequestBody @Valid ProductRequest productRequest, BindingResult bindingResult) {
        ResponseEntity<?> invalidResponse = UserController.invalidResponse(bindingResult);
        if (invalidResponse != null) {
            return invalidResponse;
        }
        MessageResponse addProductResponse = productService.add(productRequest);
        return ResponseEntity.status(HttpStatus.OK).body(addProductResponse);
    }

    @PostMapping("/private/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateProduct(@RequestBody @Valid ProductRequest productRequest, BindingResult bindingResult) {
        ResponseEntity<?> invalidResponse = UserController.invalidResponse(bindingResult);
        if (invalidResponse != null) {
            return invalidResponse;
        }
        MessageResponse updatePostResponse = productService.update(productRequest);
        return ResponseEntity.status(HttpStatus.OK).body(updatePostResponse);
    }

    @DeleteMapping("/private/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        MessageResponse messageResponse = productService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }

}
