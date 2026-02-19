package com.example.application.ui.dialogs;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;

public class DeleteDialog extends ConfirmDialog {

    public DeleteDialog() {
        this.setMaxWidth(25, Unit.EM);
        this.setCancelable(true);
        this.setCloseOnEsc(true);
        this.setText("Удалить на всегда?");

        this.addCancelListener(event -> close());
        this.setConfirmText("Delete");
        this.setConfirmButtonTheme("error primary");
    }

}
