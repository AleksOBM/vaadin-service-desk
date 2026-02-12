package com.example.application.ui.view;

import com.example.application.backend.model.Client;
import com.example.application.backend.repository.ClientRepository;
import com.example.application.ui.templates.BaseView;
import com.example.application.ui.util.NotificationSupport;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Collection;

@Route("clients")
@PageTitle("Client List")
@Menu(order = 2, icon = "vaadin:user-star", title = "Клиенты")
public class ClientsView extends BaseView {

    private final ClientRepository clientRepository;
    private final Grid<Client> grid = new Grid<>();

    public ClientsView(ClientRepository clientRepository) {
        super("Клиенты");
        addClassName("clients-view");
        this.clientRepository = clientRepository;
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
        NotificationSupport.showFunctionNotImplemented();
    }

    private void updateClient() {
        NotificationSupport.showFunctionNotImplemented();
    }

    private void deleteClient() {
        NotificationSupport.showFunctionNotImplemented();
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

        grid.getColumns().getFirst().setFooter(createCountFooter());

        grid.setSizeFull();
        grid.getStyle().setMarginTop("20px");
        grid.setItems(clientRepository.findAll());
        return grid;
    }

    private String createCountFooter() {
        return String.format("Всего %s", clientRepository.count());
    }

    private void getGridData() {
        String value = super.filterText.getValue();

        if (value == null || value.isBlank()) {
            grid.setItems(clientRepository.findAll());
            return;
        }

        Collection<Client> content =  clientRepository.findAll().stream()
                .filter(client ->
                        client.getName().toLowerCase().contains(value.toLowerCase()) ||
                                client.getServiceDeskNumber().toLowerCase().contains(value.toLowerCase())
                )
                .toList();

        grid.setItems(content);
    }

}
