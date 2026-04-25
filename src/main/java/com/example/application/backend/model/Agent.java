package com.example.application.backend.model;

import com.example.application.backend.util.enums.EntityType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "agents")
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

    @Override
    public void setName(String name) {
        this.name = name;
        super.setName(name);
    }
}
