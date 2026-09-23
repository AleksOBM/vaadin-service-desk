package com.example.vsd.frontend.render.dialog;

import com.example.vsd.frontend.render.template.BaseDialog;
import com.vaadin.flow.component.textfield.TextField;

public class ClientDialog extends BaseDialog.EntityDialog {
    TextField name = new TextField("Название");
    public ClientDialog() {
        add(name);
    }
}
