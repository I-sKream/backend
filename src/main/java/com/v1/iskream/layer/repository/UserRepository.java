package com.v1.iskream.layer.repository;

import com.v1.iskream.layer.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
