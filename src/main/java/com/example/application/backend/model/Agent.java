package com.example.application.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.util.LinkedList;
import java.util.List;

@Data
@Entity
@Table(name = "agents")
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Agent extends BaseEntity {

    public Agent() {
       super(EntityType.AGENT);
    }

    @NotBlank(message = "Имя обязательно")
    @Column(name = "agent_name", length = 50, nullable = false, unique = true)
    String name;

    @OneToMany(mappedBy = "agent")
    List<Order> orders = new LinkedList<>();

    public String getServiceDeskNumber() {
        return String.format("AG-%03d", id);
    }
}


