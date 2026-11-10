package com.example.vsd.frontend.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderStatusTest {

	private final Order order = Order.builder()
			.startLine(LocalDate.of(2026, 1, 1))
			.firstControlLine(LocalDate.of(2026, 1, 4))
			.seondControlLine(LocalDate.of(2026, 1, 7))
			.deadLine(LocalDate.of(2026, 1, 10))
			.status(OrderStatus.NEW)
			.client(Client.builder().name("Client").deleted(false).build())
			.agent(Agent.builder().name("Agent").deleted(false).build())
			.deleted(false)
			.build();

	@Test
	void determinesLatestReachedStatusOnDate() {
		assertEquals(OrderStatus.NEW,
				OrderStatus.onDate(order, LocalDate.of(2025, 12, 31)));
		assertEquals(OrderStatus.IN_PROGRESS,
				OrderStatus.onDate(order, LocalDate.of(2026, 1, 1)));
		assertEquals(OrderStatus.FIRST_CONTROL,
				OrderStatus.onDate(order, LocalDate.of(2026, 1, 4)));
		assertEquals(OrderStatus.SECOND_CONTROL,
				OrderStatus.onDate(order, LocalDate.of(2026, 1, 7)));
		assertEquals(OrderStatus.DEADLINE,
				OrderStatus.onDate(order, LocalDate.of(2026, 1, 10)));
	}

	@Test
	void keepsCompletedOrderGrayAfterDeadline() {
		Order completedOrder = Order.builder()
				.startLine(order.getStartLine())
				.firstControlLine(order.getFirstControlLine())
				.seondControlLine(order.getSeondControlLine())
				.deadLine(order.getDeadLine())
				.completedDate(LocalDate.of(2026, 1, 11).atStartOfDay())
				.status(OrderStatus.COMPLETED)
				.client(order.getClient())
				.agent(order.getAgent())
				.deleted(false)
				.build();

		assertEquals(OrderStatus.COMPLETED,
				OrderStatus.onDate(completedOrder, LocalDate.of(2026, 1, 11)));
	}
}
