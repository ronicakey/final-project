package com.example.finalproject.services;

import com.example.finalproject.models.CartItem;
import com.example.finalproject.repositories.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CartService {
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public List<CartItem> getCartItemsByUserId(int id) {
        return cartRepository.findByUserId(id);
    }

    @Transactional
    public void addToCart(CartItem cart) {
        cartRepository.save(cart);
    }

    @Transactional
    public void deleteCartItem(int id) {
        cartRepository.deleteById(id);
    }

    @Transactional
    public void deleteTokenFromAllCarts(int id) {
        cartRepository.deleteByTokenId(id);
    }
}
