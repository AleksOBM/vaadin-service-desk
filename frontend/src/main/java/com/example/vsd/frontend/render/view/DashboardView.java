package com.example.vsd.frontend.render.view;

import com.example.vsd.frontend.grpc.client.OrderService;
import com.example.vsd.frontend.model.Order;
import com.example.vsd.frontend.render.template.BaseView;
import com.flowingcode.addons.ycalendar.MonthCalendar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.RouteScope;
import com.vaadin.flow.spring.annotation.SpringComponent;
import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import jakarta.annotation.security.PermitAll;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

@PermitAll
@RouteScope
@SpringComponent
@Route(value = "dashboard")
@PageTitle("Dashboard")
@JsModule("./scripts/dashboard-calendar.js")
@Menu(order = 1, icon = "vaadin:dashboard", title = "Календарь")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DashboardView extends BaseView {

	OrderService orderService;

	@SuppressWarnings("unused")
	public DashboardView(OrderService orderService) {
		super("Дашборд");
		this.orderService = orderService;
		filterText.setVisible(false);
		createButton.setVisible(false);
		updateButton.setVisible(false);
		deleteButton.setVisible(false);
		setSizeFull();
		getStyle().set("min-width", "0").set("min-height", "0");

		Div calendarWrapper = new Div();
		calendarWrapper.addClassName("calendar-wrapper");
		calendarWrapper.setWidthFull();
		calendarWrapper.getStyle()
				.set("flex-grow", "1")
				.set("min-width", "0")
				.set("min-height", "0")
				.set("container-type", "size");
		MonthCalendar calendar = new MonthCalendar(YearMonth.now());
		HorizontalLayout yearNavigation = new HorizontalLayout();
		yearNavigation.addClassName("calendar-navigation");
		yearNavigation.setDefaultVerticalComponentAlignment(
				HorizontalLayout.Alignment.CENTER);
		IntegerField yearField = new IntegerField();
		yearField.setStepButtonsVisible(true);
		yearField.setStep(1);
		yearField.setValue(calendar.getYearMonth().getYear());
		yearField.setWidth("8rem");
		yearField.addClassName("calendar-year-field");

		HorizontalLayout monthNavigation = new HorizontalLayout();
		monthNavigation.addClassNames("calendar-navigation", "month-navigation");
		monthNavigation.setDefaultVerticalComponentAlignment(
				HorizontalLayout.Alignment.CENTER);
		Button previousMonth = new Button(VaadinIcon.ANGLE_LEFT.create());
		previousMonth.setAriaLabel("Предыдущий месяц");
		Button nextMonth = new Button(VaadinIcon.ANGLE_RIGHT.create());
		nextMonth.setAriaLabel("Следующий месяц");
		Div monthLabel = new Div();
		monthLabel.addClassName("calendar-month-label");
		monthNavigation.add(previousMonth, monthLabel, nextMonth);
		HorizontalLayout toolbarNavigation = new HorizontalLayout(monthNavigation, yearNavigation);
		toolbarNavigation.addClassName("calendar-toolbar-navigation");
		toolbarNavigation.setDefaultVerticalComponentAlignment(
				HorizontalLayout.Alignment.CENTER);
		toolbarNavigation.setWrap(true);
		yearNavigation.getStyle().set("padding-bottom", "0");

		Runnable updateNavigation = () -> {
			YearMonth yearMonth = calendar.getYearMonth();
			yearField.setValue(yearMonth.getYear());
			monthLabel.setText(yearMonth.getMonth()
					.getDisplayName(TextStyle.FULL_STANDALONE, Locale.forLanguageTag("ru")));
		};
		previousMonth.addClickListener(event -> {
			calendar.setYearMonth(calendar.getYearMonth().minusMonths(1));
			updateNavigation.run();
		});
		nextMonth.addClickListener(event -> {
			calendar.setYearMonth(calendar.getYearMonth().plusMonths(1));
			updateNavigation.run();
		});
		yearField.addValueChangeListener(event -> {
			Integer year = event.getValue();
			if (year != null) {
				calendar.setYearMonth(YearMonth.of(year, calendar.getMonth()));
				updateNavigation.run();
			}
		});
		yearNavigation.add(yearField);
		updateNavigation.run();
		getToolbar().getContent().add(toolbarNavigation);

		calendar.addAttachListener(event -> calendar.getElement().executeJs(
				"window.dashboardCalendar.initialize(this, $0)", getCalendarOrders().toJson()));
		calendar.setSizeFull();
		calendar.getStyle()
				.set("min-width", "0")
				.set("max-width", "100%")
				.set("overflow-y", "auto")
				.set("overflow-x", "hidden")
				.set("--__month-calendar-header-display", "none");
		calendar.setClassNameGenerator(date -> {
			if (date.equals(LocalDate.now())) {
				return "today";
			}
			return date.getDayOfWeek().getValue() >= 6 ? "weekend" : null;
		});
		calendarWrapper.add(calendar);
		add(calendarWrapper);
		setFlexGrow(1, calendarWrapper);
	}

	private JsonArray getCalendarOrders() {
		JsonArray entries = Json.createArray();
		int index = 0;
		for (Order order : orderService.getAllOrders()) {
			if (order.isCompleted() || order.getClient() == null || order.getClient().getName() == null
					|| order.getStatus() == null) {
				continue;
			}

			index = addCalendarOrder(entries, index, order, order.getStartLine(),
					"in_progress");
			index = addCalendarOrder(entries, index, order, order.getFirstControlLine(),
					"first_control");
			index = addCalendarOrder(entries, index, order, order.getSeondControlLine(),
					"second_control");
			index = addCalendarOrder(entries, index, order, order.getDeadLine(),
					"deadline");
		}
		return entries;
	}

	private int addCalendarOrder(JsonArray entries,
	                             int index,
	                             Order order,
	                             LocalDate date,
	                             String eventStatus) {
		if (date == null) {
			return index;
		}

		JsonObject entry = Json.createObject();
		entry.put("date", date.toString());
		entry.put("client", order.getClient().getName());
		entry.put("status", eventStatus);
		entries.set(index, entry);
		return index + 1;
	}
}
