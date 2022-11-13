package model.plot;

import java.time.LocalDate;

public interface ILineChart {
  void plot(String portfolioName, String startDate, String endDate);
  void show();

}
