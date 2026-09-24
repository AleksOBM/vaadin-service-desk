package com.example.vsd.manager.mapper;

import com.example.vsd.grpc.messages.OrderProto;
import com.example.vsd.grpc.messages.OrderStatusProto;
import com.example.vsd.manager.enity.Order;
import com.example.vsd.manager.model.OrderData;
import com.example.vsd.serialization.model.OrderStatus;
import com.example.vsd.serialization.timestamp.TimestampUtils;
import com.google.protobuf.Timestamp;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@UtilityClass
public class OrderMapper {

	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private final ZoneId zoneId = ZoneId.systemDefault();

	public OrderProto toProto(@NonNull Order entity) {
		OrderProto proto = OrderProto.newBuilder()
				.setId(entity.getId())
				.setName(entity.getName())
				.setDescription(entity.getDescription())
				.setStartLine(entity.getStartLine().format(formatter))
				.setDeadLine(entity.getDeadLine().format(formatter))
				.setStatus(OrderStatusProto.valueOf(entity.getStatus().name()))
				.setAgent(AgentMapper.toProto(entity.getAgent()))
				.setClient(ClientMapper.toProto(entity.getClient()))
				.setServiceDeskNumber(entity.getServiceDeskNumber())
				.setCreationDate(TimestampUtils.toTimestamp(entity.getCreationDate()))
				.setLastUpdated(TimestampUtils.toTimestamp(entity.getLastUpdated()))
				.setDeleted(entity.isDeleted())
				.build();

		if (entity.getStatus().equals(OrderStatus.COMPLETED)) {
			proto = proto.toBuilder()
					.setCompletedDate(toTimestamp(entity.getCompletedDate()))
					.build();
		}

		return proto;
	}

	public Order toEntity(@NonNull OrderProto proto) {
		return Order.builder()
				.name(proto.hasName() ? proto.getName() : null)
				.description(proto.getDescription())
				.startLine(LocalDate.parse(proto.getStartLine()))
				.deadLine(LocalDate.parse(proto.getDeadLine()))
				.completedDate(proto.hasCompletedDate() ? toLocalDateTime(proto.getCompletedDate()) : null)
				.agent(AgentMapper.toEntity(proto.getAgent()))
				.client(ClientMapper.toEntity(proto.getClient()))
				.deleted(false)
				.build();
	}

	public Order update(@NonNull Order oldOrder, @NonNull OrderData data) {

		return Order.builder()

				// Эти поля остаются как были
				.id(oldOrder.getId())
				.creationDate(oldOrder.getCreationDate())
				.serviceDeskNumber(oldOrder.getServiceDeskNumber())
				.deleted(false)

				// Это поле всегда обновляется
				.status(data.status())

				// Проверяемые поля
				.name(data.hasName() ? data.name() : oldOrder.getName())
				.description(data.hasDescription() ?
						data.description() : oldOrder.getDescription())
				.startLine(data.hasStartLine()
						? data.startLine() : oldOrder.getStartLine())
				.deadLine(data.hasDeadLine()
						? data.deadLine() : oldOrder.getDeadLine())
				.completedDate(data.hasCompletedDate() ?
						data.completedDate() : oldOrder.getCompletedDate())
				.agent(data.hasAgent() ? data.agent() : oldOrder.getAgent())
				.client(data.hasClient() ? data.client() : oldOrder.getClient())

				.build();
	}

	@NonNull
	private Timestamp toTimestamp(@NonNull LocalDateTime localDateTime) {
		return toTimestamp(localDateTime.atZone(zoneId).toInstant());
	}

	@NonNull
	private LocalDateTime toLocalDateTime(@NonNull Timestamp timestamp) {
		return LocalDateTime.ofInstant(toInstant(timestamp), zoneId)
				.truncatedTo(ChronoUnit.MILLIS);
	}

	@NonNull
	private Timestamp toTimestamp(@NonNull Instant instant) {
		return Timestamp.newBuilder()
				.setSeconds(instant.getEpochSecond())
				.setNanos(instant.getNano())
				.build();
	}

	@NonNull
	private Instant toInstant(@NonNull Timestamp timestamp) {
		return Instant.ofEpochSecond(
				timestamp.getSeconds(),
				timestamp.getNanos()
		);
	}

}
