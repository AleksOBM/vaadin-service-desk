package com.example.vsd.manager.service.agent;

import com.example.vsd.manager.enity.Agent;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Transactional(readOnly = true)
public interface AgentService {

	@Transactional
	void save(Agent agent);

	Collection<Agent> findAll();

}
