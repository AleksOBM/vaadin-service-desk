package com.example.application.backend.repository;

import com.example.application.backend.model.Agent;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgentRepository extends
        JpaRepository<@NonNull Agent, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull Agent> {

    Slice<@NonNull Agent> findAllBy(Pageable pageable);

    @Query("""
            select a from Agent a
            where a.deleted = false
            and(
            lower(a.serviceDeskNumber) like lower(concat('%', :text, '%')) or
            lower(a.name) like lower(concat('%', :text, '%')))
            """)
    List<Agent> getContent(String text);

    List<Agent> findAllByDeletedTrue();

    List<Agent> findAllByDeletedFalse();
}
