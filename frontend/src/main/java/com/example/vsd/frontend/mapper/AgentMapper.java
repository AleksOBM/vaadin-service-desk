package com.example.vsd.frontend.mapper;

import com.example.vsd.frontend.model.Agent;
import com.example.vsd.grpc.messages.AgentProto;
import com.example.vsd.serialization.model.EntityType;
import com.example.vsd.serialization.timestamp.TimestampUtils;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AgentMapper {

	public AgentProto toProto(@NonNull Agent agent) {
		return AgentProto.newBuilder()
				.setId(agent.getId())
				.setName(agent.getName())
				.build();
	}

	public Agent toAgent(@NonNull AgentProto proto) {
		return Agent.builder()
				.id(proto.getId())
				.name(proto.getName())
				.type(EntityType.AGENT)
				.serviceDeskNumber(proto.getServiceDeskNumber())
				.creationDate(TimestampUtils.toLocalDateTime(proto.getCreationDate()))
				.lastUpdated(TimestampUtils.toLocalDateTime(proto.getLastUpdated()))
				.deleted(proto.getDeleted())
				.build();
	}
}
