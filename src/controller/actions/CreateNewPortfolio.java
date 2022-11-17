package controller.actions;

import model.operation.IOperation;

/**
 * This method creates a new portfolio.
 */
public class CreateNewPortfolio implements IActions {
  private String portfolioName;

  public CreateNewPortfolio(String portfolioName) {
    this.portfolioName = portfolioName;
  }

  @Override
  public String operate(IOperation operation) {
    operation.createLockedPortfolio(this.portfolioName);
    return "Portfolio " + this.portfolioName + " successfully created. \n";
  }

}
