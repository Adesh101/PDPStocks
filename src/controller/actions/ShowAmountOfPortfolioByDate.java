package controller.actions;

import model.operation.IOperation;

/**
 * This class fetches the total amount of a particular portfolio as per the passed date.
 */
public class ShowAmountOfPortfolioByDate implements IActions {

  private String portfolioName;
  private String date;

  public ShowAmountOfPortfolioByDate(String portfolioName, String date) {
    this.portfolioName = portfolioName;
    this.date = date;
  }

  @Override
  public String operate(IOperation operation) {
    return "VALUE OF " + this.portfolioName + " ON " + this.date + " IS : "
        + operation.getPortfolioByDate(this.portfolioName, this.date) + "$";
  }
}
