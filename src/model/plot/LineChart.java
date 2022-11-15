package model.plot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import model.operation.IOperation;
import model.operation.Operation;
import model.portfolio.FlexiblePortfolio;
import model.portfolio.IFlexiblePortfolio;
import model.portfolio.IInflexiblePortfolio;
import model.portfolio.InflexiblePortfolio;
import model.stocks.IStocks;
import model.stocks.Stocks;

public class LineChart implements ILineChart {
  HashMap<String, HashMap<String, Double>> costBasisMap = new HashMap<String, HashMap<String , Double>>();
  protected HashMap<String, HashMap<String, HashMap<String, List<String>>>> portfolioMap
      = new HashMap<String, HashMap<String, HashMap<String, List<String>>>>();
  IFlexiblePortfolio FPortfolio = new FlexiblePortfolio();
  private String portfolioName;
  private IStocks stocks = new Stocks();


  @Override
  public String plot(String portfolioName, String startDate, String endDate) throws ParseException {
    getCostBasisMap();
    this.portfolioName=portfolioName;
    String chart="";
    //StringBuilder sb=new StringBuilder();
    int differenceInDays = differenceBetweenDaysInDays(startDate, endDate);
    int differenceInMonths = differenceBetweenDaysInMonths(startDate, endDate);
    int differenceInYears = differenceBetweenDaysInYears(startDate, endDate);
    if(differenceInDays<=30){
      chart=daySpan(startDate, endDate, differenceInDays);

    } else if (differenceInDays>30 && differenceInMonths<5){
      int diff= (differenceInDays/7) + 1;
      chart=weekSpan(startDate, endDate, diff);

    } else if (differenceInMonths>=5 && differenceInMonths<=30) {
      chart=monthSpan(startDate, endDate, differenceInMonths);
    } else if (differenceInMonths>30 && differenceInYears<5) {
      int diff= (differenceInMonths/3)+1;
      chart=threeMonthSpan(startDate, endDate, diff);
    } else if (differenceInYears>=5 && differenceInYears<=30) {
      chart=yearSpan(startDate, endDate, differenceInYears);
    }
    else {
      // handle this
    }
    return chart;
  }


  @Override
  public void show() {

  }
  private HashMap<String, Integer> voidgetportfolioMap(HashMap<String, Integer> map) {

  }
  private void getCostBasisMap(){
    costBasisMap= FPortfolio.returnCostBasisMap();
  }

  public String chart(String portfolioName, String startDate, String endDate) {
    HashMap<String, String> map = new HashMap<String, String>();
    int differenceInDays = differenceBetweenDaysInDays(startDate, endDate);
    int differenceInMonths = differenceBetweenDaysInMonths(startDate, endDate);
    int differenceInYears = differenceBetweenDaysInYears(startDate, endDate);
    if(differenceInDays>30 && differenceInMonths<5){
      chart = weekSpan(startDate, endDate, differenceInDays);
    } else if (differenceInMonths>=5 && differenceInMonths<=30) {
      chart = monthSpan(startDate, endDate, differenceInMonths);
    } else if (differenceInMonths>30 && differenceInYears<5) {
      int diff = (differenceInMonths/3)+1;
      chart = threeMonthSpan(startDate, endDate, diff);
    } else if (differenceInYears>=5 && differenceInYears<=30) {
      chart=yearSpan(startDate, endDate, differenceInYears);
    }
    else {
      // handle this
    }
    return chart;
    return null;
  }

  private TreeMap<String, Double> daySpan(HashMap<String, Integer> portfolio, String startDate,
      String endDate, int difference) {
    TreeMap<String, Double> map = new TreeMap<>();
    String keyValue = "";
    Date sDate;
    Date eDate;
    String pattern = "yyyy-MM-dd";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    try {
      sDate = simpleDateFormat.parse(startDate);
      eDate = simpleDateFormat.parse(endDate);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }

    simpleDateFormat = new SimpleDateFormat("dd");
    int startValue = Integer.parseInt(simpleDateFormat.format(sDate));
    int endValue = Integer.parseInt(simpleDateFormat.format(eDate));

    String checkDate = startDate;
    double totalValue = 0, tempValue = 0, maxValue = Double.MIN_VALUE;
    while (startValue != endValue) {
      keyValue = "";
      keyValue = startValue + " ";

      simpleDateFormat = new SimpleDateFormat("MMM");
      keyValue += simpleDateFormat.format(sDate).toUpperCase();

      for (String ticker : portfolio.keySet()) {
        tempValue = portfolio.get(ticker) * stocks.getPriceByDate(ticker, checkDate);
        totalValue += tempValue;
      }

      maxValue = Math.max(maxValue, totalValue);
      maxValue /= 50;

      map.put(keyValue, totalValue/maxValue);
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      Calendar c = Calendar.getInstance();
      try {
        c.setTime(sdf.parse(checkDate));
        c.add(Calendar.DATE, 1);
        checkDate = sdf.format(c.getTime());
      } catch (ParseException e) {
        throw new RuntimeException(e);
      }

      startValue++;
    }
    return map;
  }

  private String weekSpan(String startDate, String endDate, int difference){
    StringBuilder sb=new StringBuilder();
    return null;
  }
  private String monthSpan(String startDate, String endDate, int difference){
    StringBuilder sb=new StringBuilder();
    return null;
  }
  private String threeMonthSpan(String startDate, String endDate, int difference){
    StringBuilder sb=new StringBuilder();
    return null;
  }
  private String yearSpan(String startDate, String endDate, int difference){
    StringBuilder sb=new StringBuilder();
    return null;
  }

  private int differenceBetweenDaysInDays(String startDate, String endDate) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    int difference = 0;
    try {
      Date startD = formatter.parse(startDate);
      Date endD = formatter.parse(endDate);
      difference =  (int)( (endD.getTime() - startD.getTime()) / (1000 * 60 * 60 * 24));
    } catch (ParseException ex) {
      System.out.println(ex.getMessage());
    }
    return difference;
  }

  private int differenceBetweenDaysInMonths(String startDate, String endDate){
    long monthsBetween = ChronoUnit.MONTHS.between(
        YearMonth.from(LocalDate.parse(startDate)),
        YearMonth.from(LocalDate.parse(endDate))
    );
    return (int) monthsBetween;
  }

  private int differenceBetweenDaysInWeeks(String startDate, String endDate){
    long weeksBetween = ChronoUnit.WEEKS.between(
        YearMonth.from(LocalDate.parse(startDate)),
        YearMonth.from(LocalDate.parse(endDate))
    );
    return (int) weeksBetween;
  }

  private int differenceBetweenDaysInYears(String startDate, String endDate){
    long yearsBetween = ChronoUnit.YEARS.between(
        YearMonth.from(LocalDate.parse(startDate)),
        YearMonth.from(LocalDate.parse(endDate))
    );
    return (int) yearsBetween;
  }
}
