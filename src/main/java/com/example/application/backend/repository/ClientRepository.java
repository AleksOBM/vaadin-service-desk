package com.example.application.backend.repository;

import com.example.application.backend.model.Client;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends
        JpaRepository<@NonNull Client, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull Client> {

    Slice<@NonNull Client> findAllBy(Pageable pageable);

    @Query("""
            select c from Client c
            where c.deleted = false
            and(
            lower(c.serviceDeskNumber) like lower(concat('%', :text, '%')) or
            lower(c.name) like lower(concat('%', :text, '%')))
            """)
    List<Client> getContent(String text);

    List<Client> findAllByDeletedTrue();

    List<Client> findAllByDeletedFalse();
}
