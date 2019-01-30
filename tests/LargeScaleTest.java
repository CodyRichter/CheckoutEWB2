import main.Main;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * CheckoutEWB2: LargeScaleTest
 *
 * @author Cody R
 * @version 1.0
 */
public class LargeScaleTest {



    @Test
    public void testDisableNoLoadedData() {
        assertTrue(Main.guestController != null);
    }

}