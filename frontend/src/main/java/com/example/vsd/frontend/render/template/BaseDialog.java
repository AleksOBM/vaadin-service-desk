package com.example.vsd.frontend.render.template;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import lombok.extern.slf4j.Slf4j;
import org.snakeyaml.engine.v2.exceptions.DuplicateKeyException;

import java.util.function.Consumer;

@Slf4j
public class BaseDialog<T> extends Dialog {

	private final Button saveButton = new Button("Сохранить");
	private final Button cancelButton = new Button("Отмена");

	private final BeanValidationBinder<T> binder;
	private T entity;

	public BaseDialog(
			Class<T> beanClass,
			String title,
			EntityDialog form,
			Consumer<T> onSave
	) {
		this.setMaxWidth(25, Unit.EM);
		this.setCloseOnEsc(true);

		this.binder = new BeanValidationBinder<>(beanClass);

		setHeaderTitle(title);

		binder.bindInstanceFields(form);

		form.bind(binder);

		add(form);

		saveButton.setEnabled(false);

		binder.addStatusChangeListener(_ -> saveButton
				.setEnabled(binder.isValid()));

		saveButton.addClickListener(_ -> save(onSave));
		cancelButton.addClickListener(_ -> close());
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
		} catch (DuplicateKeyException e) {
			Notification.show("Обнаружено дублирование данных. Проверте корзину");
			log.error(e.getLocalizedMessage());
		} catch (Exception e) {
			Notification.show("Произошла непредвиденная ошибка");
			log.error(e.getLocalizedMessage());
		}
	}

	public abstract static class EntityDialog extends FormLayout {
		public void bind(Binder<?> binder) {
			// по умолчанию ничего не делаем
		}
	}
}

