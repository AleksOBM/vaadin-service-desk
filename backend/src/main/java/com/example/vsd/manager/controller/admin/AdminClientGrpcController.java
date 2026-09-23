package com.example.vsd.manager.controller.admin;

import com.example.vsd.grpc.messages.ClientProto;
import com.example.vsd.grpc.services.admin.AdminClientControllerGrpc.AdminClientControllerImplBase;
import com.example.vsd.manager.service.client.ClientApiService;
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
public class AdminClientGrpcController extends AdminClientControllerImplBase {

	ClientApiService clientService;

	@Override
	public void createClient(@NonNull ClientProto request,
	                         StreamObserver<ClientProto> responseObserver) {
		log.debug("""
						Получен gRPC запрос: createClient
						{
							"id": {}
							"name": "{}"
						}""",
				request.getId(),
				request.getName()
		);

		try {
			ClientProto proto = clientService.apiCreateClient(request);
			responseObserver.onNext(proto);
			responseObserver.onCompleted();

		} catch (Exception e) {
			responseObserver.onError(
					new StatusRuntimeException(Status.fromThrowable(e))
			);
		}

	}

	@Override
	public void restoreClient(@NonNull Int64Value request,
	                          StreamObserver<Empty> responseObserver) {
		log.debug("""
						Получен gRPC запрос: restoreClient
						{
							"id": {}
						}""",
				request.getValue()
		);

		try {
			clientService.apiRestoreClient(request.getValue());
			responseObserver.onNext(Empty.getDefaultInstance());
			responseObserver.onCompleted();

		} catch (Exception e) {
			responseObserver.onError(
					new StatusRuntimeException(Status.fromThrowable(e))
			);
		}
	}

	@Override
	public void deleteClientPermanently(@NonNull Int64Value request,
	                                    StreamObserver<Empty> responseObserver) {
		log.debug("""
						Получен gRPC запрос: deleteClientPermanently
						{
							"id": {}
						}""",
				request.getValue()
		);

		try {
			clientService.apiDeleteClient(request.getValue());
			responseObserver.onNext(Empty.getDefaultInstance());
			responseObserver.onCompleted();

		} catch (Exception e) {
			responseObserver.onError(
					new StatusRuntimeException(Status.fromThrowable(e))
			);
		}
	}

}
