package com.v1.iskream.layer.service;
import com.v1.iskream.layer.domain.*;
import com.v1.iskream.layer.domain.dto.request.ProductRequestDto;
import com.v1.iskream.layer.domain.dto.response.PriceResponseDto;
import com.v1.iskream.layer.domain.dto.response.ProductResponseDto;
import com.v1.iskream.layer.domain.dto.response.RecentProductResponseDto;
import com.v1.iskream.layer.domain.dto.response.ThumbnailResponseDto;
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

    @Autowired
    public ProductService(ProductRepository productRepository, OrdersRepository ordersRepository, PriceRepository priceRepository) {
        this.productRepository = productRepository;
        this.ordersRepository = ordersRepository;
        this.priceRepository = priceRepository;
    }

    public ProductResponseDto details(Long product_id) {
        Product product = productRepository.findById(product_id).orElseThrow(
                () -> new IllegalArgumentException("상품을 찾을 수 업습니다.")
        );
        List<Thumbnail> thumbnails = product.getThumbnails();
        List<ThumbnailResponseDto> thumbList = new ArrayList<>();
        for(Thumbnail thumbnail : thumbnails){
            ThumbnailResponseDto responseDto = new ThumbnailResponseDto(thumbnail.getUrl());
            thumbList.add(responseDto);
        }
//        List<Price> prices = product.getPrices();
        List<Price> prices = priceRepository.findAllByProductOrderByPriceDesc(product);
        List<PriceResponseDto> priceList = new ArrayList<>();
        Map<Integer,PriceResponseDto> map = new HashMap<>();
        for(Price price : prices) {
            List<Price> sizes = priceRepository.findAllByProductAndSizeOrderByPriceDesc(product, price.getSize());
            int totalPrice = 0;
            for(Price size : sizes) totalPrice += size.getPrice();
            int averagePrice = totalPrice/sizes.size();
//            Price maxPrice = priceRepository.findTopByProductAndSizeOrderByPriceDesc(product, price.getSize());
            int priceDiff = price.getPrice()-averagePrice;
            PriceResponseDto responseDto = new PriceResponseDto(price, priceDiff);
            map.put(price.getSize(), responseDto);
        }
        Set<Integer> keySet = map.keySet();
        for(Integer key : keySet) priceList.add(map.get(key));
        return new ProductResponseDto(product,priceList,thumbList);
    }

    public ResponseEntity<String> buy(Long product_id, ProductRequestDto requestDto, User user) {
        Product product = productRepository.findById(product_id).orElseThrow(
                () -> new IllegalArgumentException("상품을 찾을 수 업습니다.")
        );
        Price price = priceRepository.findTopByProductAndSizeOrderByPriceAsc(product, requestDto.getSize());
        if (price != null) {
            product.addPrice(price);
            Orders order = new Orders(user, price);
            ordersRepository.save(order);
        } else throw new IllegalArgumentException("해당 사이즈가 없습니다.");
        String msg = "구매 성공";
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    // 최근 등록 상품 조회 로직
    public List<RecentProductResponseDto> recentProduct(){
        return productRepository.findRecentProduct().stream()
                .map(x -> mappingRecentProductResponse(x))
                .collect(Collectors.toList());
    }

    // 평균 가격 구하는 로직 - recentProduct
    private BigInteger getAvgPriceFromProduct(Long product_id){
        BigInteger price = priceRepository.avgPriceById(product_id);
        price = price.divide(BigInteger.valueOf(10000));
        price = price.multiply(BigInteger.valueOf(10000));
        return price;
    }

    // recent product 조회 값 response 변환
    private RecentProductResponseDto mappingRecentProductResponse(RecentProductInterface recentProductInterface){
        return RecentProductResponseDto.builder()
                .product_name_eng(recentProductInterface.getProductNameEng())
                .product_brand(recentProductInterface.getProductBrand())
                .thumbnail(recentProductInterface.getThumbnail())
                .product_price(getAvgPriceFromProduct(recentProductInterface.getId()))
                .build();
    }
}
