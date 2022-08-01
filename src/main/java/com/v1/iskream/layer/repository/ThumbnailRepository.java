package com.v1.iskream.layer.repository;

import com.v1.iskream.layer.domain.Product;
import com.v1.iskream.layer.domain.Thumbnail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThumbnailRepository extends JpaRepository<Thumbnail,Long> {
    List<Thumbnail> findAllByProduct(Product product);
}
