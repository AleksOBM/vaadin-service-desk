package com.example.application.backend.service;

import com.example.application.backend.model.Order;
import com.example.application.backend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

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
}
