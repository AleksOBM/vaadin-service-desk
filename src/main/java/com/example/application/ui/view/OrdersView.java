package com.example.application.ui.view;

import com.example.application.backend.model.Order;
import com.example.application.backend.service.AgentService;
import com.example.application.backend.service.ClientService;
import com.example.application.backend.service.OrderService;
import com.example.application.ui.details.OrdersDetails;
import com.example.application.ui.dialogs.OrderDialog;
import com.example.application.ui.templates.BaseDialog;
import com.example.application.ui.templates.BaseView;
import com.example.application.ui.util.NotificationSupport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Collection;

@Route("orders")
@PageTitle("Order List")
@Menu(order = 2, icon = "vaadin:calendar-briefcase", title = "Заявки")
public class OrdersView extends BaseView {

    // todo: добавить постраничный просмотр
    // todo: добавить нотификацию при различных действиях
    // todo: добавить логику светофоров, с учетом выходных
    // todo: добавить проверки абсурдных случаев при создании заявки
    // todo: добавить логику отправки новой заявки на почту через MailService
    // todo: добавить логику редактирования, возможно с использованием OrdersDetails

    private final OrderService orderService;
    private final ClientService clientService;
    private final AgentService agentService;

    private final Grid<Order> grid = new Grid<>();

    public OrdersView(
            OrderService orderService,
            ClientService clientService,
            AgentService agentService
    ) {
        super("Заявки");
        this.orderService = orderService;
        this.clientService = clientService;
        this.agentService = agentService;

        addClassName("orders-view");
        configureView();
        add(getOrdersGrid());
    }

    private void configureView() {
        filterText.addValueChangeListener(event -> getGridData());
        createButton.addClickListener(click -> createOrder());
        updateButton.addClickListener(click -> updateOrder());
        deleteButton.addClickListener(click -> deleteOrder());
    }

    private void createOrder() {
        BaseDialog<Order> dialog = new BaseDialog<>(
                Order.class,
                "Новая заявка",
                new OrderDialog(clientService.findAll(), agentService.findAll()),
                order -> {
                    orderService.save(order);
                    NotificationSupport.showSuccess("Заявка добавлена.");
                    if (filterText.getValue() == null) {
                        updateGrid();
                    }
                    getGridData();
                });
        dialog.setEntity(new Order());
        dialog.open();
    }

    private void updateOrder() {
        NotificationSupport.showFunctionNotImplemented();
    }

    private void deleteOrder() {
        Order order = grid.asSingleSelect().getValue();
        orderService.markAsDeleted(order);
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

        grid.addComponentColumn(order -> {
            HorizontalLayout layout = new HorizontalLayout();
            Icon icon = VaadinIcon.CIRCLE.create();
            icon.setColor("green");
            Span name = new Span(order.getServiceDeskNumber());
            layout.add(icon, name);
            layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
            return layout;
        }).setHeader("SD");

        grid.addColumn(order -> order.getClient().getName()).setHeader("Клиент");
        grid.addColumn(Order::getTitle).setHeader("Работа");

        grid.getColumns().stream().skip(1)
                .forEach(column -> {
                    column.setSortable(true);
                    column.setAutoWidth(true);
                });

        grid.getColumns().stream().skip(1).findFirst()
                .orElseThrow().setFooter(createCountFooter());

        grid.addSelectionListener(event -> {
            boolean enabled = event.getFirstSelectedItem().isPresent();
            updateButton.setEnabled(enabled);
            deleteButton.setEnabled(enabled);
        });

        updateGrid();
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
        String value = super.filterText.getValue();

        if (value == null || value.isBlank()) {
            updateGrid();
            return;
        }

        Collection<Order> content = orderService.findAll().stream()
                .filter(order ->
                        order.getTitle().toLowerCase().contains(value.toLowerCase()) ||
                                order.getServiceDeskNumber().toLowerCase().contains(value.toLowerCase())
                )
                .toList();

        grid.setItems(content);
    }

    private void updateGrid() {
        grid.setItems(orderService.findAll().stream()
                .filter(order -> !order.isDeleted()).toList());
    }

    private String createCountFooter() {
        return String.format("Всего %s", orderService.count());
    }
}
