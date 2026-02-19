package com.example.application.backend.service;

import com.example.application.backend.model.BaseEntity;
import com.example.application.backend.model.Order;
import com.example.application.backend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public void save(Order order) {
        orderRepository.save(order);
    }

    public Collection<Order> findAll() {
        return orderRepository.findAll().stream().filter(order -> !order.isDeleted()).toList();
    }

    public Collection<Order> findDeleted() {
        return orderRepository.findAll().stream().filter(BaseEntity::isDeleted).toList();
    }

    public long count() {
        return orderRepository.count();
    }

    public void markAsDeleted(Order order) {
        order.setDeleted(true);
        order.setLastUpdated(LocalDateTime.now());
        orderRepository.save(order);
    }

    public LocalDate getFirstControlLine(Order order) {
        int controlDaysCount = Math.round((float) getDaysCount(order) / 3);
        return order.getStartLine().plus(Period.ofDays(controlDaysCount));
    }

    public LocalDate getSecondControlLine(Order order) {
        int controlDaysCount = Math.round((float) getDaysCount(order) / 3);
        return order.getDeadLine().minus(Period.ofDays(controlDaysCount));
    }

    public int getDaysCount(Order order) {
        return Period.between(order.getStartLine(), order.getDeadLine()).getDays();
    }

    public void deleteForever(Order order) {
        orderRepository.delete(order);
    }

    public void restore(Order order) {
        order.setDeleted(false);
        order.setLastUpdated(LocalDateTime.now());
        orderRepository.save(order);
    }
}
