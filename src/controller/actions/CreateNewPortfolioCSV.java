package controller.actions;

import model.operation.IOperation;

/**
 * This class creates a new portfolio using CSV filetype.
 */
public class CreateNewPortfolioCSV implements IActions {

  private String fileName;

  public CreateNewPortfolioCSV(String fileName) {
    this.fileName = fileName;
  }

  @Override
  public String operate(IOperation operation) {
    return operation.readFromFile(this.fileName);
  }
}
