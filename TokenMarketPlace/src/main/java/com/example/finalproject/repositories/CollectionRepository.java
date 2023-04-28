package com.example.finalproject.repositories;

import com.example.finalproject.models.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Integer> {
    List<Collection> findByCreatorId(int id);
    List<Collection> findByOrderByDateTimeDesc();
}
