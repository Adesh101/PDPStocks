package controller;

import controller.actions.AddStockToFlexiblePortfolio;
import controller.actions.CostBasisByDate;
import controller.actions.CreateFlexiblePortfolio;
import controller.actions.IActions;
import controller.actions.AddStockToPortfolio;
import controller.actions.CreateNewPortfolio;
import controller.actions.CreateNewPortfolioCSV;
import controller.actions.SellStock;
import controller.actions.ShowAmountOfPortfolioByDate;
import controller.actions.ShowComposition;
import controller.actions.ShowExistingPortfolios;
import java.util.TreeMap;
import model.operation.IOperation;
import view.IView;

/**
 * This is the main controller of the program. This controller will handle input and pass them to
 * the appropriate child controllers.
 */
public class Controller implements IController {

  private IOperation operation;
  private IView view;
  private IActions action;

  /**
   * Constructor for the main controller.
   *
   * @param: operation
   * @param: view
   * @throws: IllegalArgumentException
   */
  public Controller(IOperation operation, IView view) throws IllegalArgumentException {
    if (operation == null || view == null) {
      throw new IllegalArgumentException();
    }
    this.operation = operation;
    this.view = view;
  }

  @Override
  public void operate(IOperation operation) {
    view.showWelcomeMessage();
    String menuOption = "";
    boolean flag = false;
    while (!flag) {
      menuOption = menuHelper();
      try {
        switch (menuOption) {
          case "1":
            createPortfolioHelper();
            break;
          case "2":
            addStocksToFlexiblePortfolioHelper();
            break;
          case "3":
            sellStockHelper();
            break;
          case "4":
            showExistingPortfolioHelper();
            break;
          case "5":
            showAmountByDateHelper();
            break;
          case "6":
            showCompositionHelper();
            break;
          case "7":
            showCostBasisByDateHelper();
            break;
          case "8":
            showGraph();
          case "9":
            flag = true;
            break;
          default:
            System.out.println("INVALID RESPONSE.");
        }
      } catch (Exception ex) {
        view.displayInput(ex.getMessage());
      }
    }
  }

  @Override
  public void createPortfolioHelper() {
    String typeOption = view.showPortfolioTypeMenu();
    if (typeOption.equals("1")) {
      createFlexiblePortfolio();
    } else if (typeOption.equals("2")) {
      createInflexiblePortfolio();
    }
  }

  @Override
  public void createFlexiblePortfolio() {
    String option = view.showPortfolioMenuOption();
    if (option.equals("1")) {
      String portfolioName = view.showEnterNewPortfolioName();
      String date = view.showPortfolioCreationDate();
      action = new CreateFlexiblePortfolio(portfolioName, date);
      view.displayInput(action.operate(operation));
      view.showAddStock();
      addStocksHelper(portfolioName);
    } else if (option.equals("2")) {
      createPortfolioCSV();
    }
  }

  @Override
  public void createInflexiblePortfolio() {
    String option = view.showPortfolioMenuOption();
    if (option.equals("1")) {
      String portfolioName = view.showEnterNewPortfolioName();
      String date = view.showPortfolioCreationDate();
      action = new CreateNewPortfolio(portfolioName, date);
      view.displayInput(action.operate(operation));
      view.showAddStock();
      addStocksHelper(portfolioName);
    } else if (option.equals("2")) {
      createPortfolioCSV();
    }
  }

  @Override
  public void showExistingPortfolioHelper() {
    action = new ShowExistingPortfolios();
    view.displayInput(action.operate(operation));
  }

  @Override
  public void showAmountByDateHelper() {
    String portfolioName = view.showEnterNewPortfolioName();
    String date = view.showPortfolioCreationDate();
    action = new ShowAmountOfPortfolioByDate(portfolioName, date);
    view.displayInput(action.operate(operation));
  }

  @Override
  public void showCompositionHelper() {
    String portfolioName = view.showEnterNewPortfolioName();
    action = new ShowComposition(portfolioName);
    view.displayInput(action.operate(operation));
  }

  @Override
  public void showCostBasisByDateHelper() {
    String portfolioName = view.showEnterNewPortfolioName();
    String costBasisDate = view.showCostBasisDate();
    action = new CostBasisByDate(portfolioName, costBasisDate);
    view.displayInput(action.operate(operation));
  }

  @Override
  public void sellStockHelper() {
    String continueSellingOfStocks = "Y";
    String portfolioName = view.showEnterNewPortfolioName();
    String ticker = view.showTicker();
    int quantity = view.showQuantity();
    String sellDate = view.showSellDate();
    while (continueSellingOfStocks.equalsIgnoreCase("Y")) {
      action = new SellStock(portfolioName, ticker, quantity, sellDate);
      view.displayInput(action.operate(operation));
      continueSellingOfStocks = view.showSellConfirmation();
    }
  }

  @Override
  public void createPortfolioCSV() {
    String fileName = view.showFileName();
    action = new CreateNewPortfolioCSV(fileName);
    view.displayInput(action.operate(operation));
  }

  @Override
  public void addStocksHelper(String portfolioName) {
    String addStocks = "Y";
    String input = "";
    while (addStocks.equalsIgnoreCase("Y") || addStocks.equalsIgnoreCase("y")) {
      if (operation.checkWhetherFlexible(portfolioName)) {
        String ticker = view.showTicker();
        int quantity = view.showQuantity();
        String buyDate = view.showBuyDate();
        action = new AddStockToFlexiblePortfolio(portfolioName, ticker, quantity, buyDate);
      } else {
        String ticker = view.showTicker();
        int quantity = view.showQuantity();
        action = new AddStockToPortfolio(portfolioName, ticker, quantity);
      }
      view.displayInput(action.operate(operation));
      addStocks = view.showPostConfirmation();
    }
    operation.writeToCSV(input);
  }

  @Override
  public void addStocksToFlexiblePortfolioHelper() {
    String portfolioName = view.showEnterNewPortfolioName();
    addStocksHelper(portfolioName);
  }

  @Override
  public void showGraph() {
    String portfolioName = view.showEnterNewPortfolioName();
    TreeMap<String, Integer> map = operation.getGraph(portfolioName, "2016-11-01", "2022-02-10");
    System.out.println(map);
  }

  @Override
  public String menuHelper() {
    view.showMenu();
    return view.fetchInput();
  }
}
