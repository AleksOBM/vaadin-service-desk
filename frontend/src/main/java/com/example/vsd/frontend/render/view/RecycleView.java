package com.example.vsd.frontend.render.view;

import com.example.vsd.frontend.render.dialog.DeleteDialog;
import com.example.vsd.frontend.grpc.client.RecycleService;
import com.example.vsd.frontend.model.BaseEntity;
import com.example.vsd.frontend.service.SessionInitService;
import com.example.vsd.frontend.render.template.BaseView;
import com.example.vsd.frontend.render.notification.NotificationSupport;
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
import com.vaadin.flow.spring.annotation.RouteScope;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route("recycle")
@PageTitle("Recycle")
@Menu(order = 5, icon = "vaadin:recycle", title = "Корзина")
@SpringComponent
@Slf4j
@RouteScope
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RecycleView extends BaseView {

    RecycleService recycleService;
    SessionInitService sessionService;
    Grid<BaseEntity> grid = new Grid<>();
    DeleteDialog deleteDialog = new DeleteDialog();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Autowired
    public RecycleView(RecycleService recycleService, SessionInitService sessionService) {
        super("Корзина");
        this.recycleService = recycleService;
        this.sessionService = sessionService;
        addClassName("recycle-view");
        configureView();
        add(getRecycleGrid());
    }

    private void configureView() {

        // Конфигурируем кнопки
        this.createButton.setVisible(false);

        this.updateButton.setTooltipText("Восстановить");
        this.updateButton.setIcon(new Icon(VaadinIcon.REFRESH));

        this.deleteButton.setTooltipText("Удалить навсегда");
        this.deleteButton.setIcon(new Icon(VaadinIcon.RECYCLE));

        // Конфигурируем слушатели
        filterText.addValueChangeListener(_ -> getGridData());
        updateButton.addClickListener(_ -> restore(grid.asSingleSelect().getValue()));

        deleteButton.addClickListener(_ -> deleteDialog.open());
        deleteDialog.addConfirmListener(_ -> deleteForever(grid.asSingleSelect().getValue()));
    }

    private Grid<BaseEntity> getRecycleGrid() {

        // Конфигурируем таблицу
        configureGridStyle();
        configureGridColumns();
        getGridData();

        // Добавляем активацию кнопок
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
        grid.addThemeVariants(GridVariant.ROW_STRIPES);
        grid.setEmptyStateText("Данные отсутствуют.");
    }

    private void configureGridColumns() {

        // Настраиваем колонки таблицы
        grid.addColumn(BaseEntity::getServiceDeskNumber).setHeader("ID");

        grid.addColumn(new LocalDateTimeRenderer<>(
                BaseEntity::getCreationDate, () -> formatter)
        ).setHeader("Дата создания").setComparator(BaseEntity::getCreationDate);

        Grid.Column<BaseEntity> deletionDateColumn = grid.addColumn(new LocalDateTimeRenderer<>(
                BaseEntity::getLastUpdated, () -> formatter)
        ).setHeader("Дата удаления").setComparator(BaseEntity::getLastUpdated);

        Grid.Column<BaseEntity> nameColumn = grid.addColumn(BaseEntity::getName);
        nameColumn.setHeader("Наименование");

        grid.getColumns().forEach(column -> {
                    column.setSortable(true);
                    column.setAutoWidth(true);
                });

        // Настраиваем сортировку
        grid.setMultiSort(true);
        GridSortOrder<BaseEntity> sortOrder = new GridSortOrder<>(
                deletionDateColumn, SortDirection.DESCENDING);
        grid.sort(List.of(sortOrder));
    }

    private void updateFooter() {
        grid.getColumns().stream().findFirst()
                .orElseThrow().setFooter(String.format("Всего %s", entitiesCount));
    }

    private void getGridData() {
        String filterText = super.filterText.getValue();
        List<BaseEntity> content;
        if (filterText == null || filterText.isBlank()) {
            content = recycleService.getRecycle();
        } else {
            content = recycleService.getContent(filterText);
        }
        grid.setItems(content);
        entitiesCount = content.size();
        updateFooter();
    }

    private void deleteForever(BaseEntity entity) {
        try {
            recycleService.deleteForever(entity);
        } catch (Exception e) {
            log.error("Ошибка при попытке удаления объекта {}, объект еще используется. Session ID={}",
                    entity.getServiceDeskNumber(), sessionService.getSessionId()
            );
            NotificationSupport.showError("Этот объект еще используется.");
            getGridData();
            return;
        }
        getGridData();
        NotificationSupport.showInfo("Объект уничножен");
    }

    private void restore(BaseEntity entity) {
        recycleService.restore(entity);
        getGridData();
        NotificationSupport.showInfo("Объект восстановлен");
    }
}
