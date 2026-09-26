package com.example.vsd.frontend.render.view;

import com.example.vsd.frontend.render.dialog.ClientDialog;
import com.example.vsd.frontend.grpc.client.ClientService;
import com.example.vsd.frontend.model.Client;
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

import java.util.Collection;

@Route("clients")
@PageTitle("Client List")
@Menu(order = 3, icon = "vaadin:user-star", title = "Клиенты")
@SpringComponent
@RouteScope
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClientsView extends BaseView {

	ClientService clientService;
	Grid<Client> grid = new Grid<>();

	public ClientsView(ClientService clientService) {
		super("Клиенты");
		this.clientService = clientService;
		addClassName("clients-view");
		configureView();
		add(getClientsGrid());
	}

	private void configureView() {
		filterText.addValueChangeListener(_ -> getGridData());
		createButton.addClickListener(_ -> createClient());
		updateButton.addClickListener(_ -> updateClient());
		deleteButton.addClickListener(_ -> deleteClient());
	}

	private void createClient() {
		var dialog = new BaseDialog<>(
				Client.class,
				"Новый клиент",
				new ClientDialog(),
				client -> {
					clientService.saveClient(client);
					NotificationSupport.showSuccess("Клиент добавлен.");
					String text = filterText.getValue();
					if (text == null || text.isBlank()) {
						grid.setItems(clientService.getAllClients());
					} else {
						getGridData();
					}
				});
		dialog.setEntity(new Client());
		dialog.open();
	}

	private void updateClient() {
		Client oldClient = grid.asSingleSelect().getValue();
		BaseDialog<Client> dialog = new BaseDialog<>(
				Client.class,
				"Редактировать клиента",
				new ClientDialog(),
				client -> {
					clientService.saveClient(client);
					NotificationSupport.showSuccess("Клиент изменен.");
					String text = filterText.getValue();
					if (text == null || text.isBlank()) {
						grid.setItems(clientService.getAllClients());
					} else {
						getGridData();
					}
				});
		dialog.setEntity(oldClient);
		dialog.open();
	}

	private void deleteClient() {
		Client client = grid.asSingleSelect().getValue();
		clientService.markAsDeleted(client.getId());
		getGridData();
		NotificationSupport.showInfo("Клиент удален.");
	}

	private Component getClientsGrid() {
		grid.addClassNames("client-grid");
		grid.addClassName("big-header-grid");
		grid.addThemeVariants(GridVariant.ROW_STRIPES);
		grid.setEmptyStateText("Данные отсутствуют.");

		grid.addColumn(Client::getServiceDeskNumber).setHeader("CL");
		grid.addColumn(Client::getName).setHeader("Название");

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
		Collection<Client> content = clientService.getAllClients();
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
		Collection<Client> content;
		if (filterText == null || filterText.isBlank()) {
			content = clientService.getAllClients();
		} else {
			content = clientService.getContent(filterText);
		}
		grid.setItems(content);
		entitiesCount = content.size();
		updateFooter();
	}
}
