package com.example.vsd.manager.service.agent;

import com.example.vsd.grpc.messages.AgentProto;
import org.jspecify.annotations.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface AgentApiService {

	@Transactional(readOnly = true)
	List<AgentProto> apiFindAll();

	@Transactional(readOnly = true)
	List<AgentProto> findAgents(String text);

	AgentProto apiCreateAgent(AgentProto request);

	void apiDeleteAgent(Long agentId);

	void apiRestoreAgent(Long agentId);
}
