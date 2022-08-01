package com.v1.iskream.integration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.v1.iskream.config.security.jwt.JWTUtil;
import com.v1.iskream.config.security.passwordEncoder.PasswordEncoder;
import com.v1.iskream.layer.domain.User;
import com.v1.iskream.layer.domain.dto.response.ProductResponseDto;
import com.v1.iskream.layer.repository.UserRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserIntegrationTest {

    @Autowired
    TestRestTemplate testTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    private void setDB(){
        userRepository.save(new User("user", passwordEncoder.encode("1234"),"kimseonjin"));
    }

    @AfterEach
    private void resetDB(){
        userRepository.deleteAll();
    }

    private static final Algorithm ALGORITHM = Algorithm.HMAC256("carrykim");

    public HttpHeaders getLoginHeader(String username){
        String token = JWT.create()
                .withSubject(username)
                .withClaim("exp", Instant.now().getEpochSecond() + 60*60)
                .sign(ALGORITHM);
        String authorizationHeader = "Bearer " + token;
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.add("Authorization",authorizationHeader);

        return requestHeaders;
    }

    @Nested
    @DisplayName("회원가입")
    class Signup{
        @Test
        @DisplayName("성공")
        void test1(){
            //given
            SignupRequestDto signupRequestDto = new SignupRequestDto("newUser","kimseonjin","1234");
            HttpEntity signupRequest = new HttpEntity<>(signupRequestDto);

            //when
            ResponseEntity<Object> response = testTemplate
                    .postForEntity(
                            "/api/users/signup",
                            signupRequest,
                            Object.class
                    );
            //then
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("실패 - 이미 회원가입된 유저")
        void test2(){
            //given
            SignupRequestDto signupRequestDto = new SignupRequestDto("user","kimseonjin","1234");
            HttpEntity signupRequest = new HttpEntity<>(signupRequestDto);

            //when
            ResponseEntity<String> response = testTemplate
                    .postForEntity(
                            "/api/users/signup",
                            signupRequest,
                            String.class
                    );
            //then
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            String responseBody = response.getBody();
            assertEquals("이미 존재하는 유저입니다.", responseBody);
        }
    }

    @Nested
    @DisplayName("로그인")
    class Login{
        @Test
        @DisplayName("성공")
        void test1(){
            //given
            LoginRequestDto loginRequestDto = new LoginRequestDto("user","1234");
            HttpEntity loginRequest = new HttpEntity<>(loginRequestDto);

            //when
            ResponseEntity<LoginResponseDto> response = testTemplate
                    .postForEntity(
                            "/api/users/login",
                            loginRequest,
                            LoginResponseDto.class
                    );
            //then
            LoginResponseDto product = response.getBody();
            assertNotNull(product);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("로그인에 성공했습니다.",product.getMsg());
            assertNotNull(product.getToken());
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 유저")
        void test2(){
            //given
            LoginRequestDto loginRequestDto = new LoginRequestDto("notUser","1234");
            HttpEntity loginRequest = new HttpEntity<>(loginRequestDto);

            //when
            ResponseEntity<LoginResponseDto> response = testTemplate
                    .postForEntity(
                            "/api/users/login",
                            loginRequest,
                            LoginResponseDto.class
                    );
            //then
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

            LoginResponseDto responseBody = response.getBody();
            assertNotNull(responseBody);
            assertEquals("존재하지 않는 회원입니다.",responseBody.getMsg());
            assertNull(responseBody.getToken());
        }

        @Test
        @DisplayName("실패 - 비밀번호 틀림")
        void test3(){
            //given
            LoginRequestDto loginRequestDto = new LoginRequestDto("user","123456");
            HttpEntity loginRequest = new HttpEntity<>(loginRequestDto);

            //when
            ResponseEntity<LoginResponseDto> response = testTemplate
                    .postForEntity(
                            "/api/users/login",
                            loginRequest,
                            LoginResponseDto.class
                    );
            //then
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

            LoginResponseDto responseBody = response.getBody();
            assertNotNull(responseBody);
            assertEquals("비밀번호가 틀렸습니다.",responseBody.getMsg());
            assertNull(responseBody.getToken());
        }

        @Test
        @DisplayName("실패 - 잘못된 Request 형식")
        void test4(){
            //given
            HttpEntity loginRequest = new HttpEntity<>(null);

            //when
            ResponseEntity<LoginResponseDto> response = testTemplate
                    .postForEntity(
                            "/api/users/login",
                            loginRequest,
                            LoginResponseDto.class
                    );
            //then
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

            LoginResponseDto responseBody = response.getBody();
            assertNotNull(responseBody);
            assertEquals("로그인 요청 형식에 맞지 않습니다.",responseBody.getMsg());
            assertNull(responseBody.getToken());
        }
    }

    @Nested
    @DisplayName("토큰 로그인")
    class LoginWithToken{
        @Test
        @DisplayName("성공")
        void test1(){
            //given
            HttpHeaders httpHeaders = getLoginHeader("user");
            HttpEntity loginRequest = new HttpEntity<>(httpHeaders);

            //when
            ResponseEntity<User> response = testTemplate
                    .exchange(
                            "/api/users/detail/data",
                            HttpMethod.GET,
                            loginRequest,
                            User.class
                    );
            //then
            assertEquals(HttpStatus.OK, response.getStatusCode());
            User responseBody = response.getBody();
            assertNotNull(responseBody);
            assertEquals("user", responseBody.getId());
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 유저")
        void test2(){
            //given
            HttpHeaders httpHeaders = getLoginHeader("user1");
            HttpEntity loginRequest = new HttpEntity<>(httpHeaders);

            //when
            ResponseEntity<String> response = testTemplate
                    .exchange(
                            "/api/users/detail/data",
                            HttpMethod.GET,
                            loginRequest,
                            String.class
                    );
            //then
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            String responseBody = response.getBody();
            assertEquals("존재하지 않는 유저입니다.", responseBody);
        }

        @Test
        @DisplayName("실패 - 유효하지 않은 토큰 형태")
        void test3(){
            //given
            String wrongToken = JWT.create()
                    .withSubject("user")
                    .withClaim("exp", Instant.now().getEpochSecond() + 60*60)
                    .sign(ALGORITHM) + "wrong";

            String authorizationHeader = "Bearer " + wrongToken;
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            requestHeaders.add("Authorization",authorizationHeader);

            HttpEntity loginRequest = new HttpEntity<>(requestHeaders);

            //when
            ResponseEntity<String> response = testTemplate
                    .exchange(
                            "/api/users/detail/data",
                            HttpMethod.GET,
                            loginRequest,
                            String.class
                    );
            //then
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            String responseBody = response.getBody();
            assertEquals("유효하지 않은 토큰입니다.", responseBody);
        }
    }

    @Getter
    @Builder
    static class LoginResponseDto {
        private int status;
        private String msg;
        private String token;
    }

    @Getter
    @Builder
    static class  LoginRequestDto {
        private String id;
        private String password;
    }

    @Getter
    @Builder
    static class SignupRequestDto {
        private String id;
        private String nickname;
        private String password;
    }

}
