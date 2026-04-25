package com.example.application.backend.repository;

import com.example.application.backend.model.MailEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailRepository extends JpaRepository<@NonNull MailEntity, @NonNull Long> {
}
