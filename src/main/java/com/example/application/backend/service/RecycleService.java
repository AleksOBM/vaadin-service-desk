package com.example.application.backend.service;

import com.example.application.backend.model.*;
import com.example.application.backend.util.enums.EntityType;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.stream.Stream;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RecycleService {

    OrderService orderService;
    ClientService clientService;
    AgentService agentService;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Autowired
    public RecycleService(OrderService orderService, ClientService clientService, AgentService agentService) {
        this.orderService = orderService;
        this.clientService = clientService;
        this.agentService = agentService;
    }

    public Collection<BaseEntity> getRecycleData(String text) {
        return Stream.of(
                        orderService.findDeleted(),
                        clientService.findDeleted(),
                        agentService.findDeleted()
                )
                .flatMap(Collection::stream)
                .map(entity -> (BaseEntity) entity)
                .filter(entity ->
                        entity.getServiceDeskNumber().toLowerCase().contains(text) ||
                                entity.getCreationDate().format(formatter).contains(text) ||
                                entity.getLastUpdated().format(formatter).contains(text)
                )
                .toList();
    }

    public void markAsDeleted(BaseEntity entity) {
        entity.setDeleted(true);
        entity.setLastUpdated(LocalDateTime.now());
        EntityType type = entity.getType();
        switch (type) {
            case ORDER -> orderService.save((Order) entity);
            case CLIENT -> clientService.save((Client) entity);
            case AGENT -> agentService.save((Agent) entity);
        }
    }

    public void restore(BaseEntity entity) {
        entity.setDeleted(false);
        entity.setLastUpdated(LocalDateTime.now());
        EntityType type = entity.getType();
        switch (type) {
            case ORDER -> orderService.save((Order) entity);
            case CLIENT -> clientService.save((Client) entity);
            case AGENT -> agentService.save((Agent) entity);
        }
    }

    public void deleteForever(BaseEntity entity) {
        EntityType type = entity.getType();
        switch (type) {
            case ORDER -> orderService.deleteForever((Order) entity);
            case CLIENT -> clientService.deleteForever((Client) entity);
            case AGENT -> agentService.deleteForever((Agent) entity);
        }
    }
}
