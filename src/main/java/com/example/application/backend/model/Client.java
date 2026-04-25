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
@Table(name = "clients")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Client extends BaseEntity {

    public Client() {
        super(EntityType.CLIENT);
    }

    @NotBlank(message = "Название обязательно")
    @Column(name = "client_name", length = 70, nullable = false, unique = true)
    String name;

    @OneToMany(mappedBy = "client")
    List<Order> orders = new LinkedList<>();

    @Override
    public void setName(String name) {
        this.name = name;
        super.setName(name);
    }
}

