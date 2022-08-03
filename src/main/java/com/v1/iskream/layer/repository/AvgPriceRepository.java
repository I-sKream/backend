
package com.v1.iskream.layer.repository;

import com.v1.iskream.layer.domain.AvgPrice;
import com.v1.iskream.layer.domain.Price;
import com.v1.iskream.layer.domain.Product;
import com.v1.iskream.layer.domain.dto.response.AvgPriceResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AvgPriceRepository extends JpaRepository<Price, Long> {

    @Query(value =
            "select price.product_id as productId                                              \n" +
                    "     , date                                                                \n" +
                    "     , avg(price.price) as avgPrice                                       \n" +
                    "  from orders as orders                                              \n" +
                    "  join price as price                                                \n" +
                    "    on orders.price_id = price.id                                          \n" +
                    " where price.product_id= :id                                               \n" +
                    "   and date BETWEEN DATE_ADD(NOW(),INTERVAL -1 WEEK ) AND NOW()            \n" +
                    " group by price.product_id,date                                            \n" +
                    " order by date                                                               "
            ,nativeQuery = true)
    List<AvgPrice> avgPriceById(Long id);

}
