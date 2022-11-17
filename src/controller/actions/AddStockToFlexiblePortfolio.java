package controller.actions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import model.operation.IOperation;

/**
 * Class to add stocks.
 */
public class AddStockToFlexiblePortfolio implements IActions {

  String portfolioName;
  int quantity;
  String ticker;
  double price;
  String date;
  Double fee;

  /**
   * Class to add stock to flexible portfolios.
   * @param: portfolioName
   * @param: ticker
   * @param: quantity
   * @param: date
   * @param: commissionFee
   */
  public AddStockToFlexiblePortfolio(String portfolioName, String ticker, int quantity, String date,
      Double commissionFee) {
    this.portfolioName = portfolioName;
    this.ticker = ticker;
    this.quantity = quantity;
    this.price = 0;
    this.date = date;
    this.fee = commissionFee;
  }

  @Override
  public String operate(IOperation operation) {
    if (operation.checkWhetherFlexible(this.portfolioName)) {
      if (operation.isTickerValid(this.ticker)) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.price = Double.parseDouble(operation.callStockAPI(this.ticker, this.date)[3]);
        operation.addStockToFlexiblePortfolio(this.portfolioName, this.ticker, this.quantity,
            this.price, this.date, this.fee);
        return "STOCK " + this.ticker + " WITH QUANTITY " + this.quantity + " ADDED TO "
            + this.portfolioName + " PORTFOLIO.";
      }
      throw new IllegalArgumentException("ENTER A VALID STOCK TICKER.");
    }
    throw new IllegalArgumentException("ENTER A VALID PORTFOLIO NAME.");
  }
}
