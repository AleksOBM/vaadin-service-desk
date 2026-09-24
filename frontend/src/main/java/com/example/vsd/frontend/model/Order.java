package com.example.vsd.frontend.model;

import com.example.vsd.serialization.model.OrderStatus;
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
	LocalDate startLine;
	LocalDate deadLine;
	LocalDateTime completedDate;
	Client client;
	Agent agent;
	OrderStatus status;
}
