package nz.ac.auckland.se206.words;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CategorySelector {

  public enum Difficulty {
    E,
    M,
    H
  }

  private final Map<Difficulty, List<String>> difficultyListMap;

  public CategorySelector() throws IOException, URISyntaxException, CsvException {
    difficultyListMap = new HashMap<>();
    for (Difficulty difficulty :
        Difficulty.values()) { // Creates 3 array lists one for each difficulty
      difficultyListMap.put(difficulty, new ArrayList<>());
    }
    for (String[] line :
        getLines()) { // cycles through the csv file line by line to group each word into its
      // category
      difficultyListMap.get(Difficulty.valueOf(line[1])).add(line[0]);
    }
  }

  protected List<String[]> getLines() throws IOException, CsvException, URISyntaxException {
    File file = new File(CategorySelector.class.getResource("/category_difficulty.csv").toURI());
    try (FileReader fr = new FileReader(file, StandardCharsets.UTF_8);
        CSVReader reader = new CSVReader(fr)) {
      return reader.readAll();
    }
  }

  public String getRandomCategory(Difficulty difficulty) {
    return difficultyListMap
        .get(difficulty)
        .get(new Random().nextInt(difficultyListMap.get(difficulty).size()) - 1);
  }
}

