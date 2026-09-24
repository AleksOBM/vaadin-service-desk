package com.example.vsd.manager.controller.admin;

import com.example.vsd.grpc.messages.AgentProto;
import com.example.vsd.manager.service.agent.AgentService;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgentGrpcServiceAsyncTest {

	@Mock
	private AgentService agentService;

	@InjectMocks
	AdminAgentGrpcController controller;

	@Test
	void createAgent_asyncResponse() throws Exception {

		AgentProto request = AgentProto.newBuilder()
				.setId(1L)
				.setName("Test Agent")
				.build();
		AgentProto expected = request.toBuilder().build();

		when(agentService.apiCreateAgent(any(AgentProto.class))).thenReturn(expected);

		CompletableFuture<AgentProto> responseFuture = new CompletableFuture<>();

		StreamObserver<AgentProto> observer = new StreamObserver<>() {
			@Override
			public void onNext(AgentProto value) {
				responseFuture.complete(value);
			}

			@Override
			public void onError(Throwable t) {
				responseFuture.completeExceptionally(t);
			}

			@Override
			public void onCompleted() {
				// no-op
			}
		};

		// when
		controller.createAgent(request, observer);

		// then
		AgentProto response = responseFuture.get(5, TimeUnit.SECONDS);
		assertThat(response).isEqualTo(expected);
	}
}