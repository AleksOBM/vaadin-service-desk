package com.example.vsd.frontend.model;

import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;

@Getter
public enum OrderStatus {
	COMPLETED("#7a7a7a"),
	NEW("#4a6fa5"),
	IN_PROGRESS("#4a8f5b"),
	FIRST_CONTROL("#b8952e"),
	SECOND_CONTROL("#a94a4a"),
	DEADLINE("#8b5cf6");

	private final String color;

	OrderStatus(String color) {
		this.color = color;
	}

	public static OrderStatus onDate(@NonNull Order order, LocalDate date) {
		if (order.isCompleted()) {
			return COMPLETED;
		}
		if (!date.isBefore(order.getDeadLine())) {
			return DEADLINE;
		}
		if (order.getSeondControlLine() != null && !date.isBefore(order.getSeondControlLine())) {
			return SECOND_CONTROL;
		}
		if (order.getFirstControlLine() != null && !date.isBefore(order.getFirstControlLine())) {
			return FIRST_CONTROL;
		}
		if (!date.isBefore(order.getStartLine())) {
			return IN_PROGRESS;
		}
		return NEW;
	}
}
