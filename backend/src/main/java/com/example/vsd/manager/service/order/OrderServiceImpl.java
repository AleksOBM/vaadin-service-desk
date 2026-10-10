package com.example.vsd.manager.service.order;

import com.example.vsd.grpc.messages.OrderProto;
import com.example.vsd.grpc.messages.OrderStatusProto;
import com.example.vsd.manager.enity.Agent;
import com.example.vsd.manager.enity.Client;
import com.example.vsd.manager.enity.Order;
import com.example.vsd.manager.exception.NotFoundException;
import com.example.vsd.manager.exception.ValidationException;
import com.example.vsd.manager.mapper.OrderMapper;
import com.example.vsd.manager.model.OrderGetData;
import com.example.vsd.manager.model.OrderUpdateData;
import com.example.vsd.manager.repository.AgentRepository;
import com.example.vsd.manager.repository.ClientRepository;
import com.example.vsd.manager.repository.OrderRepository;
import com.example.vsd.serialization.timestamp.TimestampUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

import static com.example.vsd.grpc.messages.OrderStatusProto.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final AgentRepository agentRepository;
	private final ClientRepository clientRepository;

	@Override
	public void saveOrder(@NonNull OrderProto proto) {
		if (proto.hasId()) {
			var response = updateOrder(proto);
			printOrderResponseLog(response);
			return;
		}
		var response = createOrder(proto);
		printOrderResponseLog(response);
	}

	@Override
	public List<OrderProto> findOrders(String text) {
		return orderRepository.findOrders(text).stream()
				.map(order -> OrderMapper.toProto(order,
						OrderGetData.builder()
								.status(getStatus(order))
								.firstControlLine(getFirstControlLine(order))
								.secondControlLine(getSecondControlLine(order))
								.daysCount(getDaysCount(order))
								.build())
				)
				.toList();
	}

	@Override
	public List<OrderProto> getAllOrders() {
		return orderRepository.findAllByDeletedFalse().stream()
				.map(order -> OrderMapper.toProto(
						order,
						OrderGetData.builder()
								.status(getStatus(order))
								.firstControlLine(getFirstControlLine(order))
								.secondControlLine(getSecondControlLine(order))
								.daysCount(getDaysCount(order))
								.build())
				)
				.toList();
	}

	private OrderProto createOrder(@NonNull OrderProto proto) {
		Order order = OrderMapper.toEntity(proto);

		if (!order.getStartLine().isBefore(order.getDeadLine())) {
			throw new IllegalArgumentException("Дата начала должна быть раньше даты окончания");
		}

		var agentId = order.getAgent().getId();
		if (agentId == null) {
			throw new ValidationException("agent.id должен быть указан.");
		}
		Agent agent = agentRepository.findById(agentId).orElseThrow(() ->
				new IllegalStateException("Сотрудник с id=%s не найден.".formatted(agentId))
		);

		if (agent.isDeleted()) {
			throw new IllegalStateException(
					"Сотрудник с id=%s удален. Проверьте корзину.".formatted(agentId));
		}

		var clientId = order.getClient().getId();
		if (clientId == null) {
			throw new ValidationException("client.id должен быть указан.");
		}
		Client client = clientRepository.findById(clientId).orElseThrow(() ->
				new IllegalStateException("Клиент с id=%s не найден.".formatted(clientId))
		);
		if (client.isDeleted()) {
			throw new IllegalStateException(
					"Клиент с id=%s удален. Проверьте корзину.".formatted(clientId));
		}

		order.setClient(client);
		order.setAgent(agent);

		if (proto.hasStatus() && proto.getStatus().equals(COMPLETED)) {
			order.setCompletedDate(TimestampUtils.toLocalDateTime(proto.getCompletedDate()));
		}

		return OrderMapper.toProto(
				orderRepository.save(order),
				OrderGetData.builder()
						.status(getStatus(order))
						.firstControlLine(getFirstControlLine(order))
						.secondControlLine(getSecondControlLine(order))
						.daysCount(getDaysCount(order))
						.build()
		);
	}

	private OrderProto updateOrder(@NonNull OrderProto proto) {
		Long orderId = proto.getId();

		Order oldOrder = orderRepository.findById(orderId).orElseThrow(() ->
				new NotFoundException("Заказ с id=%s не найден".formatted(orderId)));
		if (oldOrder.isDeleted()) {
			throw new IllegalStateException(
					"Заказ с id=%s удален. Проверьте корзину.".formatted(orderId));
		}

		LocalDate startLine = proto.hasStartLine()
				? TimestampUtils.toLocalDate(proto.getStartLine()) : oldOrder.getStartLine();
		LocalDate deadLine = proto.hasDeadLine()
				? TimestampUtils.toLocalDate(proto.getDeadLine()) : oldOrder.getDeadLine();

		if (!startLine.isBefore(deadLine)) {
			throw new IllegalArgumentException("Дата начала должна быть раньше даты окончания");
		}

		var agentId = proto.hasAgent() ? proto.getAgent().getId() : null;
		Agent agent = agentId == null ? null : agentRepository.findById(agentId).orElseThrow(() ->
				new IllegalStateException("Сотрудник с id=%s не найден.".formatted(agentId)));

		var clientId = proto.hasClient() ? proto.getClient().getId() : null;
		Client client = clientId == null ? null : clientRepository.findById(clientId).orElseThrow(
				() -> new IllegalStateException("Клиент с id=%s не найден.".formatted(clientId)));

		LocalDateTime completedDate;
		if (proto.hasCompletedDate()) {
			completedDate = TimestampUtils.toLocalDateTime(proto.getCompletedDate());
		} else {
			if (proto.hasStatus() && proto.getStatus() == OrderStatusProto.COMPLETED) {
				completedDate = null;
			} else {
				completedDate = oldOrder.getCompletedDate();
			}
		}

		var orderData = OrderUpdateData.builder()
				.name(proto.getName())
				.description(proto.getDescription())
				.startLine(startLine)
				.deadLine(deadLine)
				.completedDate(completedDate)
				.client(client)
				.agent(agent)
				.build();

		var updatedOrder = orderRepository.save(OrderMapper.toUpdatedOrder(oldOrder, orderData));

		return OrderMapper.toProto(
				updatedOrder,
				OrderGetData.builder()
						.status(getStatus(updatedOrder))
						.firstControlLine(getFirstControlLine(updatedOrder))
						.secondControlLine(getSecondControlLine(updatedOrder))
						.daysCount(getDaysCount(updatedOrder))
						.build()
		);
	}

	@Override
	public void setOrderDeleted(Long orderId) {
		Order order = orderRepository.findById(orderId).orElseThrow(() ->
				new NotFoundException("Заказ с id=%s не найден".formatted(orderId)));
		if (order.isDeleted()) {
			throw new IllegalStateException(
					"Заказ с id=%s удален. Проверьте корзину.".formatted(orderId));
		}
		order.setDeleted(true);
		orderRepository.save(order);
	}

	@Override
	public void restoreOrder(Long orderId) {
		Order order = orderRepository.findById(orderId).orElseThrow(() ->
				new NotFoundException("Заказ с id=%s не найден".formatted(orderId)));
		order.setDeleted(false);
		orderRepository.save(order);
	}

	@Override
	public void deleteOrder(Long orderId) {
		orderRepository.deleteById(orderId);
	}

	private OrderStatusProto getStatus(@NonNull Order order) {
		LocalDate date = LocalDate.now();
		LocalDate firstControl = getFirstControlLine(order);
		LocalDate secondControl = getSecondControlLine(order);

		// Определяем статус (порядок важен)
		if (order.getCompletedDate() != null) {
			return COMPLETED;
		} else if (order.getStartLine().isAfter(date)) {
			return NEW;
		} else if (firstControl.isAfter(date)) {
			return IN_PROGRESS;
		} else if ((firstControl.isEqual(date) || firstControl.isBefore(date))
				&& secondControl.isAfter(date)) {
			return FIRST_CONTROL;
		} else if (secondControl.isEqual(date) || secondControl.isBefore(date)) {
			return SECOND_CONTROL;
		} else {
			throw new IllegalStateException("что-то напутано с датами.");
		}
	}

	@NonNull
	private LocalDate getFirstControlLine(@NonNull Order order) {
		int controlDaysCount = Math.round((float) getDaysCount(order) / 3);
		return order.getStartLine().plus(Period.ofDays(controlDaysCount));
	}

	@NonNull
	private LocalDate getSecondControlLine(@NonNull Order order) {
		int controlDaysCount = Math.round((float) getDaysCount(order) / 3);
		return order.getDeadLine().minus(Period.ofDays(controlDaysCount));
	}

	private int getDaysCount(@NonNull Order order) {
		return Period.between(order.getStartLine(), order.getDeadLine()).getDays();
	}

	private void printOrderResponseLog(@NonNull OrderProto request) {
		log.debug("""
						OrderResponse
						{
							"id": {}
							"name": "{}",
							"description": "{}",
							"startLine": "{}"
							"deadLine": "{}"
							"firstControlLine": "{}",
							"secondControlLine": "{}",
							"daysCount": "{}",
							"completedDate": "{}"
							"status": "{}",
							"client": {
								"id": {},
								"name": {}
							},
							"agent": {
								"id": {},
								"name": {}
							},
							"serviceDeskNumber": "{}",
							"creationDate": "{}",
							"lastUpdated": "{}"
							"deleted": {}
						}""",
				request.getId(),
				request.getName(),
				request.getDescription(),
				request.hasStartLine() ? TimestampUtils.toLocalDate(request.getStartLine()) : null,
				request.hasDeadLine() ? TimestampUtils.toLocalDate(request.getDeadLine()) : null,
				request.hasFirstControlLine() ? TimestampUtils.toLocalDate(request.getFirstControlLine()) : null,
				request.hasSecondControlLine() ? TimestampUtils.toLocalDate(request.getSecondControlLine()) : null,
				request.getDaysCount(),
				request.hasCompletedDate() ? TimestampUtils.toLocalDateTime(request.getCompletedDate()) : null,
				request.getStatus(),
				request.getClient().getId(),
				request.getClient().getName(),
				request.getAgent().getId(),
				request.getAgent().getName(),
				request.getServiceDeskNumber(),
				request.hasCreationDate() ? TimestampUtils.toLocalDate(request.getCreationDate()) : null,
				request.hasLastUpdated() ? TimestampUtils.toLocalDate(request.getLastUpdated()) : null,
				request.getDeleted()
		);
	}

}
