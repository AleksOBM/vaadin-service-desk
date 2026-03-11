package com.example.application.backend.service;

import com.example.application.backend.model.Agent;
import com.example.application.backend.model.BaseEntity;
import com.example.application.backend.repository.AgentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AgentService {
    private final AgentRepository agentRepository;

    public void save(Agent agent) {
        agentRepository.save(agent);
    }

    public Collection<Agent> findAll() {
        return agentRepository.findAll().stream().filter(agent -> !agent.isDeleted()).toList();
    }

    public Collection<Agent> getContent(String text) {
        return agentRepository.findAll().stream()
                .filter(agent -> !agent.isDeleted())
                .filter(agent ->
                        agent.getName().toLowerCase().contains(text.toLowerCase()) ||
                                agent.getServiceDeskNumber().toLowerCase().contains(text.toLowerCase())
                )
                .toList();
    }

    public Collection<Agent> findDeleted() {
        return agentRepository.findAll().stream().filter(BaseEntity::isDeleted).toList();
    }

    public long count() {
        return agentRepository.count();
    }

    public void deleteForever(Agent agent) {
        agentRepository.delete(agent);
    }

    public void restore(Agent agent) {
        agent.setDeleted(false);
        agent.setLastUpdated(LocalDateTime.now());
        agentRepository.save(agent);
    }
}
