package view;

/**
 * Interface for view class.
 */
public interface IView {

  void showWelcomeMessage();

  void showError();

  void showMenu();

  String fetchInput();

  String showEnterNewPortfolioName();

  String showTicker();

  int showQuantity();

  String showPostConfirmation();

  String showQuit();

  void displayInput(String input);

  boolean isValidDate(String input);

  String showDate();

  String showStockMenu();

  String showPortfolioMenuOption();

  void showInvalidPortfolioName();

  String showFileName();

  void showInvalidFileError();

  void showOrderCompleted();

  void showMenuMessage();

  void showPortfolioNames(String[] names);

  String showEnterPortfolioToAddStocks();

  void showValidPortfolio();

  void showPortfolioMessage();

  void showStockPortfolio(String[][] composition);

  void showAllPortfolioNames(String names);

  void showTickerError();

  void showPortfolioLockedError();

  void showPortfolioExists();

  void showPortfolioCreatedSuccessfully(String name);

  void showNoPortfoliosPresent();
  String showPortfolioCreationDate();
  String showInflexiblePortfolioMenu();
  String showPortfolioTypeMenu();
  String showSellConfirmation();
  double showCommissionFee();
  String showBuyDate();
  String showCompositionDat();
  String showCostBasisDate();
  String showSellDate();
}
