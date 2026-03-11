package com.example.application.backend.service;

import com.vaadin.flow.server.*;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SessionInitService implements VaadinServiceInitListener, SessionInitListener {

    private static final Logger log = LoggerFactory.getLogger(SessionInitService.class);
    private String sessionId;

    @Override
    public void serviceInit(ServiceInitEvent event) {
       event.getSource().addSessionInitListener(this);
    }

    @Override
    public void sessionInit(SessionInitEvent event) {
        sessionId = event.getSession().getSession().getId();
        log.info("Новая сессия: {}, ID={}", LocalDateTime.now(), sessionId);
    }

    public String getSessionId() {
        return sessionId;
    }
}
