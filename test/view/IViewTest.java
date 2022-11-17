package view;

import static org.junit.Assert.assertEquals;

import java.io.PrintStream;
import java.io.StringReader;
import org.junit.Before;
import org.junit.Test;

/**
 * Class to test the View Part of MVC.
 */
public class IViewTest {
  private Readable in = new StringReader("");
  private Appendable out;
  private PrintStream out2 = new PrintStream(System.out);


  @Before
  public void setUp() {
    out = new StringBuilder();
  }

  @Test
  public void checkMenuOption() {
    IView view = new View(in, out);
    view.showMenu();
    String expectedString = "----------------------------\n"
        + "          STOCKS\n"
        + "----------------------------\n"
        + "1. CREATE NEW PORTFOLIO\n"
        + "2. ADD STOCKS TO A FLEXIBLE PORTFOLIO\n"
        + "3. SELL STOCKS FROM A FLEXIBLE PORTFOLIO\n"
        + "4. VIEW PORTFOLIO NAMES\n"
        + "5. VIEW PORTFOLIO AMOUNT BY DATE\n"
        + "6. VIEW PORTFOLIO COMPOSITION\n"
        + "7. VIEW COST BASIS OF A PORTFOLIO FOR A GIVEN DATE\n"
        + "8. VIEW PERFORMANCE GRAPH\n"
        + "9. QUIT\n"
        + "------------------------------\n";
    assertEquals(expectedString, out.toString());
  }
}