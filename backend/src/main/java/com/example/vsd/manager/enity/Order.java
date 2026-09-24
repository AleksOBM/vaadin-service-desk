package com.example.vsd.manager.enity;

import com.example.vsd.manager.model.TypedEntity;
import com.example.vsd.serialization.model.EntityType;
import com.example.vsd.serialization.model.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@SuperBuilder
@Table(name = "orders")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity implements TypedEntity {

	@Transient
	@Builder.Default
	EntityType type = EntityType.ORDER;

	@Column(name = "title", nullable = false, length = 50)
	String name;

	@Column(length = 200)
	String description;

	@Column(name = "start_line", nullable = false)
	LocalDate startLine;

	@Column(name = "dead_line", nullable = false)
	LocalDate deadLine;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(name = "order_status")
	OrderStatus status = OrderStatus.NEW;

	@Builder.Default
	@Column(name = "completed_date")
	LocalDateTime completedDate = null;

	@ManyToOne
	@JoinColumn(name = "client_id", nullable = false)
	@JsonIgnoreProperties({"orders"})
	Client client;

	@ManyToOne
	@JoinColumn(name = "agent_id", nullable = false)
	@JsonIgnoreProperties({"orders"})
	Agent agent;

	public void setCompleted(boolean status) {
		if (status) {
			setCompletedDate(LocalDateTime.now());
			setStatus(OrderStatus.COMPLETED);
		} else {
			setCompletedDate(null);
		}
	}
}

