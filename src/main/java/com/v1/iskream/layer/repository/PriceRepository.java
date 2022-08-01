package com.v1.iskream.layer.repository;

import com.v1.iskream.layer.domain.Price;
import com.v1.iskream.layer.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface PriceRepository extends JpaRepository<Price, Long> {
    List<Price> findAllByProductOrderByPriceDesc(Product product);
    List<Price> findAllByProductAndSizeOrderByPriceDesc(Product product,int size);
    Price findTopByProductAndSizeOrderByPriceAsc(Product product,int size);

    @Query(value =
            "SELECT AVG(price.price)\n" +
                    "\t\tFROM kream.price as price \n" +
                    "        WHERE product_id = :id\n" +
                    "\t\tGROUP BY product_id"
            ,nativeQuery = true)
    BigInteger avgPriceById(Long id);

}
