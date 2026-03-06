package com.example.application.backend.service;

import com.example.application.backend.exception.ParameterNotValidException;
import com.example.application.backend.model.BaseEntity;
import com.example.application.backend.model.Order;
import com.example.application.backend.model.OrderStatus;
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

    // todo: добавить в логику светофоров учет выходных
    // todo: добавить валидацию абсурдных случаев при создании заявки
    // todo: добавить логику отправки новой заявки на почту через MailService

    private final OrderRepository orderRepository;

    public void save(Order order) {
        orderRepository.save(order);
    }

    public Collection<Order> findAll() {
        return orderRepository.findAll().stream().filter(order -> !order.isDeleted()).toList();
    }

    public Collection<Order> getContent(String filterText) {
        return orderRepository.findAll().stream()
                .filter(order -> !order.isDeleted())
                .filter(order ->
                        order.getTitle().toLowerCase().contains(filterText.toLowerCase()) ||
                                order.getServiceDeskNumber().toLowerCase().contains(filterText.toLowerCase())
                )
                .toList();
    }

    public Collection<Order> findDeleted() {
        return orderRepository.findAll().stream().filter(BaseEntity::isDeleted).toList();
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

    public OrderStatus getStatus(Order order) {
        LocalDate date = LocalDate.now();
        LocalDate firstControl = getFirstControlLine(order);
        LocalDate secondControl = getSecondControlLine(order);
        if (order.isCompleted()) {
            return OrderStatus.COMPLETED;
        } else if (firstControl.isAfter(date)) {
            return OrderStatus.IN_PROGRESS;
        } else if((firstControl.isEqual(date) || firstControl.isBefore(date)) && secondControl.isAfter(date)) {
            return OrderStatus.FIRST_CONTROL;
        } else if(secondControl.isEqual(date) || secondControl.isBefore(date)) {
            return OrderStatus.SECOND_CONTROL;
        } else {
            throw new ParameterNotValidException("status", "что-то напутано с датами.");
        }
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
