package com.example.vsd.frontend.model;

import com.example.vsd.serialization.model.EntityType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
public class BaseEntity {
	Long id;
	String name;
	EntityType type;
	String serviceDeskNumber;
	LocalDateTime creationDate;
	LocalDateTime lastUpdated;
	boolean deleted;
}
