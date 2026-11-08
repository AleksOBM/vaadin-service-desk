package com.example.vsd.frontend.render.view;

import com.example.vsd.frontend.grpc.client.MailService;
import com.example.vsd.frontend.render.notification.NotificationSupport;
import com.example.vsd.frontend.render.template.BaseView;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.RouteScope;
import com.vaadin.flow.spring.annotation.SpringComponent;
import io.grpc.StatusRuntimeException;
import jakarta.annotation.security.RolesAllowed;

@RouteScope
@SpringComponent
@Route("settings")
@PageTitle("Settings")
@RolesAllowed("ROLE_ADMIN")
@Menu(order = 6, icon = "vaadin:tools", title = "Настройки")
public class SettingsView extends BaseView {

	private final MailService mailService;

	private final EmailField senderEmail = new EmailField("Базовый почтовый ящик");
	private final EmailField notificationRecipient = new EmailField("Целевой почтовый ящик");
	private final PasswordField appPassword = new PasswordField("Пароль приложения");

	@SuppressWarnings("unused")
	public SettingsView(MailService mailService) {
		super("Настройки почты");
		this.mailService = mailService;

		filterText.setVisible(false);
		createButton.setVisible(false);
		deleteButton.setVisible(false);
		updateButton.setTooltipText("Сохранить настройки почты");
		updateButton.setIcon(new Icon(VaadinIcon.CHECK));
		updateButton.setEnabled(true);
		updateButton.addClickListener(event -> saveConfiguration());

		configureFields();
		add(createLayout());
		loadConfiguration();
	}

	private void configureFields() {
		senderEmail.setRequiredIndicatorVisible(true);
		senderEmail.setErrorMessage("Укажите корректный адрес почты");
		senderEmail.setWidthFull();
		senderEmail.setMaxWidth(25, Unit.EM);

		notificationRecipient.setRequiredIndicatorVisible(true);
		notificationRecipient.setErrorMessage("Укажите корректный адрес получателя уведомлений");
		notificationRecipient.setWidthFull();
		notificationRecipient.setMaxWidth(25, Unit.EM);

		appPassword.setRequiredIndicatorVisible(true);
		appPassword.setPlaceholder("Введите пароль приложения");
		appPassword.setWidthFull();
		appPassword.setMaxWidth(25, Unit.EM);

	}

	private VerticalLayout createLayout() {
		VerticalLayout layout = new VerticalLayout(
				senderEmail,
				notificationRecipient,
				appPassword
		);
		layout.setPadding(true);
		layout.setSpacing(true);
		layout.setWidthFull();
		return layout;
	}

	private void saveConfiguration() {
		if (senderEmail.isEmpty() || senderEmail.isInvalid()) {
			senderEmail.setInvalid(true);
			NotificationSupport.showError("Проверьте адрес почтового ящика");
			return;
		}
		if (notificationRecipient.isEmpty() || notificationRecipient.isInvalid()) {
			notificationRecipient.setInvalid(true);
			NotificationSupport.showError("Проверьте адрес получателя уведомлений");
			return;
		}
		if (appPassword.isEmpty()) {
			NotificationSupport.showError("Введите пароль приложения");
			return;
		}

		try {
			if (mailService.updateConfig(senderEmail.getValue().trim(), appPassword.getValue(),
					notificationRecipient.getValue().trim())) {
				appPassword.clear();
				NotificationSupport.showSuccess("Настройки почты сохранены");
			} else {
				NotificationSupport.showError("Не удалось сохранить настройки почты");
			}
		} catch (StatusRuntimeException e) {
			NotificationSupport.showError("Не удалось сохранить настройки почты");
		}
	}

	private void loadConfiguration() {
		try {
			notificationRecipient.setValue(mailService.getNotificationRecipient());
		} catch (StatusRuntimeException e) {
			NotificationSupport.showError("Не удалось загрузить настройки почты");
		}
	}

}
