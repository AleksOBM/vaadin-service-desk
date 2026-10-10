package com.example.vsd.manager.mapper;

import com.example.vsd.grpc.messages.OrderProto;
import com.example.vsd.manager.enity.Order;
import com.example.vsd.manager.model.OrderGetData;
import com.example.vsd.manager.model.OrderUpdateData;
import com.example.vsd.serialization.timestamp.TimestampUtils;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import static com.example.vsd.grpc.messages.OrderStatusProto.*;

@UtilityClass
public class OrderMapper {

	public OrderProto toProto(@NonNull Order entity, @NonNull OrderGetData data) {
		OrderProto.Builder builder = OrderProto.newBuilder()
				.setId(entity.getId())
				.setName(entity.getName())
				.setDescription(entity.getDescription())
				.setStartLine(TimestampUtils.toDate(entity.getStartLine()))
				.setDeadLine(TimestampUtils.toDate(entity.getDeadLine()))
				.setFirstControlLine(TimestampUtils.toDate(data.firstControlLine()))
				.setSecondControlLine(TimestampUtils.toDate(data.secondControlLine()))
				.setDaysCount(data.daysCount())
				.setStatus(data.status())
				.setAgent(AgentMapper.toProto(entity.getAgent()))
				.setClient(ClientMapper.toProto(entity.getClient()))
				.setServiceDeskNumber(entity.getServiceDeskNumber())
				.setCreationDate(TimestampUtils.toTimestamp(entity.getCreationDate()))
				.setLastUpdated(TimestampUtils.toTimestamp(entity.getLastUpdated()))
				.setDeleted(entity.isDeleted());

		if (data.status().equals(COMPLETED)) {
			builder.setCompletedDate(TimestampUtils.toTimestamp(entity.getCompletedDate()));
		}

		return builder.build();
	}

	public Order toEntity(@NonNull OrderProto proto) {
		return Order.builder()
				.name(proto.hasName() ? proto.getName() : null)
				.description(proto.getDescription())
				.startLine(TimestampUtils.toLocalDate(proto.getStartLine()))
				.deadLine(TimestampUtils.toLocalDate(proto.getDeadLine()))
				.completedDate(proto.hasCompletedDate()
						? TimestampUtils.toLocalDateTime(proto.getCompletedDate()) : null)
				.agent(AgentMapper.toEntity(proto.getAgent()))
				.client(ClientMapper.toEntity(proto.getClient()))
				.deleted(false)
				.build();
	}

	public Order toUpdatedOrder(@NonNull Order oldOrder, @NonNull OrderUpdateData data) {

		return Order.builder()

				// Эти поля остаются как были
				.id(oldOrder.getId())
				.creationDate(oldOrder.getCreationDate())
				.lastUpdated(oldOrder.getLastUpdated())
				.serviceDeskNumber(oldOrder.getServiceDeskNumber())
				.deleted(false)

				// Проверяемые поля
				.name(data.hasName() ? data.name() : oldOrder.getName())
				.description(data.hasDescription() ?
						data.description() : oldOrder.getDescription())
				.startLine(data.hasStartLine()
						? data.startLine() : oldOrder.getStartLine())
				.deadLine(data.hasDeadLine()
						? data.deadLine() : oldOrder.getDeadLine())
				.completedDate(data.completedDate())
				.agent(data.hasAgent() ? data.agent() : oldOrder.getAgent())
				.client(data.hasClient() ? data.client() : oldOrder.getClient())

				.build();
	}

}
