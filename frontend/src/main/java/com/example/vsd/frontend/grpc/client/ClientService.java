package com.example.vsd.frontend.grpc.client;

import com.example.vsd.frontend.mapper.ClientMapper;
import com.example.vsd.frontend.model.Client;
import com.example.vsd.grpc.messages.GetClientsResponse;
import com.example.vsd.grpc.services.admin.AdminClientControllerGrpc.AdminClientControllerBlockingStub;
import com.example.vsd.grpc.services.free.FreeControllerGrpc.FreeControllerBlockingStub;
import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
import com.google.protobuf.StringValue;
import io.grpc.StatusRuntimeException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClientService {

	AdminClientControllerBlockingStub adminStub;
	FreeControllerBlockingStub freeStub;

	public List<Client> getContent(String text) {
		GetClientsResponse response;
		try {
			response = freeStub.findClients(StringValue.of(text));
			log.debug("Found {} agents by text '{}'", text, response.getClientsList().size());
		} catch (StatusRuntimeException e) {
			log.error("Error while fetching agents by text '{}'", text, e);
			throw e;
		}
		return response.getClientsList().stream()
				.map(ClientMapper::toClient)
				.toList();
	}

	public void saveClient(Client client) {
		try {
			var _ = adminStub.saveClient(ClientMapper.toProto(client));
			log.debug("Client with name {} has been saved", client.getName());
		} catch (StatusRuntimeException e) {
			log.error("Error while saving client with name {}", client.getName(), e);
		}
	}

	public List<Client> getAllClients() {
		GetClientsResponse response;
		try {
			response = freeStub.getAllClients(Empty.getDefaultInstance());
			log.debug("Found {} clients", response.getClientsList().size());
		} catch (StatusRuntimeException e) {
			log.error("Error while fetching agents", e);
			throw e;
		}
		return response.getClientsList().stream()
				.map(ClientMapper::toClient)
				.toList();
	}

	public void markAsDeleted(Long clientId) {
		try {
			var _ = adminStub.setClientDeleted(Int64Value.of(clientId));
			log.debug("Client with id {} has been deleted", clientId);
		} catch (StatusRuntimeException e) {
			log.error("Error while saving client with id {}", clientId, e);
			throw e;
		}
	}

}
