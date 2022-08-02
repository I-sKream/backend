package com.v1.iskream.layer.repository;

import com.v1.iskream.layer.domain.Product;
import com.v1.iskream.layer.domain.RecentProductInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value =
            "SELECT product.id as id, product.name_eng as productNameEng, product.brand as productBrand, c.url as thumbnail\n" +
                    "FROM (\n" +
                    "\tSELECT th.id, url, th.product_id\n" +
                    "    FROM thumbnail as th\n" +
                    "\tWHERE(th.product_id, th.id)\n" +
                    "    in(\n" +
                    "\t\tselect sub_th.product_id, min(id) as id \n" +
                    "        \tfrom thumbnail as sub_th\n" +
                    "            group by sub_th.product_id\n" +
                    "    )\n" +
                    ")c\n" +
                    "JOIN product as product\n" +
                    "WHERE product.id = c.product_id and product.id \n" +
                    "IN (\n" +
                    "\tselect * from(\n" +
                    "\t\tSELECT product_id\n" +
                    "\t\tFROM price as price \n" +
                    "\t\tGROUP BY product_id\n" +
                    "\t\tORDER BY price.id DESC\n" +
                    "\t\tLIMIT :limit\n" +
                    "        offset :offset\n" +
                    "    ) as tmp\n" +
                    ");"
            , nativeQuery = true
    )
    List<RecentProductInterface> findRecentProduct(int limit, int offset);
}
