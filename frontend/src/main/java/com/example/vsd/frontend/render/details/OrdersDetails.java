package com.example.vsd.frontend.render.details;

import com.example.vsd.frontend.model.Order;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import org.jspecify.annotations.NonNull;

import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

public class OrdersDetails extends FormLayout {
	TextArea descriptionField = new TextArea("Описание");
	TextField startLineField = new TextField("Дата старта");
	TextField firstControlLineField = new TextField("Контроль запуска");
	TextField secondControlLineField = new TextField("Контроль завершения");
	TextField daysCountField = new TextField("Срок");
	TextField deadLineField = new TextField("Дедлайн");
	TextField agentField = new TextField("Сотрудник");

	public OrdersDetails() {
		addClassName("order-details");

		Stream.of(descriptionField, startLineField, firstControlLineField,
						secondControlLineField, daysCountField, deadLineField, agentField)
				.forEach(field -> {
					field.setReadOnly(true);
					add(field);
				});

		Stream.of(startLineField, firstControlLineField,
						secondControlLineField, deadLineField)
				.forEach(field -> {
					field.setReadOnly(true);
					add(field);
				});

		setResponsiveSteps(new ResponsiveStep("0", 3));
		setColspan(descriptionField, 3);
	}

	public void setOrder(@NonNull Order order) {
		descriptionField.setValue(order.getDescription());
		startLineField.setValue(order.getStartLine().format(DateTimeFormatter.ISO_DATE));
		firstControlLineField.setValue(order.getFirstControlLine().format(DateTimeFormatter.ISO_DATE));
		secondControlLineField.setValue(order.getSeondControlLine().format(DateTimeFormatter.ISO_DATE));
		daysCountField.setValue(order.getDaysCount() + " дней");
		deadLineField.setValue(order.getDeadLine().format(DateTimeFormatter.ISO_DATE));
		agentField.setValue(order.getAgent().getName());
	}

}
