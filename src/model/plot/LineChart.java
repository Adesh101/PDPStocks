package model.plot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;
import model.stocks.IStocks;
import model.stocks.Stocks;

/**
 * Class to implement line chart.
 */
public class LineChart implements ILineChart {

  IStocks stocks;
  protected int scale;

  public LineChart() {
    stocks = new Stocks();
    scale = 0;
  }

  @Override
  public TreeMap<String, Integer> plot(HashMap<String, Integer> portfolio, String startDate,
      String endDate)
      throws ParseException {
    TreeMap<String, Integer> result = new TreeMap<>();
    int differenceInDays = differenceBetweenDaysInDays(startDate, endDate);
    int differenceInMonths = differenceBetweenDaysInMonths(startDate, endDate);
    int noOfYears = differenceBetweenDaysInYears(startDate, endDate);
    if (differenceInDays <= 30) {
      result = daySpan(portfolio, startDate, differenceInDays);
    } else if (differenceInDays > 30 && differenceInMonths < 5) {
      int noOfWeeks = 0;
      if (differenceInDays % 7 != 0) { // change. wrong logic
        noOfWeeks = 1;
      }
      noOfWeeks += differenceInDays / 7;
      result = weekSpan(portfolio, startDate, noOfWeeks);
    } else if (differenceInMonths >= 5 && differenceInMonths <= 30) {
      result = monthSpan(portfolio, startDate, differenceInMonths);
    } else if (differenceInMonths > 30 && noOfYears < 5) {
      int noOfMonths = 0;
      if (differenceInMonths % 3 != 0) {
        noOfMonths = 1;               // change. wrong logic
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

  private Date parseDate(String date) {
    Date newDate;
    String checkDate = date;
    String pattern = "yyyy-MM-dd";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    try {
      newDate = simpleDateFormat.parse(checkDate);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    return newDate;
  }

  private int getTotalValue(HashMap<String, Integer> portfolio, String startDate) {
    int tempValue = 0;
    int totalValue = 0;
    for (String ticker : portfolio.keySet()) {
      startDate = stocks.isWeekend(startDate);
      tempValue = (int) (portfolio.get(ticker) * stocks.getPriceByDate(ticker, startDate));
      totalValue += tempValue;
    }
    return totalValue;
  }

  private TreeMap<String, Integer> daySpan(HashMap<String, Integer> portfolio, String startDate,
      int differenceInDays) {
    int totalValue = 0;
    Date sDate = parseDate(startDate);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    int maxValue = Integer.MIN_VALUE;
    TreeMap<String, Integer> map = new TreeMap<>();

    while (differenceInDays > 0) {
      totalValue = getTotalValue(portfolio, startDate);
      map.put(startDate, totalValue);
      maxValue = Math.max(maxValue, totalValue);
      sDate = increaseDate(sDate, 1);
      startDate = simpleDateFormat.format(sDate);
      differenceInDays--;
    }
    this.scale = maxValue / 10;
    return getCount(map, maxValue / 10);
  }

  private TreeMap<String, Integer> weekSpan(HashMap<String, Integer> portfolio, String startDate,
      int noOfWeeks) {
    int totalValue = 0;
    Date sDate = parseDate(startDate);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    int maxValue = Integer.MIN_VALUE;
    TreeMap<String, Integer> map = new TreeMap<>();

    while (noOfWeeks > 0) {
      totalValue = getTotalValue(portfolio, startDate);
      map.put(startDate, totalValue);
      maxValue = Math.max(maxValue, totalValue);
      sDate = increaseDate(sDate, 7);
      startDate = simpleDateFormat.format(sDate);
      noOfWeeks--;
    }
    this.scale = maxValue / 10;
    return getCount(map, maxValue / 10);
  }

  private TreeMap<String, Integer> monthSpan(HashMap<String, Integer> portfolio, String startDate,
      int noOfMonths) {
    Date sDate = parseDate(startDate);
    int totalValue = 0;
    String keyValue = "";
    int maxValue = Integer.MIN_VALUE;
    TreeMap<String, Integer> map = new TreeMap<>();

    while (noOfMonths > 0) {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM yyyy");
      keyValue += simpleDateFormat.format(sDate).toUpperCase();
      totalValue = getTotalValue(portfolio, startDate);
      map.put(keyValue, totalValue);
      maxValue = Math.max(maxValue, totalValue);
      sDate = increaseDateMonthly(sDate, 1);
      simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
      startDate = simpleDateFormat.format(sDate);
      keyValue = "";
      noOfMonths--;
    }
    this.scale = maxValue / 10;
    return getCount(map, maxValue / 10);
  }

  private TreeMap<String, Integer> threeMonthSpan(HashMap<String, Integer> portfolio,
      String startDate,
      int noOfMonths) {
    Date sDate = parseDate(startDate);
    int totalValue = 0;
    String keyValue = "";
    int maxValue = Integer.MIN_VALUE;
    TreeMap<String, Integer> map = new TreeMap<>();

    while (noOfMonths > 0) {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM yyyy");
      keyValue += simpleDateFormat.format(sDate).toUpperCase();
      totalValue = getTotalValue(portfolio, startDate);
      map.put(keyValue, totalValue);
      maxValue = Math.max(maxValue, totalValue);
      sDate = increaseDateMonthly(sDate, 3);
      simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
      startDate = simpleDateFormat.format(sDate);
      keyValue = "";
      noOfMonths--;
    }
    this.scale = maxValue / 10;
    return getCount(map, maxValue / 10);
  }

  private TreeMap<String, Integer> yearSpan(HashMap<String, Integer> portfolio, String startDate,
      int noOfYears) {
    Date sDate = parseDate(startDate);
    int totalValue = 0;
    String keyValue = "";
    int maxValue = Integer.MIN_VALUE;
    TreeMap<String, Integer> map = new TreeMap<>();

    while (noOfYears > 0) {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
      keyValue += simpleDateFormat.format(sDate).toUpperCase();
      totalValue = getTotalValue(portfolio, startDate);
      map.put(keyValue, totalValue);
      maxValue = Math.max(maxValue, totalValue);
      sDate = increaseDateYearly(sDate);
      simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
      startDate = simpleDateFormat.format(sDate);
      keyValue = "";
      noOfYears--;
    }
    this.scale = maxValue / 10;
    return getCount(map, maxValue / 10);
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

  private Date increaseDateMonthly(Date date, int noOfMonths) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Calendar c = Calendar.getInstance();
    try {
      c.setTime(date);
      c.add(Calendar.MONTH, noOfMonths);
      date = simpleDateFormat.parse(simpleDateFormat.format(c.getTime()));
    } catch (ParseException e) {
      System.out.println(e.getMessage());
    }
    return date;
  }

  private Date increaseDateYearly(Date date) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Calendar c = Calendar.getInstance();
    try {
      c.setTime(date);
      c.add(Calendar.YEAR, 1);
      date = simpleDateFormat.parse(simpleDateFormat.format(c.getTime()));
    } catch (ParseException e) {
      System.out.println(e.getMessage());
    }
    return date;
  }

  @Override
  public int scale() {
    return scale;
  }
}
