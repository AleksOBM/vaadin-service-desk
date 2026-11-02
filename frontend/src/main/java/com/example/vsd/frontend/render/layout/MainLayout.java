package com.example.vsd.frontend.render.layout;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;

@SuppressWarnings("unused")
@Layout
@AnonymousAllowed
public final class MainLayout extends AppLayout {

	private final AuthenticationContext authContext;

	MainLayout(AuthenticationContext authContext) {
		this.authContext = authContext;
		setPrimarySection(Section.DRAWER);

		var drawerContent = new VerticalLayout();
		drawerContent.setSizeFull();
		drawerContent.setPadding(false);
		drawerContent.setSpacing(false);

		var header = createHeader();
		var scroller = new Scroller(createSideNav());
		scroller.setSizeFull();
		scroller.getStyle().setFlexGrow("1");

		var userSection = createUserSection();

		drawerContent.add(header, scroller, userSection);
		drawerContent.setFlexGrow(1, scroller);

		addToDrawer(drawerContent);
	}

	private Component createUserSection() {
		var button = new Button();
//		button.setWidth(15, Unit.EM);
		button.setWidthFull();

		if (authContext.isAuthenticated()) {
			var username = authContext.getAuthenticatedUser(UserDetails.class)
					.map(UserDetails::getUsername)
					.orElse("");

			var userIcon = new Icon(VaadinIcon.USER);
			var nameLabel = new Span(username);
			nameLabel.addClassName("username-label");

			var nameRow = new HorizontalLayout(userIcon, nameLabel);
			nameRow.setAlignItems(FlexComponent.Alignment.CENTER);
			nameRow.setWidthFull();
			nameRow.setHeight(3, Unit.EM);
			nameRow.addClassName("username-box");

			button.setText("Выйти");
			button.setIcon(new Icon(VaadinIcon.SIGN_OUT));
			button.addClickListener(event -> authContext.logout());

			var userSection = new VerticalLayout(nameRow, button);
			userSection.setWidthFull();
			userSection.setPadding(false);
			userSection.setSpacing(false);
			userSection.setAlignItems(FlexComponent.Alignment.STRETCH);

			return userSection;
		} else {
			button.setText("Войти");
			button.setIcon(new Icon(VaadinIcon.SIGN_IN));
			button.addClickListener(event ->
					getUI().ifPresent(ui -> ui.getPage().setLocation("/login"))
			);

			var enterSection = new VerticalLayout(button);
			enterSection.setWidthFull();
			enterSection.setPadding(false);
			enterSection.setSpacing(true);
			enterSection.setAlignItems(FlexComponent.Alignment.STRETCH);
			return enterSection;
		}
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

		boolean isAuthenticated = authContext.isAuthenticated();

		MenuConfiguration.getMenuEntries().forEach(entry -> {
			boolean isPublicEntry = entry.path().equals("/") || entry.path().equals("/about");
			if (isAuthenticated || isPublicEntry) {
				nav.addItem(createSideNavItem(entry));
			}
		});
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
