package com.v1.iskream.layer.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ThumbnailTest {

    private Thumbnail defaultAllArgumentThumbnail(){
        return new Thumbnail(1L, new Product(), "");
    }

    private Thumbnail defaultNoArgumentThumbnail(){
        return new Thumbnail();
    }

    @Test
    @DisplayName("1. All Argument 생성자 테스트")
    void buildAllArgument(){
        Thumbnail thumbnail = defaultAllArgumentThumbnail();
        assertNotNull(thumbnail);
    }

    @Test
    @DisplayName("2. No Argument 생성자 테스트")
    void buildNoArgument(){
        Thumbnail thumbnail = defaultNoArgumentThumbnail();
        assertNotNull(thumbnail);
    }

    @Test
    void getId() {
        Thumbnail thumbnail = defaultAllArgumentThumbnail();
        assertEquals(thumbnail.getId(), 1L);
    }

    @Test
    void getProduct() {
        Thumbnail thumbnail = defaultAllArgumentThumbnail();
        assertNotNull(thumbnail.getProduct());
    }

    @Test
    void getUrl() {
        Thumbnail thumbnail = defaultAllArgumentThumbnail();
        assertEquals(thumbnail.getUrl(), "");
    }

    @Test
    void setProduct() {
        Thumbnail thumbnail = defaultNoArgumentThumbnail();
        thumbnail.setProduct(new Product());
        assertNotNull(thumbnail.getProduct());
    }
}