package com.v1.iskream.layer.controller;

import com.v1.iskream.layer.domain.User;
import com.v1.iskream.layer.dto.ProductRequestDto;
import com.v1.iskream.layer.dto.ProductResponseDto;
import com.v1.iskream.layer.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/api/products/{product_id}")
    public ProductResponseDto getDetails(@PathVariable Long product_id){
        return productService.details(product_id);
    }

    @PostMapping("/api/products/{product_id}")
    public ResponseEntity<String> getBuy(@PathVariable Long product_id, @RequestBody ProductRequestDto requestDto, @AuthenticationPrincipal User user){
        return productService.buy(product_id,requestDto,user);
    }
}
