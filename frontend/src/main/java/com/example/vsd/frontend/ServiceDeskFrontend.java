package com.example.vsd.frontend;

import com.example.vsd.grpc.services.admin.AdminAgentControllerGrpc.AdminAgentControllerBlockingStub;
import com.example.vsd.grpc.services.admin.AdminClientControllerGrpc.AdminClientControllerBlockingStub;
import com.example.vsd.grpc.services.admin.AdminOrderControllerGrpc.AdminOrderControllerBlockingStub;
import com.example.vsd.grpc.services.admin.AdminRecycleControllerGrpc.AdminRecycleControllerBlockingStub;
import com.example.vsd.grpc.services.user.UserControllerGrpc.UserControllerBlockingStub;
import com.example.vsd.grpc.services.free.FreeControllerGrpc.FreeControllerBlockingStub;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.ColorScheme;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.aura.Aura;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.grpc.client.ImportGrpcClients;

@SpringBootApplication
@StyleSheet(Aura.STYLESHEET)
@StyleSheet(Lumo.UTILITY_STYLESHEET)
@ColorScheme(ColorScheme.Value.SYSTEM)
@StyleSheet("styles.css")
@PWA(
		name = "Service desk",
		shortName = "SD",
		offlinePath = "offline.html",
		offlineResources = {"images/offline.png"}
)
@ImportGrpcClients(
		target = "backend-service",
		types = {
		AdminAgentControllerBlockingStub.class,
		AdminClientControllerBlockingStub.class,
		AdminOrderControllerBlockingStub.class,
		AdminRecycleControllerBlockingStub.class,
		UserControllerBlockingStub.class,
		FreeControllerBlockingStub.class
})
public class ServiceDeskFrontend implements AppShellConfigurator {
	static void main(String[] args) {
		SpringApplication.run(ServiceDeskFrontend.class, args);
	}
}
