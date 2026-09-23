package com.example.vsd.manager.controller.admin;

import com.example.vsd.grpc.messages.AgentProto;
import com.example.vsd.grpc.services.admin.AdminAgentControllerGrpc.AdminAgentControllerImplBase;
import com.example.vsd.manager.service.agent.AgentApiService;
import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminAgentGrpcController extends AdminAgentControllerImplBase {

	AgentApiService agentApiService;

	@Override
	public void createAgent(@NonNull AgentProto request, StreamObserver<AgentProto> responseObserver) {

		log.debug("""
						Получен gRPC запрос: createAgent
						{
							"name": "{}"
						}""",
				request.getName()
		);

		try {
			AgentProto proto = agentApiService.apiCreateAgent(request);
			responseObserver.onNext(proto);
			responseObserver.onCompleted();

		} catch (Exception e) {
			responseObserver.onError(
					new StatusRuntimeException(Status.fromThrowable(e))
			);
		}
	}

	@Override
	public void restoreAgent(@NonNull Int64Value request, StreamObserver<Empty> responseObserver) {

		log.debug("""
						Получен gRPC запрос: restoreAgent
						{
							"id": {}
						}""",
				request.getValue()
		);

		try {
			agentApiService.apiRestoreAgent(request.getValue());
			responseObserver.onNext(Empty.getDefaultInstance());
			responseObserver.onCompleted();

		} catch (Exception e) {
			responseObserver.onError(
					new StatusRuntimeException(Status.fromThrowable(e))
			);
		}
	}

	@Override
	public void deleteAgentPermanently(@NonNull Int64Value request, StreamObserver<Empty> responseObserver) {

		log.debug("""
						Получен gRPC запрос: deleteAgentPermanently
						{
							"id": {}
						}""",
				request.getValue()
		);

		try {
			agentApiService.apiDeleteAgent(request.getValue());
			responseObserver.onNext(Empty.getDefaultInstance());
			responseObserver.onCompleted();

		} catch (Exception e) {
			responseObserver.onError(
					new StatusRuntimeException(Status.fromThrowable(e))
			);
		}
	}

}
