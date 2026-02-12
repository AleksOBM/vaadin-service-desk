package com.example.application.ui.view;

import com.example.application.ui.templates.BaseView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.page.ColorScheme;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("settings")
@PageTitle("Settings")
@Menu(order = 4, icon = "vaadin:tools", title = "Настройки")
public class SettingsView extends BaseView {

    Select<ColorScheme.Value> brightBox = new Select<>();

    public SettingsView() {
        super("Настройки");

        super.createButton.setVisible(false);
        super.updateButton.setVisible(false);
        super.deleteButton.setVisible(false);
        super.filterText.setVisible(false);

        Scroller scroller = new Scroller(getBrightBox());
        scroller.getStyle().setMarginTop("20px");
        add(scroller);

        brightBox.addValueChangeListener(event ->
                        setNewTheme(event.getValue())
                );
    }

    private void setNewTheme(ColorScheme.Value value) {
        UI.getCurrent().getElement().setAttribute("theme", value.getThemeValue());
    }

    private Select<ColorScheme.Value> getBrightBox() {
        brightBox.setItems(ColorScheme.Value.values());
        brightBox.setEnabled(true);
        brightBox.setRequiredIndicatorVisible(false);
        brightBox.setValue(ColorScheme.Value.SYSTEM);
        brightBox.setLabel("Тема");
        return brightBox;
    }

}
