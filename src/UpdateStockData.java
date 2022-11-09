import java.io.File;
import model.filehandling.csvFiles;
import model.stocks.IStocks;
import model.stocks.Stocks;


public class UpdateStockData {

  public void getFiles() {
    csvFiles file = new csvFiles();
    IStocks stock = new Stocks();
    //List<String> listOfFiles = new ArrayList<String>();
    File folder = new File("./data/");
    File[] listOfFiles = folder.listFiles();

    assert listOfFiles != null;
    for (File listOfFile : listOfFiles) {
      if (listOfFile.isFile()) {
        String fileName = listOfFile.getName();
        int pos = fileName.lastIndexOf(".");
        if (pos > 0 && pos < (fileName.length() - 1)) { // If '.' is not the first or last character.
          fileName = fileName.substring(0, pos);
        }
        stock.updateFile(fileName);
      }
    }
  }
}
