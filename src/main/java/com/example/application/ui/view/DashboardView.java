package com.example.application.ui.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "dashboard")
@PageTitle("Dashboard")
@Menu(order = 1, icon = "vaadin:dashboard", title = "Диаграмма")
public class DashboardView extends VerticalLayout {
    // todo: добавить диаграмму метрик, календарь и/или диаграмму Ганта
}
