package com.example.vsd.frontend.render.view;

import com.example.vsd.frontend.grpc.client.MailService;
import com.example.vsd.frontend.model.MailConfigData;
import com.example.vsd.frontend.render.notification.NotificationSupport;
import com.example.vsd.frontend.render.template.BaseView;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.RouteScope;
import com.vaadin.flow.spring.annotation.SpringComponent;
import io.grpc.StatusRuntimeException;
import jakarta.annotation.Nonnull;
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
	private final TextField smtpHost = new TextField("SMTP хост");
	private final IntegerField smtpPort = new IntegerField("SMTP порт");
	private final Checkbox smtpAuth = new Checkbox("Требуется авторизация");
	private final Checkbox smtpSsl = new Checkbox("Использовать SSL");
	private final Button lockButton = new Button();
	private final ConfirmDialog senderEmailDialog = new ConfirmDialog();

	private boolean passwordConfigured;
	private boolean passwordMaskDisplayed;
	private boolean configurationLocked;
	private boolean pendingEmailChanged;
	private boolean pendingPasswordChanged;
	private String originalSenderEmail = "";
	private String originalNotificationRecipient = "";
	private String originalSmtpHost = "";
	private Integer originalSmtpPort;
	private boolean originalSmtpAuth;
	private boolean originalSmtpSsl;

	public SettingsView(MailService mailService) {
		super("Настройки почты");
		this.mailService = mailService;

		filterText.setVisible(false);
		createButton.setVisible(false);
		deleteButton.setVisible(false);
		updateButton.setTooltipText("Сохранить настройки почты");
		updateButton.setIcon(new Icon(VaadinIcon.CHECK));
		updateButton.setEnabled(false);
		updateButton.addClickListener(_ -> saveConfiguration());
		lockButton.addClickListener(_ -> toggleConfigurationLock());
		configureSenderEmailDialog();

		configureFields();
		toolbar.addActionBefore(lockButton, updateButton);
		add(createLayout());
		loadConfiguration();
		senderEmail.addValueChangeListener(
				_ -> updateActionButtonsState());
		notificationRecipient.addValueChangeListener(
				_ -> updateActionButtonsState());
		appPassword.addValueChangeListener(
				_ -> updateActionButtonsState());
		smtpHost.addValueChangeListener(
				_ -> updateActionButtonsState());
		smtpPort.addValueChangeListener(
				_ -> updateActionButtonsState());
		smtpAuth.addValueChangeListener(
				_ -> updateActionButtonsState());
		smtpSsl.addValueChangeListener(
				_ -> updateActionButtonsState());
		updateActionButtonsState();
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
		appPassword.setPlaceholder("Новый пароль; пустое поле — оставить текущий");
		appPassword.setWidthFull();
		appPassword.setMaxWidth(25, Unit.EM);

		smtpHost.setRequiredIndicatorVisible(true);
		smtpHost.setErrorMessage("Укажите SMTP хост");
		smtpHost.setWidthFull();
		smtpHost.setMaxWidth(25, Unit.EM);

		smtpPort.setMin(1);
		smtpPort.setMax(65535);
		smtpPort.setStepButtonsVisible(true);
		smtpPort.setRequiredIndicatorVisible(true);
		smtpPort.setErrorMessage("Порт должен быть в диапазоне 1-65535");
		smtpPort.setWidthFull();
		smtpPort.setMaxWidth(25, Unit.EM);
		setConfigurationLocked(false);
	}

	@Nonnull
	private VerticalLayout createLayout() {
		VerticalLayout layout = new VerticalLayout(
				senderEmail,
				notificationRecipient,
				appPassword,
				smtpHost,
				smtpPort,
				smtpAuth,
				smtpSsl
		);
		layout.setPadding(true);
		layout.setSpacing(true);
		layout.setWidthFull();
		return layout;
	}

	private void saveConfiguration() {
		if (senderEmailDialog.isOpened()) {
			return;
		}
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
		if (smtpHost.isEmpty() || smtpHost.getValue().isBlank()) {
			smtpHost.setInvalid(true);
			NotificationSupport.showError("Укажите SMTP хост");
			return;
		}
		Integer port = smtpPort.getValue();
		if (port == null || port < 1 || port > 65535) {
			smtpPort.setInvalid(true);
			NotificationSupport.showError("Порт должен быть в диапазоне 1-65535");
			return;
		}
		String cleanedPassword = appPassword.getValue().replaceAll("\\s", "");
		boolean whitespaceOnlyPassword = !appPassword.isEmpty()
				&& !"********".equals(appPassword.getValue())
				&& cleanedPassword.isEmpty();
		if ((!passwordConfigured && cleanedPassword.isEmpty()) || whitespaceOnlyPassword) {
			NotificationSupport.showError("Введите пароль приложения");
			updateActionButtonsState();
			return;
		}
		boolean emailChanged = !senderEmail.getValue().trim().equals(originalSenderEmail);
		boolean passwordChanged = !appPassword.isEmpty()
				&& !"********".equals(appPassword.getValue());
		if (!isFormModified()) {
			NotificationSupport.showSuccess("Изменений нет, настройки не перезаписаны");
			return;
		}

		if (emailChanged || passwordChanged) {
			pendingEmailChanged = emailChanged;
			pendingPasswordChanged = passwordChanged;
			if (pendingEmailChanged && pendingPasswordChanged) {
				senderEmailDialog.setText("Вы меняете и почту, и пароль. Продолжить?");
			} else if (pendingEmailChanged) {
				senderEmailDialog.setText(
						"Вы меняете почтовый ящик. Текущий пароль может не подойти к новой почте. Продолжить?");
			} else {
				senderEmailDialog.setText(
						"Вы изменяете пароль приложения. Убедитесь, что он верный. Продолжить?");
			}
			senderEmailDialog.open();
			updateButton.setEnabled(false);
			lockButton.setEnabled(false);
			return;
		}

		performSave();
	}

	private void performSave() {
		try {
			Integer port = smtpPort.getValue();
			String password = appPassword.getValue().replaceAll("\\s", "");
			var mailConfigData = MailConfigData.builder()
					.email(senderEmail.getValue().trim())
					.password(password)
					.notificationRecipient(notificationRecipient.getValue().trim())
					.resetPassword(false)
					.smtpHost(smtpHost.getValue().trim())
					.smtpPort(port)
					.smtpAuth(smtpAuth.getValue())
					.smtpSsl(smtpSsl.getValue())
					.build();

			var updated = mailService.updateConfig(mailConfigData);
			if (updated) {
				passwordConfigured |= !password.isEmpty();
				appPassword.setRequiredIndicatorVisible(!passwordConfigured);
				originalSenderEmail = senderEmail.getValue().trim();
				originalNotificationRecipient = notificationRecipient.getValue().trim();
				originalSmtpHost = smtpHost.getValue().trim();
				originalSmtpPort = port;
				originalSmtpAuth = smtpAuth.getValue();
				originalSmtpSsl = smtpSsl.getValue();
				passwordMaskDisplayed = passwordConfigured;
				appPassword.setValue(passwordMaskDisplayed ? "********" : "");
				setConfigurationLocked(true);
				updateActionButtonsState();
				NotificationSupport.showSuccess("Настройки почты сохранены");
			} else {
				NotificationSupport.showError("Не удалось сохранить настройки почты");
			}
		} catch (StatusRuntimeException e) {
			NotificationSupport.showError("Не удалось сохранить настройки почты");
		}
		updateActionButtonsState();
	}

	private void loadConfiguration() {
		try {
			var config = mailService.getMailConfig();
			senderEmail.setValue(config.getEmail());
			originalSenderEmail = config.getEmail();
			notificationRecipient.setValue(config.getNotificationRecipient());
			originalNotificationRecipient = config.getNotificationRecipient();
			smtpHost.setValue(config.getSmtpHost());
			originalSmtpHost = config.getSmtpHost();
			smtpPort.setValue(config.getSmtpPort());
			originalSmtpPort = config.getSmtpPort();
			smtpAuth.setValue(config.getSmtpAuth());
			originalSmtpAuth = config.getSmtpAuth();
			smtpSsl.setValue(config.getSmtpSsl());
			originalSmtpSsl = config.getSmtpSsl();
			passwordConfigured = config.getPasswordConfigured();
			appPassword.setRequiredIndicatorVisible(!passwordConfigured);
			passwordMaskDisplayed = passwordConfigured;
			appPassword.setValue(passwordMaskDisplayed ? "********" : "");
			setConfigurationLocked(passwordConfigured);
		} catch (StatusRuntimeException e) {
			NotificationSupport.showError("Не удалось загрузить настройки почты");
		}
	}

	private boolean isFormModified() {
		boolean passwordModified = !appPassword.isEmpty()
				&& !"********".equals(appPassword.getValue());
		return !senderEmail.getValue().equals(originalSenderEmail)
				|| !notificationRecipient.getValue().equals(originalNotificationRecipient)
				|| !smtpHost.getValue().equals(originalSmtpHost)
				|| !java.util.Objects.equals(smtpPort.getValue(), originalSmtpPort)
				|| smtpAuth.getValue() != originalSmtpAuth
				|| smtpSsl.getValue() != originalSmtpSsl
				|| passwordModified;
	}

	private void setConfigurationLocked(boolean locked) {
		if (locked) {
			lockButton.focus();
		}
		configurationLocked = locked;
		senderEmail.setEnabled(!locked);
		notificationRecipient.setEnabled(!locked);
		appPassword.setEnabled(!locked);
		smtpHost.setEnabled(!locked);
		smtpPort.setEnabled(!locked);
		smtpAuth.setEnabled(!locked);
		smtpSsl.setEnabled(!locked);
		lockButton.setIcon(new Icon(locked ? VaadinIcon.UNLOCK : VaadinIcon.LOCK));
		lockButton.setTooltipText(locked ? "Разблокировать" : "Заблокировать");
		updateActionButtonsState();
	}

	private void updateActionButtonsState() {
		if (senderEmailDialog.isOpened()) {
			return;
		}
		if (configurationLocked) {
			lockButton.setEnabled(true);
			updateButton.setEnabled(false);
			return;
		}
		boolean modified = isFormModified();
		lockButton.setEnabled(!modified);
		updateButton.setEnabled(modified);
	}

	private void toggleConfigurationLock() {
		if (senderEmailDialog.isOpened()) {
			return;
		}
		if (configurationLocked) {
			if (passwordMaskDisplayed) {
				appPassword.clear();
				passwordMaskDisplayed = false;
			}
			setConfigurationLocked(false);
			return;
		}
		loadConfiguration();
		setConfigurationLocked(true);
	}

	private void configureSenderEmailDialog() {
		senderEmailDialog.setHeader("Сброс пароля");
		senderEmailDialog.setText("Подтвердите изменения.");
		senderEmailDialog.setConfirmText("Да");
		senderEmailDialog.setCancelText("Нет");
		senderEmailDialog.setCancelable(true);
		senderEmailDialog.setCloseOnEsc(true);
		senderEmailDialog.addOpenedChangeListener(event -> {
			if (event.isOpened()) {
				updateButton.setEnabled(false);
				lockButton.setEnabled(false);
			} else {
				updateActionButtonsState();
			}
		});
		senderEmailDialog.addConfirmListener(_ -> {
			pendingEmailChanged = false;
			pendingPasswordChanged = false;
			senderEmailDialog.close();
			updateActionButtonsState();
			performSave();
		});
		senderEmailDialog.addCancelListener(_ -> {
			pendingEmailChanged = false;
			pendingPasswordChanged = false;
			senderEmailDialog.close();
			updateActionButtonsState();
		});
		add(senderEmailDialog);
	}

}
