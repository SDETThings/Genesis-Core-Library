package DataHandlerUtils;

import LogUtils.LibraryLoggingUtils;
import com.google.gson.*;
import io.restassured.response.Response;
import org.openqa.selenium.json.Json;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

/**
 * JsonOperations class contains the methods for performing operations on json file
 */
public class JsonOperations {
    LibraryLoggingUtils libraryLoggingUtils;
    String className = Thread.currentThread().getStackTrace()[1].getClassName();
    /**
     * readJsonFileAndStoreInJsonObject method is used to read the json file and return the content as JsonObject
     * @param jsonFilePath Accepts the actual path of the json file
     * @return JsonObject which contain all the content as JsonObject
     */
    public synchronized JsonObject readJsonFileAndStoreInJsonObject(String jsonFilePath) throws IOException {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        libraryLoggingUtils = new LibraryLoggingUtils();
        FileReader reader = null;
        try {
            reader = new FileReader(jsonFilePath);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (JsonIOException | JsonSyntaxException e) {
            libraryLoggingUtils.getCurrentMethodException(className,methodName,e);
        }
        return JsonParser.parseReader(reader).getAsJsonObject();
    }
    /**
     * readJsonFileAndStoreInJsonObject method is used to read the json file and return the content as JsonObject
     * @param reader Accepts the FileReader object of the json file
     * @return JsonObject which contain all the content as JsonObject
     */
    public synchronized JsonObject readJsonFileAndStoreInJsonObject(FileReader reader) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        libraryLoggingUtils = new LibraryLoggingUtils();
        JsonObject jsonObject=null;
        try {
            jsonObject= JsonParser.parseReader(reader).getAsJsonObject();
        } catch (JsonIOException | JsonSyntaxException e) {
            libraryLoggingUtils.getCurrentMethodException(className,methodName,e);
        }
        return jsonObject;
    }
    /**
     * convertResponseToJsonElement method is used to read the json response and return the content as JsonElement
     * @param response Accepts the Rest assured response object of the json file
     * @return jsonElement which contain all the content as JsonElement
     */
    public synchronized JsonElement convertResponseToJsonElement(Response response) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        libraryLoggingUtils = new LibraryLoggingUtils();
        JsonElement jsonElement = null;
        try {
            if (response.getBody().asString().startsWith("{"))
            {
                jsonElement = JsonParser.parseString(response.getBody().asString()).getAsJsonObject();
            } else if(response.getBody().asString().startsWith("["))
            {
                jsonElement = JsonParser.parseString(response.getBody().asString()).getAsJsonArray();

            }
        } catch (JsonSyntaxException e) {
            libraryLoggingUtils.getCurrentMethodException(className,methodName,e);
        }
        return jsonElement;
    }
    /**
     * convertResponseToJsonArray method is used to read the json response and return the content as JsonArray(use this when we don't know the response is an array or object ).
     * @param response Accepts the Rest assured Response object of the json file
     * @return jsonArray which contain all the content as JsonArray
     */
    public synchronized JsonArray convertResponseToJsonArray(Response response) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        libraryLoggingUtils = new LibraryLoggingUtils();
        JsonArray jsonArray=null;
        try {
            jsonArray = JsonParser.parseString(response.getBody().asString()).getAsJsonArray();
        } catch (JsonSyntaxException e) {
            libraryLoggingUtils.getCurrentMethodException(className,methodName,e);
        }
        return jsonArray;
    }
    /**
     * convertResponseToJsonObject method is used to read the json response and return the content as JsonObject
     * @param response Accepts the Rest assured Response object of the json file
     * @return jsonObject which contain all the content as JsonObject
     */
    public synchronized JsonObject convertResponseToJsonObject(Response response) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        libraryLoggingUtils = new LibraryLoggingUtils();
        JsonObject jsonObject=null;
        try {
            jsonObject = JsonParser.parseString(response.getBody().asString()).getAsJsonObject();
        } catch (JsonSyntaxException e) {
            libraryLoggingUtils.getCurrentMethodException(className,methodName,e);
        }
        return jsonObject;
    }
    /**
     * modifyKeyValueInJson method is used to modify existing JsonObject return the modified JsonObject.
     * @param jsonStructure Accepts the JsonElement to be modified.
     * @param key Key which needs to be modified.
     * @param value New value which needs to be replaced against the key.
     * @return payload which contain updated JsonElement.
     */
    public synchronized JsonElement modifyKeyValueInJson(JsonElement jsonStructure , String key , JsonElement value) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        libraryLoggingUtils = new LibraryLoggingUtils();
        try {
            if(jsonStructure.isJsonObject()){
                JsonObject payload = jsonStructure.getAsJsonObject();
                if (payload.has(key))
                {
                payload.add(key, value);
                return payload;
                }
            for (Map.Entry<String, JsonElement> entry : payload.entrySet()) {
                JsonElement element = entry.getValue();
                if (element.isJsonObject()) {
                    JsonElement updatedNestedJsonObject = modifyKeyValueInJson(element.getAsJsonObject(), key, value);
                    if (updatedNestedJsonObject != null) {
                        payload.add(entry.getKey(), updatedNestedJsonObject);
                        return payload;
                    }
                } else if (element.isJsonArray()) {
                    JsonArray array = element.getAsJsonArray();
                    for (int i = 0; i < array.size(); i++) {
                        JsonElement element1 = array.get(i);
                        if (element1.isJsonObject()) {
                            JsonElement updatedNestedJsonObject = modifyKeyValueInJson(element1.getAsJsonObject(), key, value);
                            if (updatedNestedJsonObject != null) {
                                array.set(i, updatedNestedJsonObject);
                            }
                        }
                    }
                }
            }
            }
            else if(jsonStructure.isJsonArray()){
                JsonArray initialJsonArray = jsonStructure.getAsJsonArray();
                for(JsonElement arrayElement1 :initialJsonArray)
                {
                 if(arrayElement1.isJsonObject())
                 {
                     JsonObject payload = jsonStructure.getAsJsonObject();
                    if (payload.has(key))
                    {
                        payload.add(key, value);
                        return payload;
                    }
                    for (Map.Entry<String, JsonElement> entry : payload.entrySet()) {
                        JsonElement element = entry.getValue();
                        if (element.isJsonObject()) {
                            JsonElement updatedNestedJsonObject = modifyKeyValueInJson(element.getAsJsonObject(), key, value);
                            if (updatedNestedJsonObject != null) {
                                payload.add(entry.getKey(), updatedNestedJsonObject);
                                return payload;
                            }
                        } else if (element.isJsonArray()) {
                            JsonArray array = element.getAsJsonArray();
                            for (int i = 0; i < array.size(); i++) {
                                JsonElement element1 = array.get(i);
                                if (element1.isJsonObject()) {
                                    JsonElement updatedNestedJsonObject = modifyKeyValueInJson(element1.getAsJsonObject(), key, value);
                                    if (updatedNestedJsonObject != null) {
                                        array.set(i, updatedNestedJsonObject);
                                    }
                                }
                            }
                        }
                    }
                }
                 }
            }
        } catch (Exception e) {
            libraryLoggingUtils.getCurrentMethodException(className,methodName,e);
        }
        return null;
}
}
