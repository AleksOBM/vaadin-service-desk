package com.example.vsd.manager.grpc.admin;

import com.example.vsd.grpc.services.admin.AdminOrderControllerGrpc.AdminOrderControllerImplBase;
import com.example.vsd.manager.security.AdminOnly;
import com.example.vsd.manager.service.order.OrderServiceImpl;
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
@AdminOnly
@GrpcService
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminOrderGrpcService extends AdminOrderControllerImplBase {

	OrderServiceImpl orderService;

	@Override
	public void restoreOrder(@NonNull Int64Value request,
	                         StreamObserver<Empty> responseObserver) {
		log.debug("""
						Получен gRPC запрос: restoreOrder
						{
							"id": {}
						}""",
				request.getValue()
		);

		try {
			orderService.restoreOrder(request.getValue());
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
	public void deleteOrderPermanently(@NonNull Int64Value request,
	                                   StreamObserver<Empty> responseObserver) {
		log.debug("""
						Получен gRPC запрос: deleteOrderPermanently
						{
							"id": {}
						}""",
				request.getValue()
		);

		try {
			orderService.deleteOrder(request.getValue());
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
