package com.v1.iskream.layer.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class OrdersTest {

    private Orders defaultAllArgumentOrders(){
        return new Orders(1L, new User(), new Price());
    }

    private Orders defaultNoArgumentOrders(){
        return new Orders();
    }

    private Orders defaultWithUserAndPriceOrders(){
        return new Orders(new User(), new Price());
    }

    @Test
    @DisplayName("1. All Argument 생성자 테스트")
    void buildAllArgument(){
        Orders orders = defaultAllArgumentOrders();
        assertNotNull(orders);
    }

    @Test
    @DisplayName("2. No Argument 생성자 테스트")
    void buildNoArgument(){
        Orders orders = defaultNoArgumentOrders();
        assertNotNull(orders);
    }

    @Test
    @DisplayName("3. No Argument 생성자 테스트")
    void buildWithUserAndPrice(){
        Orders orders = defaultWithUserAndPriceOrders();
        assertNotNull(orders);
    }

    @Test
    void getId() {
        Orders orders = defaultAllArgumentOrders();
        assertEquals(orders.getId(), 1L);
    }

    @Test
    void getBuyer() {
        Orders orders = defaultAllArgumentOrders();
        assertNotNull(orders.getBuyer());
    }

    @Test
    void getPrice() {
        Orders orders = defaultAllArgumentOrders();
        assertNotNull(orders.getPrice());
    }
}