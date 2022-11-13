package model.plot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.List;

public class LineChart implements ILineChart {

  @Override
  public void plot(String portfolioName, String startDate, String endDate) {
    int differenceInDays = differenceBetweenDaysInDays(startDate, endDate);
    int differenceInMonths = differenceBetweenDaysInMonths(startDate, endDate);
    int differenceInYears = diffferenceBetweenDaysInYears(startDate, endDate);
    if(differenceInDays>=5 || differenceInDays<=30){

    } else if (differenceInDays>30 || differenceInDays<900) {

    } //else if () {

    }


  @Override
  public void show() {

  }
  private List<String> daySpan(){
    return null;
  }
  private List<String> monthSpan(){
    return null;
  }
  private List<String> threeMonthSpan(){
    return null;
  }
  private List<String> yearSpan(){
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
  private int diffferenceBetweenDaysInYears(String startDate, String endDate){
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
