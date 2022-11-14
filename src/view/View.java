package view;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;


/**
 * View for the MVC interface.
 */
public class View implements IView {

  private Appendable out;
  private Scanner scanner;

  /**
   * Constructor for View Class.
   * @param: in
   * @param: out
   * @throws: IllegalArgumentException
   */
  public View(Readable in, Appendable out) throws IllegalArgumentException {
    if (in == null || out == null) {
      throw new IllegalArgumentException("I/O CANNOT BE NULL.");
    }
    Readable read = in;
    this.out = out;
    scanner = new Scanner(read);
  }

  @Override
  public String fetchInput() {
    return nextInput();
  }

  /**
   * Private method to fetch the next input from the user.
   * @return: input
   * @throws: IllegalArgumentException
   */
  private String nextInput() throws IllegalArgumentException {
    try {
      return this.scanner.nextLine();
    } catch (Exception e) {
      throw new IllegalArgumentException("INVALID INPUT.");
    }
  }

  @Override
  public void displayInput(String input) {
    try {
      out.append(input).append("\n");
    } catch (IOException e) {
      throw new IllegalArgumentException("INVALID INPUT.");
    }
  }

  @Override
  public boolean isValidDate(String input) {
    String dateFormat = "yyyy-MM-dd";
    if (input.length() != 10)
      return false;
    try {
      DateFormat df = new SimpleDateFormat(dateFormat);
      df.setLenient(false);
      df.parse(input);
      return true;
    } catch (ParseException e) {
      return false;
    }
  }

  @Override
  public String showDate() {
    while (true) {
      displayInput("ENTER DATE TO SEE PORTFOLIO VALUE (FORMAT: YYYY-MM-DD):");
      String date = nextInput();
      if (isValidDate(date)) {
        return date;
      } else {
        displayInput("ENTER DATE IN YYYY-MM-DD FORMAT.");
      }
    }
  }

  @Override
  public String showStockMenu() {
    return null;
  }

  @Override
  public void showWelcomeMessage() {
    StringBuilder sb = new StringBuilder();
    sb.append("Welcome! Enter the number denoting the operation to be performed:");
    displayInput(sb.toString());
  }

  @Override
  public void showError() {
    displayInput("ENTER VALID INPUT.");
  }

  @Override
  public void showMenu() {
    StringBuilder sb = new StringBuilder();
    sb.append("----------------------------\n");
    sb.append("          STOCKS\n");
    sb.append("----------------------------\n");
    sb.append("1. CREATE NEW PORTFOLIO\n");
    sb.append("2. ADD STOCKS TO A PORTFOLIO\n");
    sb.append("3. SELL STOCKS FROM A PORTFOLIO\n");
    sb.append("4. VIEW PORTFOLIO NAMES\n");
    sb.append("5. VIEW PORTFOLIO AMOUNT BY DATE\n");
    sb.append("6. VIEW PORTFOLIO COMPOSITION\n");
    sb.append("7. VIEW COST BASIS OF A PORTFOLIO FOR A GIVEN DATE\n");
    sb.append("8. VIEW PERFORMANCE GRAPH\n");
    sb.append("9. QUIT\n");
    sb.append("------------------------------");
    displayInput(sb.toString());
  }

  @Override
  public String showEnterNewPortfolioName() {
    displayInput("ENTER PORTFOLIO NAME:");
    return nextInput();
  }

  @Override
  public String showTicker() {
    displayInput("ENTER STOCK TICKER:");
    return nextInput();
  }

  @Override
  public int showQuantity() {
    while (true) {
      displayInput("ENTER STOCK QUANTITY:");
      String quantity = nextInput();
      try {
        if(Integer.parseInt(quantity) < 0)
          throw new NumberFormatException();
        int quantityInt = Math.round(Float.parseFloat(quantity));
        return quantityInt;
      } catch (NumberFormatException ex) {
        displayInput("STOCK QUANTITY SHOULD BE POSITIVE WHOLE NUMBERS ONLY.");
      }
    }
  }

  @Override
  public String showPostConfirmation() {
    displayInput("ORDER PLACED SUCCESSFULLY!");
    while (true) {
      displayInput("DO YOU WISH TO ADD MORE STOCKS? (Y/N)");
      String confirmation = nextInput();
      if (confirmation.equalsIgnoreCase("Y") || confirmation.equalsIgnoreCase("N")) {
        return confirmation;
      }
      displayInput("ENTER Y/N INPUT.");
    }
  }
  @Override
  public String showSellConfirmation(){
    displayInput("ORDER PLACED SUCCESSFULLY!");
    while (true) {
      displayInput("DO YOU WISH TO SELL MORE STOCKS? (Y?N)");
      String confirmation = nextInput();
      if(confirmation.equalsIgnoreCase("Y") || confirmation.equalsIgnoreCase("N")){
        return confirmation;
      }
      displayInput("ENTER Y?N INPUT.");
    }
  }

  @Override
  public double showCommissionFee() {
    String fee="";
    while (true) {
      displayInput("ENTER COMMISSION FEE:");
      fee = nextInput();
      try {
        if(Double.parseDouble(fee)<0)
          throw new IllegalArgumentException();
        return Double.parseDouble(fee);
      } catch (IllegalArgumentException ex) {
        displayInput("COMMISSION FEE CANNOT BE NEGATIVE");
      }
    }

  }

  @Override
  public String showBuyDate() {
    while (true) {
      displayInput("ENTER BUY DATE (FORMAT: YYYY-MM-DD):");
      String date = nextInput();
      if (isValidDate(date)) {
        return date;
      } else {
        displayInput("ENTER DATE IN YYYY-MM-DD FORMAT.");
      }
    }
  }

  @Override
  public String showQuit() {
    while (true) {
      displayInput("DO YOU WANT TO QUIT? (Y/N)");
      String confirmation = nextInput();
      if (confirmation.equals("Y") || confirmation.equals("N")) {
        return confirmation;
      }
      displayInput("INVALID INPUT.");
    }
  }

  @Override
  public String showPortfolioMenuOption() {
    StringBuilder sb = new StringBuilder();
    sb.append("1. CREATE PORTFOLIO THROUGH CONSOLE\n");
    sb.append("2. CREATE PORTFOLIO READING FILE.");
    displayInput(sb.toString());
    while (true) {
      String option = nextInput();
      if (option.equals("1") || option.equals("2")) {
        return option;
      }
      displayInput("INVALID INPUT.");
    }
  }

  @Override
  public void showInvalidPortfolioName() {
    displayInput("ENTER VALID PORTFOLIO NAME.");
  }

  @Override
  public String showFileName() {
    displayInput("ENTER FILE NAME:");
    return nextInput();
  }

  @Override
  public void showInvalidFileError() {
    displayInput("INVALID FILE.");
  }

  @Override
  public void showOrderCompleted() {
    displayInput("ORDER COMPLETED.");
  }

  @Override
  public void showMenuMessage() {
    displayInput("PORTFOLIO LOCKED!");
    displayInput("EXITING TO MAIN MENU.");
  }

  @Override
  public void showPortfolioNames(String[] names) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < names.length; i++) {
      sb.append(i + 1 + " " + names[i]).append("\n");
    }
    displayInput(sb.toString());
  }

  @Override
  public String showEnterPortfolioToAddStocks() {
    displayInput("ENTER PORTFOLIO NAME:");
    return nextInput();
  }

  @Override
  public void showValidPortfolio() {
    displayInput("PORTFOLIO FOUND.");
  }

  @Override
  public void showPortfolioMessage() {
    displayInput("ENTER PORTFOLIO NAME:");
  }

  @Override
  public void showStockPortfolio(String[][] composition) {
    StringBuilder sb = new StringBuilder();
    sb.append("YOUR PORTFOLIO COMPOSITION IS: \n");
    for (int i = 0; i < composition.length; i++) {
      int j = 0;
      sb.append("TICKER : ");
      sb.append(composition[i][j]);
      sb.append(" QUANTITY: ");
      sb.append(composition[i][++j]);
      sb.append(" BUY PRICE : ");
      sb.append(composition[i][++j]);
      sb.append(" CURRENT VALUE : ");
      sb.append(composition[i][++j]);
      sb.append("\n");
    }
    displayInput(sb.toString());
  }

  @Override
  public void showAllPortfolioNames(String names) {
    displayInput(names);
  }

  @Override
  public void showTickerError() {
    displayInput("ENTER VALID TICKER.");
  }

  @Override
  public void showPortfolioLockedError() {
    displayInput("THIS PORTFOLIO IS LOCKED.");
    displayInput("ENTER UNLOCKED PORTFOLIO NAME OR CREATE A NEW PORTFOLIO.");
  }

  @Override
  public void showPortfolioExists() {
    displayInput("PORTFOLIO ALREADY EXISTS");
  }

  @Override
  public void showPortfolioCreatedSuccessfully(String name) {
    displayInput("PORTFOLIO " + name + " CREATED SUCCESFULLY!");
  }

  @Override
  public void showNoPortfoliosPresent() {
    displayInput("No portfolios Found");
  }

  @Override
  public String showPortfolioCreationDate() {
    while (true) {
      displayInput("ENTER PORTFOLIO CREATION DATE (FORMAT: YYYY-MM-DD):");
      String date = nextInput();
      if (isValidDate(date)) {
        return date;
      } else {
        displayInput("ENTER DATE IN YYYY-MM-DD FORMAT.");
      }
    }
  }

  @Override
  public String showPortfolioTypeMenu() {
    StringBuilder sb = new StringBuilder();
    sb.append("1. CREATE FLEXIBLE PORTFOLIO\n");
    sb.append("2. CREATE LOCKED PORTFOLIO");
    displayInput(sb.toString());
    while (true){
      String option = nextInput();
      if(option.equals("1") || option.equals("2")){
        return option;
      }
      displayInput("Invalid Input.");
    }
  }

  @Override
  public String showInflexiblePortfolioMenu() {
    StringBuilder sb = new StringBuilder();
    sb.append("1. CREATE NEW COMPOSITION\n");
    sb.append("2. GET EXISTING COMPOSITION FROM CSV");
    displayInput(sb.toString());
    while (true) {
      String option = nextInput();
      if (option.equals("1") || option.equals("2")) {
        return option;
      }
      displayInput("INVALID INPUT.");
    }
  }
  @Override
  public String showCompositionDat(){
    while (true) {
      displayInput("ENTER DATE WHOSE COMPOSITION IS NEEDED (FORMAT: YYYY-MM-DD):");
      String date = nextInput();
      if (isValidDate(date)) {
        return date;
      } else {
        displayInput("ENTER DATE IN YYYY-MM-DD FORMAT.");
      }
    }
  }
  @Override
  public String showCostBasisDate() {
    while (true) {
      displayInput("ENTER DATE TO SEE COST BASIS (FORMAT: YYYY-MM-DD):");
      String date = nextInput();
      if (isValidDate(date)) {
        return date;
      } else {
        displayInput("ENTER DATE IN YYYY-MM-DD FORMAT.");
      }
    }
  }
  @Override
  public String showSellDate() {
    while (true) {
      displayInput("ENTER DATE TO SELL STOCK (FORMAT: YYYY-MM-DD):");
      String date = nextInput();
      if (isValidDate(date)) {
        return date;
      } else {
        displayInput("ENTER DATE IN YYYY-MM-DD FORMAT.");
      }
    }
  }
}
