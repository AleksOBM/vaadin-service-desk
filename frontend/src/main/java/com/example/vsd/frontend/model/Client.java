package com.example.vsd.frontend.model;

import lombok.Builder;

@Builder(toBuilder = true)
public record Client(
		Long id,
		String name,
		String serviceDeskNumber
) {
}