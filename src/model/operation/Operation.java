package model.operation;

import java.util.Calendar;
import java.util.Date;
import model.filehandling.csvFiles;
import model.portfolio.FlexiblePortfolio;
import model.portfolio.IFlexiblePortfolio;
import model.portfolio.IInflexiblePortfolio;
import model.portfolio.InflexiblePortfolio;
import model.stocks.IStocks;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class contains all the major methods that are required for the stock software.
 */
public class Operation implements IOperation {
  protected HashMap<String, HashMap<String, List<String>>> portfolios
      = new HashMap<String, HashMap<String, List<String>>>();
  protected String portfolioName;
  csvFiles files = new csvFiles();
  protected HashMap<String, HashMap<String, HashMap<String, List<String>>>> FlexibleMap
      = new HashMap<String, HashMap<String, HashMap<String, List<String>>>>();
  protected HashMap<String, HashMap<String, List<String>>> InflexibleMap
      = new HashMap<String, HashMap<String, List<String>>>();
  protected double totalValue;
  protected String date;
  protected IStocks stocks;
  protected IFlexiblePortfolio Fportfolio;
  protected IInflexiblePortfolio IfPortfolio;
  private static final String CSV_SEPARATOR = ",";


  /**
   * Constructor for Operation class.
   *
   * @param: stocks
   */
  public Operation(IStocks stocks) {
    this.portfolioName = "";
    this.totalValue = 0;
    this.stocks = stocks;
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
  public void sellStock(String portfolioName, String ticker, int quantity, double price,String date){
    Fportfolio.sellStock(portfolioName, ticker, quantity, price, date);
  }

  @Override
  public boolean checkPortfolioAlreadyExists(String name) {
    return portfolios.containsKey(name);
  }

  @Override
  public String getExistingPortfolios() {
    getFlexibleMap();
    getInflexibleMap();
    //if (portfolios.size() == 0) {
    if(FlexibleMap.size()==0 && InflexibleMap.size()==0){
      throw new IllegalArgumentException("NO AVAILABLE PORTFOLIOS TO DISPLAY.");
    }
    StringBuilder allPortfolios = new StringBuilder();
    if(FlexibleMap.size()!=0)
      allPortfolios.append(getFlexiblePortfolios(FlexibleMap));
    if(InflexibleMap.size()!=0)
      allPortfolios.append(getInFlexiblePortfolios(InflexibleMap));
//    for (String portfolioNames : portfolios.keySet()) {
//      allPortfolios.append(portfolioNames + "\n");
//    }
    return allPortfolios.toString();
  }
  private String getFlexiblePortfolios(HashMap<String, HashMap<String, HashMap<String, List<String>>>> FlexibleMap) {
    StringBuilder allPortfolios =new StringBuilder();
    allPortfolios.append("FLEXIBLE PORTFOLIOS:\n");
    for(String portfolioNames: FlexibleMap.keySet()){
      allPortfolios.append(portfolioNames + "\n");
    }
    return allPortfolios.toString();

  }
  private String getInFlexiblePortfolios(HashMap<String, HashMap<String, List<String>>> InflexibleMap) {
    StringBuilder allPortfolios =new StringBuilder();
    allPortfolios.append("INFLEXIBLE PORTFOLIOS:\n");
    for(String portfolioNames: InflexibleMap.keySet()){
      allPortfolios.append(portfolioNames + "\n");
    }
    return allPortfolios.toString();

  }
//
//  @Override
//  public double getCurrentPrice(String ticker) {
//    return stocks.getStockCurrentPrice(ticker);
//  }

  @Override
  public boolean isTickerValid(String ticker) {
    return files.isTickerValid(ticker);
  }

  @Override
  public void writeToCSV(String portfolioName) {
    try {
      BufferedWriter bw = new BufferedWriter(
          new OutputStreamWriter(new FileOutputStream("./res/" + portfolioName + ".csv"), "UTF-8"));
      HashMap<String, HashMap<String, List<String>>> portfolios = this.portfolios;
      for (String individualPortfolioName : portfolios.keySet()) {
        StringBuffer oneLine = new StringBuffer();
        oneLine.append("Portfolio Name");
        oneLine.append(CSV_SEPARATOR);
        oneLine.append(portfolioName);
        oneLine.append("\n");
        oneLine.append("Stock");
        oneLine.append(CSV_SEPARATOR);
        oneLine.append("Quantity");
        oneLine.append(CSV_SEPARATOR);
        oneLine.append("Price");
        oneLine.append(CSV_SEPARATOR);
        oneLine.append("Total");
        oneLine.append(CSV_SEPARATOR);
        oneLine.append("\n");
        for (String stockData : portfolios.get(individualPortfolioName).keySet()) {
          oneLine.append(stockData);
          oneLine.append(CSV_SEPARATOR);
          for (String metaStockData : portfolios.get(individualPortfolioName).get(stockData)) {
            oneLine.append(metaStockData);
            oneLine.append(CSV_SEPARATOR);
          }
        }
        bw.write(oneLine.toString());
        bw.newLine();
      }
      bw.flush();
      bw.close();
    } catch (IOException e) {
      System.out.println("");
    }
  }

  @Override
  public String readFromFile(String fileName) {
    String line = "";
    String splitBy = ",";
    try {
      BufferedReader br = new BufferedReader(new FileReader(fileName));
      while ((line = br.readLine()) != null) {   //returns a Boolean value
        String[] portfolioNames = line.split(splitBy);    // use comma as separator
        if (portfolioNames.length == 1) {
          if (portfolioNames[0].length() != 0) {
            createNewPortfolio(portfolioNames[0]);
            this.portfolioName = portfolioNames[0];
            break;
          }
        }
      }
    } catch (Exception ex) {
      return "INVALID FILE.";
    }
    return "PORTFOLIO " + this.portfolioName + " SUCCESSFULLY CREATED.";
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

//  @Override
//  public double callStockAPIByDateHelper(HashMap<String, List<String>> map, String date) {
//    double currentValue = 0;
//    for (String string : map.keySet()) {
//      stocks.callStockAPIByDate(string, date);
//      double tempValue = stocks.getStockCurrentPriceByDate(string);
//      tempValue *= Double.parseDouble(map.get(string).get(0));
//      currentValue += tempValue;
//    }
//    return currentValue;
//  }


  @Override
  public int getMapSize(String portfolioName) {
    return this.portfolios.get(portfolioName).size();
  }

  @Override
  public String getPortfolioComposition(String portfolioName) {
//    StringBuilder sb = new StringBuilder();
//    String finalString = "";
    //if (checkPortfolioAlreadyExists(portfolioName)) {
    if(checkWhetherFlexible(portfolioName)){
      return getFlexibleComposition(FlexibleMap);
    } else if (checkWhetherInflexible(portfolioName)) {
      return getInFlexibleComposition(InflexibleMap);
    }
    throw new IllegalArgumentException("ENTER VALID PORTFOLIO NAME.");
  }
  private String getFlexibleComposition(HashMap<String, HashMap<String, HashMap<String, List<String>>>> FlexibleMap){
    StringBuilder sb = new StringBuilder();
    String finalString = "";
    sb.append("Portfolio : ").append(FlexibleMap).append("\n");
    sb.append("TICK - QTY - PRICE - TOTAL \n");
    for (String stockName : FlexibleMap.get(portfolioName).get(date).keySet()) {
      sb.append(stockName).append(" - ");
      for (int i = 0; i < FlexibleMap.get(portfolioName).get(date).get(stockName).size(); i++) {
        sb.append(FlexibleMap.get(portfolioName).get(date).get(stockName).get(i)).append(" - ");
      }
    }
    if (sb.toString().endsWith("- ")) {
      finalString = sb.substring(0, sb.length() - 3);
    }
    return finalString;
  }
  private String getInFlexibleComposition(HashMap<String,HashMap<String, List<String>>> InflexibleMap){
    StringBuilder sb = new StringBuilder();
    String finalString = "";
    sb.append("Portfolio : ").append(InflexibleMap).append("\n");
    sb.append("TICK - QTY - PRICE - TOTAL \n");
    for (String stockName : InflexibleMap.get(portfolioName).keySet()) {
      sb.append(stockName).append(" - ");
      for (int i = 0; i < InflexibleMap.get(portfolioName).get(stockName).size(); i++) {
        sb.append(InflexibleMap.get(portfolioName).get(stockName).get(i)).append(" - ");
      }
    }
    if (sb.toString().endsWith("- ")) {
      finalString = sb.substring(0, sb.length() - 3);
    }
    return finalString;
  }

  @Override
  public double getPortfolioByDate(String portfolioName, String date) {
    date = stocks.isWeekend(date);
//    HashMap<String, List<String>> map = new HashMap<String, List<String>>();
//    String dateFormat = "yyyy-MM-dd";
////    try {
////      DateFormat df = new SimpleDateFormat(dateFormat);
////      df.setLenient(false);
////      df.parse(date);
////      double totalValueByDate = 0;
//      for (String string : this.portfolios.get(portfolioName).keySet()) {
//        map.put(string, new ArrayList<>());
//        map.get(string).add(portfolios.get(portfolioName).get(string).get(0));
//      }
//      double finalValue = 0;//callStockAPIByDateHelper(map, date);
//      return finalValue;
//    } catch (ParseException e) {
//      throw new IllegalArgumentException("ENTER DATE IN YYYY-MM-DD FORMAT");
//    }

    double finalValue=0;

    if(checkWhetherFlexible(portfolioName)){
//      HashMap<String, HashMap<String, HashMap<String, List<String>>>> FlexibleMap
//          = new HashMap<String, HashMap<String, HashMap<String, List<String>>>>();
      finalValue=getFlexibleValueByDate(FlexibleMap);
    } else if (checkWhetherInflexible(portfolioName)) {
//      HashMap<String, HashMap<String, List<String>>> InflexibleMap
//          = new HashMap<String, HashMap<String, List<String>>>();
      finalValue= getInFlexibleValueByDate(InflexibleMap);
    }
//    for(String string : this.portfolios.get(portfolioName).keySet()){
//      individualValue = stocks.getPriceByDate(string, date);
//      quantity = Integer.parseInt(portfolios.get(portfolioName).get(string).get(0));
//      finalValue = finalValue + (individualValue*quantity);
//    }
    return finalValue;
  }
  private double getFlexibleValueByDate(HashMap<String, HashMap<String, HashMap<String, List<String>>>> FlexibleMap){
    double individualValue =0, finalValue=0;
    int quantity=0;
    for(String string : this.FlexibleMap.get(portfolioName).get(date).keySet()){
      individualValue = stocks.getPriceByDate(string, date);
      quantity = Integer.parseInt(FlexibleMap.get(portfolioName).get(date).get(string).get(0));
      finalValue = finalValue + (individualValue*quantity);
    }
    return finalValue;
  }
  private double getInFlexibleValueByDate(HashMap<String, HashMap<String, List<String>>> InflexibleMap){
    double individualValue =0, finalValue=0;
    int quantity=0;
    for(String string : this.InflexibleMap.get(portfolioName).keySet()){
      individualValue = stocks.getPriceByDate(string, date);
      quantity = Integer.parseInt(InflexibleMap.get(portfolioName).get(string).get(0));
      finalValue = finalValue + (individualValue*quantity);
    }
    return finalValue;
  }
  @Override
  public Date yesterdaysDate(){
    Calendar startDate = Calendar.getInstance();
    startDate.setTime(new Date(System.currentTimeMillis()));
    if (startDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
      return new Date(System.currentTimeMillis()-2*24*60*60*1000);
    }
    return new Date(System.currentTimeMillis()-24*60*60*1000);
  }
  @Override
  public void getFlexibleMap(){
    if(Fportfolio.returnMap().size()!=0)
      FlexibleMap = Fportfolio.returnMap();
  }
  @Override
  public int getFlexibleMapSize(){
    return Fportfolio.returnMap().size();
  }
  @Override
  public void getInflexibleMap(){
    if(IfPortfolio.returnMap().size()!=0)
      InflexibleMap = IfPortfolio.returnMap();
  }

  @Override
  public void addStockToFlexiblePortfolio(String portfolioName, String ticker, int quantity,
      double price, String date) {
   // String date=""; // Implement date logic
    this.portfolioName=portfolioName;
    Fportfolio.buyStock(portfolioName, ticker,quantity,price,date);
  }
  @Override
  public void addStockToInFlexiblePortfolio(String portfolioName, String ticker, int quantity,
      double price) {
    String date=""; // Implement date logic
    this.portfolioName=portfolioName;
    IfPortfolio.buyStock(portfolioName, ticker,quantity,price,date);
  }

  @Override
  public double costBasisByDate(String portfolioName, String date) {
    return Fportfolio.costBasisByDate(portfolioName, date);
  }


  @Override
  public boolean checkWhetherFlexible(String portFolioName) {
    getFlexibleMap();
    return FlexibleMap.containsKey(portfolioName);
  }
  @Override
  public boolean checkWhetherInflexible(String portfolioName){
    getInflexibleMap();
    return InflexibleMap.containsKey(portfolioName);
  }

  @Override
  public String[] callStockAPI(String ticker,  String date) {
    return stocks.callStockAPI(ticker, date);
  }

  @Override
  public void createFlexiblePortfolio(String portfolioName, String date) {
    this.portfolioName=portfolioName;
    this.date=date;
    Fportfolio = new FlexiblePortfolio();
    Fportfolio.createPortfolio(portfolioName,date);
  }

  @Override
  public void createLockedPortfolio(String portfolioName, String date) {
    this.portfolioName=portfolioName;
    this.date=date;
    IfPortfolio = new InflexiblePortfolio();
    IfPortfolio.createPortfolio(portfolioName,date);
  }

//  @Override
//  public void createFlexiblePortfolio(String portfolioName, String date) {
//    this.portfolioName=portfolioName;
//    this.date=date;
//    portfolio = new FlexiblePortfolio();
//    portfolio.createPortfolio(portfolioName,date);
//  }
//
//  @Override
//  public void createLockedPortfolio(String portfolioName, String date) {
//    this.portfolioName=portfolioName;
//    this.date=date;
//    portfolio = new InflexiblePortfolio();
//    portfolio.createPortfolio(portfolioName,date);
//  }
}
