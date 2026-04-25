package com.example.application.backend.service;

import com.example.application.backend.util.exception.ParameterNotValidException;
import com.example.application.backend.model.Order;
import com.example.application.backend.util.enums.OrderStatus;
import com.example.application.backend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public void update(Order order) {
//        orderRepository.update(new UpdateSpecification<Order>(sp -> ));
    }

    public Collection<Order> findAll() {
        return orderRepository.findAllByDeletedFalse();
    }

    public Collection<Order> getContent(String text) {
        return orderRepository.getContent(text);
    }

    public OrderStatus getStatus(Order order) {
        LocalDate date = LocalDate.now();
        LocalDate firstControl = order.getFirstControlLine();
        LocalDate secondControl = order.getSecondControlLine();
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

    public void restore(Order order) {
        order.setDeleted(false);
        order.setLastUpdated(LocalDateTime.now());
        orderRepository.save(order);
    }
}
