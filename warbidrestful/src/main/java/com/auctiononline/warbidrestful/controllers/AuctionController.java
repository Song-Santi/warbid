package com.auctiononline.warbidrestful.controllers;


import com.auctiononline.warbidrestful.payload.request.AuctionRegisterRequest;
import com.auctiononline.warbidrestful.payload.request.AuctionRequest;
import com.auctiononline.warbidrestful.payload.response.GetAllResponse;
import com.auctiononline.warbidrestful.payload.response.MessageResponse;
import com.auctiononline.warbidrestful.services.ilterface.AuctionService;
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
@RequestMapping("/api/auction")
public class AuctionController {
    @Autowired
    private AuctionService auctionService;

    @PostMapping("/register-auction")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> registerAuction(@RequestBody @Valid AuctionRegisterRequest auctionRegisterRequest, BindingResult bindingResult) {
        ResponseEntity<?> invalidResponse = UserController.invalidResponse(bindingResult);
        if (invalidResponse != null) {
            return invalidResponse;
        }
        MessageResponse auctionRegisterResponse = auctionService.registerAuction(auctionRegisterRequest);
        return ResponseEntity.status(HttpStatus.OK).body(auctionRegisterResponse);
    }

    @PostMapping("/auctioning")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> incrementPriceAuction(@RequestBody @Valid AuctionRequest auctionRequest, BindingResult bindingResult) {
        ResponseEntity<?> invalidResponse = UserController.invalidResponse(bindingResult);
        if (invalidResponse != null) {
            return invalidResponse;
        }
        MessageResponse auctionRegisterResponse = auctionService.updatePriceIncrease(auctionRequest);
        return ResponseEntity.status(HttpStatus.OK).body(auctionRegisterResponse);
    }

    @GetMapping("/get-best-price/{productId}")
    public ResponseEntity<?> getBestPrice(@PathVariable Long productId) {
        GetAllResponse postLabelResponse = auctionService.bestAuctionProduct(productId);
        return ResponseEntity.status(HttpStatus.OK).body(postLabelResponse);
    }
}
