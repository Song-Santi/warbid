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

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllProduct() {
        GetAllResponse postLabelResponse = productService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(postLabelResponse);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        GetAllResponse postLabelResponse = productService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(postLabelResponse);
    }

    @GetMapping("/get-by-search/{search}")
    public ResponseEntity<?> getProductByKeyword(@PathVariable String search) {
        GetAllResponse postLabelResponse = productService.getBySearch(search);
        return ResponseEntity.status(HttpStatus.OK).body(postLabelResponse);
    }

    @GetMapping("/get-by-category-id/{categoryId}")
    public ResponseEntity<?> getProductByCategoryId(@PathVariable Integer categoryId) {
        GetAllResponse postLabelResponse = productService.getByCategory(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(postLabelResponse);
    }

    @GetMapping("/get-by-yet-auction")
    public ResponseEntity<?> getProductByYetAuction() {
        GetAllResponse postLabelResponse = productService.getByYetAuction();
        return ResponseEntity.status(HttpStatus.OK).body(postLabelResponse);
    }

    @GetMapping("/get-by-auctionning")
    public ResponseEntity<?> getProductByAuctionning() {
        GetAllResponse postLabelResponse = productService.getByAuctioning();
        return ResponseEntity.status(HttpStatus.OK).body(postLabelResponse);
    }

    @GetMapping("/get-by-auctioned")
    public ResponseEntity<?> getProductByAuctioned() {
        GetAllResponse postLabelResponse = productService.getByAuctioned();
        return ResponseEntity.status(HttpStatus.OK).body(postLabelResponse);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addProduct(@RequestBody @Valid ProductRequest productRequest, BindingResult bindingResult) {
        ResponseEntity<?> invalidResponse = UserController.invalidResponse(bindingResult);
        if (invalidResponse != null) {
            return invalidResponse;
        }
        MessageResponse addProductResponse = productService.add(productRequest);
        return ResponseEntity.status(HttpStatus.OK).body(addProductResponse);
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateProduct(@RequestBody @Valid ProductRequest productRequest, BindingResult bindingResult) {
        ResponseEntity<?> invalidResponse = UserController.invalidResponse(bindingResult);
        if (invalidResponse != null) {
            return invalidResponse;
        }
        MessageResponse updatePostResponse = productService.update(productRequest);
        return ResponseEntity.status(HttpStatus.OK).body(updatePostResponse);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        MessageResponse messageResponse = productService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }

}
