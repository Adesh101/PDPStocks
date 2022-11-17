package controller.actions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import model.operation.IOperation;

/**
 * This class executes the function that will add stocks to a particular portfolio.
 */
public class AddStockToPortfolio implements IActions {

  String portfolioName;
  int quantity;
  String ticker;
  double price;

  /**
   * Constructor for AddStockToPortfolio class.
   *
   * @param: portfolioName
   * @param: ticker
   * @param: quantity
   */
  public AddStockToPortfolio(String portfolioName, String ticker, int quantity) {
    this.portfolioName = portfolioName;
    this.ticker = ticker;
    this.quantity = quantity;
    this.price = 0;
  }

  @Override
  public String operate(IOperation operation) {
    if (operation.checkWhetherInflexible(this.portfolioName)) {
      if (operation.isTickerValid(this.ticker)) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.price = Double.parseDouble(operation.callStockAPI(this.ticker,
            dateFormat.format(operation.yesterdaysDate()))[3]);
        operation.addStockToInFlexiblePortfolio(this.portfolioName,
            this.ticker, this.quantity, this.price);
        return "STOCK " + this.ticker + " WITH QUANTITY " + this.quantity + " ADDED TO "
            + this.portfolioName + " PORTFOLIO.";
      }
      throw new IllegalArgumentException("ENTER A VALID STOCK TICKER.");
    }
    throw new IllegalArgumentException("ENTER A VALID PORTFOLIO NAME.");
  }
}

