package com.example.vsd.manager.service.order;

import com.example.vsd.grpc.messages.OrderProto;
import com.example.vsd.manager.enity.Agent;
import com.example.vsd.manager.enity.Client;
import com.example.vsd.manager.enity.Order;
import com.example.vsd.manager.exception.NotFoundException;
import com.example.vsd.manager.exception.ValidationException;
import com.example.vsd.manager.mapper.OrderMapper;
import com.example.vsd.manager.model.OrderData;
import com.example.vsd.manager.repository.AgentRepository;
import com.example.vsd.manager.repository.ClientRepository;
import com.example.vsd.manager.repository.OrderRepository;
import com.example.vsd.serialization.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.List;

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
			var order = updateOrder(proto);
			log.info("Saving order with id {}", order.getId());
			return;
		}
		var order = createOrder(proto);
		log.info("Saving order with id {}", order.getId());
	}

	@Override
	public List<OrderProto> findOrders(String text) {
		return orderRepository.findOrders(text).stream()
				.map(OrderMapper::toProto)
				.toList();
	}

	@Override
	public List<OrderProto> getAllOrders() {
		return orderRepository.findAllByDeletedFalse().stream()
				.sorted(Comparator.comparingLong(Order::getId))
				.map(OrderMapper::toProto)
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

		return OrderMapper.toProto(orderRepository.save(order));
	}

	private OrderProto updateOrder(@NonNull OrderProto proto) {
		Long orderId = proto.getId();

		Order oldOrder = orderRepository.findById(orderId).orElseThrow(() ->
				new NotFoundException("Заказ с id=%s не найден".formatted(orderId)));
		if (oldOrder.isDeleted()) {
			throw new IllegalStateException(
					"Заказ с id=%s удален. Проверьте корзину.".formatted(orderId));
		}

		if (oldOrder.getStatus().equals(OrderStatus.COMPLETED)) {
			throw new IllegalStateException("Нельзя изменить завершенный заказ");
		}

		var startLine = proto.hasStartLine()
				? LocalDate.parse(proto.getStartLine()) : oldOrder.getStartLine();
		var deadLine = proto.hasDeadLine()
				? LocalDate.parse(proto.getDeadLine()) : oldOrder.getDeadLine();

		if (!startLine.isBefore(deadLine)) {
			throw new IllegalArgumentException("Дата начала должна быть раньше даты окончания");
		}

		var agentId = proto.hasAgent() ? proto.getAgent().getId() : null;
		Agent agent = agentId == null ? null : agentRepository.findById(agentId).orElseThrow(() ->
				new IllegalStateException("Сотрудник с id=%s не найден.".formatted(agentId)));

		var clientId = proto.hasClient() ? proto.getClient().getId() : null;
		Client client = clientId == null ? null : clientRepository.findById(clientId).orElseThrow(
				() -> new IllegalStateException("Клиент с id=%s не найден.".formatted(clientId)));

		var orderData = OrderData.builder()
				.name(proto.getName())
				.description(proto.getDescription())
				.startLine(startLine)
				.deadLine(deadLine)
				.status(getStatus(startLine, deadLine))
				.client(client)
				.agent(agent)
				.build();

		return OrderMapper.toProto(orderRepository.save(OrderMapper.update(oldOrder, orderData)));
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

	private OrderStatus getStatus(@NonNull LocalDate startLine, @NonNull LocalDate deadLine) {
		LocalDate date = LocalDate.now();
		LocalDate firstControl = getFirstControlLine(startLine, deadLine);
		LocalDate secondControl = getSecondControlLine(startLine, deadLine);
		if (startLine.isAfter(date)) {
			return OrderStatus.NEW;
		} else if (firstControl.isAfter(date)) {
			return OrderStatus.IN_PROGRESS;
		} else if ((firstControl.isEqual(date) || firstControl.isBefore(date))
				&& secondControl.isAfter(date)) {
			return OrderStatus.FIRST_CONTROL;
		} else if (secondControl.isEqual(date) || secondControl.isBefore(date)) {
			return OrderStatus.SECOND_CONTROL;
		} else {
			throw new IllegalStateException("что-то напутано с датами.");
		}
	}

	@NonNull
	private LocalDate getFirstControlLine(@NonNull LocalDate startLine, @NonNull LocalDate deadLine) {
		int controlDaysCount = Math.round((float) getDaysCount(startLine, deadLine) / 3);
		return startLine.plus(Period.ofDays(controlDaysCount));
	}

	@NonNull
	private LocalDate getSecondControlLine(@NonNull LocalDate startLine, @NonNull LocalDate deadLine) {
		int controlDaysCount = Math.round((float) getDaysCount(startLine, deadLine) / 3);
		return deadLine.minus(Period.ofDays(controlDaysCount));
	}

	private int getDaysCount(@NonNull LocalDate startLine, @NonNull LocalDate deadLine) {
		return Period.between(startLine, deadLine).getDays();
	}

}
