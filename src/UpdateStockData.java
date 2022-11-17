import java.io.File;
import model.filehandling.CsvFiles;
import model.stocks.IStocks;
import model.stocks.Stocks;

/**
 * Class to update local stock data.
 */
public class UpdateStockData {

  /**
   * Method to fetch local files and update local data.
   */
  public void getFiles() {
    CsvFiles file = new CsvFiles();
    IStocks stock = new Stocks();
    File folder = new File("./data/");
    File[] listOfFiles = folder.listFiles();

    assert listOfFiles != null;
    for (File listOfFile : listOfFiles) {
      if (listOfFile.isFile()) {
        String fileName = listOfFile.getName();
        int pos = fileName.lastIndexOf(".");
        if (pos > 0 && pos < (fileName.length() - 1)) {
          fileName = fileName.substring(0, pos);
        }
        stock.updateFile(fileName);
      }
    }
  }
}
