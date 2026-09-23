package com.example.vsd.frontend.model;

import com.example.vsd.serialization.model.EntityType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record BaseEntity(
		Long id,
		EntityType type,
		String name,
		String serviceDeskNumber,
		LocalDateTime creationDate,
		LocalDateTime lastUpdated,
		boolean deleted
) {
}
