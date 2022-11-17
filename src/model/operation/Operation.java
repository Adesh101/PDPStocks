package model.operation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.ListIterator;
import java.util.TreeMap;
import javax.sound.sampled.Line;
import model.filehandling.csvFiles;
import model.plot.ILineChart;
import model.plot.LineChart;
import model.portfolio.FlexiblePortfolio;
import model.portfolio.IFlexiblePortfolio;
import model.portfolio.IInflexiblePortfolio;
import model.portfolio.InflexiblePortfolio;
import model.stocks.IStocks;
import java.io.BufferedReader;
import java.io.FileReader;
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
  csvFiles files = new csvFiles();
  protected HashMap<String, HashMap<String, HashMap<String, List<String>>>> FlexibleMap
      = new HashMap<String, HashMap<String, HashMap<String, List<String>>>>();
 protected HashMap<String, HashMap<String, HashMap<String, List<String>>>> InflexibleMap=new HashMap<>();
  protected double totalValue;
  protected String date;
  protected IStocks stocks;
  protected ILineChart lineChart;

  protected HashMap<String,String> creationDateMap = new HashMap<>();
  protected IFlexiblePortfolio flexiblePortfolio;
  protected IInflexiblePortfolio inflexiblePortfolio;
  private static final String CSV_SEPARATOR = ",";



  /**
   * Constructor for Operation class.
   *
   * @param: stocks
   */
  public Operation(IInflexiblePortfolio inflexiblePortfolio, IFlexiblePortfolio flexiblePortfolio, IStocks stocks, ILineChart lineChart) {
    this.portfolioName = "";
    this.totalValue = 0;
    this.stocks = stocks;
    this.inflexiblePortfolio = inflexiblePortfolio;
    this.flexiblePortfolio = flexiblePortfolio;
    this.lineChart=lineChart;
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
  public void sellStock(String portfolioName, String ticker, int quantity, double price,String date, double fee){

    flexiblePortfolio.sellStock(portfolioName, ticker, quantity, price, date, fee);
  }

  @Override
  public HashMap<String, HashMap<String, HashMap<String, List<String>>>> returnPortfoliosMap(
      String portfolioName) {
    return null;
  }

  @Override
  public HashMap<String, Integer> returnPortfolioData(String portfolioName) {
    HashMap<String, Integer> map = new HashMap<>();
    for (String stockDates : FlexibleMap.get(portfolioName).keySet()) {
      for (String ticker : FlexibleMap.get(portfolioName).get(stockDates).keySet()) {
        int quantity = Integer.parseInt(
            FlexibleMap.get(portfolioName).get(stockDates).get(ticker).get(0));
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
    List<String> dates=new ArrayList<>();
    ListIterator<String> dateIterator = new ArrayList<String>(map.get(name).keySet()).listIterator();
    if(map.get(name).keySet().size()==1){
      return map.get(name).keySet().iterator().next();
    }
    while (dateIterator.hasNext()){
      dates.add(dateIterator.next()); // check for last element
    }
    Collections.sort(dates);
    if(dates.get(0).equals(currentDate)){
      return currentDate;
    }
    for(int i=1;i<map.get(name).keySet().size();i++){
      if(dates.get(i).equals(currentDate)){
        return dates.get(i-1);
      }
    }


    return dates.get(dates.size()-1);
  }

  @Override
  public TreeMap<String, Integer> getGraph(String portfolioName, String startDate, String endDate) {
    HashMap<String, Integer> portfolioData = returnPortfolioData(portfolioName);
    TreeMap<String, Integer> map;
    if(!checkWhetherFlexible(portfolioName) && !checkWhetherInflexible(portfolioName)){
      throw new IllegalArgumentException("No such portfolio Exists");
    }
    if(creationDateMap.get(portfolioName).compareTo(startDate)>0){
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
  private String getInFlexiblePortfolios(HashMap<String, HashMap<String, HashMap<String, List<String>>>> InflexibleMap) {
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
  public HashMap<String, HashMap<String, HashMap<String, List<String>>>> returnPortfoliosMap(){
    return FlexibleMap;
  }

  @Override
  public void writeToCSV(String portfolioName) {
    files.writeToCSV(portfolioName, this.FlexibleMap);
//    try {
//      BufferedWriter bw = new BufferedWriter(
//          new OutputStreamWriter(new FileOutputStream("./res/" + portfolioName + ".csv"), "UTF-8"));
//      HashMap<String, HashMap<String, List<String>>> portfolios = this.portfolios;
//      for (String individualPortfolioName : portfolios.keySet()) {
//        StringBuffer oneLine = new StringBuffer();
//        oneLine.append("Portfolio Name");
//        oneLine.append(CSV_SEPARATOR);
//        oneLine.append(portfolioName);
//        oneLine.append("\n");
//        oneLine.append("Stock");
//        oneLine.append(CSV_SEPARATOR);
//        oneLine.append("Quantity");
//        oneLine.append(CSV_SEPARATOR);
//        oneLine.append("Price");
//        oneLine.append(CSV_SEPARATOR);
//        oneLine.append("Total");
//        oneLine.append(CSV_SEPARATOR);
//        oneLine.append("\n");
//        for (String stockData : portfolios.get(individualPortfolioName).keySet()) {
//          oneLine.append(stockData);
//          oneLine.append(CSV_SEPARATOR);
//          for (String metaStockData : portfolios.get(individualPortfolioName).get(stockData)) {
//            oneLine.append(metaStockData);
//            oneLine.append(CSV_SEPARATOR);
//          }
//        }
//        bw.write(oneLine.toString());
//        bw.newLine();
//      }
//      bw.flush();
//      bw.close();
//    } catch (IOException e) {
//      System.out.println("");
//    }
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
    if(!checkWhetherFlexible(portfolioName) && !checkWhetherInflexible(portfolioName)){
      throw new IllegalArgumentException("No such portfolio Exists");
    }
    if(checkWhetherFlexible(portfolioName)){
      return getFlexibleComposition(FlexibleMap,portfolioName);
    } else if (checkWhetherInflexible(portfolioName)) {
      return getFlexibleComposition(InflexibleMap,portfolioName);
    }
    throw new IllegalArgumentException("ENTER VALID PORTFOLIO NAME.");
  }
  private String getFlexibleComposition(HashMap<String, HashMap<String, HashMap<String, List<String>>>> map, String name){
    StringBuilder sb = new StringBuilder();
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    Date d = new Date();
    String cdate=dateFormat.format(d);
    cdate=getPreviousDate(map,cdate,name);
    String finalString = "";
    sb.append("TICK - QTY \n");
    for (String stockName : map.get(name).get(cdate).keySet()) {
      sb.append(stockName).append(" - ");
      for (int i = 0; i < map.get(name).get(cdate).get(stockName).size()-2; i++) {
        sb.append(map.get(name).get(cdate).get(stockName).get(i)).append(" - ");
      }
    }
    if (sb.toString().endsWith("- ")) {
      finalString = sb.substring(0, sb.length() - 3);
    }
    return finalString;
  }
//  private String getInFlexibleComposition(HashMap<String, HashMap<String, HashMap<String, List<String>>>> InflexibleMap){
//    StringBuilder sb = new StringBuilder();
//    String finalString = "";
//    sb.append("Portfolio : ").append(InflexibleMap).append("\n");
//    sb.append("TICK - QTY - PRICE - TOTAL \n");
//    for (String stockName : InflexibleMap.get(portfolioName).keySet()) {
//      sb.append(stockName).append(" - ");
//      for (int i = 0; i < InflexibleMap.get(portfolioName).get(stockName).size(); i++) {
//        sb.append(InflexibleMap.get(portfolioName).get(stockName).get(i)).append(" - ");
//      }
//    }
//    if (sb.toString().endsWith("- ")) {
//      finalString = sb.substring(0, sb.length() - 3);
//    }
//    return finalString;
//  }

  @Override
  public double getPortfolioByDate(String portfolioName, String date) {
    date = stocks.isWeekend(date);
    double finalValue=0;
    if(!checkWhetherFlexible(portfolioName) && !checkWhetherInflexible(portfolioName)){
      throw new IllegalArgumentException("No such portfolio Exists");
    }
    if(checkWhetherFlexible(portfolioName)){
      if(creationDateMap.get(portfolioName).compareTo(date)>0)
        return 0.00;
    finalValue=getValueByDate(FlexibleMap,portfolioName,date);
    } else if (checkWhetherInflexible(portfolioName)) {
      if(creationDateMap.get(portfolioName).compareTo(date)>0)
        throw new IllegalArgumentException("Portfolio doesnt exist for the given date");
      DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
      Date d = new Date();
      String currentDate= stocks.isWeekend(dateFormat.format(d));
      finalValue= getValueByDate(InflexibleMap, portfolioName, currentDate);
    }
    return finalValue;
  }
  private double getValueByDate(HashMap<String, HashMap<String, HashMap<String, List<String>>>> map, String name, String date){
    IStocks stocks=new Stocks();
    date=getPreviousDate(FlexibleMap, date, name);
    double portfolioValue=0.00;
    for (String stock: map.get(name).get(date).keySet()){
      portfolioValue+=stocks.getPriceByDate(stock, date);
    }
    return portfolioValue;
  }
//  private double getInFlexibleValueByDate(HashMap<String, HashMap<String, HashMap<String, List<String>>>> InflexibleMap){
//    double individualValue =0, finalValue=0;
//    int quantity=0;
//    for(String string : this.InflexibleMap.get(portfolioName).keySet()){
//      individualValue = stocks.getPriceByDate(string, date);
//      quantity = Integer.parseInt(InflexibleMap.get(portfolioName).get(date).get(string).get(0)); // change date
//      finalValue = finalValue + (individualValue*quantity);
//    }
//    return finalValue;
//  }
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
    if(flexiblePortfolio.returnMap().size()!=0)
      FlexibleMap = flexiblePortfolio.returnMap();
  }
  @Override
  public int getFlexibleMapSize(){
    return flexiblePortfolio.returnMap().size();
  }
  @Override
  public void getInflexibleMap(){
    if(inflexiblePortfolio.returnMap().size()!=0)
      InflexibleMap = inflexiblePortfolio.returnMap();
  }

  @Override
  public void addStockToFlexiblePortfolio(String portfolioName, String ticker, int quantity,
      double price, String date, double fee) {
   // String date=""; // Implement date logic
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    Date d = new Date();
    String cdate=dateFormat.format(d);
    if(creationDateMap.get(portfolioName).compareTo(date)>0 || cdate.compareTo(date)<0){
      throw new IllegalArgumentException("Date should le after portfolio creation date and before current date");
    }
    this.portfolioName=portfolioName;
    flexiblePortfolio.buyStock(portfolioName, ticker,quantity,price,date,fee);
  }
  @Override
  public void addStockToInFlexiblePortfolio(String portfolioName, String ticker, int quantity,
      double price) {
    this.portfolioName=portfolioName;
    inflexiblePortfolio.buyStock(portfolioName, ticker,quantity,price);
  }


  @Override
  public double costBasisByDate(String portfolioName, String date) {
    if(!checkWhetherFlexible(portfolioName) && !checkWhetherInflexible(portfolioName)){
      throw new IllegalArgumentException("No such portfolio Exists");
    }
    if(checkWhetherFlexible(portfolioName))
      date=getPreviousDate(FlexibleMap,date,portfolioName);
//    else if (checkWhetherInflexible(portfolioName))
//      date=getPreviousDate(InflexibleMap,date,portfolioName);
    return flexiblePortfolio.costBasisByDate(portfolioName, date);
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
    if(checkWhetherInflexible(portfolioName)){
      throw new IllegalArgumentException("Inflexible portfolio with this name already exists");
    } else if (checkWhetherFlexible(portfolioName)) {
      throw new IllegalArgumentException("Flexible portfolio with this name already exists");
    }
    else if(!checkWhetherFlexible(portfolioName) && !checkWhetherInflexible(portfolioName)) {
      creationDateMap.put(portfolioName, date);
      flexiblePortfolio = new FlexiblePortfolio();
      flexiblePortfolio.createPortfolio(portfolioName, date);
    }

  }

  @Override
  public void createLockedPortfolio(String portfolioName, String date) {
    this.portfolioName=portfolioName;
    this.date=date;
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    Date d = new Date();
    String inflexPortfolioDate=dateFormat.format(d);
    if(checkWhetherInflexible(portfolioName)){
      throw new IllegalArgumentException("Inflexible portfolio with this name already exists");
    } else if (checkWhetherFlexible(portfolioName)) {
      throw new IllegalArgumentException("Flexible portfolio with this name already exists");
    }
    else if(!checkWhetherFlexible(portfolioName) && !checkWhetherInflexible(portfolioName)) {
      creationDateMap.put(portfolioName,inflexPortfolioDate);
      inflexiblePortfolio = new InflexiblePortfolio();
      inflexiblePortfolio.createPortfolio(portfolioName,date);
    }

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
  @Override
  public int getLineChartScale(){
    return lineChart.scale();
  }
}
