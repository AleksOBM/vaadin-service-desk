package com.example.application.ui.templates;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.Style;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PROTECTED)
public class BaseView extends VerticalLayout {

    TextField filterText = new TextField();
    Button createButton = new Button();
    Button updateButton = new Button();
    Button deleteButton = new Button();
    int entitiesCount;

    public BaseView(String title) {
        filterText.setPlaceholder("Поиск");
        filterText.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);

        createButton.setTooltipText("Создать");
        createButton.setIcon(new Icon(VaadinIcon.PLUS));
        createButton.addThemeVariants(ButtonVariant.AURA_TERTIARY);

        updateButton.setTooltipText("Редактировать");
        updateButton.setIcon(new Icon(VaadinIcon.WRENCH));
        updateButton.addThemeVariants(ButtonVariant.AURA_TERTIARY);
        updateButton.setEnabled(false);

        deleteButton.setTooltipText("Удалить");
        deleteButton.setIcon(new Icon(VaadinIcon.TRASH));
        deleteButton.addThemeVariants(ButtonVariant.AURA_TERTIARY);
        deleteButton.setEnabled(false);

        var toolbar = new BaseToolbar(title, filterText, createButton, updateButton, deleteButton);
        toolbar.addClassName("toolbar");
        setSpacing(false);
        getStyle().setOverflow(Style.Overflow.HIDDEN);

        add(toolbar);
    }
}
