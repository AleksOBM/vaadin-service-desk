package com.example.application.ui.dialog;

import com.example.application.backend.model.Client;
import com.example.application.ui.template.BaseDialog;
import com.vaadin.flow.component.textfield.TextField;

public class ClientDialog extends BaseDialog.EntityDialog<Client> {
    TextField name = new TextField("Название");

    public ClientDialog() {
        add(name);
    }
}
