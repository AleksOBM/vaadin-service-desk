package com.example.application.backend.service;

import com.example.application.backend.model.*;
import com.example.application.backend.repository.AgentRepository;
import com.example.application.backend.repository.ClientRepository;
import com.example.application.backend.repository.OrderRepository;
import com.example.application.backend.util.enums.EntityType;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.stream.Stream;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RecycleService {

    OrderRepository orderRepository;
    ClientRepository clientRepository;
    AgentRepository agentRepository;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Autowired
    public RecycleService(OrderRepository orderRepository,
                          ClientRepository clientRepository,
                          AgentRepository agentRepository) {
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
        this.agentRepository = agentRepository;
    }

    public Collection<BaseEntity> getRecycleData(String text) {
        return Stream.of(orderRepository.findAllByDeletedTrue(),
                        clientRepository.findAllByDeletedTrue(),
                        agentRepository.findAllByDeletedTrue())
                .flatMap(Collection::stream)
                .map(entity -> (BaseEntity) entity)
                .filter(entity ->
                        entity.getServiceDeskNumber().toLowerCase().contains(text) ||
                                entity.getCreationDate().format(formatter).contains(text) ||
                                entity.getLastUpdated().format(formatter).contains(text)
                ).toList();
    }

    public void markAsDeleted(@NonNull BaseEntity entity) {
        entity.setDeleted(true);
        saveEntity(entity);
    }

    public void restore(@NonNull BaseEntity entity) {
        entity.setDeleted(false);
        saveEntity(entity);
    }

    public void deleteForever(@NonNull BaseEntity entity) {
        EntityType type = entity.getType();
        switch (type) {
            case ORDER -> orderRepository.delete((Order) entity);
            case CLIENT -> clientRepository.delete((Client) entity);
            case AGENT -> agentRepository.delete((Agent) entity);
        }
    }

    private void saveEntity(@NonNull BaseEntity entity) {
        EntityType type = entity.getType();
        switch (type) {
            case ORDER -> orderRepository.save((Order) entity);
            case CLIENT -> clientRepository.save((Client) entity);
            case AGENT -> agentRepository.save((Agent) entity);
        }
    }
}
