package com.example.application.backend.model;

import jakarta.persistence.*;
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
