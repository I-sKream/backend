package com.v1.iskream.integration;

import com.v1.iskream.layer.domain.dto.response.AvgPriceResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChartIntegrationTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    @Nested
    @DisplayName("차트 조회")
    class ProductChart{

        @Test
        @DisplayName("성공")
        void test1(){
            //given
            long productId = 2;

            //when
            ResponseEntity<AvgPriceResponseDto[]> response = testRestTemplate
                    .getForEntity(
                            "/api/products/avgprice/"+productId,
                            AvgPriceResponseDto[].class
                    );
            //then
            AvgPriceResponseDto[] charts = response.getBody();
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(charts);
            assertEquals(7, charts.length);
        }
    }
}
