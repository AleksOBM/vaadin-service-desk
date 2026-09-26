package com.example.vsd.frontend.render.dialog;

import com.example.vsd.frontend.render.template.BaseDialog;
import com.vaadin.flow.component.textfield.TextField;

public class AgentDialog extends BaseDialog.EntityDialog {
    TextField name = new TextField("Имя");
    public AgentDialog() {
        add(name);
    }
}
