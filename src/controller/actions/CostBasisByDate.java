package controller.actions;

import model.operation.IOperation;

/**
 * Class to show cost basis.
 */
public class CostBasisByDate implements IActions {

  private String portfolioName;
  private String date;

  public CostBasisByDate(String portfolioName, String date) {
    this.portfolioName = portfolioName;
    this.date = date;
  }

  @Override
  public String operate(IOperation operation) {
    return "COST BASIS OF PORTFOLIO " + portfolioName + " IS " + operation.costBasisByDate(
        portfolioName, date);
  }
}
