package model.operation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.ListIterator;
import java.util.TreeMap;
import model.filehandling.CsvFiles;
import model.plot.ILineChart;
import model.portfolio.FlexiblePortfolio;
import model.portfolio.IFlexiblePortfolio;
import model.portfolio.IInflexiblePortfolio;
import model.portfolio.InflexiblePortfolio;
import model.stocks.IStocks;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import model.stocks.Stocks;

/**
 * This class contains all the major methods that are required for the stock software.
 */
public class Operation implements IOperation {

  protected HashMap<String, HashMap<String, List<String>>> portfolios
      = new HashMap<String, HashMap<String, List<String>>>();
  protected String portfolioName;
  CsvFiles files = new CsvFiles();
  protected HashMap<String, HashMap<String, HashMap<String, List<String>>>> flexibleMap
      = new HashMap<String, HashMap<String, HashMap<String, List<String>>>>();
  protected HashMap<String, HashMap<String, HashMap<String,
      List<String>>>> inflexibleMap = new HashMap<>();
  protected double totalValue;
  protected String date;
  protected IStocks stocks;
  protected ILineChart lineChart;

  protected HashMap<String, String> creationDateMap = new HashMap<>();
  protected IFlexiblePortfolio flexiblePortfolio;
  protected IInflexiblePortfolio inflexiblePortfolio;
  private static final String CSV_SEPARATOR = ",";


  /**
   * Constructor for Operation class.
   *
   * @param: stocks
   */
  public Operation(IInflexiblePortfolio inflexiblePortfolio, IFlexiblePortfolio flexiblePortfolio,
      IStocks stocks, ILineChart lineChart) {
    this.portfolioName = "";
    this.totalValue = 0;
    this.stocks = stocks;
    this.inflexiblePortfolio = inflexiblePortfolio;
    this.flexiblePortfolio = flexiblePortfolio;
    this.lineChart = lineChart;
  }

  @Override
  public void createNewPortfolio(String portfolioName) {
    if (checkPortfolioAlreadyExists(portfolioName)) {
      if (getMapSize(portfolioName) != 0) {
        throw new IllegalArgumentException("CANNOT MODIFY A LOCKED PORTFOLIO.");
      } else {
        throw new IllegalArgumentException(
            "PORTFOLIO ALREADY PRESENT. ADD STOCKS.");
      }
    }
    this.portfolios.put(portfolioName, new HashMap<String, List<String>>());
    this.portfolioName = portfolioName;
  }

  @Override
  public void addStockToPortfolio(String portfolioName, String ticker, int quantity, double price) {
    if (!portfolios.containsKey(portfolioName)) {
      throw new IllegalArgumentException("Enter valid portfolio name.");
    }
    if (portfolios.get(portfolioName).containsKey(ticker)) {
      int existingNoOfStocks = Integer.parseInt(portfolios.get(portfolioName).get(ticker).get(0));
      portfolios.get(portfolioName).get(ticker)
          .set(0, String.valueOf(existingNoOfStocks + quantity));
      double existingPrice = Double.parseDouble(portfolios.get(portfolioName).get(ticker).get(1));
      portfolios.get(portfolioName).get(ticker).set(1, String.valueOf((existingPrice + price) / 2));
      double existingTotalStockValue = Double.parseDouble(
          portfolios.get(portfolioName).get(ticker).get(2));
      portfolios.get(portfolioName).get(ticker)
          .set(2, String.valueOf(existingTotalStockValue + (quantity * price)));
      this.totalValue = totalValue + Math.round(quantity * price);
    } else {
      portfolios.get(portfolioName).put(ticker, new ArrayList<String>());
      portfolios.get(portfolioName).get(ticker).add(String.valueOf(quantity));
      portfolios.get(portfolioName).get(ticker).add(String.valueOf(price));
      portfolios.get(portfolioName).get(ticker).add(String.valueOf(quantity * price));
      this.totalValue = totalValue + (quantity * price);
    }
  }

  @Override
  public void sellStock(String portfolioName, String ticker, int quantity, double price,
      String date, double fee) {
    flexiblePortfolio.sellStock(portfolioName, ticker, quantity, price, date, fee);
  }

  @Override
  public HashMap<String, Integer> returnPortfolioData(String portfolioName) {
    HashMap<String, Integer> map = new HashMap<>();
    for (String stockDates : flexibleMap.get(portfolioName).keySet()) {
      for (String ticker : flexibleMap.get(portfolioName).get(stockDates).keySet()) {
        int quantity = Integer.parseInt(
            flexibleMap.get(portfolioName).get(stockDates).get(ticker).get(0));
        if (map.containsKey(ticker)) {
          map.put(ticker, map.get(ticker) + quantity);
        } else {
          map.put(ticker, quantity);
        }
      }
    }
    return map;
  }

  @Override
  public String getPreviousDate(HashMap<String, HashMap<String, HashMap<String, List<String>>>> map,
      String currentDate, String name) {
    List<String> dates = new ArrayList<>();
    ListIterator<String> dateIterator = new ArrayList<String>(
        map.get(name).keySet()).listIterator();
    if (map.get(name).keySet().size() == 1) {
      return map.get(name).keySet().iterator().next();
    }
    while (dateIterator.hasNext()) {
      dates.add(dateIterator.next());
    }
    Collections.sort(dates);
    if (dates.get(0).equals(currentDate)) {
      return currentDate;
    }
    for (int i = 1; i < map.get(name).keySet().size(); i++) {
      if (dates.get(i).equals(currentDate)) {
        return dates.get(i - 1);
      }
    }
    return dates.get(dates.size() - 1);
  }

  @Override
  public TreeMap<String, Integer> getGraph(String portfolioName, String startDate, String endDate) {
    HashMap<String, Integer> portfolioData = returnPortfolioData(portfolioName);
    TreeMap<String, Integer> map;
    if (!checkWhetherFlexible(portfolioName) && !checkWhetherInflexible(portfolioName)) {
      throw new IllegalArgumentException("No such portfolio Exists");
    }
    if (creationDateMap.get(portfolioName).compareTo(startDate) > 0) {
      throw new IllegalArgumentException("Start date should be after the creation date");
    }
    try {
      map = lineChart.plot(portfolioData, startDate, endDate);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    return map;
  }

  @Override
  public boolean checkPortfolioAlreadyExists(String name) {
    return portfolios.containsKey(name);
  }

  @Override
  public String getExistingPortfolios() {
    getFlexibleMap();
    getInflexibleMap();
    if (flexibleMap.size() == 0 && inflexibleMap.size() == 0) {
      throw new IllegalArgumentException("NO AVAILABLE PORTFOLIOS TO DISPLAY.");
    }
    StringBuilder allPortfolios = new StringBuilder();
    if (flexibleMap.size() != 0) {
      allPortfolios.append(getFlexiblePortfolios(flexibleMap));
    }
    if (inflexibleMap.size() != 0) {
      allPortfolios.append(getInFlexiblePortfolios(inflexibleMap));
    }
    return allPortfolios.toString();
  }

  private String getFlexiblePortfolios(
      HashMap<String, HashMap<String, HashMap<String, List<String>>>> flexibleMap) {
    StringBuilder allPortfolios = new StringBuilder();
    allPortfolios.append("FLEXIBLE PORTFOLIOS:\n");
    for (String portfolioNames : flexibleMap.keySet()) {
      allPortfolios.append(portfolioNames + "\n");
    }
    return allPortfolios.toString();
  }

  private String getInFlexiblePortfolios(
      HashMap<String, HashMap<String, HashMap<String, List<String>>>> inflexibleMap) {
    StringBuilder allPortfolios = new StringBuilder();
    allPortfolios.append("INFLEXIBLE PORTFOLIOS:\n");
    for (String portfolioNames : inflexibleMap.keySet()) {
      allPortfolios.append(portfolioNames + "\n");
    }
    return allPortfolios.toString();
  }

  @Override
  public boolean isTickerValid(String ticker) {
    return files.isTickerValid(ticker);
  }


  @Override
  public void writeToCSV(String portfolioName) {
    if (checkWhetherFlexible(portfolioName)) {
      files.writeToCSV(portfolioName, this.flexibleMap, "Flexible");
    } else if (checkWhetherInflexible(portfolioName)) {
      files.writeToCSV(portfolioName, this.inflexibleMap, "Inflexible");
    }
  }

  @Override
  public String readFromFile(String fileName) {
    CsvFiles files = new CsvFiles();
    if (files.checkMapType("./res/" + fileName + ".csv") == 1) {
      this.flexibleMap = files.readFromFile("./res/" + fileName + ".csv");
      return "PORTFOLIO " + fileName + " SUCCESSFULLY CREATED.";
    } else if (files.checkMapType("./res/" + fileName + ".csv") == 2) {
      this.inflexibleMap = files.readFromFile("./res/" + fileName + ".csv");
      return "PORTFOLIO " + fileName + " SUCCESSFULLY CREATED.";
    }
    return "INVALID FILE NAME.";
  }

  @Override
  public List<String> getStockNamesFromPortfolio() {
    List<String> list = new ArrayList<String>();
    for (String portfolioName : this.portfolios.keySet()) {
      for (String stockName : this.portfolios.get(portfolioName).keySet()) {
        list.add(stockName);
      }
    }
    return list;
  }

  @Override
  public HashMap<String, HashMap<String, List<String>>> getPortfolioMap() {
    return this.portfolios;
  }

  @Override
  public int getMapSize(String portfolioName) {
    return this.portfolios.get(portfolioName).size();
  }

  @Override
  public String getPortfolioComposition(String portfolioName) {
    if (!checkWhetherFlexible(portfolioName) && !checkWhetherInflexible(portfolioName)) {
      throw new IllegalArgumentException("No such portfolio Exists");
    }
    if (checkWhetherFlexible(portfolioName)) {
      return getFlexibleComposition(flexibleMap, portfolioName);
    } else if (checkWhetherInflexible(portfolioName)) {
      return getFlexibleComposition(inflexibleMap, portfolioName);
    }
    throw new IllegalArgumentException("ENTER VALID PORTFOLIO NAME.");
  }

  private String getFlexibleComposition(
      HashMap<String, HashMap<String, HashMap<String, List<String>>>> map, String name) {
    StringBuilder sb = new StringBuilder();
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date d = new Date();
    String cdate = dateFormat.format(d);
    cdate = getPreviousDate(map, cdate, name);
    sb.append("TICK - QTY \n");
    for (String stockName : map.get(name).get(cdate).keySet()) {
      sb.append(stockName).append(" - ");
      for (int i = 0; i < map.get(name).get(cdate).get(stockName).size() - 2; i++) {
        sb.append(map.get(name).get(cdate).get(stockName).get(i));
      }
      sb.append("\n");
    }
    return sb.toString();
  }

  @Override
  public double getPortfolioByDate(String portfolioName, String date) {
    date = stocks.isWeekend(date);
    double finalValue = 0;
    if (!checkWhetherFlexible(portfolioName) && !checkWhetherInflexible(portfolioName)) {
      throw new IllegalArgumentException("NO SUCH PORTFOLIO EXISTS.");
    }
    if (checkWhetherFlexible(portfolioName)) {
      if (creationDateMap.get(portfolioName).compareTo(date) > 0) {
        return 0.00;
      }
      finalValue = getValueByDate(flexibleMap, portfolioName, date);
    } else if (checkWhetherInflexible(portfolioName)) {
      if (creationDateMap.get(portfolioName).compareTo(date) > 0) {
        throw new IllegalArgumentException("PORTFOLIO DOESN'T EXIST FOR THE GIVEN DATE.");
      }
      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      Date d = new Date();
      String currentDate = stocks.isWeekend(dateFormat.format(d));
      finalValue = getValueByDate(inflexibleMap, portfolioName, currentDate);
    }
    return finalValue;
  }

  private double getValueByDate(HashMap<String, HashMap<String, HashMap<String, List<String>>>> map,
      String name, String date) {
    IStocks stocks = new Stocks();
    date = getPreviousDate(map, date, name);
    double portfolioValue = 0.00;
    for (String stock : map.get(name).get(date).keySet()) {
      portfolioValue += stocks.getPriceByDate(stock, date);
    }
    return portfolioValue;
  }

  @Override
  public Date yesterdaysDate() {
    Calendar startDate = Calendar.getInstance();
    startDate.setTime(new Date(System.currentTimeMillis()));
    if (startDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
      return new Date(System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000);
    }
    return new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
  }

  @Override
  public void getFlexibleMap() {
    if (flexiblePortfolio.returnMap().size() != 0) {
      flexibleMap = flexiblePortfolio.returnMap();
    }
  }

  @Override
  public int getFlexibleMapSize() {
    return flexiblePortfolio.returnMap().size();
  }

  @Override
  public void getInflexibleMap() {
    if (inflexiblePortfolio.returnMap().size() != 0) {
      inflexibleMap = inflexiblePortfolio.returnMap();
    }
  }

  @Override
  public void addStockToFlexiblePortfolio(String portfolioName, String ticker, int quantity,
      double price, String date, double fee) {
    // String date=""; // Implement date logic
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date d = new Date();
    String cdate = dateFormat.format(d);
    if (creationDateMap.get(portfolioName).compareTo(date) > 0 || cdate.compareTo(date) < 0) {
      throw new IllegalArgumentException(
          "Date should le after portfolio creation date and before current date");
    }
    this.portfolioName = portfolioName;
    flexiblePortfolio.buyStock(portfolioName, ticker, quantity, price, date, fee);
  }

  @Override
  public void addStockToInFlexiblePortfolio(String portfolioName, String ticker, int quantity,
      double price) {
    this.portfolioName = portfolioName;
    inflexiblePortfolio.buyStock(portfolioName, ticker, quantity, price);
  }


  @Override
  public double costBasisByDate(String portfolioName, String date) {
    if (!checkWhetherFlexible(portfolioName) && !checkWhetherInflexible(portfolioName)) {
      throw new IllegalArgumentException("No such portfolio Exists");
    }
    if (checkWhetherFlexible(portfolioName)) {
      date = getPreviousDate(flexibleMap, date, portfolioName);
    }
    return flexiblePortfolio.costBasisByDate(portfolioName, date);
  }


  @Override
  public boolean checkWhetherFlexible(String portfolioName) {
    getFlexibleMap();
    return flexibleMap.containsKey(portfolioName);
  }

  @Override
  public boolean checkWhetherInflexible(String portfolioName) {
    getInflexibleMap();
    return inflexibleMap.containsKey(portfolioName);
  }

  @Override
  public String[] callStockAPI(String ticker, String date) {
    return stocks.callStockAPI(ticker, date);
  }

  @Override
  public void createFlexiblePortfolio(String portfolioName, String date) {
    this.portfolioName = portfolioName;
    this.date = date;
    if (checkWhetherInflexible(portfolioName)) {
      throw new IllegalArgumentException("Inflexible portfolio with this name already exists");
    } else if (checkWhetherFlexible(portfolioName)) {
      throw new IllegalArgumentException("Flexible portfolio with this name already exists");
    } else if (!checkWhetherFlexible(portfolioName) && !checkWhetherInflexible(portfolioName)) {
      creationDateMap.put(portfolioName, date);
      flexiblePortfolio = new FlexiblePortfolio();
      flexiblePortfolio.createPortfolio(portfolioName, date);
    }

  }

  @Override
  public void createLockedPortfolio(String portfolioName) {
    this.portfolioName = portfolioName;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date d = new Date();
    String inflexPortfolioDate = dateFormat.format(d);
    if (checkWhetherInflexible(portfolioName)) {
      throw new IllegalArgumentException("PORTFOLIO WITH THIS NAME ALREADY EXISTS.");
    } else if (checkWhetherFlexible(portfolioName)) {
      throw new IllegalArgumentException("PORTFOLIO WITH THIS NAME ALREADY EXISTS.");
    } else if (!checkWhetherFlexible(portfolioName) && !checkWhetherInflexible(portfolioName)) {
      creationDateMap.put(portfolioName, inflexPortfolioDate);
      inflexiblePortfolio = new InflexiblePortfolio();
      inflexiblePortfolio.createPortfolio(portfolioName, inflexPortfolioDate);
    }
  }

  @Override
  public int getLineChartScale() {
    return lineChart.scale();
  }
}
