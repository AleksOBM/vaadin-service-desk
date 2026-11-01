package com.example.vsd.frontend;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.ColorScheme;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.aura.Aura;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@StyleSheet(Aura.STYLESHEET)
@StyleSheet(Lumo.UTILITY_STYLESHEET)
@ColorScheme(ColorScheme.Value.SYSTEM)
@StyleSheet("styles.css")
@PWA(
		name = "Service desk",
		shortName = "SD",
		offlinePath = "offline-stub.html",
		offlineResources = {"images/offline.png"}
)
public class ServiceDeskFrontend implements AppShellConfigurator {
	static void main(String[] args) {
		SpringApplication.run(ServiceDeskFrontend.class, args);
	}
}
