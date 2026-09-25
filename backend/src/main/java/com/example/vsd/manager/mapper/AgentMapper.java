package com.example.vsd.manager.mapper;

import com.example.vsd.grpc.messages.AgentProto;
import com.example.vsd.manager.enity.Agent;
import com.example.vsd.serialization.timestamp.TimestampUtils;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class AgentMapper {

	public Agent toEntity(@NonNull AgentProto proto) {
		return Agent.builder()
				.id(proto.hasId() ? proto.getId() : null)
				.name(proto.hasName() ? proto.getName() : null)
				.orders(List.of())
				.deleted(false)
				.serviceDeskNumber(null)
				.build();
	}

	@NonNull
	public AgentProto toProto(@NonNull Agent entity) {
		return AgentProto.newBuilder()
				.setId(entity.getId())
				.setName(entity.getName())
				.setServiceDeskNumber(entity.getServiceDeskNumber())
				.setCreationDate(TimestampUtils.toTimestamp(entity.getCreationDate()))
				.setLastUpdated(TimestampUtils.toTimestamp(entity.getLastUpdated()))
				.setDeleted(entity.isDeleted())
				.build();
	}

}
