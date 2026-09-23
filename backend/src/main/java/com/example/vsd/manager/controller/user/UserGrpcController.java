package com.example.vsd.manager.controller.user;

import com.example.vsd.grpc.messages.OrderProto;
import com.example.vsd.grpc.services.user.UserControllerGrpc.UserControllerImplBase;
import com.example.vsd.manager.service.order.OrderApiService;
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
public class UserGrpcController extends UserControllerImplBase {

	OrderApiService orderApiService;

	public void createOrder(OrderProto request,
	                        StreamObserver<OrderProto> responseObserver) {
		log.debug("""
						Получен gRPC запрос: createOrder
						{
							"name": "{}",
							"description": "{}",
							"startLine": "{}"
							"deadLine": "{}"
							"completedDate": "{}"
							"client": {
								"id": {},
								"name": {}
							},
							"agent": {
								"id": {},
								"name": {}
							}
						}""",
				request.getName(),
				request.getDescription(),
				request.getStartLine(),
				request.getDeadLine(),
				request.getCompletedDate(),
				request.getClient().getId(),
				request.getClient().getName(),
				request.getAgent().getId(),
				request.getAgent().getName()
		);

		try {
			OrderProto proto = orderApiService.apiCreateOrder(request);
			responseObserver.onNext(proto);
			responseObserver.onCompleted();

		} catch (Exception e) {
			responseObserver.onError(
					new StatusRuntimeException(Status.fromThrowable(e))
			);
		}
	}

	public void updateOrder(@NonNull OrderProto request,
	                        StreamObserver<OrderProto> responseObserver) {
		log.debug("""
						Получен gRPC запрос: updateOrder
						{
							"id": {}
							"name": "{}",
							"description": "{}",
							"startLine": "{}"
							"deadLine": "{}"
							"completedDate": "{}"
							"client": {
								"id": {},
								"name": {}
							},
							"agent": {
								"id": {},
								"name": {}
							}
						}""",
				request.getId(),
				request.getName(),
				request.getDescription(),
				request.getStartLine(),
				request.getDeadLine(),
				request.getCompletedDate(),
				request.getClient().getId(),
				request.getClient().getName(),
				request.getAgent().getId(),
				request.getAgent().getName()
		);

		try {
			OrderProto proto = orderApiService.apiUpdateOrder(request);
			responseObserver.onNext(proto);
			responseObserver.onCompleted();

		} catch (Exception e) {
			responseObserver.onError(
					new StatusRuntimeException(Status.fromThrowable(e))
			);
		}
	}

	public void setOrderDeleted(@NonNull Int64Value request,
	                            StreamObserver<Empty> responseObserver) {
		log.debug("""
						Получен gRPC запрос: setOrderDeleted
						{
							"id": {}
						}""",
				request.getValue()
		);

		try {
			orderApiService.apiSetOrderDeleted(request.getValue());
			responseObserver.onNext(Empty.getDefaultInstance());
			responseObserver.onCompleted();

		} catch (Exception e) {
			responseObserver.onError(
					new StatusRuntimeException(Status.fromThrowable(e))
			);
		}
	}

}
