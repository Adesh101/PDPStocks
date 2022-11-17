package model;

import static junit.framework.TestCase.assertEquals;

import model.operation.IOperation;
import model.operation.Operation;
import model.plot.ILineChart;
import model.portfolio.FlexiblePortfolio;
import model.portfolio.IInflexiblePortfolio;
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
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,this.lineChart);
  }

  @Test
  public void checkCreateNewPortfolio() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,this.lineChart);
    operation.createNewPortfolio("College Savings");
    assertEquals(true, operation.checkPortfolioAlreadyExists("College Savings"));
  }

  @Test
  public void checkaddStockToPortfolio() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,this.lineChart);
    operation.createNewPortfolio("College Savings");
    operation.addStockToPortfolio("College Savings", "GOOG", 100, 89.90);
    assertEquals("Portfolio : College Savings\n"
        + "TICK - QTY - PRICE - TOTAL \n"
        + "GOOG - 100 - 89.9 - 8990.0", operation.getPortfolioComposition("College Savings"));
  }

  @Test
  public void checkSaveFileToCSV() throws IOException {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,this.lineChart);
    HashMap<String, HashMap<String, List<String>>> portfolios = new HashMap<>();
    portfolios.put("College Savings", new HashMap<String, List<String>>());
    portfolios.get("College Savings").put("GOOG", new ArrayList<String>());
    portfolios.get("College Savings").get("GOOG").add(0, "10");
    this.operation.isTickerValid("GOOG");
//    portfolios.get("College Savings").get("GOOG")
//            .add(1, String.valueOf(this.operation.getCurrentPrice("GOOG")));
    portfolios.get("College Savings").put("META", new ArrayList<String>());
    portfolios.get("College Savings").get("META").add(0, "10");
    operation.isTickerValid("META");
//    portfolios.get("College Savings").get("META")
//            .add(1, String.valueOf(this.operation.getCurrentPrice("META")));
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
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,this.lineChart);
    operation.createNewPortfolio("College Savings");
    assertEquals(true, operation.checkPortfolioAlreadyExists("College Savings"));
  }

  @Test (expected = IllegalArgumentException.class)
  public void checkPortfolioAlreadyExists() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,this.lineChart);
    operation.createNewPortfolio("College Savings");
    operation.createNewPortfolio("College Savings");
    assertEquals("PORTFOLIO ALREADY PRESENT. ADD STOCKS.",
        operation.checkPortfolioAlreadyExists("College Savings"));
  }

  @Test
  public void checkGetExistingPortfolios() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,this.lineChart);
    operation.createNewPortfolio("College Savings");
    operation.createNewPortfolio("Retirement Fund");
    assertEquals("College Savings\n"
        + "Retirement Fund\n", operation.getExistingPortfolios());
  }

  @Test (expected = IllegalArgumentException.class)
  public void checkGetExistingPortfoliosWith0PortfoliosCreated() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,this.lineChart);
    operation.getExistingPortfolios();
  }

  @Test
  public void checkGetCurrentPrice() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,this.lineChart);
    operation.createNewPortfolio("College Savings");
    operation.isTickerValid("GOOG");
    //assertEquals(87.07, operation.getCurrentPrice("GOOG"), 0);
  }

  @Test
  public void checkReadFromFile() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,this.lineChart);
    assertEquals("PORTFOLIO ABCD SUCCESSFULLY CREATED.", operation.readFromFile("./stocks.csv"));
  }

  @Test
  public void checkGetPortfolioComposition() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,this.lineChart);
    operation.createNewPortfolio("College Savings");
    operation.addStockToPortfolio("College Savings", "GOOG", 100, 89.90);
    assertEquals("Portfolio : College Savings\n"
        + "TICK - QTY - PRICE - TOTAL \n"
        + "GOOG - 100 - 89.9 - 8990.0", operation.getPortfolioComposition("College Savings"));
  }

  @Test (expected = IllegalArgumentException.class)
  public void checkGetPortfolioByDateInvalidDate() {
    this.stocks = new Stocks();
    this.operation = new Operation(this.inflexiblePortfolio, this.flexiblePortfolio, this.stocks,this.lineChart);
    operation.createNewPortfolio("College Savings");
    operation.addStockToPortfolio("College Savings", "GOOG", 100, 89.90);
    assertEquals("", operation.getPortfolioByDate("College Savings", "2022312-1032-2931"));
  }
}


