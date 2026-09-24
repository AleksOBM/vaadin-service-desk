package com.example.vsd.manager.service.recycle;

import com.example.vsd.manager.enity.BaseEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Transactional
public interface RecycleService {

	@Transactional(readOnly = true)
	Collection<BaseEntity> getRecycleData(String text);

	void markAsDeleted(@NonNull BaseEntity entity);

	void restore(@NonNull BaseEntity entity);

	void deleteForever(@NonNull BaseEntity entity);

}
