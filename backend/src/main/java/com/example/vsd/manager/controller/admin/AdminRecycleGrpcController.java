package com.example.vsd.manager.controller.admin;

import com.example.vsd.grpc.messages.BaseEntityProto;
import com.example.vsd.grpc.messages.GetRecycleResponse;
import com.example.vsd.grpc.services.admin.AdminRecycleControllerGrpc.AdminRecycleControllerImplBase;
import com.example.vsd.manager.service.recycle.RecycleService;
import com.google.protobuf.Empty;
import com.google.protobuf.StringValue;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.grpc.server.service.GrpcService;

import java.util.Collection;
import java.util.List;

@Slf4j
@GrpcService
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminRecycleGrpcController extends AdminRecycleControllerImplBase {

	RecycleService recycleService;

	public void getFullRecycle(Empty request,
	                           StreamObserver<GetRecycleResponse> responseObserver) {
		log.debug("""
				Получен gRPC запрос: getRecycle""");

		try {
			Collection<BaseEntityProto> proto = recycleService.apiFindAll();
			responseObserver.onNext(GetRecycleResponse.newBuilder()
					.addAllRecycles(proto)
					.build());
			responseObserver.onCompleted();

		} catch (Exception e) {
			log.debug(e.getMessage(), e);
			responseObserver.onError(
					new StatusRuntimeException(Status.fromThrowable(e))
			);
		}
	}

	public void findRecycle(@NonNull StringValue request,
	                        StreamObserver<GetRecycleResponse> responseObserver) {
		log.debug("""
				Получен gRPC запрос: findRecycle
				{
					"text": {}
				}""", request.getValue());

		try {
			List<BaseEntityProto> proto = recycleService.findRecycle(request.getValue());
			responseObserver.onNext(GetRecycleResponse.newBuilder()
					.addAllRecycles(proto)
					.build());
			responseObserver.onCompleted();

		} catch (Exception e) {
			log.debug(e.getMessage(), e);
			responseObserver.onError(
					new StatusRuntimeException(Status.fromThrowable(e))
			);
		}
	}

}
