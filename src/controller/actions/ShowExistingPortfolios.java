package controller.actions;

import model.operation.IOperation;

/**
 * This class will show the existing portfolios.
 */
public class ShowExistingPortfolios implements IActions {
  @Override
  public String operate(IOperation operation) {
    return "LIST OF AVAILABLE PORTFOLIOS: \n" + operation.getExistingPortfolios();
  }
}
