package com.example.vsd.manager.service.order;

import com.example.vsd.manager.enity.Order;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Transactional(readOnly = true)
public interface OrderService {

	@Transactional
	void save(Order order);

	Collection<Order> findAll();

}
