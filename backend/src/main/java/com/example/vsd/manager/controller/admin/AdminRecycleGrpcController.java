package com.example.vsd.manager.controller.admin;

import com.example.vsd.grpc.messages.BaseEntityProto;
import com.example.vsd.grpc.messages.GetRecycleResponse;
import com.example.vsd.grpc.services.admin.AdminRecycleControllerGrpc.AdminRecycleControllerImplBase;
import com.example.vsd.manager.service.recycle.RecycleApiService;
import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.server.service.GrpcService;

import java.util.Collection;

@Slf4j
@GrpcService
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminRecycleGrpcController extends AdminRecycleControllerImplBase {

	RecycleApiService recycleApiService;

	public void getRecycle(Empty request,
	                       StreamObserver<GetRecycleResponse> responseObserver) {
		log.debug("""
				Получен gRPC запрос: getRecycle""");

		try {
			Collection<BaseEntityProto> proto = recycleApiService.apiFindAll();
			responseObserver.onNext(GetRecycleResponse.newBuilder()
					.addAllRecycles(proto)
					.build());
			responseObserver.onCompleted();

		} catch (Exception e) {
			responseObserver.onError(
					new StatusRuntimeException(Status.fromThrowable(e))
			);
		}
	}

}
