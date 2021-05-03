package erika.fujitsu.frontend.feedbackform;

import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.EmailValidator;
import erika.fujitsu.backend.entity.PersonEntry;
import erika.fujitsu.backend.service.PersonService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;

import erika.fujitsu.frontend.main.MainView;

import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.component.dependency.CssImport;

import org.vaadin.gatanaso.MultiselectComboBox;

import java.util.Collections;


/**
 * FeedbackFormView class controls the database grid layout
 * as well as the Feedback Sender form.
 * It also validates the data input and then tells the PersonService instance to
 * save the new feedback into the local database.
 *
 * @author Erika Maksimova
 */
@Route(value = "fujitsu_feedback", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Feedback form")
@CssImport("./views/feedbackform/feedbackform-view.css")
public class FeedbackFormView extends Div {

    private final Grid<PersonEntry> grid = new Grid<>(PersonEntry.class, false);

    private final TextField name = new TextField("Name");
    private final EmailField email = new EmailField("Email");
    // A user-made component; extends the official Vaadin Component. Not supported in tests.
    private final MultiselectComboBox<String> categories = new MultiselectComboBox<>();
    private final TextArea text = new TextArea("Text");

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Send");

    private final BeanValidationBinder<PersonEntry> binder;
    private final PersonService personService;
    private PersonEntry personEntry;


    /**
     * FeedbackFormView constructor.
     *
     * @param personService instance is passed as an argument to access/modify the database
     *                      without directly working with the PersonRepository (our database) class.
     */
    public FeedbackFormView(PersonService personService) {
        this.personService = personService;
        addClassName("feedbackform-view");

        // Tie the binder to the PersonEntry class
        binder = new BeanValidationBinder<>(PersonEntry.class);

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        createGridLayout(splitLayout);
        createFeedbackSaverLayout(splitLayout);
        add(splitLayout);

        // Configure Grid
        configureGrid();
        // Update form
        updateGrid();

        // The button "cancel" now clears our form and then refreshes the grid of data once again.
        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        // The button "save" now saves all of the data in the form (if no exceptions were thrown).
        // All of our bound fields are now personEntry variables that get saved into the repository.
        // Then the form clears the form, updates the grid with a new entry and refreshes its columns width.
        // If an exception was raised, nothing happens except a notification saying that something went wrong.
        save.addClickListener(e -> {
            try {
                if (this.personEntry == null) {
                    this.personEntry = new PersonEntry();
                }
                binder.writeBean(this.personEntry);

                personService.save(this.personEntry);
                clearForm();
                updateGrid();
                refreshGrid();
                Notification.show("Thank you for your feedback!");
            } catch (ValidationException validationException) {
                Notification.show("Your feedback is not complete. Please fill all of the fields correctly!");
            }
        });
    }

    /**
     * This method configures the grid.
     * Every column is now "bound" to the PersonEntry variable with the same name.
     */
    private void configureGrid() {
        grid.addClassName("person-entry-grid");
        grid.addColumn("name").setAutoWidth(true);
        grid.addColumn("email").setAutoWidth(true);
        grid.addColumn("categories").setAutoWidth(true);
        grid.addColumn("text").setAutoWidth(true);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();
    }

    /**
     * This method creates the feedback form itself.
     * Every TextField is also configured here, making sure the data input is correctly validated.
     *
     * @param splitLayout: grid + feedback form together
     */
    private void createFeedbackSaverLayout(SplitLayout splitLayout) {
        Div feedbackLayoutDiv = new Div();
        feedbackLayoutDiv.setId("editor-layout");
        Div editorDiv = new Div();
        editorDiv.setId("editor");

        // The headline that is displayed on top of the form.
        HorizontalLayout headlineLayout = new HorizontalLayout();
        headlineLayout.setId("headline");
        headlineLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        headlineLayout.add(new H4("Please give us feedback"));

        feedbackLayoutDiv.add(editorDiv);

        // Making working buttons: data validation
        // Name text field: the full name should contain a whitespace and be minimum 5 letters long.
        name.setClearButtonVisible(true);
        name.setPlaceholder("Your full name");
        binder.forField(name)
                .withValidator(n -> n.contains(" "), "Please enter your full (first and last) name!")
                .withValidator(n -> n.length() >= 5, "Your full name should be at least four letters!")
                .bind(PersonEntry::getName, PersonEntry::setName);
        // Email field: should be the correct email format
        email.setClearButtonVisible(true);
        email.setPlaceholder("Your email address");
        email.setErrorMessage("Please enter a valid email address!");
        binder.forField(email)
                .withValidator(new EmailValidator("Please enter a valid email address!"))
                .bind(PersonEntry::getEmail, PersonEntry::setEmail);
        // Categories: a category should be chosen!
        categories.setLabel("Application categories");
        categories.setItems("Patients portal", "Doctors portal",
                "Registration", "Virtual visit",
                "Open KM", "Microsoft SharePoint");
        categories.setPlaceholder("Select one or more category");
        categories.setErrorMessage("Please choose a category");
        categories.setClearButtonVisible(true);
        binder.forField(categories)
                .asRequired("Please choose at least one category")
                .bind(personEntry -> Collections.singleton(personEntry.getCategories()),
                        (personEntry, formValue) -> personEntry.setCategories(String.join(", ", formValue)));
        // Text field: at least something should be typed.
        text.setPlaceholder("Your feedback here");
        text.getStyle().set("minHeight", "100px");
        text.setHelperText("Here you can share what you've liked and what can be improved");
        binder.forField(text)
                .asRequired("The feedback cannot be empty")
                .bind(PersonEntry::getText, PersonEntry::setText);


        FormLayout formLayout = new FormLayout();
        Component[] fields = new Component[]{name, email, categories, text};

        for (Component field : fields) {
            ((HasStyle) field).addClassName("full-width");
        }
        formLayout.add(fields);
        editorDiv.add(headlineLayout, formLayout);
        createButtonLayout(feedbackLayoutDiv);

        splitLayout.addToSecondary(feedbackLayoutDiv);
    }

    /**
     * Create a new layout for correct button placement.
     *
     * @param editorLayoutDiv our feedback form layout
     */
    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setId("button-layout");
        buttonLayout.setWidthFull();
        buttonLayout.setSpacing(true);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    /**
     * Create a new layout for our grid of data stored.
     *
     * @param splitLayout our main layout (grid + feedback form together)
     */
    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    /**
     * Update the grid.
     * This method adds all of the current data stored to the grid.
     */
    private void updateGrid() {
        grid.setItems(personService.findAll());
        grid.recalculateColumnWidths();
    }

    /**
     * This method refreshes the grid.
     */
    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    /**
     * This method once again makes this.personEntry == null, which effectively clears our form.
     */
    private void clearForm() {
        setPersonEntry(null);
    }

    /**
     * This method sets this.personEntry to the passed value.
     * The binder then reads the entry, all of its variables bound tot he fields.
     *
     * @param value of the entry
     */
    public void setPersonEntry(PersonEntry value) {
        this.personEntry = value;
        binder.readBean(this.personEntry);
    }
}
