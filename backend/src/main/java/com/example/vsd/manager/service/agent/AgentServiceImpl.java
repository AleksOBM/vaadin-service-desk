package com.example.vsd.manager.service.agent;

import com.example.vsd.grpc.messages.AgentProto;
import com.example.vsd.manager.enity.Agent;
import com.example.vsd.manager.exception.NotFoundException;
import com.example.vsd.manager.exception.ValidationException;
import com.example.vsd.manager.mapper.AgentMapper;
import com.example.vsd.manager.repository.AgentRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements AgentService {

	private final AgentRepository agentRepository;

	@Override
	public void saveAgent(@NonNull AgentProto proto) {
		if (!proto.hasName()) {
			throw new ValidationException("Agent name is required");
		}

		if (agentRepository.existsByName(proto.getName())) {
			throw new IllegalArgumentException("Agent already exists");
		}

		if (!proto.hasId()) {
			agentRepository.save(AgentMapper.toEntity(proto));
			return;
		}

		var agent = getAgentById(proto.getId());
		agent.setName(proto.getName());
		agentRepository.save(agent);
	}

	@Override
	public List<AgentProto> findAgents(String text) {
		return agentRepository.findAgents(text).stream()
				.map(AgentMapper::toProto)
				.toList();
	}

	@Override
	public List<AgentProto> getAllAgents() {
		return agentRepository.findAllByDeletedFalse().stream()
				.sorted(Comparator.comparingLong(Agent::getId))
				.map(AgentMapper::toProto)
				.toList();
	}

	@Override
	public void setAgentDeleted(long agentId) {
		var agent = getAgentById(agentId);
		if (agent.isDeleted()) {
			throw new IllegalStateException(
					"Сотрудник с id=%s удален. Проверьте корзину."
							.formatted(agentId));
		}
		agent.setDeleted(true);
		agentRepository.save(agent);
	}

	@Override
	public void restoreAgent(Long agentId) {
		var agent = getAgentById(agentId);
		agent.setDeleted(false);
		agentRepository.save(agent);
	}

	@Override
	public void deleteAgent(Long agentId) {
		agentRepository.deleteById(agentId);
	}

	@NonNull
	private Agent getAgentById(long agentId) {
		return agentRepository.findById(agentId)
				.orElseThrow(() -> new NotFoundException(
						"Сотрудник с id=%s не найден".formatted(agentId)));
	}

}
