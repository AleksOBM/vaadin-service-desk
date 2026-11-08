package com.example.vsd.manager.grpc.user;

import com.example.vsd.grpc.messages.*;
import com.example.vsd.grpc.services.user.UserControllerGrpc.UserControllerImplBase;
import com.example.vsd.manager.security.AgentOrAdminOnly;
import com.example.vsd.manager.service.agent.AgentService;
import com.example.vsd.manager.service.client.ClientService;
import com.example.vsd.manager.service.mail.MailConfigService;
import com.example.vsd.manager.service.mail.MailSenderService;
import com.example.vsd.manager.service.order.OrderService;
import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Slf4j
@GrpcService
@AgentOrAdminOnly
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserGrpcService extends UserControllerImplBase {

	OrderService orderService;
	AgentService agentService;
	ClientService clientService;
	MailConfigService mailConfigService;
	MailSenderService mailSenderService;

	@Override
	public void saveOrder(@NonNull OrderProto request,
	                      StreamObserver<Empty> responseObserver) {

		writeSaveOrderLog(request);

		try {
			boolean newOrder = !request.hasId();
			String creator = newOrder ? getCreatorName() : null;
			OrderProto savedOrder = orderService.saveOrder(request);
			if (newOrder) {
				sendOrderCreatedNotification(savedOrder, creator);
			}
			responseObserver.onNext(Empty.getDefaultInstance());
			responseObserver.onCompleted();

		} catch (Exception e) {
			log.debug(e.getMessage(), e);
			responseObserver.onError(
					new StatusRuntimeException(Status.fromThrowable(e))
			);
		}
	}

	private String getCreatorName() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new IllegalStateException("Authenticated user is required to create an order");
		}
		return authentication.getName();
	}

	private void sendOrderCreatedNotification(OrderProto order, String creator) {
		String notificationRecipient;
		try {
			notificationRecipient = mailConfigService.get().notificationRecipient();
		} catch (IllegalStateException e) {
			log.warn("Mail config not initialized, skipping email for request {}", order.getId());
			return;
		}

		try {
			if (notificationRecipient == null || notificationRecipient.isBlank()) {
				log.warn("notificationRecipient not configured, skipping email for request {}",
						order.getId());
				return;
			}
			String body = """
					Создана новая заявка.
					Создатель: %s
					Работа: %s
					Описание: %s
					Клиент: %s
					Ответственный: %s
					Дата начала: %s
					Дедлайн: %s
					""".formatted(
					creator,
					order.getName(),
					order.getDescription(),
					order.hasClient() ? order.getClient().getName() : "",
					order.hasAgent() ? order.getAgent().getName() : "",
					order.hasStartLine() ? order.getStartLine() : "",
					order.hasDeadLine() ? order.getDeadLine() : ""
			);
			mailSenderService.send(notificationRecipient,
					order.getServiceDeskNumber(), body);
		} catch (Exception e) {
			log.warn("Failed to send order-created email for request {}", order.getId(), e);
		}
	}

	@Override
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
			orderService.setOrderDeleted(request.getValue());
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
			log.debug(e.getMessage(), e);
			responseObserver.onError(
					new StatusRuntimeException(Status.fromThrowable(e))
			);
		}
	}

	@Override
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
			log.debug(e.getMessage(), e);
			responseObserver.onError(
					new StatusRuntimeException(Status.fromThrowable(e))
			);
		}
	}

	@Override
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
			log.debug(e.getMessage(), e);
			responseObserver.onError(
					new StatusRuntimeException(Status.fromThrowable(e))
			);
		}
	}

	@Override
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
			log.debug(e.getMessage(), e);
			responseObserver.onError(
					new StatusRuntimeException(Status.fromThrowable(e))
			);
		}
	}

	@Override
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
			log.debug(e.getMessage(), e);
			responseObserver.onError(
					new StatusRuntimeException(Status.fromThrowable(e))
			);
		}
	}

	@Override
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
			log.debug(e.getMessage(), e);
			responseObserver.onError(
					new StatusRuntimeException(Status.fromThrowable(e))
			);
		}
	}

	private void writeSaveOrderLog(@NonNull OrderProto request) {
		log.debug("""
						Получен gRPC запрос: saveOrder
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
	}

}
