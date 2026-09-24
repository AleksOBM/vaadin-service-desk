package com.example.vsd.frontend.grpc.client;

import com.example.vsd.frontend.mapper.AgentMapper;
import com.example.vsd.frontend.model.Agent;
import com.example.vsd.grpc.messages.AgentProto;
import com.example.vsd.grpc.services.admin.AdminAgentControllerGrpc.AdminAgentControllerBlockingStub;
import com.example.vsd.grpc.services.free.FreeControllerGrpc.FreeControllerBlockingStub;
import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
import com.google.protobuf.StringValue;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AgentService {

	AdminAgentControllerBlockingStub adminStub;
	FreeControllerBlockingStub freeStub;

	public List<Agent> getContent(String text) {
		return freeStub.findAgents(StringValue.of(text)).getAgentsList().stream()
				.map(AgentMapper::toAgent)
				.toList();
	}

	public void saveAgent(String name) {
		var result = adminStub.saveAgent(AgentProto.newBuilder()
				.setName(name)
				.build());

		if (result != null) {
			log.debug("Agent with name {} has been created", name);
		}
	}

	public List<Agent> getAllAgents() {
		return freeStub.getAllAgents(Empty.getDefaultInstance()).getAgentsList().stream()
				.map(AgentMapper::toAgent)
				.toList();
	}

	public void markAsDeleted(Long agentId) {
		Empty result = adminStub.setAgentDeleted(Int64Value.of(agentId));
		if (result != null) {
			log.debug("Agent with id {} has been deleted", agentId);
		}
	}

}
