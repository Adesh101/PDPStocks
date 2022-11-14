package model.portfolio;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class FlexiblePortfolio implements IFlexiblePortfolio {
  protected HashMap<String, HashMap<String, List<String>>> portfolios
      = new HashMap<String, HashMap<String, List<String>>>();
  protected HashMap<String, HashMap<String, HashMap<String, List<String>>>> map = new HashMap<>();
  protected HashMap<String, HashMap<String, Double>> costBasis
      = new HashMap<String, HashMap<String, Double>>();
  protected String portfolioName;
  protected double totalValue;

  public FlexiblePortfolio(){
    this.portfolioName="";
    this.totalValue=0;
  }

  @Override
  public void createPortfolio(String portfolioName, String date) {
    if (checkPortfolioAlreadyExists(portfolioName)) {
        throw new IllegalArgumentException(
            "PORTFOLIO ALREADY PRESENT. ADD STOCKS.");
    }
    //HashMap<String, HashMap<String, HashMap<String, List<String>>>> map = new HashMap<>();

   this.map.put(portfolioName, new HashMap<>());
    //this.portfolios.put(portfolioName, new HashMap<String, List<String>>());
    this.costBasis.put(portfolioName, new HashMap<>());
    this.costBasis.get(portfolioName).put(date, 0.00);
    this.portfolioName = portfolioName;
  }

  @Override
  public boolean checkPortfolioAlreadyExists(String name) {
    return portfolios.containsKey(name);
  }

  @Override
  public void buyStock(String portfolioName, String ticker, int quantity, double price, String date) {
    if (!map.containsKey(portfolioName)) {
      throw new IllegalArgumentException("Enter valid portfolio name.");
    }
    if(map.get(portfolioName).containsKey(date)) {
      if (map.get(portfolioName).get(date).containsKey(ticker)) {
        //if (portfolios.get(portfolioName).containsKey(ticker)) {
        int existingNoOfStocks = Integer.parseInt(map.get(portfolioName).get(date).get(ticker).get(0));
        map.get(portfolioName).get(date).get(ticker)
            .set(0, String.valueOf(existingNoOfStocks + quantity));
        double existingPrice = Double.parseDouble(map.get(portfolioName).get(date).get(ticker).get(1));
        map.get(portfolioName).get(date).get(ticker)
            .set(1, String.valueOf((existingPrice + price) / 2));
        double existingTotalStockValue = Double.parseDouble(
            map.get(portfolioName).get(date).get(ticker).get(2));
        map.get(portfolioName).get(date).get(ticker)
            .set(2, String.valueOf(existingTotalStockValue + (quantity * price)));
//        int existingNoOfStocks = Integer.parseInt(portfolios.get(portfolioName).get(ticker).get(0));
//        portfolios.get(portfolioName).get(ticker)
//            .set(0, String.valueOf(existingNoOfStocks + quantity));
//        double existingPrice = Double.parseDouble(portfolios.get(portfolioName).get(ticker).get(1));
//        portfolios.get(portfolioName).get(ticker)
//            .set(1, String.valueOf((existingPrice + price) / 2));
//        double existingTotalStockValue = Double.parseDouble(
//            portfolios.get(portfolioName).get(ticker).get(2));
//        portfolios.get(portfolioName).get(ticker)
//            .set(2, String.valueOf(existingTotalStockValue + (quantity * price)));
        this.totalValue = totalValue + Math.round(quantity * price);
      }
      else{
        addStockDataHelper(portfolioName, ticker, quantity, price, date);
      }
    }else {
      map.get(portfolioName).put(date, new HashMap<>());
      addStockDataHelper(portfolioName, ticker, quantity, price, date);
//      portfolios.get(portfolioName).put(ticker, new ArrayList<String>());
//      portfolios.get(portfolioName).get(ticker).add(String.valueOf(quantity));
//      portfolios.get(portfolioName).get(ticker).add(String.valueOf(price));
//      portfolios.get(portfolioName).get(ticker).add(String.valueOf(quantity * price));
      this.totalValue = totalValue + (quantity * price);
    }
//    if(costBasis.get(portfolioName).containsKey(date)){
//      //costBasis.get(portfolioName).
//    }
//    else {
//      costBasis.get(portfolioName).put(date, totalValue);
//    }
//    costBasis.put(portfolioName, costBasis.get());
    costBasis.get(portfolioName).put(date,totalValue); // check if overwrties
    //costBasis.get(portfolioName).set(0,totalValue);

  }

  private void addStockDataHelper(String portfolioName, String ticker, int quantity, double price,
      String date) {
    map.get(portfolioName).get(date).put(ticker, new ArrayList<>());
    map.get(portfolioName).get(date).get(ticker).add(String.valueOf(quantity));
    map.get(portfolioName).get(date).get(ticker).add(String.valueOf(price));
    map.get(portfolioName).get(date).get(ticker).add(String.valueOf(quantity*price));
  }

  @Override
  public void sellStock(String portfolioName, String ticker, int quantity, double price, String date) {
    if(!map.containsKey(portfolioName)){
      throw new IllegalArgumentException("Enter valid portfolio name.");
    }
    String prevdate=getPreviousDate(date, portfolioName);
    if (map.get(portfolioName).get(prevdate).containsKey(ticker)) {
      int existingNoOfStocks = Integer.parseInt(map.get(portfolioName).get(prevdate).get(ticker).get(0));
      if(quantity==existingNoOfStocks){
        map.get(portfolioName).remove(ticker);
      } else if (quantity>existingNoOfStocks) {
        throw new IllegalArgumentException("NOT ENOUGH STOCKS PRESENT IN PORTFOLIO");
      } else {
        map.get(portfolioName).get(prevdate).get(ticker)
            .set(0, String.valueOf(existingNoOfStocks - quantity));
        double existingPrice = Double.parseDouble(map.get(portfolioName).get(prevdate).get(ticker).get(1));
        map.get(portfolioName).get(prevdate).get(ticker)
            .set(1, String.valueOf((existingPrice + price) / 2)); // chrck
        double existingTotalStockValue = Double.parseDouble(
            map.get(portfolioName).get(prevdate).get(ticker).get(2));
        map.get(portfolioName).get(prevdate).get(ticker)
            .set(2, String.valueOf(existingTotalStockValue - (quantity * price)));
        this.totalValue = totalValue - Math.round(quantity * price);
      }
    } else {
       throw new IllegalArgumentException("No such stock present.");
    }
  }

  @Override
  public double costBasisByDate(String portFolioName, String date) {
    return costBasis.get(portfolioName).get(date);

  }

  @Override
  public double portfolioValue(String portfolioName, String date) {
    double value=0;
    List<String> dates=new ArrayList<>();
    ListIterator<String> dateIterator = new ArrayList<String>(map.get(portfolioName).keySet()).listIterator();
    while (dateIterator.hasNext()){
      dates.add(dateIterator.next()); // check for last element
    }
    Collections.sort(dates);
    if(dates.get(0).equals(date)){
      return value;
    }
    for(String stock: map.get(portfolioName).get(date).keySet()){
      value+=Double.parseDouble(map.get(portfolioName).get(date).get(stock).get(2));
    }
    return value;
  }

//  @Override
//  public HashMap<String, HashMap<String, List<String>>> returnMap() {
//    return map;
//  }
  @Override
  public HashMap<String, HashMap<String, HashMap<String, List<String>>>> returnMap(){
    return map;
  }

  @Override
  public HashMap<String, HashMap<String, Double>> returnCostBasisMap() {
    return costBasis;
  }

  private String getPreviousDate(String currentDate, String name){
    List<String> dates=new ArrayList<>();
    ListIterator<String> dateIterator = new ArrayList<String>(map.get(name).keySet()).listIterator();
    if(map.get(name).keySet().size()==1){
      //Set<String> date=map.get(name).keySet();
      return map.get(name).keySet().iterator().next();
    }
    while (dateIterator.hasNext()){
      dates.add(dateIterator.next()); // check for last element
    }
    Collections.sort(dates);
    String previousDate=dateIterator.next();
//    if(previousDate.equals(currentDate)){
//      return currentDate;
//    }
    if(dates.get(0).equals(currentDate)){
      return currentDate;
    }
    for(int i=1;i<map.get(name).keySet().size();i++){
      if(dates.get(i).equals(currentDate)){
        return dates.get(i-1);
      }
    }
//    for(int i=0;i<map.get(name).keySet().size();i++) {
//      previousDate = dateIterator.next();
//      if (previousDate.equals(currentDate)) {
//        return dateIterator.previous();
//      }
//
//    }
    return "";
  }
}
