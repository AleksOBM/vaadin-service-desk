package com.example.vsd.manager.service.agent;

import com.example.vsd.grpc.messages.AgentProto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface AgentService {

	@Transactional(readOnly = true)
	List<AgentProto> getAllAgents();

	@Transactional(readOnly = true)
	List<AgentProto> findAgents(String text);

	void saveAgent(AgentProto request);

	void deleteAgent(Long agentId);

	void restoreAgent(Long agentId);

	void setAgentDeleted(long agentId);
}
