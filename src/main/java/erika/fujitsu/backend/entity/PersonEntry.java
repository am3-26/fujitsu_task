package erika.fujitsu.backend.entity;

import javax.persistence.Entity;


/**
 * This is a PersonEntry class. Every instance is a feedback stored from the form.
 * There are four fields in the form --> four variables: name, email, categories and text.
 * Each instance is stored in the PersonRepository upon creation
 * (via personService instance in theFeedbackFromView class).
 *
 * @author Erika Maksimova
 */
@Entity
public class PersonEntry extends AbstractEntity {

    private String name;
    private String email;
    private String categories;
    private String text;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
