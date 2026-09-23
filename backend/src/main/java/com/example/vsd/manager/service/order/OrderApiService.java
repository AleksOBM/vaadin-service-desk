package com.example.vsd.manager.service.order;

import com.example.vsd.grpc.messages.OrderProto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface OrderApiService {

	@Transactional(readOnly = true)
	List<OrderProto> apiFindAll();

	@Transactional(readOnly = true)
	List<OrderProto> apiFindOrders(String text);

	OrderProto apiCreateOrder(OrderProto proto);

	void apiSetOrderDeleted(Long orderId);

	void apiRestoreOrder(Long orderId);

	void apiDeleteOrder(Long orderId);

	OrderProto apiUpdateOrder(OrderProto proto);

}
