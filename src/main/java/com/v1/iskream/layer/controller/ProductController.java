package com.v1.iskream.layer.controller;

import com.v1.iskream.layer.domain.User;
import com.v1.iskream.layer.domain.dto.request.ProductRequestDto;
import com.v1.iskream.layer.domain.dto.response.ProductResponseDto;
import com.v1.iskream.layer.domain.dto.response.RecentProductResponseDto;
import com.v1.iskream.layer.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Validated
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

    @GetMapping("/api/products/recent")
    public ResponseEntity getRecentProducts(){
        List<RecentProductResponseDto> productResponseDtoList = productService.getProducts(15,0);
        return new ResponseEntity(productResponseDtoList, HttpStatus.OK);
    }

    @GetMapping("/api/products")
    public ResponseEntity getProducts(@RequestParam @Min(1) int page){
        List<RecentProductResponseDto> productResponseDtoList = productService.getProducts(15, (page-1) * 15);
        return new ResponseEntity(productResponseDtoList, HttpStatus.OK);
    }
}
