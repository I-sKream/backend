package com.v1.iskream.integration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.v1.iskream.config.security.passwordEncoder.PasswordEncoder;
import com.v1.iskream.layer.domain.Price;
import com.v1.iskream.layer.domain.Product;
import com.v1.iskream.layer.domain.Thumbnail;
import com.v1.iskream.layer.domain.User;
import com.v1.iskream.layer.domain.dto.response.AvgPriceResponseDto;
import com.v1.iskream.layer.domain.dto.response.ProductResponseDto;
import com.v1.iskream.layer.repository.PriceRepository;
import com.v1.iskream.layer.repository.ProductRepository;
import com.v1.iskream.layer.repository.ThumbnailRepository;
import com.v1.iskream.layer.repository.UserRepository;
import lombok.Builder;
import lombok.Getter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProductIntegrationTest {

    @Autowired
    TestRestTemplate testRestTemplate;
    @BeforeAll
    private static void setDB(
            @Autowired UserRepository userRepository,
            @Autowired ProductRepository productRepository,
            @Autowired PriceRepository priceRepository,
            @Autowired ThumbnailRepository thumbnailRepository,
            @Autowired PasswordEncoder passwordEncoder){
        User user = new User("test1", passwordEncoder.encode("1234"),"nickname");
        userRepository.save(user);

        for(int i = 0; i < 16; i++){
            String index = Integer.toString(i);
            Product product = new Product(null,"Nike"+index,"Nike"+index,"?????????"+index);
            productRepository.save(product);
            Price price = new Price(null, 240, product, 10000, user);
            Thumbnail thumbnail = new Thumbnail(null, product, "test"+index);
            priceRepository.save(price);
            thumbnailRepository.save(thumbnail);
        }
    }

    /**
     * ?????? h2 ????????????????????? ????????? ??????, product ???????????? ?????????
     * user1(id='test1', nickname='nickname', password='1234')
     * pruduct1(id=1,brand='Nike',nameEng='Nike',nameKor='?????????')
     */
    private static final Algorithm ALGORITHM = Algorithm.HMAC256("carrykim");
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
    @DisplayName("?????? ?????? ??????")
    class ProductDetails{

        @Test
        @DisplayName("??????")
        void test1(){
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

            assertEquals("Nike0",product.getProduct_name_eng());
            assertEquals("?????????0",product.getProduct_name_kor());
            assertEquals("Nike0",product.getProduct_brand());
            assertEquals(240,product.getPrices().get(0).getSize());
            assertEquals(10000,product.getPrices().get(0).getPrice());
            assertEquals(0,product.getPrices().get(0).getPrice_diff());
        }
    }

    @Nested
    @DisplayName("?????? ??????")
    class ProductBuy{

        @Nested
        @DisplayName("??????")
        class Fail{
            @Test
            @DisplayName("?????? ??????")
            void test1(){
                //given
                long productId = 17;

                ProductRequest productRequest = ProductRequest.builder()
                        .size(240).build();

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
                assertEquals("????????? ?????? ??? ????????????.",response.getBody());
            }

            @Test
            @DisplayName("????????? ??????")
            void test2(){
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
                assertEquals("?????? ???????????? ????????????.",response.getBody());
            }

            @Test
            @DisplayName("????????? 0")
            void test3(){
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
                assertEquals("?????? ??????????????????.",response.getBody());
            }

            @Test
            @DisplayName("????????? ??? ???")
            void test4(){
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
                assertEquals("???????????? ???????????????.",response.getBody());
            }
        }

        @Nested
        @DisplayName("??????")
        class Success{

            @Test
            @DisplayName("?????? ??????")
            void test5(){
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
                assertEquals("?????? ??????",response.getBody());
            }
        }
    }

    @Nested
    @DisplayName("?????? ??????")
    class ProductSell{

        @Nested
        @DisplayName("??????")
        class Fail{
            @Test
            @DisplayName("?????? ??????")
            void test1(){
                //given
                long productId = 17;

                ProductRequest productRequest = ProductRequest.builder()
                        .size(240).price(10000).build();

                HttpEntity<?> requestEntity = getHeader(productRequest);

                //when
                ResponseEntity<String> response = testRestTemplate
                        .postForEntity(
                                "/api/products/sell/"+productId,
                                requestEntity,
                                String.class
                        );
                //then
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertEquals("????????? ?????? ??? ????????????.",response.getBody());
            }

            @Test
            @DisplayName("?????????, ?????? 0")
            void test2(){
                //given
                long productId = 1;

                ProductRequest productRequest = ProductRequest.builder()
                        .size(0).price(0).build();

                HttpEntity<?> requestEntity = getHeader(productRequest);

                //when
                ResponseEntity<String> response = testRestTemplate
                        .postForEntity(
                                "/api/products/sell/"+productId,
                                requestEntity,
                                String.class
                        );
                //then
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertEquals("?????? ??????????????????.",response.getBody());
            }

            @Test
            @DisplayName("????????? 0")
            void test3(){
                //given
                long productId = 1;

                ProductRequest productRequest = ProductRequest.builder()
                        .size(0).price(10000).build();

                HttpEntity<?> requestEntity = getHeader(productRequest);

                //when
                ResponseEntity<String> response = testRestTemplate
                        .postForEntity(
                                "/api/products/sell/"+productId,
                                requestEntity,
                                String.class
                        );
                //then
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertEquals("?????? ??????????????????.",response.getBody());
            }

            @Test
            @DisplayName("?????? 0")
            void test4(){
                //given
                long productId = 1;

                ProductRequest productRequest = ProductRequest.builder()
                        .size(240).price(0).build();

                HttpEntity<?> requestEntity = getHeader(productRequest);

                //when
                ResponseEntity<String> response = testRestTemplate
                        .postForEntity(
                                "/api/products/sell/"+productId,
                                requestEntity,
                                String.class
                        );
                //then
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertEquals("?????? ??????????????????.",response.getBody());
            }

            @Test
            @DisplayName("?????? ?????? ??????")
            void test5(){
                //given
                long productId = 1;

                ProductRequest productRequest = ProductRequest.builder()
                        .size(240).price(1000100).build();

                HttpEntity<?> requestEntity = getHeader(productRequest);

                //when
                ResponseEntity<String> response = testRestTemplate
                        .postForEntity(
                                "/api/products/sell/"+productId,
                                requestEntity,
                                String.class
                        );
                //then
                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertEquals("????????? ??????????????? ??????????????????.",response.getBody());
            }

            @Test
            @DisplayName("????????? ??? ???")
            void test6(){
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
                assertEquals("???????????? ???????????????.",response.getBody());
            }
        }

        @Nested
        @DisplayName("??????")
        class Success{

            @Test
            @DisplayName("?????? ??????")
            void test7(){
                //given
                long productId = 1;

                ProductRequest productRequest = ProductRequest.builder()
                        .size(240).price(10000).build();

                HttpEntity<?> requestEntity = getHeader(productRequest);

                //when
                ResponseEntity<String> response = testRestTemplate
                        .postForEntity(
                                "/api/products/sell/"+productId,
                                requestEntity,
                                String.class
                        );
                //then
                assertEquals(HttpStatus.CREATED, response.getStatusCode());
                assertEquals("?????? ??????",response.getBody());
            }
        }
    }

    @Nested
    @DisplayName("?????? ?????? ?????? ??????")
    class RecentProduct{
        @Test
        @DisplayName("?????? ??????")
        void test1() {
            //given
            //when
            ResponseEntity<List<SimpleProductResponseDto>> response = testRestTemplate
                    .exchange(
                            "/api/products/recent",
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<List<SimpleProductResponseDto>>() {}
                    );
            //then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(16, response.getBody().size());

        }
    }

    @Nested
    @DisplayName("?????? ?????? ??????")
    class ProductWithPage{
        @Test
        @DisplayName("?????? ??????")
        void test1() {
            //given
            //when
            ResponseEntity<List<SimpleProductResponseDto>> response = testRestTemplate
                    .exchange(
                            "/api/products?page=1",
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<List<SimpleProductResponseDto>>() {}
                    );
            //then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(16, response.getBody().size());

        }
    }

    @Getter
    @Builder
    static class ProductRequest{
        int size;
        int price;
    }

    @Getter
    @Builder
    static class SimpleProductResponseDto {
        private Long id;
        private String thumbnail;
        private String product_name_eng;
        private BigInteger product_price;
        private String product_brand;
    }
}

