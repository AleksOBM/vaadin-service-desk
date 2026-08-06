package com.example.application.backend.service;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.SessionInitEvent;
import com.vaadin.flow.server.SessionInitListener;
import com.vaadin.flow.server.VaadinServiceInitListener;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Getter
@Component
public class SessionInitService implements VaadinServiceInitListener, SessionInitListener {

	private static final Logger log = LoggerFactory.getLogger(SessionInitService.class);

	private String sessionId;

	@Override
	public void serviceInit(@NonNull ServiceInitEvent event) {
		event.getSource().addSessionInitListener(this);
	}

	@Override
	public void sessionInit(@NonNull SessionInitEvent event) {
		sessionId = event.getSession().getSession().getId();
		log.info("Новая сессия: {}, ID={}", LocalDateTime.now(), sessionId);
	}

}
