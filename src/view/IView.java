package view;

import java.util.TreeMap;

/**
 * Interface for view class.
 */
public interface IView {

  /**
   * A method to print welcome message.
   */

  void showWelcomeMessage();

  /**
   * A method to print error message.
   */

  void showError();

  /**
   * A method to print the menu.
   */

  void showMenu();

  /**
   * A method to get input from the console.
   *
   * @return returns the input
   */

  String fetchInput();

  /**
   * A method to get portfolio name.
   *
   * @return returns portfolio name
   */

  String showEnterNewPortfolioName();

  /**
   * A method to show ticker message.
   *
   * @return returns the ticker
   */

  String showTicker();

  /**
   * A method to show quantity message.
   *
   * @return returns the quantity
   */

  int showQuantity();

  /**
   * A method to show confirmation message.
   *
   * @return returns the confirmation
   */

  String showPostConfirmation();

  /**
   * A method to show quit message.
   *
   * @return returns quit confirmation
   */
  String showQuit();

  /**
   * A method to display message on the console.
   *
   * @param input message to be displayed
   */

  void displayInput(String input);

  /**
   * A method to check if date is valid.
   *
   * @param input date
   * @return returns true if date is valid else false
   */

  boolean isValidDate(String input);

  /**
   * A method to show portfolio menu.
   *
   * @return choice of user
   */

  String showPortfolioMenuOption();

  /**
   * A method to prompt user for the file name.
   *
   * @return file name
   */

  String showFileName();

  /**
   * A method to prompt user for portfolio creation date.
   *
   * @return portfolio creation date
   */

  String showPortfolioCreationDate();

  /**
   * A method to show portfolio types.
   *
   * @return choice of user
   */
  String showPortfolioTypeMenu();

  /**
   * A method to show sell confirmation message.
   *
   * @return confirmation
   */
  String showSellConfirmation();

  /**
   * A method to prompt user for commission fee for the transaction.
   *
   * @return commission fee
   */
  double showCommissionFee();

  /**
   * A method to prompt user for buy date.
   *
   * @return buy date
   */
  String showBuyDate();

  /**
   * A method to prompt user for cost basis date.
   *
   * @return date
   */
  String showCostBasisDate();

  /**
   * A method to prompt user for sell date.
   *
   * @return sell date
   */
  String showSellDate();

  /**
   * A method to add stock.
   */
  void showAddStock();

  /**
   * A method to plot the performance graph as a line chart.
   *
   * @param map          map containing time stamp as the key and number of stars as the value
   * @param porfolioName name of the portfolio
   * @param startDate    start date of the range
   * @param endDate      end date of the range
   * @param scale        scale of the line chart
   */
  void showLineChart(TreeMap<String, Integer> map, String porfolioName,
      String startDate, String endDate, int scale);

  /**
   * A method to get start date of graph.
   *
   * @return start date
   */
  String showGraphStartDate();

  /**
   * A method to get end date of graph.
   *
   * @return end date
   */
  String showGraphEndDate();

  /**
   * A method to get date to calculate portfolio value.
   * @return date
   */
  String showPortfolioValueByDate();
}
