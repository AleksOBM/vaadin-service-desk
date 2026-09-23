package com.example.vsd.frontend.model;

import lombok.Builder;

@Builder(toBuilder = true)
public record Agent(
		Long id,
		String name,
		String serviceDeskNumber
) {
}
