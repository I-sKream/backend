package com.v1.iskream.layer.service;

import com.v1.iskream.layer.domain.*;
import com.v1.iskream.layer.domain.dto.request.ProductRequestDto;
import com.v1.iskream.layer.domain.dto.response.*;
import com.v1.iskream.layer.repository.AvgPriceRepository;
import com.v1.iskream.layer.repository.OrdersRepository;
import com.v1.iskream.layer.repository.PriceRepository;
import com.v1.iskream.layer.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final OrdersRepository ordersRepository;
    private final PriceRepository priceRepository;

    private final AvgPriceRepository avgPriceRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, OrdersRepository ordersRepository, PriceRepository priceRepository, AvgPriceRepository avgPriceRepository) {
        this.productRepository = productRepository;
        this.ordersRepository = ordersRepository;
        this.priceRepository = priceRepository;
        this.avgPriceRepository = avgPriceRepository;
    }

    // 상품 상세조회
    public ProductResponseDto details(Long product_id) {
        Product product = productRepository.findById(product_id).orElseThrow(
                () -> new IllegalArgumentException("상품을 찾을 수 없습니다.")
        );
        List<Thumbnail> thumbnails = product.getThumbnails();
        List<ThumbnailResponseDto> thumbList = new ArrayList<>();
        for(Thumbnail thumbnail : thumbnails){
            ThumbnailResponseDto responseDto = new ThumbnailResponseDto(thumbnail.getUrl());
            thumbList.add(responseDto);
        }
//        List<Price> prices = product.getPrices();
//        prices.sort(Collections.reverseOrder());
        List<Price> prices = priceRepository.findAllByProductOrderByPriceDesc(product);
        List<PriceResponseDto> priceList = new ArrayList<>();
        Map<Integer,PriceResponseDto> map = new HashMap<>();
        for(Price price : prices) {
            List<Price> sizes = priceRepository.findAllByProductAndSizeOrderByPriceDesc(product, price.getSize());
            int totalPrice = 0;
            for(Price size : sizes) totalPrice += size.getPrice();
            int averagePrice = totalPrice/sizes.size();
            int priceDiff = price.getPrice()-averagePrice;
            PriceResponseDto responseDto = new PriceResponseDto(price, priceDiff);
            map.put(price.getSize(), responseDto);
        }
        Set<Integer> keySet = map.keySet();
        for(Integer key : keySet) priceList.add(map.get(key));
        Collections.sort(priceList);
        return new ProductResponseDto(product,priceList,thumbList);
    }

    // 상품 구매
    public ResponseEntity<String> buy(Long product_id, ProductRequestDto requestDto, User user) {
        Product product = productRepository.findById(product_id).orElseThrow(
                () -> new IllegalArgumentException("상품을 찾을 수 없습니다.")
        );
        if(NotLogin(user)) throw new IllegalArgumentException("로그인이 필요합니다.");
        if(EmptyValue(requestDto)) throw new IllegalArgumentException("값을 입력해주세요.");
        Price price = priceRepository.findTopByProductAndSizeOrderByPriceAsc(product, requestDto.getSize());
        if (price != null) {
            Orders order = new Orders(user, price);
            ordersRepository.save(order);
        } else throw new IllegalArgumentException("해당 사이즈가 없습니다.");
        String msg = "구매 성공";
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }

    // 상품 판매
    public ResponseEntity<String> sell(Long product_id, ProductRequestDto requestDto, User user){
        Product product = productRepository.findById(product_id).orElseThrow(
                () -> new IllegalArgumentException("상품을 찾을 수 없습니다.")
        );
        if(NotLogin(user)) throw new IllegalArgumentException("로그인이 필요합니다.");
        if(EmptyValues(requestDto)) throw new IllegalArgumentException("값을 입력해주세요.");
        if(UnitPrice(requestDto)) throw new IllegalArgumentException("가격은 천원단위로 입력해주세요.");
        Price price = new Price(requestDto,product,user);
        product.addPrice(price);
        priceRepository.save(price);
        String msg = "판매 성공";
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }

    // 최근 등록 상품 조회 로직
    public List<SimpleProductResponseDto> getProducts(int limit, int offset){
        return productRepository.findRecentProduct(limit,offset).stream()
                .map(x -> mappingRecentProductResponse(x))
                .collect(Collectors.toList());
    }

    public List<AvgPriceResponseDto> getAvgPrice(Long product_id){
        List<AvgPrice> list = avgPriceRepository.avgPriceById(product_id);
        List<AvgPriceResponseDto> responseDtoList = new ArrayList<>();
        for(int i = 0; i < 7; i++){
            responseDtoList.add(mappingAvgPriceResponseDto(list.get(i)));
        }
        return responseDtoList;
    }
    private BigInteger getAvgPriceFromProduct(Long product_id){
        BigInteger price = priceRepository.avgPriceById(product_id);
        price = price.divide(BigInteger.valueOf(10000));
        price = price.multiply(BigInteger.valueOf(10000));
        return price;
    }


    // Model to DTO
    private AvgPriceResponseDto mappingAvgPriceResponseDto(AvgPrice avgPrice){
        return new AvgPriceResponseDto(avgPrice.getProductId(), avgPrice.getDate(), avgPrice.getAvgPrice());
    }

    // recent product 조회 값 response 변환
    private SimpleProductResponseDto mappingRecentProductResponse(RecentProductInterface recentProductInterface){
        return SimpleProductResponseDto.builder()
                .id(recentProductInterface.getId())
                .product_name_eng(recentProductInterface.getProductNameEng())
                .product_brand(recentProductInterface.getProductBrand())
                .thumbnail(recentProductInterface.getThumbnail())
                .product_price(getAvgPriceFromProduct(recentProductInterface.getId()))
                .build();
    }


    // Check logic
    public boolean NotLogin(User user){
        return user == null;
    }
    public boolean EmptyValue(ProductRequestDto requestDto){
        return requestDto.getSize() == 0;
    }
    public boolean EmptyValues(ProductRequestDto requestDto){
        return requestDto.getSize() == 0 || requestDto.getPrice() == 0;
    }
    public boolean UnitPrice(ProductRequestDto requestDto){
        return requestDto.getPrice() % 1000 != 0;
    }
}
