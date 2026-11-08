package com.example.vsd.manager.repository;

import com.example.vsd.manager.enity.MailConfig;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MailConfigRepository extends JpaRepository<@NonNull MailConfig, @NonNull Long> {

    Optional<MailConfig> findFirstByOrderByIdAsc();
}
