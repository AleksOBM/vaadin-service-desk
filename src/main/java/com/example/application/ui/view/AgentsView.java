package com.example.application.ui.view;

import com.example.application.backend.model.Agent;
import com.example.application.backend.service.AgentService;
import com.example.application.backend.service.RecycleService;
import com.example.application.ui.dialog.AgentDialog;
import com.example.application.ui.template.BaseDialog;
import com.example.application.ui.template.BaseView;
import com.example.application.ui.util.NotificationSupport;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.RouteScope;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

@Route("agents")
@PageTitle("Agent List")
@Menu(order = 4, icon = "vaadin:user", title = "Сотрудники")
@SpringComponent
@RouteScope
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AgentsView extends BaseView {

    AgentService agentService;
    RecycleService recycleService;
    Grid<Agent> grid = new Grid<>();

    @Autowired
    public AgentsView(AgentService agentService, RecycleService recycleService) {
        super("Сотрудники");
        this.agentService = agentService;
        this.recycleService = recycleService;
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
                    String text = filterText.getValue();
                    if (text == null || text.isBlank()) {
                        grid.setItems(agentService.findAll());
                    } else {
                        getGridData();
                    }
                });
        dialog.setEntity(new Agent());
        dialog.open();
    }

    private void updateAgent() {
        Agent oldAgent = grid.asSingleSelect().getValue();
        BaseDialog<Agent> dialog = new BaseDialog<>(
                Agent.class,
                "Редактировать сотрудника",
                new AgentDialog(),
                agent -> {
                    agentService.save(agent);
                    NotificationSupport.showSuccess("Сотрудник изменен.");
                    String text = filterText.getValue();
                    if (text == null || text.isBlank()) {
                        grid.setItems(agentService.findAll());
                    } else {
                        getGridData();
                    }
                });
        dialog.setEntity(oldAgent);
        dialog.open();
    }

    private void deleteAgent() {
        Agent agent = grid.asSingleSelect().getValue();
        recycleService.markAsDeleted(agent);
        getGridData();
        NotificationSupport.showInfo("Сотрудник удален.");
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

        grid.addSelectionListener(event -> {
            boolean enabled = event.getFirstSelectedItem().isPresent();
            updateButton.setEnabled(enabled);
            deleteButton.setEnabled(enabled);
        });

        grid.setSizeFull();
        grid.getStyle().setMarginTop("20px");
        Collection<Agent> content = agentService.findAll();
        grid.setItems(content);
        entitiesCount = content.size();
        updateFooter();
        return grid;
    }

    private void updateFooter() {
        grid.getColumns().getFirst().setFooter(String.format("Всего %s", entitiesCount));
    }

    private void getGridData() {
        String filterText = super.filterText.getValue();
        Collection<Agent> content;
        if (filterText == null || filterText.isBlank()) {
            content = agentService.findAll();
        } else {
            content = agentService.getContent(filterText);
        }
        grid.setItems(content);
        entitiesCount = content.size();
        updateFooter();
    }
}
