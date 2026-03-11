package com.example.application.backend.service;

import com.example.application.backend.model.BaseEntity;
import com.example.application.backend.model.Client;
import com.example.application.backend.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    public Collection<Client> findAll() {
        return clientRepository.findAll().stream().filter(client -> !client.isDeleted()).toList();
    }

    public Collection<Client> getContent(String text) {
        return clientRepository.findAll().stream()
                .filter(client -> !client.isDeleted())
                .filter(client ->
                        client.getName().toLowerCase().contains(text.toLowerCase()) ||
                                client.getServiceDeskNumber().toLowerCase().contains(text.toLowerCase())
                )
                .toList();
    }

    public Collection<Client> findDeleted() {
        return clientRepository.findAll().stream().filter(BaseEntity::isDeleted).toList();
    }

    public void save(Client client) {
        clientRepository.save(client);
    }

    public long count() {
        return clientRepository.count();
    }

    public void deleteForever(Client client) {
        clientRepository.delete(client);
    }

    public void restore(Client client) {
        client.setDeleted(false);
        client.setLastUpdated(LocalDateTime.now());
        clientRepository.save(client);
    }
}
