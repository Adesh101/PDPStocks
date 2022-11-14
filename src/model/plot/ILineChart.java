package model.plot;

import java.text.ParseException;

public interface ILineChart {
  String plot(String portfolioName, String startDate, String endDate) throws ParseException;
  void show();

}
