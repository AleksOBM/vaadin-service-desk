package com.example.application.ui.view;

import com.example.application.backend.model.*;
import com.example.application.backend.service.AgentService;
import com.example.application.backend.service.ClientService;
import com.example.application.backend.service.OrderService;
import com.example.application.ui.dialogs.DeleteDialog;
import com.example.application.ui.templates.BaseView;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Route("recycle")
@PageTitle("Recycle")
@Menu(order = 5, icon = "vaadin:recycle", title = "Корзина")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecycleView extends BaseView {

    final OrderService orderService;
    final ClientService clientService;
    final AgentService agentService;

    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    final Grid<BaseEntity> grid = new Grid<>();
    final DeleteDialog deleteDialog = new DeleteDialog();

    public RecycleView(
            OrderService orderService,
            ClientService clientService,
            AgentService agentService
    ) {
        super("Корзина");
        this.orderService = orderService;
        this.clientService = clientService;
        this.agentService = agentService;
        addClassName("recycle-view");

        configureView();
        add(getRecycleGrid());
    }

    private void configureView() {
        this.createButton.setVisible(false);
        this.updateButton.setIcon(new Icon(VaadinIcon.REFRESH));
        this.deleteButton.setIcon(new Icon(VaadinIcon.RECYCLE));

        filterText.addValueChangeListener(event -> getGridData());
        updateButton.addClickListener(click -> restore(grid.asSingleSelect().getValue()));
        deleteButton.addClickListener(click -> deleteDialog.open());

        deleteDialog.addConfirmListener(event -> deleteForever(grid.asSingleSelect().getValue()));
    }

    private Grid<BaseEntity> getRecycleGrid() {
        configureGridStyle();
        configureGridColumns();

        grid.addSelectionListener(event -> {
            boolean enabled = event.getFirstSelectedItem().isPresent();
            this.updateButton.setEnabled(enabled);
            this.deleteButton.setEnabled(enabled);
        });

        return grid;
    }

    private void configureGridColumns() {
        grid.addColumn(BaseEntity::getServiceDeskNumber).setHeader("ID");

        grid.addColumn(new LocalDateTimeRenderer<>(
                BaseEntity::getCreationDate, () -> formatter)
        ).setHeader("Дата создания").setComparator(BaseEntity::getCreationDate);

        Grid.Column<BaseEntity> deletionDateColumn = grid.addColumn(new LocalDateTimeRenderer<>(
                BaseEntity::getLastUpdated, () -> formatter)
        ).setHeader("Дата удаления").setComparator(BaseEntity::getLastUpdated);

        grid.getColumns().forEach(column -> {
                    column.setSortable(true);
                    column.setAutoWidth(true);
                });

        grid.setMultiSort(true);
        GridSortOrder<BaseEntity> sortOrder1 = new GridSortOrder<>(deletionDateColumn, SortDirection.DESCENDING);
        grid.sort(List.of(sortOrder1));

        grid.getColumns().stream().findFirst()
                .orElseThrow().setFooter(createCountFooter());
    }

    private void configureGridStyle() {
        grid.addClassNames("recycle-grid");
        grid.addClassName("big-header-grid");
        grid.getStyle().setMarginTop("20px");
        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.AURA_ROW_STRIPES);
        grid.setEmptyStateText("Данные отсутствуют.");
    }

    private String createCountFooter() {
        return String.format("Всего %s", getGridData());
    }

    private int getGridData() {
        String filter = filterText.getValue();

        Collection<BaseEntity> recycleList = Stream.of(
                        orderService.findDeleted(),
                        clientService.findDeleted(),
                        agentService.findDeleted()
                )
                .flatMap(Collection::stream)
                .map(entity -> (BaseEntity) entity)
                .filter(entity ->
                        entity.getServiceDeskNumber().toLowerCase().contains(filter) ||
                                entity.getCreationDate().format(formatter).contains(filter) ||
                                entity.getLastUpdated().format(formatter).contains(filter)
                )
                .toList();

        grid.setItems(recycleList);
        return recycleList.size();
    }

    private void deleteForever(BaseEntity entity) {
        EntityType type = entity.getType();
        switch (type) {
            case ORDER -> orderService.deleteForever((Order) entity);
            case CLIENT -> clientService.deleteForever((Client) entity);
            case AGENT -> agentService.deleteForever((Agent) entity);
        }
        getGridData();
    }

    private void restore(BaseEntity entity) {
        EntityType type = entity.getType();
        switch (type) {
            case ORDER -> orderService.restore((Order) entity);
            case CLIENT -> clientService.restore((Client) entity);
            case AGENT -> agentService.restore((Agent) entity);
        }
        getGridData();
    }
}
