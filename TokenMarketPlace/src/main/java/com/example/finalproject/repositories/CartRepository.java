package com.example.finalproject.repositories;

import com.example.finalproject.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Integer> {
    List<CartItem> findByUserId(int id);
    void deleteById(int id);
    void deleteByTokenId(int id);
}
