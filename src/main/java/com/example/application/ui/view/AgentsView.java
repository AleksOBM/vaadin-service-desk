package com.example.application.ui.view;

import com.example.application.backend.model.Agent;
import com.example.application.backend.repository.AgentRepository;
import com.example.application.ui.templates.BaseView;
import com.example.application.ui.util.NotificationSupport;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Collection;

@Route("agents")
@PageTitle("Agent List")
@Menu(order = 3, icon = "vaadin:user", title = "Сотрудники")
public class AgentsView extends BaseView {

    private final AgentRepository agentRepository;
    private final Grid<Agent> grid = new Grid<>();

    public AgentsView(AgentRepository agentRepository) {
        super("Сотрудники");
        addClassName("agents-view");
        this.agentRepository = agentRepository;
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
        NotificationSupport.showFunctionNotImplemented();
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
        grid.setItems(agentRepository.findAll());
        return grid;
    }

    private String createCountFooter() {
        return String.format("Всего %s", agentRepository.count());
    }

    private void getGridData() {
        String value = super.filterText.getValue();

        if (value == null || value.isBlank()) {
            grid.setItems(agentRepository.findAll());
            return;
        }

        Collection<Agent> content = agentRepository.findAll().stream()
                .filter(agent ->
                        agent.getName().toLowerCase().contains(value.toLowerCase()) ||
                                agent.getServiceDeskNumber().toLowerCase().contains(value.toLowerCase())
                )
                .toList();

        grid.setItems(content);
    }

}
