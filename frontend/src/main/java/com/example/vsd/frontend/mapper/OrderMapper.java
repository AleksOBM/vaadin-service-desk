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
				.setName(order.getName())
				.setDescription(order.getDescription())
				.setStartLine(TimestampUtils.toString(order.getStartLine()))
				.setDeadLine(TimestampUtils.toString(order.getDeadLine()))
				.setClient(ClientMapper.toProto(order.getClient()))
				.setAgent(AgentMapper.toProto(order.getAgent()))
				.setCompletedDate(TimestampUtils.toTimestamp(order.getCompletedDate()))
				.setDeleted(false)
				.build();
	}

	public OrderProto toUpdatedProto(@NonNull Order order) {
		if (order.getId() == null) {
			throw new IllegalArgumentException("Order id cannot be null");
		}
		return OrderProto.newBuilder()
				.setId(order.getId())
				.setName(order.getName() == null
						? null : order.getName())
				.setDescription(order.getDescription() == null
						? null : order.getDescription())
				.setStartLine(order.getStartLine() == null
						? null : TimestampUtils.toString(order.getStartLine()))
				.setDeadLine(order.getDeadLine() == null
						? null : TimestampUtils.toString(order.getDeadLine()))
				.setClient(order.getClient() == null
						? null : ClientMapper.toProto(order.getClient()))
				.setAgent(order.getAgent() == null
						? null : AgentMapper.toProto(order.getAgent()))
				.setCompletedDate(order.getCompletedDate() == null
						? null : TimestampUtils.toTimestamp(order.getCompletedDate()))
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
