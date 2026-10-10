package com.example.vsd.frontend.model;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order extends BaseEntity {

	String description;

	@NotNull(message = "Укажите дату начала")
	LocalDate startLine;

	@NotNull(message = "Укажите дедлайн")
	LocalDate deadLine;

	LocalDate firstControlLine;

	LocalDate seondControlLine;

	Integer daysCount;

	LocalDateTime completedDate;

	@NotNull(message = "Выберите клиента")
	Client client;

	@NotNull(message = "Выберите сотрудника")
	Agent agent;

	OrderStatus status;

	public boolean isCompleted() {
		return status == OrderStatus.COMPLETED;
	}

	public void setCompleted(boolean completed) {
		if (completed) {
			setCompletedDate(LocalDateTime.now());
			setStatus(OrderStatus.COMPLETED);
		} else {
			setCompletedDate(null);
		}
	}
}
