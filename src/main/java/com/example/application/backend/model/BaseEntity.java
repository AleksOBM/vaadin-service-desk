package com.example.application.backend.model;

import com.example.application.backend.util.enums.EntityType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@FieldDefaults(level = AccessLevel.PROTECTED)
public class BaseEntity {

    public BaseEntity(EntityType type) {
        this.type = type;
    }

    @Transient
    final EntityType type;

    @Transient
    String name;

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

    @Column(insertable = false, updatable = false)
    String serviceDeskNumber;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        if (org.hibernate.Hibernate.getClass(this)
                != org.hibernate.Hibernate.getClass(o)) {
            return false;
        }

        BaseEntity other = (BaseEntity) o;

        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return org.hibernate.Hibernate.getClass(this).hashCode();
    }
}
