package com.v1.iskream.layer.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.AbstractList;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Product defaultAllArgumentProduct(){
        return new Product(1L, "Nike", "Nike", "나이키", new ArrayList<>(), new ArrayList<>());
    }

    private Product defaultNoArgumentProduct(){
        return new Product();
    }

    @Test
    @DisplayName("1. All Argument 생성자 테스트")
    void buildAllArgument(){
        Product product = defaultAllArgumentProduct();
        assertNotNull(product);
    }

    @Test
    @DisplayName("2. No Argument 생성자 테스트")
    void buildNoArgument(){
        Product product = defaultNoArgumentProduct();
        assertNotNull(product);
    }

    @Test
    void getId() {
        Product product = defaultAllArgumentProduct();
        assertEquals(product.getId(), 1L);
    }

    @Test
    void getBrand() {
        Product product = defaultAllArgumentProduct();
        assertEquals(product.getBrand(), "Nike");
    }

    @Test
    void getNameEng() {
        Product product = defaultAllArgumentProduct();
        assertEquals(product.getNameEng(), "Nike");
    }

    @Test
    void getNameKor() {
        Product product = defaultAllArgumentProduct();
        assertEquals(product.getNameKor(), "나이키");
    }

    @Test
    void getPrices() {
        Product product = defaultAllArgumentProduct();
        assertEquals(product.getPrices(), new ArrayList<>());
    }

    @Test
    void getThumbnails() {
        Product product = defaultAllArgumentProduct();
        assertEquals(product.getThumbnails(), new ArrayList<>());
    }


    @Test
    void addPrice() {
        Product product = defaultAllArgumentProduct();
        product.addPrice(new Price());
        assertEquals(product.getPrices().size(), 1);
    }


    @Test
    void addThumbnail() {
        Product product = defaultAllArgumentProduct();
        product.addThumbnail(new Thumbnail());
        assertEquals(product.getThumbnails().size(), 1);
    }
}