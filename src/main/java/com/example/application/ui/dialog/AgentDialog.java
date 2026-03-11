package com.example.application.ui.dialog;

import com.example.application.backend.model.Agent;
import com.example.application.ui.template.BaseDialog;
import com.vaadin.flow.component.textfield.TextField;

public class AgentDialog extends BaseDialog.EntityDialog<Agent> {
    TextField name = new TextField("Имя");

    public AgentDialog() {
        add(name);
    }
}
