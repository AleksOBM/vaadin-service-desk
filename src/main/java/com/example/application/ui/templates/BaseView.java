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

//@Route(value = "", layout = MainLayout.class)
//@RouteAlias(value = "orders", layout = MainLayout.class)
//@PageTitle("Orders")
//@Menu(order = 3, icon = "vaadin:user", title = "Сотрудники")
//@SpringComponent
//@Scope("prototype")
@Getter
@FieldDefaults(level = AccessLevel.PROTECTED)
public class BaseView extends VerticalLayout {

    TextField filterText = new TextField();
    Button createButton;
    Button updateButton;
    Button deleteButton;

    public BaseView(String title) {
        filterText.setPlaceholder("Фильтр");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
//        filterText.addValueChangeListener(e -> updateList());

        createButton = new Button(new Icon(VaadinIcon.PLUS));
//        createButton.addClickListener(click -> addAgent());
        createButton.addThemeVariants(ButtonVariant.AURA_TERTIARY);

        updateButton = new Button(new Icon(VaadinIcon.WRENCH));
        updateButton.addThemeVariants(ButtonVariant.AURA_TERTIARY);

        deleteButton = new Button(new Icon(VaadinIcon.TRASH));
        deleteButton.addThemeVariants(ButtonVariant.AURA_TERTIARY);

        var toolbar = new BaseToolbar(title, filterText, createButton, updateButton, deleteButton);
        toolbar.addClassName("toolbar");
        setSpacing(false);
        getStyle().setOverflow(Style.Overflow.HIDDEN);

        add(toolbar);
    }

}
