package com.example.vsd.frontend.mapper;

import com.example.vsd.frontend.model.Agent;
import com.example.vsd.grpc.messages.AgentProto;
import com.example.vsd.serialization.model.EntityType;
import com.example.vsd.serialization.timestamp.TimestampUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AgentMapper {

	public static AgentProto toProto(Agent agent) {
		AgentProto.Builder builder = AgentProto.newBuilder()
				.setName(agent.getName());
		if (agent.hasId()) {
			builder.setId(agent.getId());
		}
		return builder.build();
	}

	public static Agent toAgent(AgentProto proto) {
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
