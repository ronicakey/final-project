package com.example.finalproject.repositories;

import com.example.finalproject.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByOrderByDateTimeDesc();
    List<Order> findByUserIdOrderByDateTimeDesc(int userId);
    List<Order> findByNumberEndsWithIgnoreCase(String keyword);
}
