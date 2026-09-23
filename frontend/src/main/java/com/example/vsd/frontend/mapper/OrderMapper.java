package com.example.vsd.frontend.mapper;

import com.example.vsd.frontend.model.Agent;
import com.example.vsd.frontend.model.Client;
import com.example.vsd.frontend.model.Order;
import com.example.vsd.grpc.messages.OrderProto;
import com.example.vsd.serialization.model.OrderStatus;
import com.example.vsd.serialization.timestamp.TimestampUtils;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OrderMapper {

	public OrderProto toCreatedProto(@NonNull Order order) {
		return OrderProto.newBuilder()
				.setName(order.name())
				.setDescription(order.description())
				.setStartLine(TimestampUtils.toString(order.startLine()))
				.setDeadLine(TimestampUtils.toString(order.deadLine()))
				.setClient(ClientMapper.toProto(order.client()))
				.setAgent(AgentMapper.toProto(order.agent()))
				.setCompletedDate(TimestampUtils.toTimestamp(order.completedDate()))
				.setDeleted(false)
				.build();
	}

	public OrderProto toUpdatedProto(@NonNull Order order) {
		if (order.id() == null) {
			throw new IllegalArgumentException("Order id cannot be null");
		}
		return OrderProto.newBuilder()
				.setId(order.id())
				.setName(order.name() == null ? null : order.name())
				.setDescription(order.description() == null ? null : order.description())
				.setStartLine(order.startLine() == null ? null : TimestampUtils.toString(order.startLine()))
				.setDeadLine(order.deadLine() == null ? null : TimestampUtils.toString(order.deadLine()))
				.setClient(order.client() == null ? null : ClientMapper.toProto(order.client()))
				.setAgent(order.agent() == null ? null : AgentMapper.toProto(order.agent()))
				.setCompletedDate(order.completedDate() == null
						? null : TimestampUtils.toTimestamp(order.completedDate()))
				.setDeleted(false)
				.build();
	}

	public Order toOrder(@NonNull OrderProto proto) {
		return Order.builder()
				.id(proto.getId())
				.name(proto.getName())
				.description(proto.getDescription())
				.startLine(TimestampUtils.toLocalDate(proto.getStartLine()))
				.deadLine(TimestampUtils.toLocalDate(proto.getDeadLine()))
				.completedDate(TimestampUtils.toLocalDateTime(proto.getCompletedDate()))
				.status(OrderStatus.valueOf(proto.getStatus().name()))
				.client(Client.builder()
						.id(proto.getClient().getId())
						.name(proto.getClient().getName())
						.serviceDeskNumber(proto.getClient().getServiceDeskNumber())
						.build()
				)
				.agent(Agent.builder()
						.id(proto.getAgent().getId())
						.name(proto.getAgent().getName())
						.serviceDeskNumber(proto.getAgent().getServiceDeskNumber())
						.build()
				)
				.serviceDeskNumber(proto.getServiceDeskNumber())
				.build();
	}
}
