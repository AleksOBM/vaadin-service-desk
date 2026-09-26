package com.example.vsd.manager.service.client;

import com.example.vsd.grpc.messages.ClientProto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ClientService {

	@Transactional(readOnly = true)
	List<ClientProto> getAllClients();

	@Transactional(readOnly = true)
	List<ClientProto> findClients(String text);

	void saveClient(ClientProto request);

	void deleteClient(Long clientId);

	void restoreClient(Long clientId);

	void setClientDeleted(long clientId);
}
