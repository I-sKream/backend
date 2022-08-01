package com.v1.iskream.layer.repository;

import com.v1.iskream.layer.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders,Long> {
}
