package com.example.vsd.frontend.mapper;

import com.example.vsd.frontend.model.Client;
import com.example.vsd.grpc.messages.ClientProto;
import com.example.vsd.serialization.timestamp.TimestampUtils;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ClientMapper {

	public ClientProto toProto(@NonNull Client client) {
		return ClientProto.newBuilder()
				.setId(client.getId())
				.setName(client.getName())
				.build();
	}

	public Client toClient(@NonNull ClientProto proto) {
		return Client.builder()
				.id(proto.getId())
				.name(proto.getName())
				.serviceDeskNumber(proto.getServiceDeskNumber())
				.creationDate(TimestampUtils.toLocalDateTime(proto.getCreationDate()))
				.lastUpdated(TimestampUtils.toLocalDateTime(proto.getLastUpdated()))
				.deleted(proto.getDeleted())
				.build();
	}
}
