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
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class csvFiles implements fileHandling {

  @Override
  public boolean checkLocalData(String ticker) {
    //Currently, just checking for file, will see how to check for date as well
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
        //if (line.contains(date)) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = sdf.parse(date);
        sdf.applyPattern("dd-MM-yyyy");
        String difDate = sdf.format(dt);
        if (line.contains(date) || line.contains(difDate)) {
          tempData = line.split(splitBy);
          break;
        }
      }
     // return tempData;
    } catch (Exception ex) {
      System.out.println(ex.getMessage());;
    }
   // return new String[0];
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
        if (line.equalsIgnoreCase(ticker)) {
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
    // Path of file
    Path path = Paths.get("./data/" + file + ".csv");
    // Read all lines
    List<String> lines = null;
    try {
      lines = Files.readAllLines(path, StandardCharsets.UTF_8);
      lines.add(1, data);
      //lines.set(2, data);
      Files.write(path, lines, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

//    String line = "";
//    try {
//      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
//          new FileOutputStream(file, true), "UTF-8"));
//      StringBuffer oneLine = new StringBuffer();
//      oneLine.append(data);
//      bw.write(oneLine.toString());
//      bw.flush();
//      bw.close();
//    } catch (IOException e) {
//      System.out.println("");
//    }
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
          }
        }
      }
      return date;
    } catch (Exception ex) {
      System.out.println(ex.getMessage());;
    }
    return currentLine;
  }
}
