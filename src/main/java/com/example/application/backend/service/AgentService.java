package com.example.application.backend.service;

import com.example.application.backend.model.Agent;
import com.example.application.backend.repository.AgentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AgentService {
	private final AgentRepository agentRepository;

	public void save(Agent agent) {
		agentRepository.save(agent);
	}

	public Collection<Agent> findAll() {
		return agentRepository.findAllByDeletedFalse();
	}

	public Collection<Agent> getContent(String text) {
		return agentRepository.getContent(text);
	}
}
