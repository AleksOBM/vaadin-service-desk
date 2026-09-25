package com.example.vsd.frontend.grpc.client;

import com.example.vsd.frontend.mapper.OrderMapper;
import com.example.vsd.frontend.model.Order;
import com.example.vsd.grpc.messages.GetOrdersResponse;
import com.example.vsd.grpc.messages.OrderProto;
import com.example.vsd.grpc.services.free.FreeControllerGrpc.FreeControllerBlockingStub;
import com.example.vsd.grpc.services.user.UserControllerGrpc.UserControllerBlockingStub;
import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
import com.google.protobuf.StringValue;
import io.grpc.StatusRuntimeException;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {

	UserControllerBlockingStub userStub;
	FreeControllerBlockingStub freeStub;

	public List<Order> getContent(String text) {
		GetOrdersResponse response;
		try {
			response = freeStub.findOrders(StringValue.of(text));
			log.debug("Found {} orders by text '{}'", response.getOrdersList().size(), text);
		} catch (StatusRuntimeException e) {
			log.error("Error while fetching orders by text '{}'", text, e);
			throw e;
		}
		return response.getOrdersList().stream()
				.map(OrderMapper::toOrder)
				.toList();
	}

	public List<Order> getAllOrders() {
		GetOrdersResponse response;
		try {
			response = freeStub.getAllOrders(Empty.getDefaultInstance());
			log.debug("Found all orders");
		} catch (StatusRuntimeException e) {
			log.error("Error while fetching all orders", e);
			throw e;
		}
		return response.getOrdersList().stream()
				.map(OrderMapper::toOrder)
				.toList();
	}

	public void saveOrder(@NonNull Order order) {
		OrderProto proto;
		boolean check = order.getId() != null && order.getId() > 0;
		if (check) {
			proto = OrderMapper.toUpdatedProto(order);
		} else {
			proto = OrderMapper.toCreatedProto(order);
		}

		try {
			var _ = userStub.saveOrder(proto);
			log.debug("Order has been created with name {}", order.getName());
		} catch (StatusRuntimeException e) {
			log.error("Error while saving order with name {}", order.getName(), e);
			throw e;
		}
	}

	public void markAsDeleted(Long orderId) {
		try {
			var _ = userStub.setOrderDeleted(Int64Value.of(orderId));
			log.debug("Order with id {} has been marked as deleted", orderId);
		} catch (StatusRuntimeException e) {
			log.error("Error while marked as deleted order with id {}", orderId, e);
			throw e;
		}
	}
}
