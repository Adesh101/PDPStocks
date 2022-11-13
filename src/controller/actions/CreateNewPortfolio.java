package controller.actions;

import model.operation.IOperation;

/**
 * This method creates a new portfolio.
 */
public class CreateNewPortfolio implements IActions {
  private String portfolioName;
  private String date;

  public CreateNewPortfolio(String portfolioName, String date) {
    this.portfolioName = portfolioName;
    this.date=date;
  }

  @Override
  public String operate(IOperation operation) {
    operation.createLockedPortfolio(this.portfolioName,this.date);
    return "Portfolio " + this.portfolioName + " successfully created. \n";
  }

}
