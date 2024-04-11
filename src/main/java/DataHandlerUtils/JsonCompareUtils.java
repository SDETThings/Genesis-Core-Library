package DataHandlerUtils;

import LogUtils.LibraryLoggingUtils;
import com.google.gson.*;

import java.util.*;

/**
 * JsonCompareUtils class contains the methods for comparison of json files
 */
public class JsonCompareUtils {
    Map<String , JsonElement> keyValue = new HashMap<>();
    JsonObject requiredBlock;
    LibraryLoggingUtils libraryLoggingUtils;
    String className = Thread.currentThread().getStackTrace()[1].getClassName();
    /**
     * comapreJson method is used to get the compare 2 json structures and provide the mismatches(if any) into a java Map(mismatchedKey,MismatchedValue).
     * @param expectedJson This is the expected json structure ( ideally it should be a subset of the actual json ).
     * @param actualJson This is the actual json structure ( ideally it should be a superset of the expected json ).
     * @return  misMap This is the HashMap for all the key-value pairs present in actual json which does not match with expected json.
     */
    public synchronized Map<String,JsonElement> comapreJson(JsonElement expectedJson , JsonElement actualJson) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        libraryLoggingUtils = new LibraryLoggingUtils();
        Map<String, JsonElement> misMap = null;
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Map mismatchedElements = new HashMap();
            misMap = new HashMap<>();
            compareJsonValues("", expectedJson, actualJson, mismatchedElements);
            misMap.putAll(mismatchedElements);
        } catch (Exception e) {
            libraryLoggingUtils.getCurrentMethodException(className,methodName,e);
        }
        return misMap;
    }
    /**
     * compareJsonValues method is used to get the compare 2 json structures and provide the mismatches(if any) into a java Map(mismatchedKey,MismatchedValue).
     * @param expectedElement This is the expected json structure ( ideally it should be a subset of the actual json ).
     * @param actualElement This is the actual json structure ( ideally it should be a superset of the expected json ).
     * @param mismatchedElements This is the empty temporary Map which is passed to store the mismatched values during comaprison.
     */
    public synchronized void compareJsonValues(String currentKey, JsonElement expectedElement , JsonElement actualElement,Map<String,JsonElement> mismatchedElements){
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        libraryLoggingUtils = new LibraryLoggingUtils();
        try {
            if (expectedElement.isJsonObject() && actualElement.isJsonObject()) {
                JsonObject expectedObject = expectedElement.getAsJsonObject();
                JsonObject actualObject = actualElement.getAsJsonObject();
                Set<Map.Entry<String, JsonElement>> expectedEntries = expectedObject.entrySet();
                Iterator itr1 = expectedEntries.iterator();
                while (itr1.hasNext()) {
                    Map.Entry<String, JsonElement> entry = (Map.Entry) itr1.next();
                    String key = entry.getKey();
                    JsonElement expectedValue = entry.getValue();
                    if (getValueFromComplexJson(actualObject, key, "") != null) {
                        JsonElement actualValue = getValueFromComplexJson(actualObject, key, "");
                        compareJsonValues(currentKey.isEmpty() ? key : currentKey + "." + key, expectedValue, actualValue, mismatchedElements);
                    } else {
                        mismatchedElements.put(key, null);
                    }
                }
            } else if (expectedElement.isJsonArray() && actualElement.isJsonArray()) {
                JsonArray expectedArray = expectedElement.getAsJsonArray();
                JsonArray actualdArray = actualElement.getAsJsonArray();
                for (int i = 0; i < expectedArray.size(); ++i) {
                    JsonObject expectedIndicator = expectedArray.get(i).getAsJsonObject();
                    for (int j = 0; i < actualdArray.size(); ++j) {
                        JsonObject actualIndicator = actualdArray.get(j).getAsJsonObject();
                        compareJsonValues(currentKey + "[" + i + "]", expectedIndicator, actualIndicator, mismatchedElements);
                    }
                }
            } else if (!expectedElement.equals(actualElement)) {
                if (expectedElement.getAsString().equals("not null")) {
                    if (actualElement.equals(JsonNull.INSTANCE)) {
                        mismatchedElements.put(currentKey, actualElement);
                    }
                } else {
                    mismatchedElements.put(currentKey, actualElement);
                }
            }
        } catch (Exception e) {
            libraryLoggingUtils.getCurrentMethodException(className,methodName,e);
        }
    }
    /**
     * getKeyPathFromComplexJson method is used to get the path of any 'KEY' with in a json structure provided the key which is being provided is unique and only one instance exists in the entire json structure.
     * @param element This is the json structure where to find the key path.
     * @param key This is the name of the key.
     * @param currentPath This is the current path ( usually passed as "" - empty string ) to the function that is used to store the current path during iteration through the json structure.
     * @return  returns a String object containing the actual path of the key provided with in the json.
     */
    public synchronized String getKeyPathFromComplexJson(JsonElement element,String key,String currentPath){
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        libraryLoggingUtils = new LibraryLoggingUtils();

        if(element.isJsonArray())
        {
            JsonArray jsonArray = element.getAsJsonArray();
            for(JsonElement initialArrayElement : jsonArray)
            {
                if(initialArrayElement.isJsonObject())
                {
                    JsonObject jsonObject = initialArrayElement.getAsJsonObject();
                    try {
                        for(Map.Entry<String,JsonElement> entry : jsonObject.entrySet())
                        {
                            String entryKey = entry.getKey();
                            JsonElement entryValue = entry.getValue();
                            String newPath = currentPath.isEmpty()?entryKey:currentPath+"."+entryKey;
                            if(entryKey.equals(key))
                            {
                                return newPath;
                            }
                            if(entryValue.isJsonObject())
                            {
                                String nestedPath = getKeyPathFromComplexJson(entryValue.getAsJsonObject(),key,newPath);
                                if(!nestedPath.isEmpty())
                                {
                                    return nestedPath;
                                }
                            } else if (entryValue.isJsonArray()) {
                                JsonArray nestedJsonArray = entryValue.getAsJsonArray();
                                for(int i=0;i< nestedJsonArray.size();i++)
                                {
                                    JsonElement arrayElement = nestedJsonArray.get(i);
                                    if(arrayElement.isJsonObject())
                                    {
                                        String arrayElementPath = getKeyPathFromComplexJson(arrayElement.getAsJsonObject(),key,newPath+"["+i+"]");
                                        if(!arrayElementPath.isEmpty())
                                        {
                                            return arrayElementPath;
                                        }
                                    }
                                }
                            }
                        }

                    } catch (Exception e) {
                        libraryLoggingUtils.getCurrentMethodException(className,methodName,e);
                    }
                }
            }
        }
        else if (element.isJsonObject())
        {
            JsonObject jsonObject = element.getAsJsonObject();
            try {
                for(Map.Entry<String,JsonElement> entry : jsonObject.entrySet())
                {
                    String entryKey = entry.getKey();
                    JsonElement entryValue = entry.getValue();
                    String newPath = currentPath.isEmpty()?entryKey:currentPath+"."+entryKey;
                    if(entryKey.equals(key))
                    {
                        return newPath;
                    }
                    if(entryValue.isJsonObject())
                    {
                        String nestedPath = getKeyPathFromComplexJson(entryValue.getAsJsonObject(),key,newPath);
                        if(!nestedPath.isEmpty())
                        {
                            return nestedPath;
                        }
                    } else if (entryValue.isJsonArray()) {
                        JsonArray jsonArray = entryValue.getAsJsonArray();
                        for(int i=0;i< jsonArray.size();i++)
                        {
                            JsonElement arrayElement = jsonArray.get(i);
                            if(arrayElement.isJsonObject())
                            {
                                String arrayElementPath = getKeyPathFromComplexJson(arrayElement.getAsJsonObject(),key,newPath+"["+i+"]");
                                if(!arrayElementPath.isEmpty())
                                {
                                    return arrayElementPath;
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                libraryLoggingUtils.getCurrentMethodException(className,methodName,e);
            }
        }


        return null;
    }
    /**
     * getValueFromComplexJson method is used to get the value of any key with in a json structure provided the key which is being provided is unique and only one instance exists in the entire json structure.
     * @param jsonElement This is the json structure where to find the key path.
     * @param key This is the name of the key.
     * @param currentKey This is the current key ( usually passed as "" - empty string ) to the function that is used to store the current key during iteration through the json structure.
     * @return  returns a jsonElement object containing the actual value of the key provided with in the json.
     */
    public synchronized JsonElement getValueFromComplexJson(JsonElement jsonElement,String key,String currentKey) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        libraryLoggingUtils = new LibraryLoggingUtils();
        try {
            if(!key.equals(currentKey)){
                JsonElement value = null;
                if(jsonElement.isJsonObject())
                {
                    JsonObject actualObject = jsonElement.getAsJsonObject();
                    Set<Map.Entry<String,JsonElement>> entries = actualObject.entrySet();
                    Iterator itr = entries.iterator();
                    while(itr.hasNext())
                    {
                        Map.Entry<String,JsonElement> entry = (Map.Entry) itr.next();
                        String currentKey1 = entry.getKey();
                        JsonElement expectedValue = entry.getValue();
                        getValueFromComplexJson(expectedValue,key,currentKey1);
                    }
                } else if (jsonElement.isJsonArray()) {
                    JsonObject element =null;
                    JsonArray nestedJsonArray;
                    JsonArray jsonArray = jsonElement.getAsJsonArray();
                    for(int i=0;i<jsonArray.size();++i)
                    {
                        if(!jsonArray.get(i).isJsonArray())
                        {
                            if(!jsonArray.get(i).isJsonPrimitive())
                            {
                                element = jsonArray.get(i).getAsJsonObject();
                                getValueFromComplexJson(element,key,currentKey+"["+i+"]");
                            }
                        }else {
                            nestedJsonArray = jsonArray.get(i).getAsJsonArray();
                            getValueFromComplexJson(nestedJsonArray,key,currentKey+"["+i+"]");
                        }
                    }
                }
            }else {
                keyValue.put(key,jsonElement);
            }
        } catch (Exception e) {
            libraryLoggingUtils.getCurrentMethodException(className,methodName,e);
        }
        return jsonElement;
    }
    /**
     * extractSpecificArrayElementFromResponseObject method is used to get the specific array element from a json structure
     * @param arrayBlock This is the actual json structure from which the value has to extracted
     * @param jsonElement This is the actual json structure from which the value has to extracted( one is to preserve the actual structure to recursion calls).
     * @param key This is the name of the key.
     * @param value This is the value for the above key.
     * @param currentKey This is the value for the current key during iteration( usually passed as empty string).
     * @return  returns the array element as a JsonObject where the key-value pair was found in the json structure.
     */
    public synchronized JsonObject extractSpecificArrayElementFromResponseObject(JsonElement arrayBlock,JsonElement jsonElement,String key,JsonElement value,String currentKey) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        libraryLoggingUtils = new LibraryLoggingUtils();
        try {
            if (jsonElement.isJsonObject()) {
                JsonObject actualObject = jsonElement.getAsJsonObject();
                Set<Map.Entry<String, JsonElement>> entries = actualObject.entrySet();
                Iterator itr = entries.iterator();
                while (itr.hasNext()) {
                    Map.Entry<String, JsonElement> entry = (Map.Entry) itr.next();
                    String currentKey1 = entry.getKey();
                    JsonElement currentValue = entry.getValue();
                    if (!(currentKey1.equalsIgnoreCase(key) && currentValue.getAsString().equalsIgnoreCase(value.getAsString()))) {
                        extractSpecificArrayElementFromResponseObject(arrayBlock, currentValue, key, value, currentKey1);
                    } else {
                        requiredBlock = arrayBlock.getAsJsonObject();
                        break;
                    }
                }
            } else if (jsonElement.isJsonArray()) {
                JsonObject element = null;
                JsonArray nestedJsonArray;
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                for (int i = 0; i < jsonArray.size(); ++i) {
                    if (!jsonArray.get(i).isJsonArray()) {
                        if (!jsonArray.get(i).isJsonPrimitive()) {
                            element = jsonArray.get(i).getAsJsonObject();
                            if (requiredBlock == null) {
                                extractSpecificArrayElementFromResponseObject(element, element, key, value, currentKey + "[" + i + "]");
                            } else {
                                break;
                            }
                        }
                    } else {
                        nestedJsonArray = jsonArray.get(i).getAsJsonArray();
                        if (requiredBlock == null) {
                            extractSpecificArrayElementFromResponseObject(nestedJsonArray, nestedJsonArray, key, value, currentKey + "[" + i + "]");
                        } else {
                            requiredBlock = arrayBlock.getAsJsonObject();
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            libraryLoggingUtils.getCurrentMethodException(className,methodName,e);
        }
        return requiredBlock;
    }
    /**
     * getListOfValueFromComplexJson method is used to get the list of values for a key occurring in multiple instances throughout the json structure.
     * @param jsonElement This is the actual json structure from which the list of values have to extracted
     * @param key This is the name of the key whose values are to be extracted into a list.
     * @param currentKey This is the name of the current key used during iteration of json structure.
     * @param count This is the count of the key occurances in the json structure during recursive iterations.
     * @param listOfValues This is the value for the list where the values are stored.
     * @return  returns the listOfValues for the given key instances.
     */
    public synchronized List<JsonElement> getListOfValueFromComplexJson(JsonElement jsonElement,String key,String currentKey,int count,List<JsonElement> listOfValues) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        libraryLoggingUtils = new LibraryLoggingUtils();
        try {
            if(count>0)
            {
                listOfValues.clear();
                count=0;
            }
            if(!(key.equals(currentKey)))
            {
                JsonElement value = null;
                if(jsonElement.isJsonObject())
                {
                    JsonObject actualObject = jsonElement.getAsJsonObject();
                    Set<Map.Entry<String,JsonElement>> entries = actualObject.entrySet();
                    Iterator itr = entries.iterator();
                    while(itr.hasNext())
                    {
                        Map.Entry<String,JsonElement> entry = (Map.Entry) itr.next();
                        String currentKey1 = entry.getKey();
                        JsonElement expectedValue = entry.getValue();
                        getListOfValueFromComplexJson(expectedValue,key,currentKey1,count,listOfValues);
                    }
                } else if (jsonElement.isJsonArray()) {
                    JsonObject element = null;
                    JsonArray jsonArray = jsonElement.getAsJsonArray();
                    for(int i=0;i<jsonArray.size();++i)
                    {
                        if(!jsonArray.get(i).isJsonPrimitive())
                        {
                            element = jsonArray.get(i).getAsJsonObject();
                            getListOfValueFromComplexJson(element,key,currentKey+"["+i+"]",count,listOfValues);
                        }
                    }
                }
            }else {
                listOfValues.add(jsonElement);
            }
        } catch (Exception e) {
            libraryLoggingUtils.getCurrentMethodException(className,methodName,e);
        }
        return listOfValues;
    }
    /**
     * modifyKeyValueFromNestedJson method is used to get the list of values for a key occurring in multiple instances throughout the json structure.
     * @param jsonElement This is the actual json structure from which the list of values have to extracted
     * @param key This is the name of the key whose values are to be extracted into a list.
     * @param currentKey This is the name of the current key used during iteration of json structure.
     * @return  returns the JsonElement for the given key instances.
     */
    public synchronized JsonElement modifyKeyValueFromNestedJson(JsonElement jsonElement,String key , String currentKey){
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        libraryLoggingUtils = new LibraryLoggingUtils();
        try {
            if(!key.equals(currentKey))
            {
                JsonElement value=null;
                if(jsonElement.isJsonObject()){
                    JsonObject actualObject = jsonElement.getAsJsonObject();
                    Set<Map.Entry<String,JsonElement>> entries = actualObject.entrySet();
                    Iterator itr =entries.iterator();
                    while(itr.hasNext())
                    {
                        Map.Entry<String,JsonElement> entry = (Map.Entry) itr.next();
                        String currentKey1 = entry.getKey();
                        JsonElement expectedValue = entry.getValue();
                        modifyKeyValueFromNestedJson(expectedValue,key,currentKey1);
                    }
                } else if (jsonElement.isJsonArray()) {
                    JsonObject element = null;
                    JsonArray jsonArray = jsonElement.getAsJsonArray();
                    for(int i=0;i<jsonArray.size();++i)
                    {
                        if(!jsonArray.get(i).isJsonPrimitive())
                        {
                            element = jsonArray.get(i).getAsJsonObject();
                            modifyKeyValueFromNestedJson(element,key,currentKey+"["+i+"]");
                        }
                    }
                }
            }else{
                keyValue.put(key,jsonElement);
            }

        } catch (Exception e) {
            libraryLoggingUtils.getCurrentMethodException(className,methodName,e);
        }
        return keyValue.get(key);
    }
}
