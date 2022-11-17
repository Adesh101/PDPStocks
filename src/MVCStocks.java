import controller.IController;
import controller.Controller;
import model.operation.IOperation;
import model.operation.Operation;
import model.plot.ILineChart;
import model.plot.LineChart;
import model.portfolio.FlexiblePortfolio;
import model.portfolio.IFlexiblePortfolio;
import model.portfolio.IInflexiblePortfolio;
import model.portfolio.InflexiblePortfolio;
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
   *
   * @param: args
   */
  public static void main(String[] args) {
    UpdateStockData data = new UpdateStockData();
    data.getFiles();
    IStocks stocks = new Stocks();
    IInflexiblePortfolio inflexiblePortfolio = new InflexiblePortfolio();
    IFlexiblePortfolio flexiblePortfolio = new FlexiblePortfolio();
    ILineChart lineChart = new LineChart();
    IOperation operation = new Operation(inflexiblePortfolio, flexiblePortfolio, stocks, lineChart);
    IView view = new View(new InputStreamReader(System.in), System.out);
    IController controller = new Controller(operation, view);
    controller.operate(operation);
  }
}
