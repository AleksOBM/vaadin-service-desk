package com.example.vsd.manager.mapper;

import com.example.vsd.grpc.messages.ClientProto;
import com.example.vsd.manager.enity.Client;
import com.example.vsd.serialization.timestamp.TimestampUtils;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ClientMapper {

	public Client toEntity(@NonNull ClientProto proto) {
		return Client.builder()
				.id(proto.hasId() ? proto.getId() : null)
				.name(proto.hasName() ? proto.getName() : null)
				.orders(List.of())
				.deleted(false)
				.build();
	}

	public ClientProto toProto(@NonNull Client entity) {
		return ClientProto.newBuilder()
				.setId(entity.getId())
				.setName(entity.getName())
				.setServiceDeskNumber(entity.getServiceDeskNumber())
				.setCreationDate(TimestampUtils.toTimestamp(entity.getCreationDate()))
				.setLastUpdated(TimestampUtils.toTimestamp(entity.getLastUpdated()))
				.setDeleted(entity.isDeleted())
				.build();
	}

}
