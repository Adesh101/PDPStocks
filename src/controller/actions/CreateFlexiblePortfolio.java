package controller.actions;

import model.operation.IOperation;
import model.portfolio.IPortfolio;

public class CreateFlexiblePortfolio implements IActions {
  private IPortfolio portfolio;
  private String portfolioName;
  public CreateFlexiblePortfolio(String portfolioName){
    this.portfolioName=portfolioName;

  }

  @Override
  public String operate(IOperation operation) {
    operation.createFlexiblePortfolio(this.portfolioName);
    return "Portfolio " + this.portfolioName + " successfully created. \n";

  }
}
