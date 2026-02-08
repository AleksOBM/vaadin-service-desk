package com.example.application.backend.repository;

import com.example.application.backend.model.Order;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderRepository extends
        JpaRepository<@NonNull Order, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull Order> {
    Slice<@NonNull Order> findAllBy(Pageable pageable);
}
