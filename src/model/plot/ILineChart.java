package model.plot;

import java.text.ParseException;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Class to plot line chart.
 */
public interface ILineChart {

  /**
   * Method to check scale.
   *
   * @return: int values
   */
  int scale();

  /**
   * Method to plot graph.
   *
   * @param: portfolio
   * @param: startDate
   * @param: endDate
   * @return: treemap with plot
   * @throws: ParseException
   */
  TreeMap<String, Integer> plot(HashMap<String, Integer> portfolio, String startDate,
      String endDate) throws ParseException;
}
