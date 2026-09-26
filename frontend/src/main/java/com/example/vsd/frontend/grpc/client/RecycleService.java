package com.example.vsd.frontend.grpc.client;

import com.example.vsd.frontend.mapper.BaseEntityMapper;
import com.example.vsd.frontend.model.BaseEntity;
import com.example.vsd.grpc.messages.GetRecycleResponse;
import com.example.vsd.grpc.services.admin.AdminAgentControllerGrpc.AdminAgentControllerBlockingStub;
import com.example.vsd.grpc.services.admin.AdminClientControllerGrpc.AdminClientControllerBlockingStub;
import com.example.vsd.grpc.services.admin.AdminOrderControllerGrpc.AdminOrderControllerBlockingStub;
import com.example.vsd.grpc.services.admin.AdminRecycleControllerGrpc.AdminRecycleControllerBlockingStub;
import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
import com.google.protobuf.StringValue;
import io.grpc.StatusRuntimeException;
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
		GetRecycleResponse response;
		try {
			response = recycleStub.findRecycle(StringValue.of(text));
			log.debug("Recycle content getting by text {} was successful", text);
		} catch (StatusRuntimeException e) {
			log.error("Error while getting recycle with text {}", text, e);
			throw e;
		}
		return response.getRecyclesList().stream()
				.map(BaseEntityMapper::toEntity)
				.toList();
	}

	public List<BaseEntity> getRecycle() {
		GetRecycleResponse response;
		try {
			response = recycleStub.getFullRecycle(Empty.getDefaultInstance());
			log.debug("Recycle content getting was successful");
		} catch (StatusRuntimeException e) {
			log.error("Error while getting recycle content", e);
			throw e;
		}
		return response.getRecyclesList().stream()
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
		try {
			var _ = adminAgentStub.restoreAgent(Int64Value.of(agentId));
			log.debug("Restore agent with id {} was successful", agentId);
		}  catch (StatusRuntimeException e) {
			log.error("Error while restoring agent with id {}", agentId, e);
			throw e;
		}
	}

	public void deleteAgentPermanently(Long agentId) {
		try {
			var _ = adminAgentStub.deleteAgentPermanently(Int64Value.of(agentId));
			log.debug("Delete agent with id {} was successful", agentId);
		} catch (StatusRuntimeException e) {
			log.error("Error while deleting agent with id {}", agentId, e);
			throw e;
		}
	}

	public void restoreOrder(Long orderId) {
		try {
			var _ = adminOrderStub.restoreOrder(Int64Value.of(orderId));
			log.debug("Restore order with id {} was successful", orderId);
		} catch (StatusRuntimeException e) {
			log.error("Error while restoring order with id {}", orderId, e);
			throw e;
		}
	}

	public void deleteOrderPermanently(Long orderId) {
		try {
			var _ = adminOrderStub.deleteOrderPermanently(Int64Value.of(orderId));
			log.debug("Delete order with id {} was successful", orderId);
		} catch (StatusRuntimeException e) {
			log.error("Error while deleting order with id {}", orderId, e);
			throw e;
		}
	}

	public void restoreClient(Long clientId) {
		try {
			var _ = adminClientStub.restoreClient(Int64Value.of(clientId));
			log.debug("Restore client with id {} was successful", clientId);
		} catch (StatusRuntimeException e) {
			log.error("Error while restoring client with id {}", clientId, e);
			throw e;
		}
	}

	public void deleteClientPermanently(Long clientId) {
		try {
			var _ = adminClientStub.deleteClientPermanently(Int64Value.of(clientId));
			log.debug("Delete client with id {} was successful", clientId);
		} catch (StatusRuntimeException e) {
			log.error("Error while deleting client with id {}", clientId, e);
			throw e;
		}
	}
}
