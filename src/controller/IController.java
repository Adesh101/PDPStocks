package controller;

import model.operation.IOperation;

/**
 * This is an interface for the Controller.
 */
public interface IController {

  /**
   * This is a method starts the program.
   */
  void operate(IOperation operation);
}
