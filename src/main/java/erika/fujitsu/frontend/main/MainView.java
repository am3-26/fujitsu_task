package erika.fujitsu.frontend.main;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.CssImport;


/**
 * The main view is a website as a whole layout class.
 * This class makes sure Drawer and header layouts are properly configured.
 *
 * @author Erika Maksimova
 */
@PWA(name = "fujitsu", shortName = "fujitsu", enableInstallPrompt = false)
@JsModule("./styles/shared-styles.js")
@CssImport("./views/main/main-view.css")
public class MainView extends AppLayout {

    /**
     * When created, it automatically creates the drawer menu as well as the drawer content and the header.
     */
    public MainView() {
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        addToDrawer(createDrawerContent());
    }


    /**
     * This is the header.
     *
     * @return layout of the horizontal bar above! (With the Drawer button.)
     */
    private Component createHeaderContent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setId("header");
        layout.getThemeList().set("dark", true);
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(new DrawerToggle());
        H1 viewTitle = new H1();
        layout.add(viewTitle);
        return layout;
    }

    /**
     * Create the info that's in the drawer.
     *
     * @return drawer to the left
     */
    private Component createDrawerContent() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);

        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.add(new Image("images/img.png", "fujitsu logo"));
        logoLayout.add(new H1("made by erika m. for fujitsu"));
        layout.add(logoLayout);

        VerticalLayout description = new VerticalLayout();
        description.setId("desc");
        description.setAlignItems(FlexComponent.Alignment.CENTER);
        description.add(new Label("The following web-application is a part of a recruitment and selection " +
                "process for Fujitsu Estonia AS."));
        description.add(new Label("Author: Erika Maksimova"));
        description.add(new Label("April 2021"));
        layout.add(description);

        return layout;
    }
}
