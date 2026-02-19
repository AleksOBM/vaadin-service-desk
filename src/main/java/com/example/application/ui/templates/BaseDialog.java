package com.example.application.ui.templates;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;

import java.util.function.Consumer;

public class BaseDialog<T> extends Dialog {

    private final Button saveButton = new Button("Сохранить");
    private final Button cancelButton = new Button("Отмена");

    private final BeanValidationBinder<T> binder;
    private T entity;

    public BaseDialog(
            Class<T> beanClass,
            String title,
            EntityDialog<T> form,
            Consumer<T> onSave
    ) {
        this.setMaxWidth(25, Unit.EM);
        this.setCloseOnEsc(true);

        this.binder = new BeanValidationBinder<>(beanClass);

        setHeaderTitle(title);

        binder.bindInstanceFields(form);

        add(form);

        saveButton.setEnabled(false);

        binder.addStatusChangeListener(e ->
                saveButton.setEnabled(binder.isValid())
        );

        saveButton.addClickListener(e -> save(onSave));
        cancelButton.addClickListener(e -> close());
        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton, saveButton);
        buttonLayout.getStyle().setMarginTop("20px");
        add(buttonLayout);
    }

    public void setEntity(T entity) {
        this.entity = entity;
        binder.readBean(entity);
    }

    private void save(Consumer<T> onSave) {
        try {
            binder.writeBean(entity);
            onSave.accept(entity);
            close();
        } catch (Exception e) {
            Notification.show("Исправьте ошибки в форме");
        }
    }

    public abstract static class EntityDialog<T> extends FormLayout {
    }

}

