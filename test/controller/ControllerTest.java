package controller;

import static org.junit.Assert.assertEquals;

import model.operation.IOperation;
import model.MockModel;
import view.IView;
import view.View;
import java.io.StringReader;
import org.junit.Before;
import org.junit.Test;

/**
 * Class to test Controller.
 */
public class ControllerTest {

  private IOperation model;
  private Controller controller;
  private IView view;
  private Readable in;
  private StringBuilder log;
  private Appendable out;

  @Before
  public void setUp() {
    log = new StringBuilder();
    model = new MockModel(log);
    out = new StringBuilder();

  }

  @Test
  public void testCreateFlexiblePortfolio() {
    in = new StringReader("1\n1\n1\nabc\n2022-01-01\nAAPL\n23\n2022-09-14\n12\nN\n9");
    view = new View(in, out);
    controller = new Controller(model, view);
    controller.operate(model);
    assertEquals("PORTFOLIO NAME: abc  DATE: 2022-01-01\n"
        + "PORTFOLIO NAME: abc\n"
        + "PORTFOLIO NAME: abc\n"
        + "TICKER: AAPL\n"
        + "STOCK NAME: AAPL DATE: 2022-09-14\n", log.toString());

  }

  @Test
  public void testCreateInflexiblePortfolio() {
    in = new StringReader("1\n2\n1\nabc\nAAPL\n23\nN\n9");
    view = new View(in, out);
    controller = new Controller(model, view);
    controller.operate(model);
    assertEquals("PORTFOLIO: abc  CREATED SUCCESSFULLY. ", log.toString());

  }

  @Test
  public void testCreateMultiplePortfolios() {
    in = new StringReader("1\n1\nabc\n1\n1\nbcd\n6");
    view = new View(in, out);
    controller = new Controller(model, view);
    //controller.go();
    controller.operate(model);
    assertEquals("PORTFOLIO: abc  CREATED SUCCESSFULLY. PORTFOLIO: bcd  CREATED SUCCESSFULLY. ",
        log.toString());
  }

  @Test
  public void testCreateMultiplePortfoliosFromConsoleThenDisplay() {

    in = new StringReader("1\n1\nabc\n1\n1\nbcd\n3\n6");
    view = new View(in, out);
    controller = new Controller(model, view);
    controller.operate(model);
    assertEquals(
        "PORTFOLIO: abc  CREATED SUCCESSFULLY. PORTFOLIO: bcd  "
            + "CREATED SUCCESSFULLY. PORTFOLIO LIST:\n",
        log.toString());
    // assertEquals(,out.toString());

  }

  @Test
  public void testAddMultipleStocks() {
    model = new MockModel(log);
    in = new StringReader("1\n1\nabc\n2\nabc\nGOOG\n10\nY\nabc\nAAPL\n10\nY\nabc\nMETA\n10\nN\n6");
    view = new View(in, out);
    controller = new Controller(model, view);
    controller.operate(model);
    assertEquals("PORTFOLIO: abc  CREATED SUCCESSFULLY. PORTFOLIO NAME: abc\n"
        + "TICKER: GOOG\n"
        + "TICKER: GOOG\n"
        + "PORTFOLIO NAME: abc STOCK: GOOG QUANTITY: 10 PRICE: 0.0 \n"
        + "PORTFOLIO NAME: abc\n"
        + "TICKER: AAPL\n"
        + "TICKER: AAPL\n"
        + "PORTFOLIO NAME: abc STOCK: AAPL QUANTITY: 10 PRICE: 0.0 \n"
        + "PORTFOLIO NAME: abc\n"
        + "TICKER: META\n"
        + "TICKER: META\n"
        + "PORTFOLIO NAME: abc STOCK: META QUANTITY: 10 PRICE: 0.0 \n", log.toString());
  }

  @Test
  public void testShowComposition() {
    model = new MockModel(log);
    in = new StringReader(
        "1\n1\nabc\n2\nabc\nGOOG\n10\nY\nabc\nAAPL\n10\nY\n"
            + "abc\nMETA\n10\nN\n5\nabc\n6");
    view = new View(in, out);
    controller = new Controller(model, view);
    controller.operate(model);
    assertEquals("PORTFOLIO: abc  CREATED SUCCESSFULLY. PORTFOLIO NAME: abc\n"
        + "TICKER: GOOG\n"
        + "TICKER: GOOG\n"
        + "PORTFOLIO NAME: abc STOCK: GOOG QUANTITY: 10 PRICE: 0.0 \n"
        + "PORTFOLIO NAME: abc\n"
        + "TICKER: AAPL\n"
        + "TICKER: AAPL\n"
        + "PORTFOLIO NAME: abc STOCK: AAPL QUANTITY: 10 PRICE: 0.0 \n"
        + "PORTFOLIO NAME: abc\n"
        + "TICKER: META\n"
        + "TICKER: META\n"
        + "PORTFOLIO NAME: abc STOCK: META QUANTITY: 10 PRICE: 0.0 \n"
        + "PORTFOLIO COMPOSITION: \n", log.toString());
  }

  @Test
  public void testShowAmountByDate() {
    model = new MockModel(log);
    in = new StringReader(
        "1\n1\nabc\n2\nabc\nGOOG\n10\nY\nabc\nAAPL\n"
            + "10\nY\nabc\nMETA\n10\nN\n4\nabc\n2022-10-10\n6");
    view = new View(in, out);
    controller = new Controller(model, view);
    controller.operate(model);
    assertEquals("PORTFOLIO: abc  CREATED SUCCESSFULLY. PORTFOLIO NAME: abc\n"
            + "TICKER: GOOG\n"
            + "TICKER: GOOG\n"
            + "PORTFOLIO NAME: abc STOCK: GOOG QUANTITY: 10 PRICE: 0.0 \n"
            + "PORTFOLIO NAME: abc\n"
            + "TICKER: AAPL\n"
            + "TICKER: AAPL\n"
            + "PORTFOLIO NAME: abc STOCK: AAPL QUANTITY: 10 PRICE: 0.0 \n"
            + "PORTFOLIO NAME: abc\n"
            + "TICKER: META\n"
            + "TICKER: META\n"
            + "PORTFOLIO NAME: abc STOCK: META QUANTITY: 10 PRICE: 0.0 \n"
            + "PORTFOLIO NAME: abc  DATE: 2022-10-10\n",
        log.toString());
  }
  @Test
  public void testSellStock(){
    model = new MockModel(log);
    in = new StringReader("1\n1\n1\nabc\n2022-01-01\nAAPL\n234\n2022-09-14\n12\nN\n3\nabc\nAAPL\n100\n2022-09-16\n12\nN\n9");
    view = new View(in, out);
    controller = new Controller(model, view);
    controller.operate(model);
    assertEquals("PORTFOLIO NAME: abc  DATE: 2022-01-01\n"
        + "PORTFOLIO NAME: abc\n"
        + "PORTFOLIO NAME: abc\n"
        + "TICKER: AAPL\n"
        + "STOCK NAME: AAPL DATE: 2022-09-14\n"
        + "PORTFOLIO NAME: abc\n"
        + "PORTFOLIO NAME: abc\n",log.toString());
  }
  @Test
  public void testCostBasis(){
    model = new MockModel(log);
    in = new StringReader("1\n1\n1\nabc\n2022-01-01\nAAPL\n234\n2022-09-14\n12\nN\n7\nabc\n2022-11-15\n9");
    view = new View(in, out);
    controller = new Controller(model, view);
    controller.operate(model);
    assertEquals("PORTFOLIO NAME: abc  DATE: 2022-01-01\n"
        + "PORTFOLIO NAME: abc\n"
        + "PORTFOLIO NAME: abc\n"
        + "TICKER: AAPL\n"
        + "STOCK NAME: AAPL DATE: 2022-09-14\n"
        + "PORTFOLIO NAME: abc\n"
        + "PORTFOLIO NAME: abc DATE: 2022-11-15",log.toString());
  }
  @Test
  public void testPerformanceGraph(){
    model = new MockModel(log);
    in = new StringReader("1\n1\n1\nabc\n2022-01-01\nAAPL\n234\n2022-09-14\n12\nN\n8\nabc\n2022-09-15\n2022-11-15\n9");
    view = new View(in, out);
    controller = new Controller(model, view);
    controller.operate(model);
    assertEquals("PORTFOLIO NAME: abc  DATE: 2022-01-01\n"
        + "PORTFOLIO NAME: abc\n"
        + "PORTFOLIO NAME: abc\n"
        + "TICKER: AAPL\n"
        + "STOCK NAME: AAPL DATE: 2022-09-14\n"
        + "PORTFOLIO NAME: abc\n"
        + "PORTFOLIO NAME: abc START DATE: 2022-09-15 END DATE: 2022-11-15\n",log.toString());
  }
}