package com.example.vsd.manager.controller.admin;

import com.example.vsd.grpc.messages.AgentProto;
import com.example.vsd.grpc.services.admin.AdminAgentControllerGrpc.AdminAgentControllerImplBase;
import com.example.vsd.manager.service.agent.AgentService;
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

	AgentService agentService;

	@Override
	public void saveAgent(@NonNull AgentProto request, StreamObserver<Empty> responseObserver) {

		log.debug("""
						Получен gRPC запрос: saveAgent
						{
							"name": "{}"
						}""",
				request.getName()
		);

		try {
			agentService.saveAgent(request);
			responseObserver.onNext(Empty.getDefaultInstance());
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
			agentService.restoreAgent(request.getValue());
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
			agentService.deleteAgent(request.getValue());
			responseObserver.onNext(Empty.getDefaultInstance());
			responseObserver.onCompleted();

		} catch (Exception e) {
			responseObserver.onError(
					new StatusRuntimeException(Status.fromThrowable(e))
			);
		}
	}

	public void setAgentDeleted(@NonNull Int64Value request,
	                            StreamObserver<Empty> responseObserver) {
		log.debug("""
						Получен gRPC запрос: setAgentDeleted
						{
							"id": {}
						}""",
				request.getValue()
		);

		try {
			agentService.setAgentDeleted(request.getValue());
			responseObserver.onNext(Empty.getDefaultInstance());
			responseObserver.onCompleted();

		} catch (Exception e) {
			responseObserver.onError(
					new StatusRuntimeException(Status.fromThrowable(e))
			);
		}
	}

}
