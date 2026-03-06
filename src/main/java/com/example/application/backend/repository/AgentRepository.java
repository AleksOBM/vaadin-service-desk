package com.example.application.backend.repository;

import com.example.application.backend.model.Agent;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentRepository extends
        JpaRepository<@NonNull Agent, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull Agent> {
    Slice<@NonNull Agent> findAllBy(Pageable pageable);
}
