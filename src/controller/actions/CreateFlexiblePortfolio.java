package controller.actions;

import model.operation.IOperation;
import model.portfolio.IFlexiblePortfolio;

public class CreateFlexiblePortfolio implements IActions {
  private String portfolioName;
  private String date;
  public CreateFlexiblePortfolio(String portfolioName, String date){
    this.portfolioName=portfolioName;
    this.date=date;
  }

  @Override
  public String operate(IOperation operation) {
    operation.createFlexiblePortfolio(this.portfolioName,this.date);
    return "Portfolio " + this.portfolioName + " successfully created on date " + this.date+"\n";

  }
}
