package com.example.vsd.manager.model;

import com.example.vsd.serialization.model.EntityType;

public interface TypedEntity {

	String getName();

	EntityType getType();

	void setName(String name);

}
