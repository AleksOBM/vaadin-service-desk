package com.example.vsd.manager.grpc.free;

import com.example.vsd.grpc.messages.LoginRequest;
import com.example.vsd.grpc.messages.LoginResponse;
import com.example.vsd.grpc.services.free.FreeControllerGrpc.FreeControllerImplBase;
import com.example.vsd.manager.service.agent.AgentService;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FreeGrpcService extends FreeControllerImplBase {

	AgentService agentService;

	@Override
	public void login(LoginRequest request,
	                  StreamObserver<LoginResponse> responseObserver) {
		log.debug("""
				Получен gRPC запрос: login""");

		try {
			var response = agentService.loginAgent(request.getUsername(), request.getPassword());
			responseObserver.onNext(response);
			responseObserver.onCompleted();
		} catch (Exception e) {
			log.debug(e.getMessage(), e);
			responseObserver.onError(
					new StatusRuntimeException(Status.fromThrowable(e))
			);
		}
	}

}
