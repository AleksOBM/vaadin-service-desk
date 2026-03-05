package com.example.application.ui.view;

import com.example.application.backend.service.DashboardService;
import com.example.application.ui.charts.ServiceDeskChart;
import com.example.application.ui.templates.BaseView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Route(value = "dashboard")
@PageTitle("Dashboard")
@Menu(order = 1, icon = "vaadin:dashboard", title = "Диаграмма")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DashboardView extends BaseView {

    // todo: добавить диаграмму метрик, календарь и/или диаграмму Ганта

    DashboardService dashboardService;
    ServiceDeskChart chart = new ServiceDeskChart(ServiceDeskChart.Type.LINE);

    public DashboardView(DashboardService dashboardService) {
        super("Дашборд");
        addClassName("dashboard-view");
        this.dashboardService = dashboardService;
        this.filterText.setVisible(false);
        this.createButton.setVisible(false);
        this.deleteButton.setVisible(false);
        this.updateButton.setVisible(false);

//        add(chartsRow());
//        refresh();
    }

    private Component chartsRow() {
        chart.setWidthFull();
        chart.setHeight(320, Unit.PIXELS);

        VerticalLayout left = new VerticalLayout(new Span("График"), chart);
        left.setPadding(false);
        left.setSpacing(true);
        left.setWidthFull();

        VerticalLayout row = new VerticalLayout(left);
        row.setWidthFull();
        row.setSpacing(true);
        row.setFlexGrow(1, left);

        return row;
    }

    private void refresh() {
        LocalDate today = LocalDate.now();

        // todo
//        DashboardKpis kpis = dashboardService.getKpis(today);
//        revenueToday.setText(formatMoney(kpis.revenueToday()));
//        revenueThisMonth.setText(formatMoney(kpis.revenueThisMonth()));
//        salesToday.setText(String.valueOf(kpis.salesCountToday()));
//        lowStockCount.setText(String.valueOf(kpis.lowStockProductCount()));
//
//        SalesSeries salesSeries = dashboardService.getDailySalesSeries(today.minusDays(13), today);
//        salesLineChart.setSeries(
//                salesSeries.labels(),
//                "Revenue",
//                salesSeries.values()
//        );
    }
}
