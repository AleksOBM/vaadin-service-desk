package com.example.vsd.frontend.render.view;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.annotation.RouteScope;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

@RouteScope
@SpringComponent
@AnonymousAllowed
@PageTitle("Вход")
@Route(value = "login", autoLayout = false)
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

	private final LoginForm loginForm = new LoginForm();

	public LoginView(AuthenticationManager authenticationManager) {

		var div = new Div();
		var img = new Image();
		img.setSrc("/images/icon.ico");
		div.add(img);
		loginForm.setForgotPasswordButtonVisible(false);

		var i18n = LoginI18n.createDefault();
		i18n.getForm().setTitle("Вход в систему");
		i18n.getForm().setUsername("Логин");
		i18n.getForm().setPassword("Пароль");
		i18n.getForm().setSubmit("Войти");
		i18n.getErrorMessage().setTitle("Не удалось войти");
		i18n.getErrorMessage().setMessage("Проверьте логин и пароль и попробуйте ещё раз.");
		loginForm.setI18n(i18n);

		loginForm.addLoginListener(event -> {
			try {
				Authentication auth = authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(
								event.getUsername(), event.getPassword())
				);

				SecurityContext context = SecurityContextHolder.createEmptyContext();
				context.setAuthentication(auth);
				SecurityContextHolder.setContext(context);

				var request = VaadinServletRequest.getCurrent().getHttpServletRequest();
				// Защита от session fixation: меняем ID сессии перед сохранением контекста
				request.changeSessionId();
				request.getSession(true).setAttribute(
						HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
						context
				);

				getUI().ifPresent(ui -> ui.getPage().setLocation("/about"));
			} catch (AuthenticationException ex) {
				loginForm.setError(true);
			}
		});

		setSizeFull();
		setAlignItems(FlexComponent.Alignment.CENTER);
		setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
		add(div, loginForm);
	}

	@Override
	public void beforeEnter(@NonNull BeforeEnterEvent event) {
		// Параметр ?error при ручном входе не появляется, оставлено для совместимости
		if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
			loginForm.setError(true);
		}
	}
}
