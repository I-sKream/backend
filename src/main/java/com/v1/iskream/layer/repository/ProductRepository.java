package com.v1.iskream.layer.repository;

import com.v1.iskream.layer.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
