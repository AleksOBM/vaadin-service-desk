package com.example.application.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "orders")
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order extends BaseEntity {

    @Column(nullable = false, length = 50)
    String title;

    @Column(length = 200)
    String description;

    @Column(name = "start_line", nullable = false)
    LocalDate startLine;

    @Column(name = "dead_line", nullable = false)
    LocalDate deadLine;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    @JsonIgnoreProperties({"orders"})
    Client client;

    @ManyToOne
    @JoinColumn(name = "agent_id", nullable = false)
    @JsonIgnoreProperties({"orders"})
    Agent agent;

    public String getServiceDeskNumber() {
        return String.format("SD-%08d", id);
    }
}

