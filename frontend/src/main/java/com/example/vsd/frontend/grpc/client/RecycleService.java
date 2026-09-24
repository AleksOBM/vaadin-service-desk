package com.example.vsd.frontend.grpc.client;

import com.example.vsd.frontend.mapper.BaseEntityMapper;
import com.example.vsd.frontend.model.BaseEntity;
import com.example.vsd.grpc.services.admin.AdminAgentControllerGrpc.AdminAgentControllerBlockingStub;
import com.example.vsd.grpc.services.admin.AdminClientControllerGrpc.AdminClientControllerBlockingStub;
import com.example.vsd.grpc.services.admin.AdminOrderControllerGrpc.AdminOrderControllerBlockingStub;
import com.example.vsd.grpc.services.admin.AdminRecycleControllerGrpc.AdminRecycleControllerBlockingStub;
import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
import com.google.protobuf.StringValue;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RecycleService {

	AdminRecycleControllerBlockingStub recycleStub;
	AdminClientControllerBlockingStub adminClientStub;
	AdminAgentControllerBlockingStub adminAgentStub;
	AdminOrderControllerBlockingStub adminOrderStub;

	public List<BaseEntity> getContent(String text) {
		return recycleStub.findRecycle(StringValue.of(text))
				.getRecyclesList().stream()
				.map(BaseEntityMapper::toEntity)
				.toList();
	}

	public List<BaseEntity> getRecycle() {
		return recycleStub.getFullRecycle(Empty.getDefaultInstance())
				.getRecyclesList().stream()
				.map(BaseEntityMapper::toEntity)
				.toList();
	}

	public void restore(@NonNull BaseEntity entity) {
		switch (entity.getType()) {
			case CLIENT -> restoreClient(entity.getId());
			case AGENT -> restoreAgent(entity.getId());
			case ORDER -> restoreOrder(entity.getId());
		}
	}

	public void deleteForever(@NonNull BaseEntity entity) {
		switch (entity.getType()) {
			case CLIENT -> deleteClientPermanently(entity.getId());
			case AGENT -> deleteAgentPermanently(entity.getId());
			case ORDER -> deleteOrderPermanently(entity.getId());
		}
	}

	public void restoreAgent(Long agentId) {
		Empty result = adminAgentStub
				.restoreAgent(Int64Value.of(agentId));
		if (result != null) {
			log.debug("restore agent {}", agentId);
		}
	}

	public void deleteAgentPermanently(Long agentId) {
		Empty result = adminAgentStub
				.deleteAgentPermanently(Int64Value.of(agentId));
		if (result != null) {
			log.debug("delete agent {}", agentId);
		}
	}

	public void restoreOrder(Long orderId) {
		Empty result = adminOrderStub
				.restoreOrder(Int64Value.of(orderId));
		if (result != null) {
			log.debug("Order with id {} restored.", orderId);
		}
	}

	public void deleteOrderPermanently(Long orderId) {
		Empty result = adminOrderStub
				.deleteOrderPermanently(Int64Value.of(orderId));
		if (result != null) {
			log.debug("Order with id {} was deleted.", orderId);
		}
	}

	public void restoreClient(Long clientId) {
		Empty result = adminClientStub
				.restoreClient(Int64Value.of(clientId));
		if (result != null) {
			log.debug("restore client with id {}", clientId);
		}
	}

	public void deleteClientPermanently(Long clientId) {
		Empty result = adminClientStub
				.deleteClientPermanently(Int64Value.of(clientId));
		if (result != null) {
			log.debug("delete client with id {}", clientId);
		}
	}
}
