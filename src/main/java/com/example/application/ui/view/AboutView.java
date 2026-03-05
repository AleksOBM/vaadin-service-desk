package com.example.application.ui.view;

import com.example.application.ui.MainLayout;
import com.example.application.ui.templates.BaseView;
import com.vaadin.flow.component.markdown.Markdown;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Route(value = "about",  layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@Menu(order = 7, icon = "vaadin:info-circle", title = "О программе")
public class AboutView extends BaseView {

    public AboutView() {
        super("О программе");

        super.createButton.setVisible(false);
        super.updateButton.setVisible(false);
        super.deleteButton.setVisible(false);
        super.filterText.setVisible(false);

        Markdown markdown = new Markdown(getMarkdownText());
        Scroller scroller = new Scroller(markdown);
        scroller.setWidthFull();

        add(scroller);
    }

    private String getMarkdownText() {
        String fileName = "static/about.md";
        try {
            ClassPathResource resource = new ClassPathResource(fileName);
            return StreamUtils.copyToString(
                    resource.getInputStream(), StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            return """
                    # Service desk
                    <font color="#953734">Sorry, content not found.</font>
                    """;
        }
    }
}
