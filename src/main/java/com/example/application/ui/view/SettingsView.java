package com.example.application.ui.view;

import com.example.application.ui.templates.BaseView;
import com.example.application.ui.util.NotificationSupport;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.ColorScheme;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("settings")
@PageTitle("Settings")
@Menu(order = 6, icon = "vaadin:tools", title = "Настройки")
public class SettingsView extends BaseView {

    // todo: реализовать смену темы, добавить другие настройки

    Select<ColorScheme.Value> brightBox = new Select<>();
    EmailField targetEmailField = new EmailField("Почта для отчетов");
    EmailField homeEmailField = new EmailField("Почта для приложения");
    TextField homeMailHostField = new TextField("Хост почты приложения");
    TextField homeMailPortField = new TextField("Порт почты приложения");
    PasswordField passwordField = new PasswordField("Пароль почты приложения");
    Checkbox mailSmtpAuth = new Checkbox("mail smtp auth");
    Checkbox mailSmtpStartTls = new Checkbox("mail smtp starttls");

    public SettingsView() {
        super("Настройки");
        super.filterText.setVisible(false);

        this.createButton.setIcon(new Icon(VaadinIcon.ENTER_ARROW));

        Scroller scroller = new Scroller();
        scroller.getStyle().setMarginTop("20px");

        VerticalLayout layout = new VerticalLayout();
        layout.add(
                getBrightBox(),
                targetEmailField,
                homeEmailField,
                homeMailHostField,
                homeMailPortField,
                passwordField,
                mailSmtpAuth,
                mailSmtpStartTls
        );

        layout.setWidth(20, Unit.EM);

        scroller.setContent(layout);
        add(scroller);

        brightBox.addValueChangeListener(event ->
                NotificationSupport.showFunctionNotImplemented()
        );
    }

    private void switchTheme(ColorScheme.Value value) {
        UI.getCurrent().getElement().setAttribute("theme", value.getThemeValue());
    }

    private Select<ColorScheme.Value> getBrightBox() {
        brightBox.setItems(ColorScheme.Value.values());
        brightBox.setEnabled(true);
        brightBox.setRequiredIndicatorVisible(false);
        brightBox.setValue(ColorScheme.Value.SYSTEM);
        brightBox.setLabel("Тема");
//        brightBox.getStyle().setMarginTop("20px");
        return brightBox;
    }
}
