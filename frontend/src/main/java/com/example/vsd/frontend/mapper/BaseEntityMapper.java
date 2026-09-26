package com.example.vsd.frontend.mapper;

import com.example.vsd.frontend.model.BaseEntity;
import com.example.vsd.grpc.messages.BaseEntityProto;
import com.example.vsd.serialization.model.EntityType;
import com.example.vsd.serialization.timestamp.TimestampUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BaseEntityMapper {

	public static BaseEntity toEntity(BaseEntityProto proto) {
		return switch (proto.getPayloadCase()) {
			case CLIENT -> BaseEntity.builder()
					.id(proto.getClient().getId())
					.name(proto.getClient().getName())
					.type(EntityType.CLIENT)
					.serviceDeskNumber(proto.getClient().getServiceDeskNumber())
					.creationDate(TimestampUtils.toLocalDateTime(proto.getClient().getCreationDate()))
					.lastUpdated(TimestampUtils.toLocalDateTime(proto.getClient().getLastUpdated()))
					.deleted(proto.getClient().getDeleted())
					.build();
			case AGENT -> BaseEntity.builder()
					.id(proto.getAgent().getId())
					.name(proto.getAgent().getName())
					.type(EntityType.AGENT)
					.serviceDeskNumber(proto.getAgent().getServiceDeskNumber())
					.creationDate(TimestampUtils.toLocalDateTime(proto.getAgent().getCreationDate()))
					.lastUpdated(TimestampUtils.toLocalDateTime(proto.getAgent().getLastUpdated()))
					.deleted(proto.getAgent().getDeleted())
					.build();
			case ORDER -> BaseEntity.builder()
					.id(proto.getOrder().getId())
					.name(proto.getOrder().getName())
					.type(EntityType.ORDER)
					.serviceDeskNumber(proto.getOrder().getServiceDeskNumber())
					.creationDate(TimestampUtils.toLocalDateTime(proto.getOrder().getCreationDate()))
					.lastUpdated(TimestampUtils.toLocalDateTime(proto.getOrder().getLastUpdated()))
					.deleted(proto.getOrder().getDeleted())
					.build();
			case PAYLOAD_NOT_SET -> throw new IllegalArgumentException("Illegal entity type");
		};
	}
}
