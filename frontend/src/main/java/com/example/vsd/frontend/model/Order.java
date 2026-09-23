package com.example.vsd.frontend.model;

import com.example.vsd.serialization.model.OrderStatus;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record Order(
		Long id,
		String name,
		String description,
		LocalDate startLine,
		LocalDate deadLine,
		LocalDateTime completedDate,
		Client client,
		Agent agent,
		OrderStatus status,
		String serviceDeskNumber
) {
}