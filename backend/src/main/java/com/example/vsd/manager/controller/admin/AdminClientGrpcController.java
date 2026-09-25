package com.example.vsd.manager.controller.admin;

import com.example.vsd.grpc.messages.ClientProto;
import com.example.vsd.grpc.services.admin.AdminClientControllerGrpc.AdminClientControllerImplBase;
import com.example.vsd.manager.service.client.ClientService;
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

	ClientService clientService;

	@Override
	public void saveClient(@NonNull ClientProto request,
	                       StreamObserver<Empty> responseObserver) {
		log.debug("""
						Получен gRPC запрос: saveClient
						{
							"id": {}
							"name": "{}"
						}""",
				request.hasId() ? request.getId() : "null",
				request.getName()
		);

		try {
			clientService.saveClient(request);
			responseObserver.onNext(Empty.getDefaultInstance());
			responseObserver.onCompleted();

		} catch (Exception e) {
			log.debug(e.getMessage(), e);
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
			clientService.restoreClient(request.getValue());
			responseObserver.onNext(Empty.getDefaultInstance());
			responseObserver.onCompleted();

		} catch (Exception e) {
			log.debug(e.getMessage(), e);
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
			clientService.deleteClient(request.getValue());
			responseObserver.onNext(Empty.getDefaultInstance());
			responseObserver.onCompleted();

		} catch (Exception e) {
			log.debug(e.getMessage(), e);
			responseObserver.onError(
					new StatusRuntimeException(Status.fromThrowable(e))
			);
		}
	}

	public void setClientDeleted(@NonNull Int64Value request,
	                             StreamObserver<Empty> responseObserver) {
		log.debug("""
						Получен gRPC запрос: setClientDeleted
						{
							"id": {}
						}""",
				request.getValue()
		);

		try {
			clientService.setClientDeleted(request.getValue());
			responseObserver.onNext(Empty.getDefaultInstance());
			responseObserver.onCompleted();

		} catch (Exception e) {
			log.debug(e.getMessage(), e);
			responseObserver.onError(
					new StatusRuntimeException(Status.fromThrowable(e))
			);
		}
	}

}
