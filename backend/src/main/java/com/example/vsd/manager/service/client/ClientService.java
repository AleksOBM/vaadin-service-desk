package com.example.vsd.manager.service.client;

import com.example.vsd.manager.enity.Client;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Transactional(readOnly = true)
public interface ClientService {

	Collection<Client> findAll();

	@Transactional
	void save(Client client);
}
