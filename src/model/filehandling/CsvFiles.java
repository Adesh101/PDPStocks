package model.filehandling;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Class to store csvFiles.
 */
public class CsvFiles implements FileHandling {

  @Override
  public boolean checkLocalData(String ticker) {
    try {
      BufferedReader br = new BufferedReader(new FileReader("./data/" + ticker + ".csv"));
    } catch (Exception ex) {
      return false;
    }
    return true;
  }

  @Override
  public String[] readFromLocalData(String ticker, String date) {
    String line = "";
    String splitBy = ",";
    String[] tempData = new String[20];
    try {
      BufferedReader br = new BufferedReader(new FileReader("./data/" + ticker + ".csv"));
      while ((line = br.readLine()) != null) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = sdf.parse(date);
        sdf.applyPattern("dd-MM-yyyy");
        String difDate = sdf.format(dt);
        if (line.contains(date) || line.contains(difDate)) {
          tempData = line.split(splitBy);
          break;
        }
      }
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }
    return tempData;
  }

  @Override
  public void addToLocalData(String ticker, StringBuilder data) {
    try {
      BufferedWriter bw = new BufferedWriter(
          new OutputStreamWriter(new FileOutputStream("./data/" + ticker + ".csv"), "UTF-8"));
      StringBuffer oneLine = new StringBuffer();
      oneLine.append(data);
      bw.write(oneLine.toString());
      bw.flush();
      bw.close();
    } catch (IOException e) {
      System.out.println("");
    }
  }

  @Override
  public boolean isTickerValid(String ticker) {
    String line = "";
    try {
      BufferedReader br = new BufferedReader(new FileReader("./sticker/stickers.txt"));
      while ((line = br.readLine()) != null) {
        if (line.equalsIgnoreCase(ticker)) {
          return true;
        }
      }
    } catch (Exception ex) {
      return false;
    }
    return false;
  }

  @Override
  public void updateFile(String file, String data) {
    Path path = Paths.get("./data/" + file + ".csv");
    List<String> lines = null;
    try {
      lines = Files.readAllLines(path, StandardCharsets.UTF_8);
      lines.add(1, data);
      Files.write(path, lines, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String getMostRecentDate(String file) {
    String currentLine = "";
    String date = "";
    try {
      BufferedReader br = new BufferedReader(new FileReader("./data/" + file + ".csv"));
      while ((currentLine = br.readLine()) != null) {
        if (!currentLine.contains("timestamp") && !currentLine.equals("")) {
          date = currentLine.split(",")[0];
          if (!Character.isDigit(date.charAt(2))) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date dt = sdf.parse(date);
            sdf.applyPattern("yyyy-MM-dd");
            return sdf.format(dt);
          } else {
            break;
          }
        }
      }
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }
    return date;
  }

  @Override
  public boolean checkIfRecentDateIsCurrentDate(String date) {
    Calendar cal1 = Calendar.getInstance();
    Calendar cal2 = Calendar.getInstance();
    Date currDate = new Date(System.currentTimeMillis());
    cal1.setTime(currDate);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    try {
      cal2.setTime(sdf.parse(date));
    } catch (ParseException e) {
      return false;
    }

    if (cal1.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
      cal1.setTime(new Date(System.currentTimeMillis() - (2 * 24 * 60 * 60 * 1000)));
    } else if (cal1.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
      cal1.setTime(new Date(System.currentTimeMillis() - (3 * 24 * 60 * 60 * 1000)));
    } else {
      cal1.setTime(new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000)));
    }

    return (cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
        || cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR))
        && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
  }

  @Override
  public void writeToCSV(String portfolioName,
      HashMap<String, HashMap<String, HashMap<String, List<String>>>> map, String portfolioType) {
    String csvSeparator = ",";
    try {
      BufferedWriter bw = new BufferedWriter(
          new OutputStreamWriter(new FileOutputStream("./res/" + portfolioName + ".csv"), "UTF-8"));
      for (String individualPortfolioName : map.keySet()) {
        StringBuffer oneLine = new StringBuffer();
        oneLine.append("Portfolio Type");
        oneLine.append(csvSeparator);
        oneLine.append(portfolioType);
        oneLine.append("\n");
        oneLine.append("Portfolio Name");
        oneLine.append(csvSeparator);
        oneLine.append(portfolioName);
        oneLine.append("\n");
        for (String date : map.get(individualPortfolioName).keySet()) {
          oneLine.append("Date");
          oneLine.append(csvSeparator);
          oneLine.append(date);
          oneLine.append("\n");
          oneLine.append("Stock");
          oneLine.append(csvSeparator);
          oneLine.append("Quantity");
          oneLine.append(csvSeparator);
          oneLine.append("Price");
          oneLine.append(csvSeparator);
          oneLine.append("Total");
          oneLine.append(csvSeparator);
          oneLine.append("\n");
          for (String stock : map.get(individualPortfolioName).get(date).keySet()) {
            oneLine.append(stock);
            oneLine.append(csvSeparator);
            for (String metaData : map.get(individualPortfolioName).get(date).get(stock)) {
              oneLine.append(metaData);
              oneLine.append(csvSeparator);
            }
            oneLine.append("\n");
          }
        }
        bw.write(oneLine.toString());
        bw.newLine();
      }
      bw.flush();
      bw.close();
    } catch (IOException e) {
      System.out.println("");
    }
  }

  @Override
  public int checkMapType(String fileName) {
    String line = "";
    String splitBy = ",";
    try {
      BufferedReader br = new BufferedReader(new FileReader(fileName));
      while ((line = br.readLine()) != null) {
        String[] portfolioNames = line.split(splitBy);
        if (portfolioNames.length == 2) {
          if (portfolioNames[1].equals("Flexible")) {
            return 1;
          } else if (portfolioNames[1].equals("Inflexible")) {
            return 2;
          }
        }
      }
    } catch (Exception ex) {
      return 0;
    }
    return 0;
  }

  @Override
  public HashMap<String, HashMap<String, HashMap<String, List<String>>>> readFromFile(
      String fileName) {
    String line = "";
    String splitBy = ",";
    StringBuilder input = new StringBuilder();
    HashMap<String, HashMap<String, HashMap<String, List<String>>>> map = new HashMap<>();
    try {
      BufferedReader br = new BufferedReader(new FileReader(fileName));
      while ((line = br.readLine()) != null) {
        String[] portfolioNames = line.split(splitBy);
        if (portfolioNames.length == 2) {
          if (portfolioNames[1].equals("Flexible") || portfolioNames[1].equals("Inflexible")) {
            while ((line = br.readLine()) != null) {
              input.append(line);
              input.append(",");
            }
            map = flexibleHelper(fileName, input.toString());
            break;
          } else {
            throw new IllegalArgumentException();
          }
        }
      }
    } catch (Exception ex) {
      return new HashMap<String, HashMap<String, HashMap<String, List<String>>>>();
    }
    return map;
  }

  private HashMap<String, HashMap<String, HashMap<String, List<String>>>> flexibleHelper(
      String fileName, String input) {
    String portfolioName = "";
    String date = "";
    String ticker = "";
    input = input.replaceAll(",,,", ",");
    input = input.replaceAll(",,", ",");
    HashMap<String, HashMap<String, HashMap<String, List<String>>>> map = new HashMap<>();
    String[] createPortfolio = input.split(",");
    for (int i = 0; i < createPortfolio.length; ) {
      if (createPortfolio[i].equals("Portfolio Name")) {
        i++;
        portfolioName = createPortfolio[i];
        map.put(portfolioName, new HashMap<String, HashMap<String, List<String>>>());
      } else if (createPortfolio[i].equals("Date")) {
        i++;
        date = createPortfolio[i];
        map.get(portfolioName).put(date, new HashMap<String, List<String>>());
      } else if (createPortfolio[i].equals("Stock")) {
        i += 4;
        while (!createPortfolio[i].equals("Date") || i > createPortfolio.length) {
          ticker = createPortfolio[i];
          map.get(portfolioName).get(date).put(ticker, new ArrayList<String>());
          i++;
          map.get(portfolioName).get(date).get(ticker).add(0, createPortfolio[i]);
          i++;
          map.get(portfolioName).get(date).get(ticker).add(1, createPortfolio[i]);
          i++;
          map.get(portfolioName).get(date).get(ticker).add(2, createPortfolio[i]);
          if (i + 1 < createPortfolio.length) {
            i++;
          } else {
            break;
          }
        }
      } else {
        i++;
      }
    }
    return map;
  }
}
