package com.example.vsd.manager.service.order;

import com.example.vsd.grpc.messages.OrderProto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface OrderService {

	@Transactional(readOnly = true)
	List<OrderProto> getAllOrders();

	@Transactional(readOnly = true)
	List<OrderProto> findOrders(String text);

	void saveOrder(OrderProto proto);

	void setOrderDeleted(Long orderId);

	void restoreOrder(Long orderId);

	void deleteOrder(Long orderId);

}
