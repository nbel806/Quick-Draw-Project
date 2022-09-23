package nz.ac.auckland.se206;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import nz.ac.auckland.se206.words.CategorySelector;
import nz.ac.auckland.se206.words.CategorySelector.Difficulty;

public class SpreadSheetReaderWriter {

  public String findWordsLeft(String currentUsername)
      throws IOException, CsvException, URISyntaxException {
    int index = findUserName(currentUsername);
    CSVReader csvReader = new CSVReader(new FileReader("userdata.csv"));
    List<String[]> allData = csvReader.readAll();
    if (allData.get(index)[1].isEmpty()) {
      CategorySelector category = new CategorySelector();
      allData.get(index)[1] = category.getCategory(Difficulty.E).toString();
    }
    String[] wordsLeft = allData.get(index)[1].split(",");
    ArrayList<String> wordsLeftFormatted = new ArrayList<>();

    wordsLeft[0] = wordsLeft[0].substring(1);
    wordsLeft[wordsLeft.length - 1] =
        wordsLeft[wordsLeft.length - 1].substring(0, wordsLeft[wordsLeft.length - 1].length() - 1);
    for (String word : wordsLeft) {
      wordsLeftFormatted.add(word.trim());
    }
    Random randomGenerator = new Random();
    int randomIndex = randomGenerator.nextInt(wordsLeftFormatted.size());
    String currentWord = wordsLeftFormatted.get(randomIndex);
    wordsLeftFormatted.remove(randomIndex);

    // stores history words
    if (allData.get(index)[5].equals("none")) {
      allData.get(index)[5] = currentWord;
      CSVWriter csvWriter = new CSVWriter(new FileWriter("userdata.csv"));
      csvWriter.writeAll(allData);
      csvWriter.flush();
    } else {
      // combine the current word with the history words string and save it again
      allData.get(index)[5] = allData.get(index)[5] + ", " + currentWord;
      CSVWriter csvWriter = new CSVWriter(new FileWriter("userdata.csv"));
      csvWriter.writeAll(allData);
      csvWriter.flush();
    }

    newFile(wordsLeftFormatted, index);

    return currentWord;
  }

  private void newFile(ArrayList<String> wordsLeftFormatted, Integer index)
      throws IOException, CsvException {
    CSVReader csvReader = new CSVReader(new FileReader("userdata.csv"));
    List<String[]> allData = csvReader.readAll();
    allData.get(index)[1] = String.valueOf(wordsLeftFormatted);

    CSVWriter csvWriter = new CSVWriter(new FileWriter("userdata.csv"));
    csvWriter.writeAll(allData);
    csvWriter.flush();
  }

  private int findUserName(String currentUsername) throws IOException, CsvException {
    CSVReader csvReader = new CSVReader(new FileReader("userdata.csv"));
    List<String[]> allData = csvReader.readAll();
    int i = 0;
    while (!allData.get(i)[0].equals(currentUsername)) {
      i++;
    }
    return i;
  }

  public void updateResult(boolean win, String currentUsername) throws IOException, CsvException {
    if (currentUsername == null) {
      return;
    }
    int index = findUserName(currentUsername);
    CSVReader csvReader = new CSVReader(new FileReader("userdata.csv"));
    List<String[]> allData = csvReader.readAll();

    if (win) {
      allData.get(index)[2] =
          String.valueOf(Integer.parseInt(allData.get(index)[2]) + 1); // increment wins
    } else {
      allData.get(index)[3] =
          String.valueOf(Integer.parseInt(allData.get(index)[3]) + 1); // increment losses
    }

    CSVWriter csvWriter = new CSVWriter(new FileWriter("userdata.csv"));
    csvWriter.writeAll(allData); // writes all the data back
    csvWriter.flush();
  }

  public void updateTime(int timeTaken, String currentUsername) throws IOException, CsvException {
    if (currentUsername == null) {
      return;
    }
    int index = findUserName(currentUsername);
    CSVReader csvReader = new CSVReader(new FileReader("userdata.csv"));
    List<String[]> allData = csvReader.readAll();
    if (Integer.parseInt(allData.get(index)[4]) > timeTaken) {
      allData.get(index)[4] = String.valueOf(timeTaken);
    }

    CSVWriter csvWriter = new CSVWriter(new FileWriter("userdata.csv"));
    csvWriter.writeAll(allData); // writes all the data back
    csvWriter.flush();
  }

  public int getWins(String username) throws IOException, CsvException {
    int index = findUserName(username);
    CSVReader csvReader = new CSVReader(new FileReader("userdata.csv"));
    List<String[]> allData = csvReader.readAll();
    return Integer.parseInt(allData.get(index)[2]);
  }

  public int getLosses(String username) throws IOException, CsvException {
    int index = findUserName(username);
    CSVReader csvReader = new CSVReader(new FileReader("userdata.csv"));
    List<String[]> allData = csvReader.readAll();
    return Integer.parseInt(allData.get(index)[3]);
  }

  public int getFastest(String currentUsername) throws IOException, CsvException {
    int index = findUserName(currentUsername);
    CSVReader csvReader = new CSVReader(new FileReader("userdata.csv"));
    List<String[]> allData = csvReader.readAll();
    return Integer.parseInt(allData.get(index)[4]);
  }

  public String getHistory(String currentUsername) throws IOException, CsvException {
    int index = findUserName(currentUsername);
    CSVReader csvReader = new CSVReader(new FileReader("userdata.csv"));
    List<String[]> allData = csvReader.readAll();
    return allData.get(index)[5];
  }
}
