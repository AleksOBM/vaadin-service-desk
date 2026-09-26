package com.example.vsd.frontend.render.view;

import com.example.vsd.frontend.grpc.client.AgentService;
import com.example.vsd.frontend.model.Agent;
import com.example.vsd.frontend.render.dialog.AgentDialog;
import com.example.vsd.frontend.render.notification.NotificationSupport;
import com.example.vsd.frontend.render.template.BaseDialog;
import com.example.vsd.frontend.render.template.BaseView;
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
import java.util.List;

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
		var dialog = new BaseDialog<>(Agent.class, "Новый сотрудник", new AgentDialog(),
				agent -> {
					agentService.saveAgent(agent);
					NotificationSupport.showSuccess("Сотрудник добавлен.");
					String text = filterText.getValue();
					if (text == null || text.isBlank()) {
						grid.setItems(agentService.getAllAgents());
					} else {
						getGridData();
					}
				});
		dialog.setEntity(new Agent());
		dialog.open();
	}

	private void updateAgent() {

		// Получаем сотрудника из выбранной строки
		Agent oldAgent = grid.asSingleSelect().getValue();

		// Создаем диалог
		var dialog = new BaseDialog<>(Agent.class, "Редактировать сотрудника", new AgentDialog(),

				// Определяем действия для диалога
				agent -> {

					// Сохраняем изменения
					agentService.saveAgent(agent);
					NotificationSupport.showSuccess("Сотрудник изменен.");

					// Получаем текст из поля поиска
					String text = filterText.getValue();

					// Обновляем таблицу после изменения данных
					if (text == null || text.isBlank()) {
						grid.setItems(agentService.getAllAgents());
					} else {
						getGridData();
					}
				});

		// Добавляем данные в диалог
		dialog.setEntity(oldAgent);

		// Запуск диалога
		dialog.open();
	}

	private void deleteAgent() {

		// Получаем сотрудника из выбранной строки
		Agent agent = grid.asSingleSelect().getValue();

		// Помечаем как удаленного
		agentService.markAsDeleted(agent.getId());

		// Обновляем данные таблицы
		getGridData();

		NotificationSupport.showInfo("Сотрудник удален.");
	}

	private Component getAgentsGrid() {

		// Основные параметры таблицы
		grid.addClassNames("agent-grid");
		grid.addClassName("big-header-grid");
		grid.addThemeVariants(GridVariant.ROW_STRIPES);
		grid.setEmptyStateText("Данные отсутствуют.");

		// Колонки таблицы
		grid.addColumn(Agent::getServiceDeskNumber).setHeader("AG");
		grid.addColumn(Agent::getName).setHeader("Имя");

		// Настройки колонок
		grid.getColumns().forEach(column -> {
			column.setSortable(true);
			column.setAutoWidth(true);
		});

		// Слушатель событий таблицы
		grid.addSelectionListener(event -> {
			boolean enabled = event.getFirstSelectedItem().isPresent();
			updateButton.setEnabled(enabled);
			deleteButton.setEnabled(enabled);
		});

		// Размеры таблицы
		grid.setSizeFull();
		grid.getStyle().setMarginTop("20px");

		// Загрузка данных в таблицу
		Collection<Agent> content = agentService.getAllAgents();
		grid.setItems(content);

		// Строка итогов
		entitiesCount = content.size();
		updateFooter();

		return grid;
	}

	private void updateFooter() {
		grid.getColumns().getFirst().setFooter(String.format("Всего %s", entitiesCount));
	}

	private void getGridData() {

		// Получаем текст из поля поиска
		String filterText = super.filterText.getValue();

		List<Agent> content;
		if (filterText == null || filterText.isBlank()) {
			// Если поле поиска пустое - грузим всё
			content = agentService.getAllAgents();
		} else {
			// Иначе грузим с фильтром
			content = agentService.getContent(filterText);
		}

		// Обновление данных таблицы
		grid.setItems(content);

		// Обновляем строку итогов
		entitiesCount = content.size();
		updateFooter();
	}
}
