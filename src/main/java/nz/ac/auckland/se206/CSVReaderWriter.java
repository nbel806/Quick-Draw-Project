package nz.ac.auckland.se206;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CSVReaderWriter {
  public String findWordsLeft(String currentUsername) throws IOException, CsvException {
    int index = findUserName(currentUsername);
    CSVReader csvReader = new CSVReader(new FileReader("userdata.csv"));
    List<String[]> allData = csvReader.readAll();
    String[] wordsLeft = allData.get(index)[1].split(",");
    ArrayList<String> wordsLeftFormatted = new ArrayList<>();

    wordsLeft[0] = wordsLeft[0].substring(1);
    wordsLeft[wordsLeft.length - 1] = wordsLeft[wordsLeft.length - 1].substring(0,
        wordsLeft[wordsLeft.length - 1].length() - 1);
    for (String word : wordsLeft) {
      wordsLeftFormatted.add(word.trim());
    }
    Random randomGenerator = new Random();
    int randomIndex = randomGenerator.nextInt(wordsLeftFormatted.size());
    String currentWord = wordsLeftFormatted.get(randomIndex);
    wordsLeftFormatted.remove(randomIndex);
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

  public int findUserName(String currentUsername) throws IOException, CsvException {
    CSVReader csvReader = new CSVReader(new FileReader("userdata.csv"));
    List<String[]> allData = csvReader.readAll();
    int i = 0;
    while (!allData.get(i)[0].equals(currentUsername)) {
      i++;
    }
    return i;
  }

}
