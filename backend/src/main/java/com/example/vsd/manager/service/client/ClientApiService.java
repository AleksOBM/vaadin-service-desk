package com.example.vsd.manager.service.client;

import com.example.vsd.grpc.messages.ClientProto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ClientApiService {

	@Transactional(readOnly = true)
	List<ClientProto> apiFindAll();

	@Transactional(readOnly = true)
	List<ClientProto> findClients(String text);

	ClientProto apiCreateClient(ClientProto request);

	void apiDeleteClient(Long clientId);

	void apiRestoreClient(Long clientId);
}
