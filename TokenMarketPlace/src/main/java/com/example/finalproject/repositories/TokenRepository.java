package com.example.finalproject.repositories;

import com.example.finalproject.enumm.RarityRank;
import com.example.finalproject.models.Token;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {
    List<Token> findByOwnerIdOrderByDateTimeDesc(int id);
    List<Token> findByCreatorIdOrderByDateTimeDesc(int id);;
    List<Token> findByOrderByDateTimeDesc();

    List<Token> findByPriceBetween(float min, float max, Sort sort);
    List<Token> findByPriceBetweenAndCollectionId(float min, float max, int collectionId, Sort sort);
    List<Token> findByPriceBetweenAndRarityRank(float min, float max, RarityRank rarityRank, Sort sort);
    List<Token> findByPriceBetweenAndTitleContainingIgnoreCase(float min, float max, String title, Sort sort);
    List<Token> findByPriceBetweenAndTitleContainingIgnoreCaseAndCollectionId(float min, float max, String title, int collectionId, Sort sort);
    List<Token> findByPriceBetweenAndCollectionIdAndRarityRank(float min, float max, int collectionId, RarityRank rarityRank, Sort sort);
    List<Token> findByPriceBetweenAndTitleContainingIgnoreCaseAndRarityRank(float min, float max, String title, RarityRank rarityRank, Sort sort);
    List<Token> findByPriceBetweenAndTitleContainingIgnoreCaseAndCollectionIdAndRarityRank(float min, float max, String title,
                                                                                           int collectionId, RarityRank rarityRank, Sort sort);
}
