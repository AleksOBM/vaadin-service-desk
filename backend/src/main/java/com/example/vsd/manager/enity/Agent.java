package com.example.vsd.manager.enity;

import com.example.vsd.manager.model.TypedEntity;
import com.example.vsd.serialization.model.EntityType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@Entity
@SuperBuilder
@Table(name = "agents")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Agent extends BaseEntity implements TypedEntity {

    @Transient
    @Builder.Default
    EntityType type = EntityType.AGENT;

    @NotBlank(message = "Имя обязательно")
    @Column(name = "agent_name", length = 50, nullable = false, unique = true)
    String name;

    @Builder.Default
    @OneToMany(mappedBy = "agent")
    List<Order> orders = new LinkedList<>();

}
