package com.example.application.ui.view;

import com.example.application.backend.model.*;
import com.example.application.backend.service.RecycleService;
import com.example.application.ui.dialogs.DeleteDialog;
import com.example.application.ui.templates.BaseView;
import com.example.application.ui.util.NotificationSupport;
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

@Route("recycle")
@PageTitle("Recycle")
@Menu(order = 5, icon = "vaadin:recycle", title = "Корзина")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecycleView extends BaseView {

    final RecycleService recycleService;
    final Grid<BaseEntity> grid = new Grid<>();
    final DeleteDialog deleteDialog = new DeleteDialog();
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    int entityCount;

    public RecycleView(RecycleService recycleService) {
        super("Корзина");
        this.recycleService = recycleService;
        addClassName("recycle-view");
        configureView();
        add(getRecycleGrid());
    }

    private void configureView() {
        this.createButton.setVisible(false);

        this.updateButton.setTooltipText("Восстановить");
        this.updateButton.setIcon(new Icon(VaadinIcon.REFRESH));

        this.deleteButton.setTooltipText("Удалить навсегда");
        this.deleteButton.setIcon(new Icon(VaadinIcon.RECYCLE));

        filterText.addValueChangeListener(event -> getGridData());
        updateButton.addClickListener(click -> restore(grid.asSingleSelect().getValue()));
        deleteButton.addClickListener(click -> deleteDialog.open());

        deleteDialog.addConfirmListener(event -> deleteForever(grid.asSingleSelect().getValue()));
    }

    private Grid<BaseEntity> getRecycleGrid() {
        configureGridStyle();
        configureGridColumns();
        getGridData();

        grid.addSelectionListener(event -> {
            boolean enabled = event.getFirstSelectedItem().isPresent();
            this.updateButton.setEnabled(enabled);
            this.deleteButton.setEnabled(enabled);
        });

        return grid;
    }

    private void configureGridStyle() {
        grid.addClassNames("recycle-grid");
        grid.addClassName("big-header-grid");
        grid.getStyle().setMarginTop("20px");
        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.AURA_ROW_STRIPES);
        grid.setEmptyStateText("Данные отсутствуют.");
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
        GridSortOrder<BaseEntity> sortOrder = new GridSortOrder<>(deletionDateColumn, SortDirection.DESCENDING);
        grid.sort(List.of(sortOrder));
    }

    private void updateFooter() {
        grid.getColumns().stream().findFirst()
                .orElseThrow().setFooter(String.format("Всего %s", entityCount));
    }

    private void getGridData() {
        String filter = filterText.getValue();
        Collection<BaseEntity> recycleList = recycleService.getRecycleData(filter);
        grid.setItems(recycleList);
        entityCount = recycleList.size();
        updateFooter();
    }

    private void deleteForever(BaseEntity entity) {
        recycleService.deleteForever(entity);
        getGridData();
        NotificationSupport.showInfo("Объект уничножен");
    }

    private void restore(BaseEntity entity) {
        recycleService.restore(entity);
        getGridData();
        NotificationSupport.showInfo("Объект восстановлен");
    }
}
