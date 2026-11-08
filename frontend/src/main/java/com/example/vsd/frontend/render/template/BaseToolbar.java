package com.example.vsd.frontend.render.template;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public final class BaseToolbar extends Composite<HorizontalLayout> {

	private final H1 title;

	public BaseToolbar(@Nullable String viewTitle, @Nonnull Component... components) {
		var toolbar = getContent();
		toolbar.setPadding(false);
		toolbar.setSpacing(true);
		toolbar.setWrap(true);
		toolbar.setWidthFull();
		toolbar.addClassName(LumoUtility.Border.BOTTOM);

		var drawerToggle = new DrawerToggle();
		drawerToggle.addClassNames(LumoUtility.Margin.NONE);

		title = new H1(viewTitle);
		title.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.Margin.NONE, LumoUtility.FontWeight.LIGHT);

		var toggleAndTitle = new HorizontalLayout(drawerToggle, title);
		toggleAndTitle.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
		toolbar.add(toggleAndTitle);
		toolbar.setFlexGrow(1, toggleAndTitle);

		if (components.length > 0) {
			var actions = new HorizontalLayout(components);
			actions.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
			toolbar.add(actions);
		}
	}

	public void setViewTitle(String viewTitle) {
		title.setText(viewTitle);
	}

}
