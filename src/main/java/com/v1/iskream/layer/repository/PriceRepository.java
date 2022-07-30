package com.v1.iskream.layer.repository;

import com.v1.iskream.layer.domain.Price;
import com.v1.iskream.layer.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceRepository extends JpaRepository<Price, Long> {
    List<Price> findAllByProduct(Product product);
    Price findTopByProductAndSizeOrderByPriceAsc(Product product,int size);
}
