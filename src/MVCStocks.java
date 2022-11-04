import controller.IController;
import controller.Controller;
import model.operation.IOperation;
import model.operation.Operation;
import model.stocks.IStocks;
import model.stocks.Stocks;
import view.IView;
import view.View;
import java.io.InputStreamReader;


/**
 * Class to run our main Stock software.
 */
public class MVCStocks {

  /**
   * main method to run the program.
   * @param: args
   */
  public static void main(String[] args) {
    IStocks stocks = new Stocks();
    IOperation operation = new Operation(stocks);
    IView view = new View(new InputStreamReader(System.in), System.out);
    IController controller = new Controller(operation, view);
    controller.operate(operation);
  }
}
