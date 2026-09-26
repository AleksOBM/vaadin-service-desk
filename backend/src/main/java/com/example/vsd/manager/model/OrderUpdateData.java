package com.example.vsd.manager.model;

import com.example.vsd.manager.enity.Agent;
import com.example.vsd.manager.enity.Client;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record OrderUpdateData(
		String name,
		String description,
		LocalDate startLine,
		LocalDate deadLine,
		LocalDateTime completedDate,
		Client client,
		Agent agent
) {
	public boolean hasName() {
		return name != null && !name.isEmpty();
	}

	public boolean hasDescription() {
		return description != null && !description.isEmpty();
	}

	public boolean hasStartLine() {
		return startLine != null;
	}

	public boolean hasDeadLine() {
		return deadLine != null;
	}

	public boolean hasCompletedDate() {
		return completedDate != null;
	}

	public boolean hasClient() {
		return client != null;
	}

	public boolean hasAgent() {
		return agent != null;
	}
}
