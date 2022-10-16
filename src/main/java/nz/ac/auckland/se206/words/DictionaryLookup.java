package nz.ac.auckland.se206.words;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class DictionaryLookup {

  private static final String API_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";

  /**
   * gets a searched word and its definition
   *
   * @param query word to be search
   * @return current word and its definition
   * @throws IOException If the model cannot be found on the file system.
   */
  public static WordInfo searchWordInfo(String query) throws IOException {
    // searches the api for the word
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder().url(API_URL + query).build();
    Response response = client.newCall(request).execute();
    ResponseBody responseBody = response.body();

    String jsonString = responseBody.string();
    // catches incase cast exception
    try {
      JSONObject jsonObj = (JSONObject) new JSONTokener(jsonString).nextValue();
    } catch (ClassCastException e) {
    }
    // catches incase cast exception
    JSONArray jArray = null;
    try {
      jArray = (JSONArray) new JSONTokener(jsonString).nextValue();
    } catch (ClassCastException e) {
    }
    List<WordEntry> entries = new ArrayList<WordEntry>();

    for (int e = 0; e < jArray.length(); e++) { // loops though for deffinition
      JSONObject jsonEntryObj = jArray.getJSONObject(e);
      JSONArray jsonMeanings = jsonEntryObj.getJSONArray("meanings");

      String partOfSpeech = "[not specified]";
      List<String> definitions = new ArrayList<String>();

      for (int m = 0; m < jsonMeanings.length(); m++) {
        JSONObject jsonMeaningObj = jsonMeanings.getJSONObject(m);
        String pos = jsonMeaningObj.getString("partOfSpeech");

        if (!pos.isEmpty()) {
          partOfSpeech = pos;
        }

        JSONArray jsonDefinitions = jsonMeaningObj.getJSONArray("definitions");
        for (int d = 0; d < jsonDefinitions.length(); d++) { // adds definition
          JSONObject jsonDefinitionObj = jsonDefinitions.getJSONObject(d);

          String definition = jsonDefinitionObj.getString("definition");
          if (!definition.isEmpty()) { // adds definition to array
            definitions.add(definition);
          }
        }
      }
      WordEntry wordEntry = new WordEntry(partOfSpeech, definitions);
      entries.add(wordEntry); // adds to final path
    }

    return new WordInfo(query, entries); // return map of both
  }
}
