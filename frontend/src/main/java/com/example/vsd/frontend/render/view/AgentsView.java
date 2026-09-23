package com.example.vsd.frontend.render.view;

import com.example.vsd.frontend.render.dialog.AgentDialog;
import com.example.vsd.frontend.grpc.client.AgentService;
import com.example.vsd.frontend.model.Agent;
import com.example.vsd.frontend.render.template.BaseDialog;
import com.example.vsd.frontend.render.template.BaseView;
import com.example.vsd.frontend.render.notification.NotificationSupport;
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
	Grid<Agent> grid = new Grid<>();

	@Autowired
	public AgentsView(AgentService agentService) {
		super("Сотрудники");
		this.agentService = agentService;
		addClassName("agents-view");
		configureView();
		add(getAgentsGrid());
	}

	private void configureView() {
		filterText.addValueChangeListener(
				_ -> getGridData());
		createButton.addClickListener(_ -> createAgent());
		updateButton.addClickListener(_ -> updateAgent());
		deleteButton.addClickListener(_ -> deleteAgent());
	}

	private void createAgent() {
		var dialog = new BaseDialog<>(
				Agent.class,
				"Новый сотрудник",
				new AgentDialog(),
				agent -> {
					agentService.createAgent(agent.name());
					NotificationSupport.showSuccess("Сотрудник добавлен.");
					String text = filterText.getValue();
					if (text == null || text.isBlank()) {
						grid.setItems(agentService.getAllAgents());
					} else {
						getGridData();
					}
				});
		dialog.setEntity(Agent.builder().build());
		dialog.open();
	}

	private void updateAgent() {
		Agent oldAgent = grid.asSingleSelect().getValue();
		BaseDialog<Agent> dialog = new BaseDialog<>(
				Agent.class,
				"Редактировать сотрудника",
				new AgentDialog(),
				agent -> {
					agentService.createAgent(agent.name());
					NotificationSupport.showSuccess("Сотрудник изменен.");
					String text = filterText.getValue();
					if (text == null || text.isBlank()) {
						grid.setItems(agentService.getAllAgents());
					} else {
						getGridData();
					}
				});
		dialog.setEntity(oldAgent);
		dialog.open();
	}

	private void deleteAgent() {
		Agent agent = grid.asSingleSelect().getValue();
		agentService.markAsDeleted(agent.id());
		getGridData();
		NotificationSupport.showInfo("Сотрудник удален.");
	}

	private Component getAgentsGrid() {
		grid.addClassNames("agent-grid");
		grid.addClassName("big-header-grid");
		grid.addThemeVariants(GridVariant.ROW_STRIPES);
		grid.setEmptyStateText("Данные отсутствуют.");

		grid.addColumn(Agent::serviceDeskNumber).setHeader("AG");
		grid.addColumn(Agent::name).setHeader("Имя");

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
		Collection<Agent> content = agentService.getAllAgents();
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
			content = agentService.getAllAgents();
		} else {
			content = agentService.getContent(filterText);
		}
		grid.setItems(content);
		entitiesCount = content.size();
		updateFooter();
	}
}
