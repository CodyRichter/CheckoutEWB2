import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import main.Main;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import static main.Main.guestController;
import static org.junit.Assert.assertTrue;
import static org.loadui.testfx.GuiTest.find;

/**
 * CheckoutEWB2 - PACKAGE_NAME
 *
 * @author LargeScaleTest
 * @version 1.0
 */
public class LargeScaleTest extends ApplicationTest {

    @Override
    public void start (Stage stage) throws Exception {
        Parent mainNode = FXMLLoader.load(Main.class.getResource("Guest.fxml"));
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
    }

    @Before
    public void setUp () throws Exception {
    }

    @After
    public void tearDown () throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }


    //
    // ------------------------------
    // Tests Begin Here
    // ------------------------------
    //

    @Test
    public void testEnglishInput () {
        guestController.createNewGuest();
        TextField t = (TextField) find("#firstName");

        clickOn("#firstName");
        write("Cody");
        assertTrue(t.getText().equals("Cody"));
    }
}
