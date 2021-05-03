package erika.fujitsu.it;


import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.formlayout.testbench.FormLayoutElement;
import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.flow.component.textfield.testbench.EmailFieldElement;
import com.vaadin.flow.component.textfield.testbench.TextAreaElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import org.junit.Assert;
import org.junit.Test;

public class FeedbackIT extends AbstractTest {
    protected FeedbackIT(String route) {
        super(route);
    }

    @Test
    public void testFormShowsCorrectData() {
        Assert.assertTrue($(GridElement.class).exists());

        // Find the grid
        GridElement grid = $(GridElement.class).first();

        // Store the [name, email, categories, text] values shown
        // in the first row of the grid for later comparison
        String name = grid.getCell(0, 0).getText();
        String email = grid.getCell(0, 1).getText();
        String categories = grid.getCell(0, 2).getText();
        String text = grid.getCell(0, 3).getText();

        Assert.assertEquals("Otto Lakk", name);
        Assert.assertEquals("test@fujitsu.ee", email);
        Assert.assertEquals("Patients portal, Microsoft SharePoint", categories);
        Assert.assertEquals("Feedback text", text);
    }

    @Test
    public void testEnterNewFeedbackWithoutCategories() {
        // Find the grid
        GridElement grid = $(GridElement.class).first();

        // Find the form
        FormLayoutElement form = $(FormLayoutElement.class).first();

        // Add data to the form
        form.$(TextFieldElement.class).first().setValue("Erika Maksimova");
        form.$(EmailFieldElement.class).first().setValue("erika.maksimova@gmail.com");
        // categories = null
        form.$(TextAreaElement.class).first().setValue("This is my feedback.");

        $(ButtonElement.class).first().click();

        Assert.assertNull(grid.getCell(1, 0).getText());
        Assert.assertNull(grid.getCell(1, 1).getText());
        Assert.assertNull(grid.getCell(1, 2).getText());
        Assert.assertNull(grid.getCell(1, 3).getText());
    }
}
