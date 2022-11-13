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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class csvFiles implements fileHandling {

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
      while((line = br.readLine()) != null) {
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
      System.out.println(ex.getMessage());;
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
      while((line = br.readLine()) != null) {
        if (line.equals(ticker)) {
          return true;
        }
      }
    } catch (Exception ex) {
      System.out.println(ex.getMessage());;
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
      while((currentLine = br.readLine()) != null) {
        if (!currentLine.contains("timestamp") && !currentLine.equals("")) {
          date = currentLine.split(",")[0];
          if(!Character.isDigit(date.charAt(2))) {
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
      System.out.println(ex.getMessage());;
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

    if (cal1.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
      cal1.setTime(new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000)));
    } else if (cal1.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
      cal1.setTime(new Date(System.currentTimeMillis() - (2 * 24 * 60 * 60 * 1000)));
    }

    return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
        cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
  }
}
