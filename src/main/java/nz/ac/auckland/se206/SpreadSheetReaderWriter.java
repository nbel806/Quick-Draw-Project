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
    if (allData.get(index)[1].isEmpty()) { // if the user has played all words
      CategorySelector category = new CategorySelector();
      allData.get(index)[1] = category.getCategory(Difficulty.E).toString(); // adds them all back
    }
    String[] wordsLeft = allData.get(index)[1].split(",");
    ArrayList<String> wordsLeftFormatted = new ArrayList<>(); // list to store the words

    wordsLeft[0] = wordsLeft[0].substring(1);
    wordsLeft[wordsLeft.length - 1] =
        wordsLeft[wordsLeft.length - 1].substring(0, wordsLeft[wordsLeft.length - 1].length() - 1);
    for (String word : wordsLeft) {
      wordsLeftFormatted.add(word.trim());
    }
    Random randomGenerator = new Random(); // chooses a random word from the array list
    int randomIndex = randomGenerator.nextInt(wordsLeftFormatted.size());
    String currentWord = wordsLeftFormatted.get(randomIndex);
    wordsLeftFormatted.remove(randomIndex); // removes the word so it cant be played again

    // stores history words
    if (allData.get(index)[5].equals("none")) {
      allData.get(index)[5] = currentWord;
      CSVWriter csvWriter = new CSVWriter(new FileWriter("userdata.csv"));
      csvWriter.writeAll(allData); // writes over
      csvWriter.flush();
    } else {
      // combine the current word with the history words string and save it again
      allData.get(index)[5] = allData.get(index)[5] + "," + currentWord;
      CSVWriter csvWriter = new CSVWriter(new FileWriter("userdata.csv"));
      csvWriter.writeAll(allData); // writes over
      csvWriter.flush();
    }

    newFile(wordsLeftFormatted, index);

    return currentWord;
  }

  private void newFile(ArrayList<String> wordsLeftFormatted, Integer index)
      throws IOException, CsvException {
    CSVReader csvReader = new CSVReader(new FileReader("userdata.csv")); // name of file
    List<String[]> allData = csvReader.readAll();
    allData.get(index)[1] =
        String.valueOf(wordsLeftFormatted); // this updates the words left for the user

    CSVWriter csvWriter = new CSVWriter(new FileWriter("userdata.csv")); // writes to file name
    csvWriter.writeAll(allData); // writes all the data to the same file
    csvWriter.flush();
  }

  private int findUserName(String currentUsername) throws IOException, CsvException {
    CSVReader csvReader = new CSVReader(new FileReader("userdata.csv"));
    List<String[]> allData =
        csvReader.readAll(); // reads the entire csv file the name of file can be changed if needed
    int i = 0;
    while (!allData.get(i)[0].equals(
        currentUsername)) { // cycles through all the users until there is a match
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
      allData.get(index)[7] =
          String.valueOf(Integer.parseInt(allData.get(index)[7]) + 1); // increment streak
      if (Integer.parseInt(allData.get(index)[7])
          > Integer.parseInt(allData.get(index)[6])) { // checks streak
        allData.get(index)[6] = allData.get(index)[7]; // makes new highest streak
      }
    } else {
      allData.get(index)[3] =
          String.valueOf(Integer.parseInt(allData.get(index)[3]) + 1); // increment losses
      allData.get(index)[7] = "0"; // reset
    }

    CSVWriter csvWriter = new CSVWriter(new FileWriter("userdata.csv"));
    csvWriter.writeAll(allData); // writes all the data back
    csvWriter.flush();
  }

  public void updateTime(int timeTaken, String currentUsername) throws IOException, CsvException {
    if (currentUsername == null) { // this is a guest user
      return;
    }
    int index = findUserName(currentUsername);
    CSVReader csvReader = new CSVReader(new FileReader("userdata.csv"));
    List<String[]> allData = csvReader.readAll();
    if (Integer.parseInt(allData.get(index)[4])
        > timeTaken) { // checks if the time is faster than the previous record
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

  public int getStreak(String currentUsername) throws IOException, CsvException {
    int index = findUserName(currentUsername);
    CSVReader csvReader = new CSVReader(new FileReader("userdata.csv"));
    List<String[]> allData = csvReader.readAll();
    return Integer.parseInt(allData.get(index)[6]);
  }
}
