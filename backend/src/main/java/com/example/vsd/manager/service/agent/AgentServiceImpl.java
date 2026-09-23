package com.example.vsd.manager.service.agent;

import com.example.vsd.grpc.messages.AgentProto;
import com.example.vsd.manager.exception.NotFoundException;
import com.example.vsd.manager.exception.ValidationException;
import com.example.vsd.manager.mapper.AgentMapper;
import com.example.vsd.manager.enity.Agent;
import com.example.vsd.manager.repository.AgentRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements AgentApiService, AgentService {

	private final AgentRepository agentRepository;

	@Override
	public void save(Agent agent) {
		agentRepository.save(agent);
	}

	@Override
	public Collection<Agent> findAll() {
		return agentRepository.findAllByDeletedFalse();
	}

	@Override
	public List<AgentProto> findAgents(String text) {
		return agentRepository.findAgents(text).stream()
				.map(AgentMapper::toProto)
				.toList();
	}

	@Override
	public List<AgentProto> apiFindAll() {
		return agentRepository.findAllByDeletedFalse().stream()
				.sorted(Comparator.comparingLong(Agent::getId))
				.map(AgentMapper::toProto)
				.toList();
	}

	@Override
	public AgentProto apiCreateAgent(@NonNull AgentProto proto) {
		if (!proto.hasName()) {
			throw new ValidationException("Agent name is required");
		}

		agentRepository.findByName(proto.getName()).ifPresent(_ -> {
			throw new IllegalArgumentException("Agent already exists");
		});

		Agent agent = agentRepository.save(AgentMapper.toEntity(proto));
		return AgentMapper.toProto(agent);
	}

	@Override
	public void apiDeleteAgent(Long agentId) {
		agentRepository.deleteById(agentId);
	}

	@Override
	public void apiRestoreAgent(Long agentId) {
		Agent agent = agentRepository.findById(agentId)
				.orElseThrow(() -> new NotFoundException("Agent not found"));
		agent.setDeleted(false);
		agentRepository.save(agent);
	}
}
