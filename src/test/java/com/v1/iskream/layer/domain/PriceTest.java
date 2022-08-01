package com.v1.iskream.layer.domain;

import com.v1.iskream.layer.domain.dto.request.ProductRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PriceTest {

    private Price defaultPriceWithRequestDto(){
        return new Price(new ProductRequestDto(), new Product(), new User());
    }

    private Price defaultAllArgumentPrice(){
        return new Price(1L, 240, new Product(), 10000, new User());
    }

    private Price defaultNoArgumentPrice(){
        return new Price();
    }

    @Test
    @DisplayName("1. All Argument 생성자 테스트")
    void buildAllArgument(){
        Price price = defaultAllArgumentPrice();
        assertNotNull(price);
    }

    @Test
    @DisplayName("2. No Argument 생성자 테스트")
    void buildNoArgument(){
        Price price = defaultNoArgumentPrice();
        assertNotNull(price);
    }

    @Test
    @DisplayName("3. With Request Dto 생성자 테스트")
    void buildWithRequestDto(){
        Price price = defaultPriceWithRequestDto();
        assertNotNull(price);
    }

    @Test
    @DisplayName("5. Getter 테스트 - id")
    void getId() {
        Price price = defaultAllArgumentPrice();
        assertEquals(price.getId(), 1L);
    }

    @Test
    @DisplayName("6. Getter 테스트 - size")
    void getSize() {
        Price price = defaultAllArgumentPrice();
        assertEquals(price.getSize(), 240);
    }

    @Test
    @DisplayName("7. Getter 테스트 - product")
    void getProduct() {
        Price price = defaultAllArgumentPrice();
        assertNotNull(price.getProduct());
    }

    @Test
    @DisplayName("8. Getter 테스트 - price")
    void getPrice() {
        Price price = defaultAllArgumentPrice();
        assertEquals(price.getPrice(),10000);
    }

    @Test
    @DisplayName("9. Getter 테스트 - seller")
    void getSeller() {
        Price price = defaultAllArgumentPrice();
        assertNotNull(price.getSeller());
    }

    @Test
    @DisplayName("10. Setter 테스트 - product")
    void setProduct() {
        Price price = defaultNoArgumentPrice();
        price.setProduct(new Product());
        assertNotNull(price.getProduct());
    }
}