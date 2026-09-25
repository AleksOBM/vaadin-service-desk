package com.example.vsd.frontend.mapper;

import com.example.vsd.frontend.model.Client;
import com.example.vsd.grpc.messages.ClientProto;
import com.example.vsd.serialization.model.EntityType;
import com.example.vsd.serialization.timestamp.TimestampUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientMapper {

	public static ClientProto toProto(Client client) {
		ClientProto.Builder builder = ClientProto.newBuilder()
				.setName(client.getName());
		if (client.hasId()) {
			builder.setId(client.getId());
		}
		return builder.build();
	}

	public static Client toClient(ClientProto proto) {
		return Client.builder()
				.id(proto.getId())
				.name(proto.getName())
				.type(EntityType.CLIENT)
				.serviceDeskNumber(proto.getServiceDeskNumber())
				.creationDate(TimestampUtils.toLocalDateTime(proto.getCreationDate()))
				.lastUpdated(TimestampUtils.toLocalDateTime(proto.getLastUpdated()))
				.deleted(proto.getDeleted())
				.build();
	}
}
