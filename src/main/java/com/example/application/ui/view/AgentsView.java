package com.example.application.ui.view;

import com.example.application.ui.templates.BaseView;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("agents")
@PageTitle("Agent List")
@Menu(order = 3, icon = "vaadin:user", title = "Сотрудники")
public class AgentsView extends BaseView {
    public AgentsView() {
        super("Сотрудники");
    }
}
