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
import model.portfolio.InflexiblePortfolio;
import model.stocks.IStocks;
import model.stocks.Stocks;

public class LineChart implements ILineChart {

  IStocks stocks;
  public LineChart () {
    stocks = new Stocks();
  }


  @Override
  public TreeMap<String, Integer> plot(HashMap<String, Integer> portfolio, String startDate, String endDate)
      throws ParseException {
    TreeMap<String, Integer> result = new TreeMap<>();
    int differenceInDays = differenceBetweenDaysInDays(startDate, endDate);
    int differenceInMonths = differenceBetweenDaysInMonths(startDate, endDate);
    int noOfYears = differenceBetweenDaysInYears(startDate, endDate);
    if (differenceInDays <= 30) {
      result = daySpan(portfolio, startDate, differenceInDays);
    } else if (differenceInDays > 30 && differenceInMonths < 5) {
      int noOfWeeks = 0;
      if (differenceInDays % 7 != 0) {
        noOfWeeks = 1;
      }
      noOfWeeks += differenceInDays / 7;
      result = weekSpan(portfolio, startDate, noOfWeeks);
    } else if (differenceInMonths >= 5 && differenceInMonths <= 30) {
      result = monthSpan(portfolio, startDate, differenceInMonths);
    } else if (differenceInMonths > 30 && noOfYears < 5) {
      int noOfMonths = 0;
      if (differenceInDays % 3 != 0) {
        noOfMonths = 1;
      }
      noOfMonths += differenceInMonths / 3;
      result = threeMonthSpan(portfolio, startDate, noOfMonths);
    } else if (noOfYears >= 5 && noOfYears <= 30) {
      result = yearSpan(portfolio, startDate, noOfYears);
    } else {
      System.out.println("Enter valid dates");
    }
    return result;
  }

  private TreeMap<String, Integer> daySpan(HashMap<String, Integer> portfolio, String startDate,
      int differenceInDays) {
    int tempValue = 0;
    int totalValue = 0;
    String keyValue = "";
    Date sDate;
    String pattern = "yyyy-MM-dd";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    int maxValue = Integer.MIN_VALUE;
    int minValue = Integer.MAX_VALUE;

    TreeMap<String, Integer> map = new TreeMap<>();

    String checkDate = startDate;
    try {
      sDate = simpleDateFormat.parse(checkDate);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }

    while (differenceInDays > 0) {
      keyValue = startDate;

      for (String ticker : portfolio.keySet()) {
        startDate = stocks.isWeekend(startDate);
        tempValue = (int) (portfolio.get(ticker) * stocks.getPriceByDate(ticker, startDate));
        totalValue += tempValue;
      }
      map.put(keyValue, totalValue);
      maxValue = Math.max(maxValue, totalValue);
      minValue = Math.min(minValue, totalValue);

      sDate = increaseDate(sDate, 1);
      startDate = simpleDateFormat.format(sDate);

      totalValue = 0;
      differenceInDays--;
    }
    return getCount(map, maxValue/10);
  }

  private TreeMap<String, Integer> weekSpan(HashMap<String, Integer> portfolio, String startDate,
      int noOfWeeks) {
    int tempValue = 0;
    int totalValue = 0;
    String keyValue = "";
    int maxValue = Integer.MIN_VALUE;
    TreeMap<String, Integer> map = new TreeMap<>();

    while (noOfWeeks > 0) {
      keyValue = startDate.length() > 2 ? startDate.substring(startDate.length() - 2) : startDate;
      keyValue += " ";
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM");
      keyValue += simpleDateFormat.format(startDate).toUpperCase();

      for (String ticker : portfolio.keySet()) {
        startDate = stocks.isWeekend(startDate);
        tempValue = (int) (portfolio.get(ticker) * stocks.getPriceByDate(ticker, startDate));
        totalValue += tempValue;
      }
      map.put(keyValue, totalValue);
      maxValue = Math.max(maxValue, totalValue);

     // startDate = increaseDate(startDate, 7);

      totalValue = 0;
      noOfWeeks--;
    }
    return getCount(map, maxValue / 50);
  }

  private TreeMap<String, Integer> monthSpan(HashMap<String, Integer> portfolio, String startDate,
      int noOfMonths) {
    int tempValue = 0;
    int totalValue = 0;
    String keyValue = "";
    int maxValue = Integer.MIN_VALUE;
    TreeMap<String, Integer> map = new TreeMap<>();

    while (noOfMonths > 0) {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM yyyy");
      keyValue += simpleDateFormat.format(startDate).toUpperCase();

      for (String ticker : portfolio.keySet()) {
        startDate = stocks.isWeekend(startDate);
        tempValue = (int) (portfolio.get(ticker) * stocks.getPriceByDate(ticker, startDate));
        totalValue += tempValue;
      }
      map.put(keyValue, totalValue);
      maxValue = Math.max(maxValue, totalValue);

      startDate = increaseDateMonthly(startDate, 1);

      totalValue = 0;
      noOfMonths--;
    }
    return getCount(map, maxValue / 50);
  }

  private TreeMap<String, Integer> threeMonthSpan(HashMap<String, Integer> portfolio, String startDate,
      int noOfMonths) {
    int tempValue = 0;
    int totalValue = 0;
    String keyValue = "";
    int maxValue = Integer.MIN_VALUE;
    TreeMap<String, Integer> map = new TreeMap<>();

    while (noOfMonths > 0) {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM yyyy");
      keyValue += simpleDateFormat.format(startDate).toUpperCase();

      for (String ticker : portfolio.keySet()) {
        startDate = stocks.isWeekend(startDate);
        tempValue = (int) (portfolio.get(ticker) * stocks.getPriceByDate(ticker, startDate));
        totalValue += tempValue;
      }
      map.put(keyValue, totalValue);
      maxValue = Math.max(maxValue, totalValue);

      startDate = increaseDateMonthly(startDate, 3);

      totalValue = 0;
      noOfMonths--;
    }
    return getCount(map, maxValue / 50);
  }

  private TreeMap<String, Integer> yearSpan(HashMap<String, Integer> portfolio, String startDate,
      int noOfYears) {
    int tempValue = 0;
    int totalValue = 0;
    String keyValue = "";
    int maxValue = Integer.MIN_VALUE;
    TreeMap<String, Integer> map = new TreeMap<>();

    while (noOfYears > 0) {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM yyyy");
      keyValue += simpleDateFormat.format(startDate).toUpperCase();

      for (String ticker : portfolio.keySet()) {
        startDate = stocks.isWeekend(startDate);
        tempValue = (int) (portfolio.get(ticker) * stocks.getPriceByDate(ticker, startDate));
        totalValue += tempValue;
      }
      map.put(keyValue, totalValue);
      maxValue = Math.max(maxValue, totalValue);

      startDate = increaseDateYearly(startDate);

      totalValue = 0;
      noOfYears--;
    }
    return getCount(map, maxValue / 50);
  }

  private int differenceBetweenDaysInDays(String startDate, String endDate) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    int difference = 0;
    try {
      Date startD = formatter.parse(startDate);
      Date endD = formatter.parse(endDate);
      difference = (int) ((endD.getTime() - startD.getTime()) / (1000 * 60 * 60 * 24));
    } catch (ParseException ex) {
      System.out.println(ex.getMessage());
    }
    return difference;
  }

  private int differenceBetweenDaysInMonths(String startDate, String endDate) {
    long monthsBetween = ChronoUnit.MONTHS.between(
        YearMonth.from(LocalDate.parse(startDate)),
        YearMonth.from(LocalDate.parse(endDate))
    );
    return (int) monthsBetween;
  }

  private int differenceBetweenDaysInYears(String startDate, String endDate) {
    long yearsBetween = ChronoUnit.YEARS.between(
        YearMonth.from(LocalDate.parse(startDate)),
        YearMonth.from(LocalDate.parse(endDate))
    );
    return (int) yearsBetween;
  }

  private TreeMap<String, Integer> getCount(TreeMap<String, Integer> treeMap, int maxValue) {
    for (String stock : treeMap.keySet()) {
      treeMap.put(stock, treeMap.get(stock) / maxValue);
    }
    return treeMap;
  }

  private Date increaseDate(Date date, int noOfDays) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Calendar c = Calendar.getInstance();
    try {
      c.setTime(date);
      c.add(Calendar.DATE, noOfDays);
      date = simpleDateFormat.parse(simpleDateFormat.format(c.getTime()));
    } catch (ParseException e) {
      System.out.println(e.getMessage());
    }
    return date;
  }

  private String increaseDateMonthly(String date, int noOfMonths) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Calendar c = Calendar.getInstance();
    try {
      c.setTime(simpleDateFormat.parse(date));
      c.add(Calendar.MONTH, noOfMonths);
      date = simpleDateFormat.format(c.getTime());
    } catch (ParseException e) {
      System.out.println(e.getMessage());
    }
    return date;
  }

  private String increaseDateYearly(String date) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Calendar c = Calendar.getInstance();
    try {
      c.setTime(simpleDateFormat.parse(date));
      c.add(Calendar.YEAR, 1);
      date = simpleDateFormat.format(c.getTime());
    } catch (ParseException e) {
      System.out.println(e.getMessage());
    }
    return date;
  }
}
