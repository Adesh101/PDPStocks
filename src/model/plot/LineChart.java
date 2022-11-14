package model.plot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import model.portfolio.FlexiblePortfolio;
import model.portfolio.IFlexiblePortfolio;

public class LineChart implements ILineChart {
  HashMap<String, HashMap<String, Double>> costBasisMap = new HashMap<String, HashMap<String , Double>>();
  IFlexiblePortfolio FPortfolio = new FlexiblePortfolio();
  private String portfolioName;

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
  private void getCostBasisMap(){
    costBasisMap= FPortfolio.returnCostBasisMap();
  }
  private String daySpan(String startDate, String endDate, int difference) throws ParseException {
    String[][] data = new String[difference][2];
    LocalDate start = LocalDate.of(Integer.parseInt(startDate.substring(0,4)),
        Integer.parseInt(startDate.substring(5,7)),Integer.parseInt(startDate.substring(8)));
    LocalDate end = LocalDate.of(Integer.parseInt(endDate.substring(0,4)),
        Integer.parseInt(endDate.substring(5,7)),Integer.parseInt(endDate.substring(8)));
    int i=0;
    Double max=0.00,min=Double.MAX_VALUE;
    Double[] costArr=new Double[difference];
    for(LocalDate date=start; !date.isAfter(end);date=date.plusDays(1)){
      costArr[i]= costBasisMap.get(portfolioName).get(date);
      if(costArr[i]>max){
        max=costArr[i];
      }
      if(costArr[i]<min){
        min=costArr[i];
      }
      i++;
    }













    return null;
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

  private int differenceBetweenDaysInDays(String startDate, String endDate){
    Period difference = LocalDateHelper(startDate, endDate);

    int differenceInDays = difference.getDays();
    return differenceInDays;

  }
  private int differenceBetweenDaysInMonths(String startDate, String endDate){
    Period difference = LocalDateHelper(startDate, endDate);
    int differenceInMonths = difference.getMonths();
    return differenceInMonths;
  }
  private int differenceBetweenDaysInYears(String startDate, String endDate){
    Period difference = LocalDateHelper(startDate, endDate);
    int differenceInYears = difference.getYears();
    return differenceInYears;
  }

  private Period LocalDateHelper(String startDate, String endDate) {
    LocalDate start = LocalDate.of(Integer.parseInt(startDate.substring(0,4)),
        Integer.parseInt(startDate.substring(5,7)),Integer.parseInt(startDate.substring(8)));
    LocalDate end = LocalDate.of(Integer.parseInt(endDate.substring(0,4)),
        Integer.parseInt(endDate.substring(5,7)),Integer.parseInt(endDate.substring(8)));

    Period difference = Period.between(start, end);
    return difference;
  }
}
