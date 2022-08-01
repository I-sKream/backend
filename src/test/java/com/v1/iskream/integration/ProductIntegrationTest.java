package com.v1.iskream.integration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.v1.iskream.layer.domain.dto.response.ProductResponseDto;
import lombok.Builder;
import lombok.Getter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.Instant;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductIntegrationTest {

    @Autowired
    TestRestTemplate testRestTemplate;
    private static final Algorithm ALGORITHM = Algorithm.HMAC256("carrykim");

    /**
     * 로컬 h2 데이터베이스에 임의의 유저, product 넣어놓고 테스트
     * user1(id='test1', nickname='nickname', password='1234')
     * pruduct1(id=1,brand='Nike',nameEng='Nike',nameKor='나이키')
     */
    public HttpEntity<?> getHeader(ProductRequest productRequest){
        String username="test1";
        String password ="1234";
        String token = JWT.create()
                .withSubject(username)
                .withClaim("exp", Instant.now().getEpochSecond() + 60*60)
                .sign(ALGORITHM);
        String authorizationHeader = "Bearer " + token;

        HttpHeaders requestHeaders = new HttpHeaders();
                    requestHeaders.setContentType(MediaType.APPLICATION_JSON);
                    requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                    requestHeaders.add("Authorization",authorizationHeader);

        return new HttpEntity<>(productRequest,requestHeaders);
    }

    @Nested
    @DisplayName("상품 상세 조회")
    class ProductDetails{

        @Test
        @DisplayName("성공")
        void test11(){
            //given
            long productId = 1;

            //when
            ResponseEntity<ProductResponseDto> response = testRestTemplate
                    .getForEntity(
                            "/api/products/"+productId,
                            ProductResponseDto.class
                    );
            //then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            ProductResponseDto product = response.getBody();
            assertNotNull(product);

            assertEquals("Nike",product.getProduct_name_eng());
            assertEquals("나이키",product.getProduct_name_kor());
            assertEquals("Nike",product.getProduct_brand());
            assertEquals(240,product.getPrices().get(0).getSize());
            assertEquals(10000,product.getPrices().get(0).getPrice());
            assertEquals(0,product.getPrices().get(0).getPrice_diff());
        }
    }

    @Nested
    @DisplayName("상품 구매")
    class ProductBuy{

        @Nested
        @DisplayName("실패")
        class Fail{
            @Test
            @DisplayName("상품 x")
            void test6(){
                //given
                long productId = 2;

                ProductRequest productRequest = ProductRequest.builder()
                        .size(240).build();

                HttpEntity<?> requestEntity = getHeader(productRequest);

                //when
                ResponseEntity<String> response = testRestTemplate
                        .withBasicAuth("test1","1234")
                        .postForEntity(
                                "/api/products/buy/"+productId,
                                requestEntity,
                                String.class
                        );
                //then
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertEquals("상품을 찾을 수 없습니다.",response.getBody());
            }

            @Test
            @DisplayName("사이즈 x")
            void test7(){
                //given
                long productId = 1;

                ProductRequest productRequest = ProductRequest.builder()
                        .size(250).build();

                HttpEntity<?> requestEntity = getHeader(productRequest);

                //when
                ResponseEntity<String> response = testRestTemplate
                        .postForEntity(
                                "/api/products/buy/"+productId,
                                requestEntity,
                                String.class
                        );
                //then
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertEquals("해당 사이즈가 없습니다.",response.getBody());
            }

            @Test
            @DisplayName("사이즈 0")
            void test8(){
                //given
                long productId = 1;

                ProductRequest productRequest = ProductRequest.builder()
                        .size(0).build();

                HttpEntity<?> requestEntity = getHeader(productRequest);

                //when
                ResponseEntity<String> response = testRestTemplate
                        .postForEntity(
                                "/api/products/buy/"+productId,
                                requestEntity,
                                String.class
                        );
                //then
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertEquals("값을 입력해주세요.",response.getBody());
            }

            @Test
            @DisplayName("로그인 안 함")
            void test9(){
                //given
                long productId = 1;

                ProductRequest productRequest = ProductRequest.builder()
                        .size(240).build();

                //when
                ResponseEntity<String> response = testRestTemplate
                        .postForEntity(
                                "/api/products/buy/"+productId,
                                productRequest,
                                String.class
                        );
                //then
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertEquals("로그인이 필요합니다.",response.getBody());
            }
        }

        @Nested
        @DisplayName("성공")
        class Success{

            @Test
            @DisplayName("구매 정상")
            void test10(){
                //given
                long productId = 1;

                ProductRequest productRequest = ProductRequest.builder()
                        .size(240).build();

                HttpEntity<?> requestEntity = getHeader(productRequest);

                //when
                ResponseEntity<String> response = testRestTemplate
                        .withBasicAuth("test1","1234")
                        .postForEntity(
                                "/api/products/buy/"+productId,
                                requestEntity,
                                String.class
                        );
                //then
                assertEquals(HttpStatus.CREATED, response.getStatusCode());
                assertEquals("구매 성공",response.getBody());
            }
        }
    }

    @Nested
    @DisplayName("상품 판매")
    class ProductSell{

        @Nested
        @DisplayName("실패")
        class Fail{
            @Test
            @DisplayName("상품 x")
            void test0(){
                //given
                long productId = 2;

                ProductRequest productRequest = ProductRequest.builder()
                        .size(240).price(10000).build();

                HttpEntity<?> requestEntity = getHeader(productRequest);

                //when
                ResponseEntity<String> response = testRestTemplate
                        .withBasicAuth("test1","1234")
                        .postForEntity(
                                "/api/products/sell/"+productId,
                                requestEntity,
                                String.class
                        );
                //then
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertEquals("상품을 찾을 수 없습니다.",response.getBody());
            }

            @Test
            @DisplayName("사이즈, 가격 0")
            void test1(){
                //given
                long productId = 1;

                ProductRequest productRequest = ProductRequest.builder()
                        .size(0).price(0).build();

                HttpEntity<?> requestEntity = getHeader(productRequest);

                //when
                ResponseEntity<String> response = testRestTemplate
                        .withBasicAuth("test1","1234")
                        .postForEntity(
                                "/api/products/sell/"+productId,
                                requestEntity,
                                String.class
                        );
                //then
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertEquals("값을 입력해주세요.",response.getBody());
            }

            @Test
            @DisplayName("사이즈 0")
            void test2(){
                //given
                long productId = 1;

                ProductRequest productRequest = ProductRequest.builder()
                        .size(0).price(10000).build();

                HttpEntity<?> requestEntity = getHeader(productRequest);

                //when
                ResponseEntity<String> response = testRestTemplate
                        .withBasicAuth("test1","1234")
                        .postForEntity(
                                "/api/products/sell/"+productId,
                                requestEntity,
                                String.class
                        );
                //then
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertEquals("값을 입력해주세요.",response.getBody());
            }

            @Test
            @DisplayName("가격 0")
            void test3(){
                //given
                long productId = 1;

                ProductRequest productRequest = ProductRequest.builder()
                        .size(240).price(0).build();

                HttpEntity<?> requestEntity = getHeader(productRequest);

                //when
                ResponseEntity<String> response = testRestTemplate
                        .withBasicAuth("test1","1234")
                        .postForEntity(
                                "/api/products/sell/"+productId,
                                requestEntity,
                                String.class
                        );
                //then
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertEquals("값을 입력해주세요.",response.getBody());
            }

            @Test
            @DisplayName("로그인 안 함")
            void test4(){
                //given
                long productId = 1;

                ProductRequest productRequest = ProductRequest.builder()
                        .size(240).price(10000).build();

                //when
                ResponseEntity<String> response = testRestTemplate
                        .postForEntity(
                                "/api/products/sell/"+productId,
                                productRequest,
                                String.class
                        );
                //then
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertEquals("로그인이 필요합니다.",response.getBody());
            }
        }

        @Nested
        @DisplayName("성공")
        class Success{

            @Test
            @DisplayName("판매 정상")
            void test5(){
                //given
                long productId = 1;

                ProductRequest productRequest = ProductRequest.builder()
                        .size(240).price(10000).build();

                HttpEntity<?> requestEntity = getHeader(productRequest);

                //when
                ResponseEntity<String> response = testRestTemplate
                        .withBasicAuth("test1","1234")
                        .postForEntity(
                                "/api/products/sell/"+productId,
                                requestEntity,
                                String.class
                        );
                //then
                assertEquals(HttpStatus.CREATED, response.getStatusCode());
                assertEquals("판매 성공",response.getBody());
            }
        }
    }

    @Getter
    @Builder
    static class ProductRequest{
        int size;
        int price;
    }
}

