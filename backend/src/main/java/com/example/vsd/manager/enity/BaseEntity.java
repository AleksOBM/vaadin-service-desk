package com.example.vsd.manager.enity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.generator.EventType;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class BaseEntity {

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

	@Generated(event = EventType.INSERT)
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
