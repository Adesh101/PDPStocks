package model.plot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

public class LineChart implements ILineChart {

  @Override
  public void plot(String portfolioName, String startDate, String endDate) {
        differenceBetweenDays(startDate, endDate);

  }

  @Override
  public void show() {

  }
  private int differenceBetweenDays(String startDate, String endDate){
    LocalDate start = LocalDate.of(Integer.parseInt(startDate.substring(0,4)),
        Integer.parseInt(startDate.substring(5,7)),Integer.parseInt(startDate.substring(8)));
    LocalDate end = LocalDate.of(Integer.parseInt(endDate.substring(0,4)),
        Integer.parseInt(endDate.substring(5,7)),Integer.parseInt(endDate.substring(8)));

    Period difference = Period.between(start, end);
    int differenceInDays = difference.getDays();
    return differenceInDays;

  }
}
