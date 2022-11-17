package model.plot;

import java.text.ParseException;
import java.util.HashMap;
import java.util.TreeMap;

public interface ILineChart {
  int scale();
  TreeMap<String, Integer> plot(HashMap<String, Integer> portfolio, String startDate, String endDate) throws ParseException;
}
