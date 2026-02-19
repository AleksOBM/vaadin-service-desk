package com.example.application.backend.service;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.SessionInitEvent;
import com.vaadin.flow.server.SessionInitListener;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SessionInitService implements VaadinServiceInitListener, SessionInitListener {

    private static final Logger log = LoggerFactory.getLogger(SessionInitService.class);

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addSessionInitListener(this);
    }

    @Override
    public void sessionInit(SessionInitEvent event) {
        log.info("Новая сессия: {}, ID={}", LocalDateTime.now(), event.getSession().getSession().getId());
    }
}
