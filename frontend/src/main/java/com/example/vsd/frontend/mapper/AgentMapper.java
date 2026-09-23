package com.example.vsd.frontend.mapper;

import com.example.vsd.frontend.model.Agent;
import com.example.vsd.grpc.messages.AgentProto;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AgentMapper {

	public AgentProto toProto(@NonNull Agent agent) {
		return AgentProto.newBuilder()
				.setId(agent.id())
				.setName(agent.name())
				.build();
	}

	public Agent toAgent(@NonNull AgentProto proto) {
		return Agent.builder()
				.id(proto.getId())
				.name(proto.getName())
				.serviceDeskNumber(proto.getServiceDeskNumber())
				.build();
	}
}
