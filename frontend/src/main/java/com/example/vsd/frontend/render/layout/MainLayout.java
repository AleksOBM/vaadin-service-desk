package com.example.vsd.frontend.render.layout;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.jspecify.annotations.NonNull;

@SuppressWarnings("unused")
@Layout
public final class MainLayout extends AppLayout {

	MainLayout() {
		setPrimarySection(Section.DRAWER);
		addToDrawer(createHeader(), new Scroller(createSideNav()));
	}

	@NonNull
	private Component createHeader() {
		var appLogo = new Image("images/icon.ico", "SD");
		appLogo.setHeight(6, Unit.EM);
		var appName = new Span("Service desk");
		appName.getStyle().setFontWeight(Style.FontWeight.BOLD);
		appName.getStyle().setFontSize("16px");

		var header = new VerticalLayout(appLogo, appName);
		header.setAlignItems(FlexComponent.Alignment.CENTER);
		return header;
	}

	@NonNull
	private SideNav createSideNav() {
		var nav = new SideNav();
		nav.addClassNames(LumoUtility.Margin.Horizontal.MEDIUM);
		nav.setMinWidth(15, Unit.EM);
		MenuConfiguration.getMenuEntries().forEach(entry -> nav.addItem(createSideNavItem(entry)));
		return nav;
	}

	@NonNull
	private SideNavItem createSideNavItem(@NonNull MenuEntry menuEntry) {
		if (menuEntry.icon() != null) {
			return new SideNavItem(menuEntry.title(), menuEntry.path(), new Icon(menuEntry.icon()));
		} else {
			return new SideNavItem(menuEntry.title(), menuEntry.path());
		}
	}

}
