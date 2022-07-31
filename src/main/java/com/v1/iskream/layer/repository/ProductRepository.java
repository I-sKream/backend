package com.v1.iskream.layer.repository;

import com.v1.iskream.layer.domain.Product;
import com.v1.iskream.layer.domain.RecentProductInterface;
import com.v1.iskream.layer.domain.dto.response.RecentProductResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value =
            "SELECT product.id as id, product.name as product_name_eng, product.brand as product_brand, c.url as thumbnail\n" +
                    "FROM (\n" +
                    "\tSELECT th.id, url, th.product_id\n" +
                    "    FROM kream.thumbnail as th\n" +
                    "\tWHERE(th.product_id, th.id)\n" +
                    "    in(\n" +
                    "\t\tselect sub_th.product_id, min(id) as id \n" +
                    "        \tfrom kream.thumbnail as sub_th\n" +
                    "            group by sub_th.product_id\n" +
                    "    )\n" +
                    ")c\n" +
                    "JOIN kream.product as product\n" +
                    "WHERE product.id = c.product_id and product.id \n" +
                    "IN (\n" +
                    "\tselect * from(\n" +
                    "\t\tSELECT product_id\n" +
                    "\t\tFROM kream.price as price \n" +
                    "\t\tGROUP BY product_id\n" +
                    "\t\tORDER BY price.id DESC\n" +
                    "\t\tLIMIT 15\n" +
                    "    ) as tmp\n" +
                    ");"
            , nativeQuery = true
    )
    List<RecentProductInterface> findRecentProduct();
}
