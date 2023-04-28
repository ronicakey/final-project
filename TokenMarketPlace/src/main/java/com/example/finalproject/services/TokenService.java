package com.example.finalproject.services;

import org.springframework.data.domain.Sort;
import com.example.finalproject.enumm.RarityRank;
import com.example.finalproject.enumm.TokenStatus;
import com.example.finalproject.models.Collection;
import com.example.finalproject.models.Token;
import com.example.finalproject.models.User;
import com.example.finalproject.repositories.TokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TokenService {
    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public List<Token> getAllToken() {
        return tokenRepository.findByOrderByDateTimeDesc();
    }

    public Token getTokenById(int id) {
        Optional<Token> optionalToken = tokenRepository.findById(id);
        return optionalToken.orElse(null);
    }

    @Transactional
    public void addToken(Token token, User user, Collection collection, String image){
        token.setPrice(null);
        token.setTokenStatus(TokenStatus.PENDING);
        token.setOwner(user);
        token.setCreator(user);
        token.setCollection(collection);
        token.setImage(image);
        token.setDeletable(true);
        token.setNewOwnerId(-1);
        collection.incrementCount();
        tokenRepository.save(token);
    }

    @Transactional
    public void deleteToken(int id, Collection collection) {
        tokenRepository.deleteById(id);
        collection.decrementCount();
    }

    @Transactional
    public void sellToken(int id, Token token) {
        token.setId(id);
        token.setTokenStatus(TokenStatus.FOR_SALE);
        token.updateDateTime();
        tokenRepository.save(token);
    }

    @Transactional
    public void withdrawToken(int id, Token token) {
        token.setId(id);
        token.setPrice(null);
        token.setTokenStatus(TokenStatus.NOT_FOR_SALE);
        tokenRepository.save(token);
    }

    @Transactional
    public void updateTokenStatus(int id, Token token, TokenStatus tokenStatus) {
        token.setId(id);
        token.setTokenStatus(tokenStatus);
        token.updateDateTime();
        tokenRepository.save(token);
    }

    @Transactional
    public void buyToken(int id, Token token, int buyerId) {
        token.setId(id);
        token.setPrice(null);
        token.setTokenStatus(TokenStatus.PENDING_ORDER);
        token.setNewOwnerId(buyerId);
        token.setDeletable(false);
        tokenRepository.save(token);
    }

    @Transactional
    public void rejectToken(int id, Token token) {
        token.setId(id);
        token.setPrice(null);
        token.setTokenStatus(TokenStatus.NOT_FOR_SALE);
        token.setNewOwnerId(-1);
        tokenRepository.save(token);
    }

    @Transactional
    public void transferToken(int id, Token token, User newOwner) {
        token.setId(id);
        token.setPrice(null);
        token.setTokenStatus(TokenStatus.NOT_FOR_SALE);
        token.setOwner(newOwner);
        token.setNewOwnerId(-1);
        tokenRepository.save(token);
    }

    public List<Token> getTokensByCreatorId(int id) {
        return tokenRepository.findByCreatorIdOrderByDateTimeDesc(id);
    }
    public List<Token> getTokensByOwnerId(int id) {
        return tokenRepository.findByOwnerIdOrderByDateTimeDesc(id);
    }
    public List<Token> getTokensByPrice(float min, float max, Sort sort){
        return tokenRepository.findByPriceBetween(min, max, sort );
    }
    public List<Token> getTokensByTitle(float min, float max, String title, Sort sort){
        return tokenRepository.findByPriceBetweenAndTitleContainingIgnoreCase(min, max, title, sort);
    }
    public List<Token> getTokensByCollection(float min, float max, int collectionId, Sort sort){
        return tokenRepository.findByPriceBetweenAndCollectionId(min, max, collectionId, sort);
    }
    public List<Token> getTokensByRarityRank(float min, float max, RarityRank rarityRank, Sort sort){
        return tokenRepository.findByPriceBetweenAndRarityRank(min, max, rarityRank, sort);
    }
    public List<Token> getTokensByTitleAndCollection(float min, float max, String title, int collectionId, Sort sort){
        return tokenRepository.findByPriceBetweenAndTitleContainingIgnoreCaseAndCollectionId(min, max, title, collectionId, sort);
    }
    public List<Token> getTokensByTitleAndRarityRank(float min, float max, String title, RarityRank rarityRank, Sort sort){
        return tokenRepository.findByPriceBetweenAndTitleContainingIgnoreCaseAndRarityRank(min, max, title, rarityRank, sort);
    }
    public List<Token> getTokensByCollectionAndRarityRank(float min, float max, int collectionId, RarityRank rarityRank, Sort sort){
        return tokenRepository.findByPriceBetweenAndCollectionIdAndRarityRank(min, max, collectionId, rarityRank, sort);
    }
    public List<Token> getTokensByTitleAndCollectionAndRarityRank(float min, float max, String title, int collectionId, RarityRank rarityRank, Sort sort){
        return tokenRepository.findByPriceBetweenAndTitleContainingIgnoreCaseAndCollectionIdAndRarityRank(min, max, title, collectionId, rarityRank, sort);
    }
}