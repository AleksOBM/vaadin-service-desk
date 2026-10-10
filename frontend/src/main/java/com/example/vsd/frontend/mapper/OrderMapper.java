package com.example.vsd.frontend.mapper;

import com.example.vsd.frontend.model.Agent;
import com.example.vsd.frontend.model.Client;
import com.example.vsd.frontend.model.Order;
import com.example.vsd.frontend.model.OrderStatus;
import com.example.vsd.grpc.messages.OrderProto;
import com.example.vsd.grpc.messages.OrderStatusProto;
import com.example.vsd.serialization.model.EntityType;
import com.example.vsd.serialization.timestamp.TimestampUtils;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OrderMapper {

	public OrderProto toCreatedProto(@NonNull Order order) {
		OrderProto.Builder builder =  OrderProto.newBuilder()
				.setName(order.getName())
				.setDescription(order.getDescription())
				.setStartLine(TimestampUtils.toDate(order.getStartLine()))
				.setDeadLine(TimestampUtils.toDate(order.getDeadLine()))
				.setClient(ClientMapper.toProto(order.getClient()))
				.setAgent(AgentMapper.toProto(order.getAgent()))
				.setDeleted(false);

		if (order.isCompleted()) {
			builder.setStatus(OrderStatusProto.COMPLETED);
			builder.setCompletedDate(TimestampUtils.toTimestamp(order.getCompletedDate()));
		}

		return builder.build();
	}

	public OrderProto toUpdatedProto(@NonNull Order order) {
		if (order.getId() == null) {
			throw new IllegalArgumentException("Order id cannot be null");
		}

		OrderProto.Builder builder = OrderProto.newBuilder()
				.setId(order.getId());

		if (order.getName() != null) {
			builder.setName(order.getName());
		}
		if (order.getDescription() != null) {
			builder.setDescription(order.getDescription());
		}
		if (order.getStartLine() != null) {
			builder.setStartLine(TimestampUtils.toDate(order.getStartLine()));
		}
		if (order.getDeadLine() != null) {
			builder.setDeadLine(TimestampUtils.toDate(order.getDeadLine()));
		}
		if (order.getCompletedDate() != null) {
			builder.setCompletedDate(TimestampUtils.toTimestamp(order.getCompletedDate()));
		}
		if (order.getStatus().equals(OrderStatus.COMPLETED)) {
			builder.setStatus(OrderStatusProto.COMPLETED);
		}

		if (order.getClient() != null) {
			builder.setClient(ClientMapper.toProto(order.getClient()));
		}
		if (order.getAgent() != null) {
			builder.setAgent(AgentMapper.toProto(order.getAgent()));
		}

		return builder.build();
	}

	public Order toOrder(@NonNull OrderProto proto) {
		return Order.builder()
				.id(proto.getId())
				.name(proto.getName())
				.type(EntityType.ORDER)
				.description(proto.getDescription())
				.startLine(TimestampUtils.toLocalDate(proto.getStartLine()))
				.deadLine(TimestampUtils.toLocalDate(proto.getDeadLine()))
				.firstControlLine(TimestampUtils.toLocalDate(proto.getFirstControlLine()))
				.seondControlLine(TimestampUtils.toLocalDate(proto.getSecondControlLine()))
				.daysCount(proto.getDaysCount())
				.completedDate(proto.hasCompletedDate()
						? TimestampUtils.toLocalDateTime(proto.getCompletedDate()) : null)
				.status(OrderStatus.valueOf(proto.getStatus().name()))
				.client(Client.builder()
						.id(proto.getClient().getId())
						.name(proto.getClient().getName())
						.serviceDeskNumber(proto.getClient().getServiceDeskNumber())
						.creationDate(TimestampUtils.toLocalDateTime(proto.getClient().getCreationDate()))
						.lastUpdated(TimestampUtils.toLocalDateTime(proto.getClient().getLastUpdated()))
						.deleted(proto.getClient().getDeleted())
						.build()
				)
				.agent(Agent.builder()
						.id(proto.getAgent().getId())
						.name(proto.getAgent().getName())
						.serviceDeskNumber(proto.getAgent().getServiceDeskNumber())
						.creationDate(TimestampUtils.toLocalDateTime(proto.getAgent().getCreationDate()))
						.lastUpdated(TimestampUtils.toLocalDateTime(proto.getAgent().getLastUpdated()))
						.deleted(proto.getAgent().getDeleted())
						.build()
				)
				.serviceDeskNumber(proto.getServiceDeskNumber())
				.creationDate(TimestampUtils.toLocalDateTime(proto.getCreationDate()))
				.lastUpdated(TimestampUtils.toLocalDateTime(proto.getLastUpdated()))
				.deleted(proto.getDeleted())
				.build();
	}
}
