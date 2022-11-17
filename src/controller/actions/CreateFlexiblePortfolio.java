package controller.actions;

import model.operation.IOperation;

/**
 * Class to create flexible portfolio.
 */
public class CreateFlexiblePortfolio implements IActions {

  private String portfolioName;
  private String date;

  public CreateFlexiblePortfolio(String portfolioName, String date) {
    this.portfolioName = portfolioName;
    this.date = date;
  }

  @Override
  public String operate(IOperation operation) {
    operation.createFlexiblePortfolio(this.portfolioName, this.date);
    return "PORTFOLIO " + this.portfolioName + " SUCCESSFULLY CREATED ON " + this.date + "\n";
  }
}
