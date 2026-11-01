package com.example.vsd.frontend.config;

import com.example.vsd.frontend.render.view.LoginView;
import com.example.vsd.frontend.security.JwtClientInterceptor;
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelBuilderCustomizer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityConfig {

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
		return config.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) {
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("/images/**", "/icons/**").permitAll()
		);

		http.with(VaadinSecurityConfigurer.vaadin(),
				configurer -> configurer.loginView(LoginView.class));
		return http.build();
	}

	@Bean
	public GrpcChannelBuilderCustomizer<?> jwtCustomizer(JwtClientInterceptor interceptor) {
		return GrpcChannelBuilderCustomizer.matching("backend-service",
				builder -> builder.intercept(interceptor));
	}
}
