package com.example.vsd.manager.mapper;

import com.example.vsd.grpc.messages.AgentProto;
import com.example.vsd.grpc.messages.BaseEntityProto;
import com.example.vsd.grpc.messages.ClientProto;
import com.example.vsd.grpc.messages.OrderProto;
import com.example.vsd.manager.enity.Agent;
import com.example.vsd.manager.enity.BaseEntity;
import com.example.vsd.manager.enity.Client;
import com.example.vsd.manager.enity.Order;
import com.example.vsd.serialization.timestamp.TimestampUtils;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RecycleMapper {

	public BaseEntityProto toProto(@NonNull BaseEntity entity) {
		BaseEntityProto.Builder builder = switch (entity) {

			case Client e -> BaseEntityProto.newBuilder()
					.setClient(ClientProto.newBuilder()
							.setId(entity.getId())
							.setName(e.getName())
							.setServiceDeskNumber(entity.getServiceDeskNumber())
							.setCreationDate(TimestampUtils.toTimestamp(e.getCreationDate()))
							.setLastUpdated(TimestampUtils.toTimestamp(e.getLastUpdated()))
							.setDeleted(entity.isDeleted())
							.build());

			case Agent a -> BaseEntityProto.newBuilder()
					.setAgent(AgentProto.newBuilder()
							.setId(entity.getId())
							.setName(a.getName())
							.setServiceDeskNumber(entity.getServiceDeskNumber())
							.setCreationDate(TimestampUtils.toTimestamp(a.getCreationDate()))
							.setLastUpdated(TimestampUtils.toTimestamp(a.getLastUpdated()))
							.setDeleted(entity.isDeleted())
							.build());

			case Order o -> BaseEntityProto.newBuilder()
					.setOrder(OrderProto.newBuilder()
							.setId(entity.getId())
							.setName(o.getName())
							.setServiceDeskNumber(entity.getServiceDeskNumber())
							.setCreationDate(TimestampUtils.toTimestamp(o.getCreationDate()))
							.setLastUpdated(TimestampUtils.toTimestamp(o.getLastUpdated()))
							.setDeleted(entity.isDeleted())
							.build());

			default -> throw new IllegalStateException("Unexpected value: " + entity);
		};
		return builder.setLastModifiedBy(
						entity.getLastModifiedBy() == null ? "" : entity.getLastModifiedBy())
				.build();
	}

}
