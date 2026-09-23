package com.example.vsd.frontend.grpc.client;

import com.example.vsd.frontend.mapper.ClientMapper;
import com.example.vsd.frontend.model.Client;
import com.example.vsd.grpc.messages.ClientProto;
import com.example.vsd.grpc.services.admin.AdminClientControllerGrpc.AdminClientControllerBlockingStub;
import com.example.vsd.grpc.services.free.FreeControllerGrpc.FreeControllerBlockingStub;
import com.google.protobuf.Empty;
import com.google.protobuf.StringValue;
import lombok.AccessLevel;
import lombok.NonNull;
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
		return freeStub.findClients(StringValue.of(text)).getClientsList().stream()
				.map(ClientMapper::toClient)
				.toList();
	}

	public Client createClient(@NonNull String name) {
		ClientProto response = adminStub.createClient(
				ClientProto.newBuilder()
						.setName(name)
						.build()
		);
		return ClientMapper.toClient(response);
	}

	public List<Client> getAllClients() {
		var response = freeStub.getAllClients(Empty.getDefaultInstance());
		return response.getClientsList().stream()
				.map(ClientMapper::toClient)
				.toList();
	}

	public void markAsDeleted(Long clientId) {
		// TODO: 23.09.2026
	}
}
