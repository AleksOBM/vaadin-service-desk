package com.example.application.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @Column(name = "client_name", length = 70, nullable = false)
    String name;

    @OneToMany(mappedBy = "client")
    List<Order> orders = new LinkedList<>();

    public String getServiceDeskNumber() {
        return String.format("CL-%05d", id);
    }
}

