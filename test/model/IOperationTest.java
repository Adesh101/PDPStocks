package model;

import static junit.framework.TestCase.assertEquals;

import model.operation.IOperation;
import model.operation.Operation;
import model.plot.ILineChart;
import model.plot.LineChart;
import model.portfolio.FlexiblePortfolio;
import model.portfolio.IInflexiblePortfolio;
import model.portfolio.InflexiblePortfolio;
import model.stocks.IStocks;
import model.stocks.Stocks;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the operation model.
 */
public class IOperationTest {

  private IStocks stocks;
  private IOperation operation;
  private IInflexiblePortfolio inflexiblePortfolio;
  private FlexiblePortfolio flexiblePortfolio;
  private ILineChart lineChart;

  @Before
  public void setUp() {
    this.stocks = new Stocks();
    this.inflexiblePortfolio = new InflexiblePortfolio();
    this.flexiblePortfolio = new FlexiblePortfolio();
    this.lineChart = new LineChart();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
  }

  @Test
  public void checkCreateNewPortfolio() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    operation.createNewPortfolio("College Savings");
    assertEquals(true, operation.checkPortfolioAlreadyExists("College Savings"));
  }

  @Test
  public void checkaddStockToPortfolio() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    operation.createNewPortfolio("College Savings");
    operation.addStockToPortfolio("College Savings", "GOOG", 100, 89.90);
    assertEquals("Portfolio : College Savings\n"
        + "TICK - QTY - PRICE - TOTAL \n"
        + "GOOG - 100 - 89.9 - 8990.0", operation.getPortfolioComposition("College Savings"));
  }

  @Test
  public void checkSaveFileToCSV() throws IOException {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    HashMap<String, HashMap<String, List<String>>> portfolios = new HashMap<>();
    portfolios.put("College Savings", new HashMap<String, List<String>>());
    portfolios.get("College Savings").put("GOOG", new ArrayList<String>());
    portfolios.get("College Savings").get("GOOG").add(0, "10");
    this.operation.isTickerValid("GOOG");
    portfolios.get("College Savings").put("META", new ArrayList<String>());
    portfolios.get("College Savings").get("META").add(0, "10");
    operation.isTickerValid("META");
    operation.writeToCSV(String.valueOf("College Savings"));

    BufferedReader br = new BufferedReader(new FileReader("stonks.csv"));
    String line = "";
    StringBuilder finalResult = new StringBuilder();
    while ((line = br.readLine()) != null) {
      finalResult.append(line);
    }
    assertEquals(finalResult.toString(), "Portfolio Name,College SavingsStock,"
        + "Quantity,Price,Total,GOOG,10,87.07,META,10,90.54,");
  }


  @Test
  public void checkGetPortfolio() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    operation.createNewPortfolio("College Savings");
    assertEquals(true, operation.checkPortfolioAlreadyExists("College Savings"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void checkPortfolioAlreadyExists() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    operation.createNewPortfolio("College Savings");
    operation.createNewPortfolio("College Savings");
    assertEquals("PORTFOLIO ALREADY PRESENT. ADD STOCKS.",
        operation.checkPortfolioAlreadyExists("College Savings"));
  }

  @Test
  public void checkGetExistingPortfolios() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    operation.createNewPortfolio("College Savings");
    operation.createNewPortfolio("Retirement Fund");
    assertEquals("College Savings\n"
        + "Retirement Fund\n", operation.getExistingPortfolios());
  }

  @Test(expected = IllegalArgumentException.class)
  public void checkGetExistingPortfoliosWith0PortfoliosCreated() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    operation.getExistingPortfolios();
  }

  @Test
  public void checkReadFromFile() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    assertEquals("PORTFOLIO ABCD SUCCESSFULLY CREATED.", operation.readFromFile("./stocks.csv"));
  }

  @Test
  public void checkGetPortfolioComposition() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    operation.createNewPortfolio("College Savings");
    operation.addStockToPortfolio("College Savings", "GOOG", 100, 89.90);
    assertEquals("Portfolio : College Savings\n"
        + "TICK - QTY - PRICE - TOTAL \n"
        + "GOOG - 100 - 89.9 - 8990.0", operation.getPortfolioComposition("College Savings"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void checkGetPortfolioByDateInvalidDate() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    operation.createNewPortfolio("College Savings");
    operation.addStockToPortfolio("College Savings", "GOOG", 100, 89.90);
    assertEquals("", operation.getPortfolioByDate("College Savings", "2022312-1032-2931"));
  }

  @Test
  public void testAddStocks() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    operation.createFlexiblePortfolio("College Savings", "2022-01-01");
    operation.addStockToFlexiblePortfolio("College Savings", "GOOG", 100, 90, "2022-09-13", 12);
    assertEquals("TICK - QTY \n" + "GOOG - 100",
        operation.getPortfolioComposition("College Savings"));
    operation.addStockToFlexiblePortfolio("College Savings", "AAPL", 345, 32, "2022-09-14", 15);
    assertEquals("TICK - QTY \n"
        + "GOOG - 100 - AAPL - 345", operation.getPortfolioComposition("College Savings"));
    operation.addStockToFlexiblePortfolio("College Savings", "GOOG", 120, 92, "2022-09-15", 12);
    assertEquals("", operation.getPortfolioComposition("College Savings"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddStockInvalidDate() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    operation.createFlexiblePortfolio("College Savings", "2022-01-01");
    operation.addStockToFlexiblePortfolio("College Savings", "GOOG", 50, 80, "2022-319-13", 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddStockBeforeCreationDate() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    operation.createFlexiblePortfolio("College Savings", "2022-01-01");
    operation.addStockToFlexiblePortfolio("College Savings", "GOOG", 50, 80, "2021-09-13", 10);

  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddStockFutureDate() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    operation.createFlexiblePortfolio("College Savings", "2022-01-01");
    operation.addStockToFlexiblePortfolio("College Savings", "GOOG", 50, 80, "2023-09-13", 10);
    operation.sellStock("College Savings", "GOOG", 55, 45, "2022-12-14", 15);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddStockInflexiblePortfolioSecondTime() {
    this.stocks = stocks;
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    operation.createLockedPortfolio("Locked Portfolio");
    operation.addStockToInFlexiblePortfolio("Locked Portfolio", "GOOG", 123, 12);
    operation.addStockToInFlexiblePortfolio("Locked Portfolio", "AAPL", 123, 12);
  }

  @Test
  public void testSellStocks() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    operation.createFlexiblePortfolio("College Savings", "2022-01-01");
    operation.addStockToFlexiblePortfolio("College Savings", "GOOG", 100, 90, "2022-09-13", 12);
    operation.sellStock("College Savings", "GOOG", 40, 80, "2022-09-13", 20);
    assertEquals("TICK - QTY \n" + "GOOG - 60",
        operation.getPortfolioComposition("College Savings"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStockQuantityMoreThanExistingQuantity() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    operation.createFlexiblePortfolio("College Savings", "2022-01-01");
    operation.addStockToFlexiblePortfolio("College Savings", "GOOG", 50, 80, "2022-09-13", 10);
    operation.sellStock("College Savings", "GOOG", 55, 45, "2022-09-14", 15);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStockInvalidDate() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    operation.createFlexiblePortfolio("College Savings", "2022-01-01");
    operation.addStockToFlexiblePortfolio("College Savings", "GOOG", 50, 80, "2022-09-13", 10);
    operation.sellStock("College Savings", "GOOG", 55, 45, "2022-19-14", 15);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStockDateBeforePurchaseDate() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    operation.createFlexiblePortfolio("College Savings", "2022-01-01");
    operation.addStockToFlexiblePortfolio("College Savings", "GOOG", 50, 80, "2022-09-13", 10);
    operation.sellStock("College Savings", "GOOG", 55, 45, "2022-09-12", 15);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStockBeforeCreationDate() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    operation.createFlexiblePortfolio("College Savings", "2022-01-01");
    operation.addStockToFlexiblePortfolio("College Savings", "GOOG", 50, 80, "2022-09-13", 10);
    operation.sellStock("College Savings", "GOOG", 55, 45, "2021-11-14", 15);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStockFutureDate() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    operation.createFlexiblePortfolio("College Savings", "2022-01-01");
    operation.addStockToFlexiblePortfolio("College Savings", "GOOG", 50, 80, "2022-09-13", 10);
    operation.sellStock("College Savings", "GOOG", 55, 45, "2022-12-14", 15);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStockInflexiblePortfolio() {
    this.stocks = stocks;
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    operation.createLockedPortfolio("Locked Portfolio");
    operation.addStockToInFlexiblePortfolio("Locked Portfolio", "GOOG", 123, 12);
    operation.sellStock("Locked Portfolio", "GOOG", 23, 32, "2022-11-17", 42);
  }

  @Test
  public void testCostBasis() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    operation.createFlexiblePortfolio("College Savings", "2022-01-01");
    operation.addStockToFlexiblePortfolio("College Savings", "GOOG", 50, 80, "2022-09-13", 10);
    assertEquals(4010.0, operation.costBasisByDate("College Savings", "2022-09-14"));
  }

  @Test
  public void testCostBasisInvalidDate() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    operation.createFlexiblePortfolio("College Savings", "2022-01-01");
    operation.addStockToFlexiblePortfolio("College Savings", "GOOG", 50, 80, "2022-09-13", 10);
    assertEquals(4010.0, operation.costBasisByDate("College Savings", "2022-09-14"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCostBasisBeforeCreationDate() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    operation.createFlexiblePortfolio("College Savings", "2022-01-01");
    operation.addStockToFlexiblePortfolio("College Savings", "GOOG", 50, 80, "2020-09-13", 10);

  }

  @Test(expected = IllegalArgumentException.class)
  public void testCostBasisFutureDate() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    operation.createFlexiblePortfolio("College Savings", "2022-01-01");
    operation.addStockToFlexiblePortfolio("College Savings", "GOOG", 50, 80, "2042-09-13", 10);
  }

  @Test
  public void testGetPortfolioValueByDateFlexible() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    operation.createFlexiblePortfolio("College Savings", "2022-01-01");
    operation.addStockToFlexiblePortfolio("College Savings", "GOOG", 50, 80, "2022-09-13", 10);
    assertEquals(105.31, operation.getPortfolioByDate("College Savings", "2022-09-14"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPortfolioValueByDateFlexibleInvalidDate() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    operation.createFlexiblePortfolio("College Savings", "2022-01-01");
    operation.addStockToFlexiblePortfolio("College Savings", "GOOG", 50, 80, "2022-09-13", 10);
    operation.getPortfolioByDate("College Savings", "201231-123141-124124");
  }

  @Test
  public void testGetPortfolioValueByDateFlexibleDateBeforeCreationDate() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    operation.createFlexiblePortfolio("College Savings", "2022-01-01");
    operation.addStockToFlexiblePortfolio("College Savings", "GOOG", 50, 80, "2022-09-13", 10);
    assertEquals(0.0, operation.getPortfolioByDate("College Savings", "2021-01-01"));

  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetPortfolioValueByDateFlexibleFutureDate() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    operation.createFlexiblePortfolio("College Savings", "2022-01-01");
    operation.addStockToFlexiblePortfolio("College Savings", "GOOG", 50, 80, "2052-09-13", 10);

  }

  @Test
  public void testCommissionFee() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    operation.createFlexiblePortfolio("College Savings", "2022-01-01");
    operation.addStockToFlexiblePortfolio("College Savings", "GOOG", 50, 80, "2022-09-13", 10);
    assertEquals(4010.0, operation.costBasisByDate("College Savings", "2022-09-14"));
    operation.addStockToFlexiblePortfolio("College Savings", "AAPL", 102, 40, "2022-09-15", 20);
    assertEquals(8110.0, operation.costBasisByDate("College Savings", "2022-09-16"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeCommissionFee() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    operation.createFlexiblePortfolio("College Savings", "2022-01-01");
    operation.addStockToFlexiblePortfolio("College Savings", "GOOG", 50, 80, "2022-09-13", -10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPerformanceGraphInvalidDates() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    operation.createFlexiblePortfolio("College Savings", "2022-01-01");
    operation.addStockToFlexiblePortfolio("College Savings", "GOOG", 50, 80, "2022-09-13", 20);
    operation.getGraph("College Savings", "2023422-09-142", "2022-1123-15");

  }

  @Test(expected = IllegalArgumentException.class)
  public void testPerformanceGraphStartDateBeforeCreationDate() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    operation.createFlexiblePortfolio("College Savings", "2022-01-01");
    operation.addStockToFlexiblePortfolio("College Savings", "GOOG", 50, 80, "2022-09-13", 20);
    operation.getGraph("College Savings", "2012-09-14", "2022-11-15");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPerformanceGraphStartDateFutureDate() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    operation.createFlexiblePortfolio("College Savings", "2022-01-01");
    operation.addStockToFlexiblePortfolio("College Savings", "GOOG", 50, 80, "2022-09-13", 20);
    operation.getGraph("College Savings", "2042-09-14", "2022-11-15");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPerformaceGraphEndDateBeforeCreationDate() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    operation.createFlexiblePortfolio("College Savings", "2022-01-01");
    operation.addStockToFlexiblePortfolio("College Savings", "GOOG", 50, 80, "2022-09-13", 20);
    operation.getGraph("College Savings", "2022-09-14", "2021-11-15");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPerformanceGraphEndDateFutureDate() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,
        this.lineChart);
    operation.createFlexiblePortfolio("College Savings", "2022-01-01");
    operation.addStockToFlexiblePortfolio("College Savings", "GOOG", 50, 80, "2022-09-13", 20);
    operation.getGraph("College Savings", "2022-09-14", "2032-11-15");
  }
}


