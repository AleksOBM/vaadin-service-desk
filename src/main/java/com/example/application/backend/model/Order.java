package com.example.application.backend.model;

import com.example.application.backend.util.enums.EntityType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Data
@Entity
@Table(name = "orders")
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order extends BaseEntity {

    public Order() {
        super(EntityType.ORDER);
    }

    @Column(name = "title", nullable = false, length = 50)
    String name;

    @Column(length = 200)
    String description;

    @Column(name = "start_line", nullable = false)
    LocalDate startLine;

    @Column(name = "dead_line", nullable = false)
    LocalDate deadLine;

    @Column(name = "completed_date")
    LocalDateTime completedDate;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    @JsonIgnoreProperties({"orders"})
    Client client;

    @ManyToOne
    @JoinColumn(name = "agent_id", nullable = false)
    @JsonIgnoreProperties({"orders"})
    Agent agent;

    Boolean completed;

    @Override
    public String getServiceDeskNumber() {
        return String.format("SD-%08d", id);
    }

    @Override
    public void setName(String name) {
        this.name = name;
        super.setName(name);
    }

    // todo: Перенести этот метод в сервис
    public LocalDate getFirstControlLine() {
        int controlDaysCount = Math.round((float) getDaysCount() / 3);
        return this.getStartLine().plus(Period.ofDays(controlDaysCount));
    }

    // todo: Перенести этот метод в сервис
    public LocalDate getSecondControlLine() {
        int controlDaysCount = Math.round((float) getDaysCount() / 3);
        return this.getDeadLine().minus(Period.ofDays(controlDaysCount));
    }

    // todo: Перенести этот метод в сервис
    public int getDaysCount() {
        return Period.between(this.getStartLine(), this.getDeadLine()).getDays();
    }

    public void setCompleted(boolean status) {
        if (status) {
            setCompletedDate(LocalDateTime.now());
        } else {
            setCompletedDate(null);
        }
    }

    public boolean isCompleted() {
        return completedDate != null;
    }
}

