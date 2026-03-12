package com.example.application.backend.model;

import com.example.application.backend.util.enums.EntityType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import java.util.LinkedList;
import java.util.List;

@Data
@Entity
@Table(name = "clients")
@EqualsAndHashCode(callSuper = true)
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
    public String getServiceDeskNumber() {
        return String.format("CL-%05d", id);
    }

    @Override
    public void setName(String name) {
        this.name = name;
        super.setName(name);
    }
}

