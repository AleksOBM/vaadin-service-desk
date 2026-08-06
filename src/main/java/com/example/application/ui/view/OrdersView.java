package com.example.application.ui.view;

import com.example.application.backend.model.Order;
import com.example.application.backend.util.enums.OrderStatus;
import com.example.application.backend.service.AgentService;
import com.example.application.backend.service.ClientService;
import com.example.application.backend.service.OrderService;
import com.example.application.backend.service.RecycleService;
import com.example.application.ui.details.OrdersDetails;
import com.example.application.ui.dialog.OrderDialog;
import com.example.application.ui.template.BaseDialog;
import com.example.application.ui.template.BaseView;
import com.example.application.ui.util.NotificationSupport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.renderer.*;
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

@Route("orders")
@PageTitle("Order List")
@Menu(order = 2, icon = "vaadin:calendar-briefcase", title = "Заявки")
@SpringComponent
@RouteScope
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrdersView extends BaseView {

    // todo: добавить фильтр по статусу
    // todo: добавить постраничный просмотр

    OrderService orderService;
    ClientService clientService;
    AgentService agentService;
    RecycleService recycleService;

    Grid<Order> grid = new Grid<>();

    @Autowired
    public OrdersView(
            OrderService orderService,
            ClientService clientService,
            AgentService agentService,
            RecycleService recycleService
    ) {
        super("Заявки");
        this.orderService = orderService;
        this.clientService = clientService;
        this.agentService = agentService;
        this.recycleService = recycleService;

        addClassName("orders-view");
        configureView();
        add(getOrdersGrid());
        updateGrid();
    }

    private void configureView() {
        filterText.addValueChangeListener(event -> getGridData());
        createButton.addClickListener(click -> createOrder());
        updateButton.addClickListener(click -> updateOrder());
        deleteButton.addClickListener(click -> deleteOrder());
    }

    private void createOrder() {
        var dialog = new BaseDialog<>(
                Order.class,
                "Новая заявка",
                new OrderDialog(clientService.findAll(), agentService.findAll()),
                order -> {
                    orderService.save(order);
                    NotificationSupport.showSuccess("Заявка добавлена.");
                    String text = filterText.getValue();
                    if (text == null || text.isBlank()) {
                        updateGrid();
                    } else {
                        getGridData();
                    }
                });
        dialog.setEntity(new Order());
        dialog.open();
    }

    private void updateOrder() {
        Order oldOrder = grid.asSingleSelect().getValue();
        BaseDialog<Order> dialog = new BaseDialog<>(
                Order.class,
                "Редактировать заявку",
                new OrderDialog(clientService.findAll(), agentService.findAll()),
                order -> {
                    orderService.save(order);
                    NotificationSupport.showSuccess("Заявка изменена.");
                    String text = filterText.getValue();
                    if (text == null || text.isBlank()) {
                        updateGrid();
                    } else {
                        getGridData();
                    }
                });
        dialog.setEntity(oldOrder);
        dialog.open();
    }

    private void deleteOrder() {
        Order order = grid.asSingleSelect().getValue();
        recycleService.markAsDeleted(order);
        getGridData();
        NotificationSupport.showInfo("Заявка удалена.");
    }

    private Grid<Order> getOrdersGrid() {
        grid.addClassNames("order-grid");
        grid.addClassName("big-header-grid");
        grid.setSizeFull();
        grid.getStyle().setMarginTop("20px");
        grid.addThemeVariants(GridVariant.AURA_ROW_STRIPES);
        grid.setEmptyStateText("Данные отсутствуют.");

        grid.addColumn(createToggleDetailsRenderer(grid)).setWidth("45px")
                .setFlexGrow(0).setFrozen(true);
        grid.setDetailsVisibleOnClick(false);
        grid.setItemDetailsRenderer(createPersonDetailsRenderer());

        Grid.Column<Order> sdColumn = grid.addComponentColumn(order -> {
            HorizontalLayout layout = new HorizontalLayout();

            Icon progressPoint = VaadinIcon.CIRCLE.create();
            OrderStatus status = orderService.getStatus(order);
            switch (status) {
                case IN_PROGRESS -> progressPoint.setColor("green");
                case FIRST_CONTROL -> progressPoint.setColor("yellow");
                case SECOND_CONTROL -> progressPoint.setColor("red");
                case COMPLETED -> progressPoint.setColor("gray");
            }

            Span name = new Span(order.getServiceDeskNumber());
            layout.add(progressPoint, name);
            layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
            return layout;
        }).setHeader("SD");

        grid.addColumn(order -> order.getClient().getName()).setHeader("Клиент");
        grid.addColumn(Order::getName).setHeader("Работа");

        Grid.Column<Order> statusColumn = grid.addColumn(orderService::getStatus).setHeader("Статус");
        statusColumn.setVisible(false);
        Grid.Column<Order> deadlineColumn = grid.addColumn(Order::getDeadLine).setHeader("Дедлайн");
        deadlineColumn.setVisible(true);

        grid.getColumns().stream().skip(1)
                .forEach(column -> {
                    column.setSortable(true);
                    column.setAutoWidth(true);
                });

        grid.setMultiSort(true);
        GridSortOrder<Order> sortOrder1 = new GridSortOrder<>(statusColumn, SortDirection.DESCENDING);
        GridSortOrder<Order> sortOrder2 = new GridSortOrder<>(deadlineColumn, SortDirection.ASCENDING);
        GridSortOrder<Order> sortOrder3 = new GridSortOrder<>(sdColumn, SortDirection.ASCENDING);
        grid.sort(List.of(sortOrder1, sortOrder2, sortOrder3));

        updateFooter();

        grid.addSelectionListener(event -> {
            boolean enabled = event.getFirstSelectedItem().isPresent();
            updateButton.setEnabled(enabled);
            deleteButton.setEnabled(enabled);
        });

        return grid;
    }

    private ComponentRenderer<OrdersDetails, Order> createPersonDetailsRenderer() {
        return new ComponentRenderer<>(OrdersDetails::new, OrdersDetails::setOrder);
    }

    private Renderer<Order> createToggleDetailsRenderer(Grid<Order> grid) {
        return LitRenderer
                .<Order>of("""
                            <vaadin-button
                                theme="tertiary icon"
                                aria-label="Toggle details"
                                aria-expanded="${model.detailsOpened ? 'true' : 'false'}"
                                @click="${handleClick}"
                            >
                                <vaadin-icon
                                .icon="${model.detailsOpened ? 'lumo:angle-down' : 'lumo:angle-right'}"
                                ></vaadin-icon>
                            </vaadin-button>
                        """)
                .withFunction("handleClick",
                        order -> {
                            grid.setDetailsVisible(order, !grid.isDetailsVisible(order));
                            grid.select(order);
                        }
                );
    }

    private void getGridData() {
        String text = super.filterText.getValue();
        if (text == null || text.isBlank()) {
            updateGrid();
            return;
        }
        Collection<Order> content = orderService.getContent(text);
        entitiesCount = content.size();
        grid.setItems(content);
        updateFooter();
    }

    public void updateGrid() {
        Collection<Order> content = orderService.findAll();
        grid.setItems(content);
        entitiesCount = content.size();
        updateFooter();
    }

    private void updateFooter() {
        grid.getColumns().stream().skip(1).findFirst()
                .orElseThrow().setFooter(
                        String.format("Всего %s", entitiesCount)
                );
    }
}
