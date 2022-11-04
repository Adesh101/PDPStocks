package controller.actions;

import model.operation.IOperation;

/**
 * This is a public interface for the actions to be performed.
 */
public interface IActions {

  /**
   * This is a method to execute the actions.
   */
  String operate(IOperation operation);
}
