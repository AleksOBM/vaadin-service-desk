package com.example.vsd.manager.service.client;


import com.example.vsd.grpc.messages.ClientProto;
import com.example.vsd.manager.mapper.ClientMapper;
import com.example.vsd.manager.enity.Client;
import com.example.vsd.manager.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientApiService, ClientService {

	private final ClientRepository clientRepository;

	@Override
	public Collection<Client> findAll() {
		return clientRepository.findAllByDeletedFalse();
	}

	@Override
	public List<ClientProto> findClients(String text) {
		return clientRepository.findClients(text).stream()
				.map(ClientMapper::toProto)
				.toList();
	}

	@Override
	public void save(Client client) {
		clientRepository.save(client);
	}

	@Override
	public List<ClientProto> apiFindAll() {
		return clientRepository.findAllByDeletedFalse().stream()
				.sorted(Comparator.comparingLong(Client::getId))
				.map(ClientMapper::toProto)
				.toList();
	}

	@Override
	public ClientProto apiCreateClient(ClientProto proto) {
		return ClientMapper.toProto(clientRepository.save(ClientMapper.toEntity(proto)));
	}

	@Override
	public void apiDeleteClient(Long clientId) {
		clientRepository.deleteById(clientId);
	}

	@Override
	public void apiRestoreClient(Long clientId) {
		Client client = clientRepository.findById(clientId)
				.orElseThrow(() -> new RuntimeException("Client not found"));
		client.setDeleted(true);
		clientRepository.save(client);
	}
}
