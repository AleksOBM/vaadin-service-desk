package com.example.application.ui.dialog;

import com.example.application.ui.template.BaseDialog;
import com.vaadin.flow.component.textfield.TextField;

public class AgentDialog extends BaseDialog.EntityDialog {
    TextField name = new TextField("Имя");
    public AgentDialog() {
        add(name);
    }
}
