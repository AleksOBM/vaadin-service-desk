package com.example.vsd.manager.service.client;


import com.example.vsd.grpc.messages.ClientProto;
import com.example.vsd.manager.enity.Client;
import com.example.vsd.manager.exception.ValidationException;
import com.example.vsd.manager.mapper.ClientMapper;
import com.example.vsd.manager.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

	private final ClientRepository clientRepository;

	@Override
	public List<ClientProto> findClients(String text) {
		return clientRepository.findClients(text).stream()
				.map(ClientMapper::toProto)
				.toList();
	}

	@Override
	public void saveClient(@NonNull ClientProto proto) {
		if (!proto.hasName()) {
			throw new ValidationException("Client name is required");
		}

		if (clientRepository.existsByName(proto.getName())) {
			throw new IllegalArgumentException("Client already exists");
		}

		if (!proto.hasId()) {
			clientRepository.save(ClientMapper.toEntity(proto));
			return;
		}

		var agent = getClientById(proto.getId());
		agent.setName(proto.getName());
		clientRepository.save(agent);
	}

	@Override
	public List<ClientProto> getAllClients() {
		return clientRepository.findAllByDeletedFalse().stream()
				.sorted(Comparator.comparingLong(Client::getId))
				.map(ClientMapper::toProto)
				.toList();
	}

	@Override
	public void deleteClient(Long clientId) {
		clientRepository.deleteById(clientId);
	}

	@Override
	public void restoreClient(Long clientId) {
		Client client = getClientById(clientId);
		client.setDeleted(false);
		clientRepository.save(client);
	}

	@Override
	public void setClientDeleted(long clientId) {
		var client = getClientById(clientId);
		if (client.isDeleted()) {
			throw new IllegalStateException("Клиент с id=%s удален. Проверьте корзину."
					.formatted(clientId));
		}
		client.setDeleted(true);
		clientRepository.save(client);
	}

	@NonNull
	private Client getClientById(Long clientId) {
		return clientRepository.findById(clientId)
				.orElseThrow(() -> new RuntimeException("Клиент с id=%s не найден".formatted(clientId)));
	}
}
