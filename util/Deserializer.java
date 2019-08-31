package cs240.evanjones.server.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.FileReader;

/**Converts json files to java*/
public class Deserializer {

    /** Constructor of Deserializer Object */
    public Deserializer() {}

    /**
     * Converts json string to object
     * @param json String
     * @return Set of values
     */
    public static JsonObject deserializeString(String json) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonobj = (JsonObject)jsonParser.parse(json);
        return jsonobj;
    }

    /**
     * converts json file to json object
     * @param filePath of file to be converted
     * @return Set of values
     * @throws Exception
     */
    public static JsonObject deserializeFile(String filePath) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        Gson gson = new Gson();
        JsonObject jsonObj = gson.fromJson(bufferedReader, JsonObject.class);
        return jsonObj;
    }

    /**
     * converts value from json object into string
     * @param obj to grab value from
     * @param param of what is to be grabbed from object
     * @return
     */
    public static String jsonToString(JsonObject obj, String param){
        return obj.get(param).toString().replaceAll("\"","");
    }
}
