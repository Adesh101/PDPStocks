package controller.actions;

import model.operation.IOperation;

/**
 * This class will show composition of a particular portfolio.
 */
public class ShowComposition implements IActions {

  private String portfolioName;

  public ShowComposition(String portfolioName) {
    this.portfolioName = portfolioName;
  }

  @Override
  public String operate(IOperation operation) {
    return "PORTFOLIO COMPOSITION FOR " + this.portfolioName + " IS: \n"
        + operation.getPortfolioComposition(this.portfolioName);
  }
}
