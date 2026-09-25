package com.example.vsd.frontend.grpc.client;

import com.example.vsd.frontend.mapper.AgentMapper;
import com.example.vsd.frontend.model.Agent;
import com.example.vsd.grpc.messages.GetAgentsResponse;
import com.example.vsd.grpc.services.admin.AdminAgentControllerGrpc.AdminAgentControllerBlockingStub;
import com.example.vsd.grpc.services.free.FreeControllerGrpc.FreeControllerBlockingStub;
import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
import com.google.protobuf.StringValue;
import io.grpc.StatusRuntimeException;
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
		GetAgentsResponse response;
		try {
			response = freeStub.findAgents(StringValue.of(text));
			log.debug("Found {} agents by text '{}'", text, response.getAgentsList().size());
		} catch (StatusRuntimeException e) {
			log.error("Error while fetching agents by text '{}'", text, e);
			throw e;
		}
		return response.getAgentsList().stream()
				.map(AgentMapper::toAgent)
				.toList();
	}

	public List<Agent> getAllAgents() {
		GetAgentsResponse response;
		try {
			response = freeStub.getAllAgents(Empty.getDefaultInstance());
			log.debug("All agents has getting success");
		} catch (StatusRuntimeException e) {
			log.error("Failed to get all agents", e);
			throw e;
		}
		return response.getAgentsList().stream()
				.map(AgentMapper::toAgent)
				.toList();
	}

	public void saveAgent(Agent agent) {
		try {
			var _ = adminStub.saveAgent(AgentMapper.toProto(agent));
			log.debug("Agent with name {} has been saved", agent.getName());
		} catch (StatusRuntimeException e) {
			log.error("Failed to save agent with name {}: {}", agent.getName(), e.getStatus(), e);
			throw e;
		}
	}

	public void markAsDeleted(Long agentId) {
		try {
			var _ = adminStub.setAgentDeleted(Int64Value.of(agentId));
			log.debug("Agent with id {} has been marked as deleted", agentId);
		} catch (StatusRuntimeException e) {
			log.error("Failed to mark agent deleted with id={}", agentId, e);
			throw e;
		}
	}

}
