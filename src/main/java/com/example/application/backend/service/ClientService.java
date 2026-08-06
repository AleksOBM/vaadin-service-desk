package com.example.application.backend.service;

import com.example.application.backend.model.Client;
import com.example.application.backend.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ClientService {
	private final ClientRepository clientRepository;

	public Collection<Client> findAll() {
		return clientRepository.findAllByDeletedFalse();
	}

	public Collection<Client> getContent(String text) {
		return clientRepository.getContent(text);
	}

	public void save(Client client) {
		clientRepository.save(client);
	}
}
