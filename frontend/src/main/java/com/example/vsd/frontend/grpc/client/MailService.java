package com.example.vsd.frontend.grpc.client;

import com.example.vsd.frontend.model.MailEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MailService {

	public Optional<MailEntity> getMailEntity() {
		return Optional.of(MailEntity.builder().build());
	}

	public void saveMailEntity(MailEntity mailEntity) {
		// TODO: 23.09.2026
	}
}
