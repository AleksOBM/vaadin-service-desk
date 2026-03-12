package com.example.application.ui.view;

import com.example.application.backend.model.Client;
import com.example.application.backend.service.ClientService;
import com.example.application.backend.service.RecycleService;
import com.example.application.ui.dialog.ClientDialog;
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

import java.util.Collection;

@Route("clients")
@PageTitle("Client List")
@Menu(order = 3, icon = "vaadin:user-star", title = "Клиенты")
@SpringComponent
@RouteScope
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClientsView extends BaseView {

    ClientService clientService;
    RecycleService recycleService;
    Grid<Client> grid = new Grid<>();

    public ClientsView(ClientService clientService, RecycleService recycleService) {
        super("Клиенты");
        this.clientService = clientService;
        this.recycleService = recycleService;
        addClassName("clients-view");
        configureView();
        add(getClientsGrid());
    }

    private void configureView() {
        filterText.addValueChangeListener(e -> getGridData());
        createButton.addClickListener(click -> createClient());
        updateButton.addClickListener(click -> updateClient());
        deleteButton.addClickListener(click -> deleteClient());
    }

    private void createClient() {
        BaseDialog<Client> dialog = new BaseDialog<>(
                Client.class,
                "Новый клиент",
                new ClientDialog(),
                client -> {
                    clientService.save(client);
                    NotificationSupport.showSuccess("Клиент добавлен.");
                    String text = filterText.getValue();
                    if (text == null || text.isBlank()) {
                        grid.setItems(clientService.findAll());
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
                    clientService.save(client);
                    NotificationSupport.showSuccess("Клиент изменен.");
                    String text = filterText.getValue();
                    if (text == null || text.isBlank()) {
                        grid.setItems(clientService.findAll());
                    } else {
                        getGridData();
                    }
                });
        dialog.setEntity(oldClient);
        dialog.open();
    }

    private void deleteClient() {
        Client client = grid.asSingleSelect().getValue();
        recycleService.markAsDeleted(client);
        getGridData();
        NotificationSupport.showInfo("Клиент удален.");
    }

    private Component getClientsGrid() {
        grid.addClassNames("client-grid");
        grid.addClassName("big-header-grid");
        grid.addThemeVariants(GridVariant.AURA_ROW_STRIPES);
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
        Collection<Client> content = clientService.findAll();
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
            content = clientService.findAll();
        } else {
            content = clientService.getContent(filterText);
        }
        grid.setItems(content);
        entitiesCount = content.size();
        updateFooter();
    }
}
