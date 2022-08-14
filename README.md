# backend

### ****ER Diagram****

![Untitled](https://user-images.githubusercontent.com/66009926/184541022-0df6ce0b-b7ed-439b-a288-e76dab3b922d.png)


### ****Folder Structure****

```bash
.
├── main
│   ├── java
│   │   └── com
│   │       └── v1
│   │           └── iskream
│   │               ├── IskreamApplication.java
│   │               ├── config
│   │               │   ├── GlobalExceptionHandler.java
│   │               │   ├── empty.txt
│   │               │   └── security
│   │               │       ├── SecurityConfig.java
│   │               │       ├── UnauthorizedEntryPoint.java
│   │               │       ├── filter
│   │               │       │   ├── JWTCheckFilter.java
│   │               │       │   └── JWTLoginFilter.java
│   │               │       ├── jwt
│   │               │       │   └── JWTUtil.java
│   │               │       ├── passwordEncoder
│   │               │       │   └── PasswordEncoder.java
│   │               │       └── userDtail
│   │               │           ├── UserDetailImpl.java
│   │               │           └── UserDetailServiceImpl.java
│   │               └── layer
│   │                   ├── controller
│   │                   │   ├── ProductController.java
│   │                   │   └── UserController.java
│   │                   ├── domain
│   │                   │   ├── AvgPrice.java
│   │                   │   ├── Orders.java
│   │                   │   ├── Price.java
│   │                   │   ├── Product.java
│   │                   │   ├── RecentProductInterface.java
│   │                   │   ├── Thumbnail.java
│   │                   │   ├── Timestamped.java
│   │                   │   ├── User.java
│   │                   │   ├── dto
│   │                   │       ├── request
│   │                   │       │   ├── LoginRequestDto.java
│   │                   │       │   ├── ProductRequestDto.java
│   │                   │       │   └── SignupRequestDto.java
│   │                   │       └── response
│   │                   │           ├── AvgPriceResponseDto.java
│   │                   │           ├── LoginResponseDto.java
│   │                   │           ├── PriceResponseDto.java
│   │                   │           ├── ProductResponseDto.java
│   │                   │           ├── SimpleProductResponseDto.java
│   │                   │           └── ThumbnailResponseDto.java
│   │                   ├── repository
│   │                   │   ├── AvgPriceRepository.java
│   │                   │   ├── OrdersRepository.java
│   │                   │   ├── PriceRepository.java
│   │                   │   ├── ProductRepository.java
│   │                   │   ├── ThumbnailRepository.java
│   │                   │   └── UserRepository.java
│   │                   └── service
│   │                       ├── ProductService.java
│   │                       └── UserService.java
│   └── resources
│       └── application.properties
└── test
    ├── java
    │   └── com
    │       └── v1
    │           └── iskream
    │               ├── IskreamApplicationTests.java
    │               ├── integration
    │               │   ├── ChartIntegrationTest.java
    │               │   ├── ProductIntegrationTest.java
    │               │   └── UserIntegrationTest.java
    │               └── layer
    │                   ├── domain
    │                   │   ├── OrdersTest.java
    │                   │   ├── PriceTest.java
    │                   │   ├── ProductTest.java
    │                   │   ├── ThumbnailTest.java
    │                   │   └── UserTest.java
    │                   └── service
    └── resources
        └── application-test.properties
```

### ****API****

https://elderly-gruyere-ed2.notion.site/API-bbb5d4593fd34997bd57d5e32ba0b111

### Properties

```
# MYSQL 설정
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url="mysql server address"
spring.datasource.username= "mysql username"
spring.datasource.password= "mysql password"
spring.jpa.database=mysql
spring.jpa.hibernate.ddl-auto=update
```

### ****Development****

```python
Based on JAVA 8 JDK with Java Language
```

### ****Requirement****

```python
JAVA JDK : >= 8
Spring boot : >= 2.6.1
```

### ****Build Setup****

```bash
$ ./gradlew build
$ ./gradlew bootjar
$ java -jar 프로젝트명.jar
```

### ****Branch Management****

```python
#Branch naming rules
main
항상 보호되는 안정된 브랜치

dev
새 버전 준비를 위한 개발 브랜치

hotfix/#notification_list
이슈 해결을 위한 브랜치, 기능 명을 기입하여 구분

feature/#notification_list
기능 추가/제거를 위한 브랜치, 기능명을 기입하여 구분

#Contribute method
1. Main 브랜치는 항상 안정된 빌드이자 사용자에게 서비스중인 빌드입니다.
2. 모든 feature 브랜치는 dev 브랜치에서 클론한다.
3. feature 브랜치는 기능 단위로 구분한다.
4. feature 작업이 끝나면 dev branch로 pull request 한다.
5. dev branch에서 모든 취합이 끝나면 main으로 pull request 한다.
6. main에서 실제 서버 코드를 돌린다.
7. merge 후 브랜치는 삭제한다.
```
