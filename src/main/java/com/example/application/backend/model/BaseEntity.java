package com.example.application.backend.model;

import com.example.application.backend.util.enums.EntityType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor(force = true)
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PROTECTED)
public class BaseEntity {

    public BaseEntity(EntityType type) {
        this.type = type;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Transient
    String name;

    @CreationTimestamp
    @Column(name = "creation_date", nullable = false, updatable = false)
    LocalDateTime creationDate;

    @UpdateTimestamp
    @Column(name = "last_updated", nullable = false)
    LocalDateTime lastUpdated;

    boolean deleted;

    String serviceDeskNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type")
    final EntityType type;
}
