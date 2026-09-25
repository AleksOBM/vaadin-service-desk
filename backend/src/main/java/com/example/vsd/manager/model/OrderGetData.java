package com.example.vsd.manager.model;

import com.example.vsd.grpc.messages.OrderStatusProto;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record OrderGetData(
		OrderStatusProto status,
		LocalDate firstControlLine,
		LocalDate secondControlLine,
		int daysCount
) {
}
