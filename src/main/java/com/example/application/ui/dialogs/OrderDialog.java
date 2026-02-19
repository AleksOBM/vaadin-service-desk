package com.example.application.ui.dialogs;

import com.example.application.backend.model.Agent;
import com.example.application.backend.model.Client;
import com.example.application.backend.model.Order;
import com.example.application.ui.templates.BaseDialog;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import java.util.Collection;

public class OrderDialog extends BaseDialog.EntityDialog<Order> {
    private final Collection<Client> clients;
    private final Collection<Agent> agents;

    private final TextField title = new TextField("Название");
    private final TextArea description = new TextArea("Описание");
    ComboBox<Client> client = new ComboBox<>("Клиент");
    ComboBox<Agent> agent = new ComboBox<>("Сотрудник");
    private final DatePicker startLine = new DatePicker("Дата начала");
    private final DatePicker deadLine = new DatePicker("Дедлайн");

    public OrderDialog(Collection<Client> clients, Collection<Agent> agents) {
        this.clients = clients;
        this.agents = agents;

        configureFields();
    }

    private void configureFields() {
        title.setRequired(true);

        startLine.setI18n(new DatePicker.DatePickerI18n().setFirstDayOfWeek(1));
        startLine.setRequired(true);

        deadLine.setI18n(new DatePicker.DatePickerI18n().setFirstDayOfWeek(1));
        deadLine.setRequired(true);

        client.setItems(clients);
        client.setItemLabelGenerator(Client::getName);
        client.setRequired(true);
        agent.setItems(agents);
        agent.setItemLabelGenerator(Agent::getName);
        agent.setRequired(true);
        description.setMinHeight(7, Unit.EM);

        add(
                title,
                description,
                client,
                agent,
                startLine,
                deadLine
        );
    }
}
