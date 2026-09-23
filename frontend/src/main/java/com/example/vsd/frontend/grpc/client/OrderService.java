package com.example.vsd.frontend.grpc.client;

import com.example.vsd.frontend.mapper.OrderMapper;
import com.example.vsd.frontend.model.Order;
import com.example.vsd.grpc.messages.OrderProto;
import com.example.vsd.grpc.services.free.FreeControllerGrpc.FreeControllerBlockingStub;
import com.example.vsd.grpc.services.user.UserControllerGrpc.UserControllerBlockingStub;
import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
import com.google.protobuf.StringValue;
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
		return freeStub.findOrders(StringValue.of(text)).getOrdersList().stream()
				.map(OrderMapper::toOrder)
				.toList();
	}

	public List<Order> getAllOrders() {
		return freeStub.getAllOrders(Empty.getDefaultInstance()).getOrdersList().stream()
				.map(OrderMapper::toOrder)
				.toList();
	}

	public void createOrder(@NonNull Order order) {
		OrderProto proto = OrderMapper.toCreatedProto(order);
		OrderProto response = userStub.createOrder(proto);
		if (response != null) {
			log.debug("Order has been created with id {}", response.getId());
		}
	}

	public Order updateOrder(Order order) {
		var proto = userStub.updateOrder(OrderMapper.toUpdatedProto(order));
		return OrderMapper.toOrder(proto);
	}

	public void markAsDeleted(Long orderId) {
		Empty result = userStub.setOrderDeleted(Int64Value.newBuilder().setValue(orderId).build());
		if (result != null) {
			log.debug("Order with id {} has been marked as deleted", orderId);
		}
	}
}
