package serverapi.Helpers;

import lombok.NoArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

@NoArgsConstructor
public class ReadJSONFileAndGetValue {
    private String pathFile;
    private String objKey;
    private String value;


    public ReadJSONFileAndGetValue(String pathFile, String objKey) {
        this.pathFile = pathFile;
        this.objKey = objKey;
    }

    public String getValue() {
        return value;
    }

    public void read() {
        JSONParser jsonParser = new JSONParser();

        try {
            FileReader fileContent = new FileReader(pathFile);
            JSONArray jsonArray = (JSONArray) jsonParser.parse(fileContent);

            jsonArray.forEach(item -> parseObject((JSONObject) item, objKey));
        } catch (IOException | ParseException ex) {
            ex.printStackTrace();
        }
    }

    public void parseObject(JSONObject jsonObject, String objKey) {
        value = (String) jsonObject.get(objKey);
    }
}
