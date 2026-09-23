package com.example.vsd.frontend.render.dialog;

import com.example.vsd.frontend.model.Agent;
import com.example.vsd.frontend.model.Client;
import com.example.vsd.frontend.render.template.BaseDialog;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import java.util.Collection;

public class OrderDialog extends BaseDialog.EntityDialog {
    private final Collection<Client> clients;
    private final Collection<Agent> agents;

    private final TextField name = new TextField("Название");
    private final TextArea description = new TextArea("Описание");
    private final ComboBox<Client> client = new ComboBox<>("Клиент");
    private final ComboBox<Agent> agent = new ComboBox<>("Сотрудник");
    private final DatePicker startLine = new DatePicker("Дата начала");
    private final DatePicker deadLine = new DatePicker("Дедлайн");
    private final Checkbox completed = new Checkbox("Отметка о выполнении");

    public OrderDialog(Collection<Client> clients, Collection<Agent> agents) {
        this.clients = clients;
        this.agents = agents;
        configureFields();
    }

    private void configureFields() {
        name.setRequired(true);

        startLine.setI18n(new DatePicker.DatePickerI18n().setFirstDayOfWeek(1));
        startLine.setRequired(true);

        deadLine.setI18n(new DatePicker.DatePickerI18n().setFirstDayOfWeek(1));
        deadLine.setRequired(true);

        client.setItems(clients);
        client.setItemLabelGenerator(Client::name);
        client.setRequired(true);

        agent.setItems(agents);
        agent.setItemLabelGenerator(Agent::name);
        agent.setRequired(true);
        description.setMinHeight(7, Unit.EM);

        add(
                name,
                description,
                client,
                agent,
                startLine,
                deadLine,
                completed
        );
    }
}
