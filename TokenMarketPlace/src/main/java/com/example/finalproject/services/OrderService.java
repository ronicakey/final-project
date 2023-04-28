package com.example.finalproject.services;

import com.example.finalproject.enumm.OrderStatus;
import com.example.finalproject.models.Order;
import com.example.finalproject.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order getOrderById(int id){
        Optional<Order> optionalOrder = orderRepository.findById(id);
        return optionalOrder.orElse(null);
    }

    public List<Order> getOrderByUserId(int userId){
        return orderRepository.findByUserIdOrderByDateTimeDesc(userId);
    }

    public List<Order> getOrderByNumberEnding(String keyword) {
        return orderRepository.findByNumberEndsWithIgnoreCase(keyword);
    }

    public List<Order> getAllOrders(){
        return orderRepository.findByOrderByDateTimeDesc();
    }

    @Transactional
    public void addOrder(Order order){
        orderRepository.save(order);
    }

    @Transactional
    public void blockOrder(int id, Order order){
        order.setId(id);
        order.setOrderStatus(OrderStatus.BLOCKED);
        orderRepository.save(order);
    }

    @Transactional
    public void approveOrder(int id, Order order){
        order.setId(id);
        order.setOrderStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);
    }

}
