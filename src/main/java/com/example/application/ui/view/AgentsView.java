package com.example.application.ui.view;

import com.example.application.backend.model.Agent;
import com.example.application.backend.service.AgentService;
import com.example.application.ui.dialogs.AgentDialog;
import com.example.application.ui.templates.BaseDialog;
import com.example.application.ui.templates.BaseView;
import com.example.application.ui.util.NotificationSupport;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.RouteScope;
import com.vaadin.flow.spring.annotation.SpringComponent;

import java.util.Collection;

@Route("agents")
@PageTitle("Agent List")
@Menu(order = 4, icon = "vaadin:user", title = "Сотрудники")
@SpringComponent
@RouteScope
public class AgentsView extends BaseView {

    // todo: добавить возможность удаления
    // todo: добавить возможность редактирования

    private final AgentService agentService;
    private final Grid<Agent> grid = new Grid<>();

    public AgentsView(AgentService agentService) {
        super("Сотрудники");
        this.agentService = agentService;
        addClassName("agents-view");
        configureView();
        add(getAgentsGrid());
    }

    private void configureView() {
        filterText.addValueChangeListener(e -> getGridData());
        createButton.addClickListener(click -> createAgent());
        updateButton.addClickListener(click -> updateAgent());
        deleteButton.addClickListener(click -> deleteAgent());
    }

    private void createAgent() {
        BaseDialog<Agent> dialog = new BaseDialog<>(
                Agent.class,
                "Новый сотрудник",
                new AgentDialog(),
                agent -> {
            agentService.save(agent);
            NotificationSupport.showSuccess("Сотрудник добавлен.");
            if (filterText.getValue() == null) {
                grid.setItems(agentService.findAll());
            }
            getGridData();
        });
        dialog.setEntity(new Agent());
        dialog.open();
    }

    private void updateAgent() {
        NotificationSupport.showFunctionNotImplemented();
    }

    private void deleteAgent() {
        NotificationSupport.showFunctionNotImplemented();
    }

    private Component getAgentsGrid() {
        grid.addClassNames("agent-grid");
        grid.addClassName("big-header-grid");
        grid.addThemeVariants(GridVariant.AURA_ROW_STRIPES);
        grid.setEmptyStateText("Данные отсутствуют.");

        grid.addColumn(Agent::getServiceDeskNumber).setHeader("AG");
        grid.addColumn(Agent::getName).setHeader("Имя");

        grid.getColumns().forEach(column -> {
            column.setSortable(true);
            column.setAutoWidth(true);
        });

        grid.getColumns().getFirst().setFooter(createCountFooter());

        grid.setSizeFull();
        grid.getStyle().setMarginTop("20px");
        grid.setItems(agentService.findAll());
        return grid;
    }

    private String createCountFooter() {
        return String.format("Всего %s", agentService.count());
    }

    private void getGridData() {
        String value = super.filterText.getValue();

        if (value == null || value.isBlank()) {
            grid.setItems(agentService.findAll());
            return;
        }

        Collection<Agent> content = agentService.findAll().stream()
                .filter(agent ->
                        agent.getName().toLowerCase().contains(value.toLowerCase()) ||
                                agent.getServiceDeskNumber().toLowerCase().contains(value.toLowerCase())
                )
                .toList();

        grid.setItems(content);
    }
}
