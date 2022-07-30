package com.v1.iskream.layer.service;

import com.v1.iskream.layer.domain.Orders;
import com.v1.iskream.layer.domain.Price;
import com.v1.iskream.layer.domain.Product;
import com.v1.iskream.layer.domain.User;
import com.v1.iskream.layer.dto.PriceResponseDto;
import com.v1.iskream.layer.dto.ProductRequestDto;
import com.v1.iskream.layer.dto.ProductResponseDto;
import com.v1.iskream.layer.repository.OrdersRepository;
import com.v1.iskream.layer.repository.PriceRepository;
import com.v1.iskream.layer.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final OrdersRepository ordersRepository;
    private final PriceRepository priceRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, OrdersRepository ordersRepository, PriceRepository priceRepository) {
        this.productRepository = productRepository;
        this.ordersRepository = ordersRepository;
        this.priceRepository = priceRepository;
    }

    public ProductResponseDto details(Long product_id) {
        Product product = productRepository.findById(product_id).orElseThrow(
                () -> new IllegalArgumentException("상품을 찾을 수 업습니다.")
        );
        List<Price> prices = priceRepository.findAllByProduct(product);
//        List<Price> prices = product.getPrices();
        Price minPrice = new Price();
        int totalPrice = 0;
        for(Price price : prices) {
            totalPrice += price.getPrice();
            minPrice = priceRepository.findTopByProductAndSizeOrderByPriceAsc(product, price.getSize());
        }
        int averagePrice = totalPrice/prices.size();
        int priceDiff = minPrice.getPrice()-averagePrice;
        return new ProductResponseDto(product, minPrice, priceDiff);
    }

    public ResponseEntity<String> buy(Long product_id, ProductRequestDto requestDto, User user) {
        Product product = productRepository.findById(product_id).orElseThrow(
                () -> new IllegalArgumentException("상품을 찾을 수 업습니다.")
        );
        Price price = priceRepository.findTopByProductAndSizeOrderByPriceAsc(product, requestDto.getSize());
        if (price != null) {
            product.addPrice(price);
            Orders order = new Orders(user, price);
            ordersRepository.save(order);
        } else throw new IllegalArgumentException("해당 사이즈가 없습니다.");
        String msg = "구매 성공";
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}
