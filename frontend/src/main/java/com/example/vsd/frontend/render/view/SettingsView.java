package com.example.vsd.frontend.render.view;

import com.example.vsd.frontend.grpc.client.MailService;
import com.example.vsd.frontend.model.MailEntity;
import com.example.vsd.frontend.render.template.BaseView;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.RouteScope;
import com.vaadin.flow.spring.annotation.SpringComponent;

@Route("settings")
@PageTitle("Settings")
@Menu(order = 6, icon = "vaadin:tools", title = "Настройки")
@SpringComponent
@RouteScope
public class SettingsView extends BaseView {

    // todo: реализовать смену темы, добавить другие настройки
    // todo: расставить подсказки TooltipText на кнопках

    private final MailService mailService;

    EmailField targetEmailField = new EmailField("Почта для отчетов");
    EmailField homeEmailField = new EmailField("Почта для приложения");
    TextField homeMailHostField = new TextField("Хост почты приложения");
    TextField homeMailPortField = new TextField("Порт почты приложения");
    PasswordField passwordField = new PasswordField("Пароль почты приложения");
    Checkbox mailSmtpAuth = new Checkbox("mail smtp auth");
    Checkbox mailSmtpStartTls = new Checkbox("mail smtp starttls");
    VerticalLayout layout = new VerticalLayout();
    MailEntity mailEntity;

    public SettingsView(MailService mailService) {
        super("Настройки");
        this.mailService = mailService;
        super.filterText.setVisible(false);
        createButton.setIcon(new Icon(VaadinIcon.ENTER_ARROW));
        createButton.setEnabled(false);
        updateButton.setEnabled(true);
        deleteButton.setEnabled(true);

        mailEntity = mailService.getMailEntity().orElse(null);

        setValues();
        configureLayout();
        configureActions();
        add(layout);
    }

    private void setValues() {
        if (mailEntity == null) {
            return;
        }
        targetEmailField.setValue(mailEntity.getTargetMail());
        homeEmailField.setValue(mailEntity.getAppMail());
        homeMailHostField.setValue(mailEntity.getAppMailHost());
        homeMailPortField.setValue(mailEntity.getAppMailPort());
        passwordField.setValue(mailEntity.getAppMailPassword());
        mailSmtpAuth.setValue(mailEntity.getMailSmtpAuth());
        mailSmtpStartTls.setValue(mailEntity.getMailSmtpStartTls());
    }

    private void configureActions() {
        updateButton.addClickListener(event -> {
            if (layout.isEnabled()) {
                layout.setEnabled(false);
            } else {
                layout.setEnabled(true);
                createButton.setEnabled(true);
            }
        });

        createButton.addClickListener(event -> {
            createEntity();
            layout.setEnabled(false);
        });
    }

    private void configureLayout() {
        layout.add(
                targetEmailField,
                homeEmailField,
                homeMailHostField,
                homeMailPortField,
                passwordField,
                mailSmtpAuth,
                mailSmtpStartTls
        );

        layout.setEnabled(false);
        layout.getStyle().setMarginTop("20px");
        layout.setSizeFull();
    }

    private void createEntity() {
        mailEntity = MailEntity.builder()
                .id(1L)
                .appMail(homeEmailField.getValue())
                .targetMail(targetEmailField.getValue())
                .appMailHost(homeMailHostField.getValue())
                .appMailPassword(passwordField.getValue())
                .appMailPort(homeMailPortField.getValue())
                .mailSmtpAuth(mailSmtpAuth.getValue())
                .mailSmtpStartTls(mailSmtpStartTls.getValue())
                .build();
        mailService.saveMailEntity(mailEntity);
    }
}
