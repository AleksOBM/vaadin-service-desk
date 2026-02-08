package com.example.application.backend.repository;

import com.example.application.backend.model.Client;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ClientRepository extends
        JpaRepository<@NonNull Client, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull Client> {
    Slice<@NonNull Client> findAllBy(Pageable pageable);
}
