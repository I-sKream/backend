package com.v1.iskream.layer.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("1. All Argument 생성자 테스트")
    void buildAllArgument(){
        User user = new User("test123", "test123", "kimseonjin");
        assertNotNull(user);
    }

    @Test
    @DisplayName("2. No Argument 생성자 테스트")
    void buildNoArgument(){
        User user = new User();
        assertNotNull(user);
    }

    @Test
    @DisplayName("3. Getter 테스트 - id")
    void getId() {
        User user = new User("test123", "test123", "kimseonjin");
        assertEquals(user.getId(), "test123");
    }

    @Test
    @DisplayName("4. Getter 테스트 - id")
    void getPassword() {
        User user = new User("test123", "test123", "kimseonjin");
        assertEquals(user.getPassword(), "test123");
    }

    @Test
    @DisplayName("5. Getter 테스트 - id")
    void getNickname() {
        User user = new User("test123", "test123", "kimseonjin");
        assertEquals(user.getNickname(), "kimseonjin");
    }
}