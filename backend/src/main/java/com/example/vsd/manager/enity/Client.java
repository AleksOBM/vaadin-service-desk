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
@Table(name = "clients")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Client extends BaseEntity implements TypedEntity {

    @Transient
    @Builder.Default
    EntityType type = EntityType.CLIENT;

    @NotBlank(message = "Название обязательно")
    @Column(name = "client_name", length = 70, nullable = false, unique = true)
    String name;

    @Builder.Default
    @OneToMany(mappedBy = "client")
    List<Order> orders = new LinkedList<>();
}

