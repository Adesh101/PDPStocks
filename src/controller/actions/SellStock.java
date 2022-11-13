package controller.actions;

import model.operation.IOperation;

public class SellStock implements IActions {
  private String portfolioName;
  private String ticker;
  private int quantity;
  public SellStock(String portfolioName, String ticker, int quantity){
    this.portfolioName=portfolioName;
    this.ticker=ticker;
    this.quantity=quantity;
  }

  @Override
  public String operate(IOperation operation) {
    return null;
    // cant sell if portfolio empty
    // throw error if portfolio is inflexible
  }
}
