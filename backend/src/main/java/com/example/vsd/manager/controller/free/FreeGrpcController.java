package com.example.vsd.manager.controller.free;

import com.example.vsd.grpc.messages.*;
import com.example.vsd.grpc.services.free.FreeControllerGrpc.FreeControllerImplBase;
import com.example.vsd.manager.service.agent.AgentService;
import com.example.vsd.manager.service.client.ClientService;
import com.example.vsd.manager.service.order.OrderService;
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

import java.util.List;

@Slf4j
@GrpcService
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FreeGrpcController extends FreeControllerImplBase {

	ClientService clientService;
	AgentService agentService;
	OrderService orderService;

	public void getAllClients(Empty request,
	                       StreamObserver<GetClientsResponse> responseObserver) {
		log.debug("""
				Получен gRPC запрос: getClients""");

		try {
			List<ClientProto> proto = clientService.getAllClients();
			responseObserver.onNext(GetClientsResponse.newBuilder()
					.addAllClients(proto)
					.build());
			responseObserver.onCompleted();

		} catch (Exception e) {
			responseObserver.onError(
					new StatusRuntimeException(Status.fromThrowable(e))
			);
		}
	}

	public void getAllAgents(Empty request,
	                      StreamObserver<GetAgentsResponse> responseObserver) {
		log.debug("""
				Получен gRPC запрос: getAgents""");

		try {
			List<AgentProto> proto = agentService.getAllAgents();
			responseObserver.onNext(GetAgentsResponse.newBuilder()
					.addAllAgents(proto)
					.build());
			responseObserver.onCompleted();

		} catch (Exception e) {
			responseObserver.onError(
					new StatusRuntimeException(Status.fromThrowable(e))
			);
		}
	}

	public void getAllOrders(Empty request,
	                      StreamObserver<GetOrdersResponse> responseObserver) {
		log.debug("""
				Получен gRPC запрос: getOrders""");

		try {
			List<OrderProto> proto = orderService.getAllOrders();
			responseObserver.onNext(GetOrdersResponse.newBuilder()
					.addAllOrders(proto)
					.build());
			responseObserver.onCompleted();

		} catch (Exception e) {
			responseObserver.onError(
					new StatusRuntimeException(Status.fromThrowable(e))
			);
		}
	}

	public void findClients(@NonNull StringValue request,
	                        StreamObserver<GetClientsResponse> responseObserver) {
		log.debug("""
				Получен gRPC запрос: findClients
				{
					"text": {}
				}""", request.getValue());

		try {
			List<ClientProto> proto = clientService.findClients(request.getValue());
			responseObserver.onNext(GetClientsResponse.newBuilder()
					.addAllClients(proto)
					.build());
			responseObserver.onCompleted();

		} catch (Exception e) {
			responseObserver.onError(
					new StatusRuntimeException(Status.fromThrowable(e))
			);
		}
	}

	public void findAgents(@NonNull StringValue request,
	                       StreamObserver<GetAgentsResponse> responseObserver) {
		log.debug("""
				Получен gRPC запрос: findAgents
				{
					"text": {}
				}""", request.getValue());

		try {
			List<AgentProto> proto = agentService.findAgents(request.getValue());
			responseObserver.onNext(GetAgentsResponse.newBuilder()
					.addAllAgents(proto)
					.build());
			responseObserver.onCompleted();

		} catch (Exception e) {
			responseObserver.onError(
					new StatusRuntimeException(Status.fromThrowable(e))
			);
		}
	}

	public void findOrders(@NonNull StringValue request,
	                       StreamObserver<GetOrdersResponse> responseObserver) {
		log.debug("""
				Получен gRPC запрос: findOrders
				{
					"text": {}
				}""", request.getValue());

		try {
			List<OrderProto> proto = orderService.findOrders(request.getValue());
			responseObserver.onNext(GetOrdersResponse.newBuilder()
					.addAllOrders(proto)
					.build());
			responseObserver.onCompleted();

		} catch (Exception e) {
			responseObserver.onError(
					new StatusRuntimeException(Status.fromThrowable(e))
			);
		}
	}

}
