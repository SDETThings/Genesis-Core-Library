package dataUtils;


import DataHandlerUtils.JsonCompareUtils;
import com.google.gson.*;
import io.restassured.response.Response;
import DataHandlerUtils.JsonOperations;
import org.openqa.selenium.json.Json;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * MasterDataUtils class contains the methods related to retrieving data from json file or JsonObject
 */
public class MasterDataUtils {
    JsonCompareUtils jsonCompareUtils = new JsonCompareUtils();
    JsonOperations jsonOperations = new JsonOperations();
    /**
     * accessRequestApiData Method is used to access data for building request payload against a test case API component from it's corresponding Master data json file
     * @param testCaseId This is testng method name
     * @param i This is to denote which array element needs to be picked
     * @param typesList This is actual JsonArray which contains the data
     * @param endpointName This is endpointName based on which data needs to be picked from multiple endpoints
     * @return Returns a JsonObject containing the required API data.
     */
    public synchronized JsonObject accessRequestApiData(String testCaseId , int i , JsonArray typesList, String endpointName) {
        JsonObject dynamicRequestPayloadDataFields=null;
        {
            JsonElement APItype= typesList.get(i);
            JsonObject jsonObject2 = APItype.getAsJsonObject();
            JsonArray iterationList = jsonObject2.get("ITERATION").getAsJsonArray();
            for(JsonElement iteration : iterationList)
            {
                int iterationNumber = iteration.getAsJsonObject().get("ITERATION_NUMBER").getAsInt();
                JsonArray iterationDetailsList = iteration.getAsJsonObject().get("ITERATION_DETAILS").getAsJsonArray();
                for(JsonElement iterationDetail : iterationDetailsList)
                {
                    JsonArray endpointDetailsList = iterationDetail.getAsJsonObject().get("ENDPOINT_DETAILS").getAsJsonArray();
                    for(JsonElement endpointDetail : endpointDetailsList)
                    {
                        JsonArray endpointsList = endpointDetail.getAsJsonObject().get("ENDPOINTS").getAsJsonArray();
                        for(JsonElement endpoints : endpointsList)
                        {
                            if(endpoints.getAsJsonObject().has("ENDPOINT_NAME") && endpoints.getAsJsonObject().get("ENDPOINT_NAME").getAsString()!=null)
                            {
                                if (endpointName.equalsIgnoreCase(endpoints.getAsJsonObject().get("ENDPOINT_NAME").getAsString())) {
                                    JsonArray testDataList = endpoints.getAsJsonObject().get("TEST_DATA").getAsJsonArray();
                                    for (JsonElement testData : testDataList) {
                                        dynamicRequestPayloadDataFields = testData.getAsJsonObject().get("DYNAMIC_REQUEST_PAYLOAD_FIELDS").getAsJsonObject();
                                    }
                                }
                            }else
                            {
                                System.out.println("Master data does not have COMPONENT field / has null value for COMPONENT field in test case :"+testCaseId);
                            }
                        }
                    }
                }
            }
        }

        return dynamicRequestPayloadDataFields;
    }
    /**
     * accessReponseApiData Method is used to access data for expected/dynamic request payload against a test case API component from it's corresponding Master data json file
     * @param testCaseId This is testng method name
     * @param i This is to denote which array element needs to be picked
     * @param typesList This is actual JsonArray which contains the data
     * @param endpointName This is endpointName based on which data needs to be picked from multiple endpoints
     * @return Returns a JsonObject containing the required API data.
     */
    public synchronized JsonObject accessReponseApiData(String testCaseId , int i , JsonArray typesList, String endpointName)
    {
        JsonObject  expectedResponsePayloadDataFields =null;

        JsonElement APItype= typesList.get(i);
        JsonObject jsonObject2 = APItype.getAsJsonObject();
        JsonArray iterationList = jsonObject2.get("ITERATION").getAsJsonArray();
        for(JsonElement iteration : iterationList)
        {
            int iterationNumber = iteration.getAsJsonObject().get("ITERATION_NUMBER").getAsInt();
            JsonArray iterationDetailsList = iteration.getAsJsonObject().get("ITERATION_DETAILS").getAsJsonArray();
            for(JsonElement iterationDetail : iterationDetailsList)
            {
                JsonArray endpointDetailsList = iterationDetail.getAsJsonObject().get("ENDPOINT_DETAILS").getAsJsonArray();
                for(JsonElement endpointDetail : endpointDetailsList)
                {
                    JsonArray endpointsList = endpointDetail.getAsJsonObject().get("ENDPOINTS").getAsJsonArray();
                    for(JsonElement endpoints : endpointsList)
                    {
                        if(endpoints.getAsJsonObject().has("ENDPOINT_NAME") && endpoints.getAsJsonObject().get("ENDPOINT_NAME").getAsString()!=null)
                        {
                            if (endpointName.equalsIgnoreCase(endpoints.getAsJsonObject().get("ENDPOINT_NAME").getAsString())) {
                                JsonArray testDataList = endpoints.getAsJsonObject().get("TEST_DATA").getAsJsonArray();
                                for (JsonElement testData : testDataList) {
                                    expectedResponsePayloadDataFields = testData.getAsJsonObject().get("EXPECTED_RESPONSE_PAYLOAD").getAsJsonObject();
                                }
                            }
                        }else
                        {
                            System.out.println("Master data does not have COMPONENT field / has null value for COMPONENT field in test case :"+testCaseId);
                        }
                    }
                }
            }
        }

        return expectedResponsePayloadDataFields;
    }
    /**
     * accessWebData Method is used to access data for UI against a test case WEB component from it's corresponding Master data json file
     * @param testCaseId This is testng method name
     * @param i This is to denote which array element needs to be picked
     * @param typesList This is actual JsonArray which contains the data
     * @return Returns a JsonObject containing the required front end data.
     */
    public synchronized JsonArray accessWebData(String testCaseId , int i , JsonArray typesList) {
        JsonArray webPageDataFields = null;
        {
            JsonElement APItype= typesList.get(i);
            JsonObject jsonObject2 = APItype.getAsJsonObject();
            JsonArray iterationList = jsonObject2.get("ITERATION").getAsJsonArray();
            for(JsonElement iteration : iterationList)
            {
                int iterationNumber = iteration.getAsJsonObject().get("ITERATION_NUMBER").getAsInt();
                JsonArray iterationDetailsList = iteration.getAsJsonObject().get("ITERATION_DETAILS").getAsJsonArray();
                for(JsonElement iterationDetail : iterationDetailsList)
                {
                    JsonArray webPagesList = iterationDetail.getAsJsonObject().get("WEB_PAGES").getAsJsonArray();
                    for(JsonElement webPages : webPagesList)
                    {
                        JsonArray webPageList = webPages.getAsJsonObject().get("WEB_PAGE").getAsJsonArray();
                        for(JsonElement webPage : webPageList)
                        {
                            webPageDataFields = webPage.getAsJsonObject().get("TEST_DATA").getAsJsonArray();

                        }
                    }
                }
            }
        }
        return webPageDataFields;
    }
    /**
     * accessMobileData Method is used to access data against a test case's MOBILE component from it's corresponding Master data json file
     * @param testCaseId This is testng method name
     * @param i This is to denote which array element needs to be picked
     * @param typesList This is actual JsonArray which contains the data
     * @return Returns a JsonObject containing the required front end data.
     */
    public synchronized JsonArray accessMobileData(String testCaseId ,int i , JsonArray typesList) {
        JsonArray mobileDataFields = null;
        {
            JsonElement APItype= typesList.get(i);
            JsonObject jsonObject2 = APItype.getAsJsonObject();
            JsonArray iterationList = jsonObject2.get("ITERATION").getAsJsonArray();
            for(JsonElement iteration : iterationList)
            {
                int iterationNumber = iteration.getAsJsonObject().get("ITERATION_NUMBER").getAsInt();
                JsonArray iterationDetailsList = iteration.getAsJsonObject().get("ITERATION_DETAILS").getAsJsonArray();
                for(JsonElement iterationDetail : iterationDetailsList)
                {
                    JsonArray webPagesList = iterationDetail.getAsJsonObject().get("WEB_PAGES").getAsJsonArray();
                    for(JsonElement webPages : webPagesList)
                    {
                        JsonArray webPageList = webPages.getAsJsonObject().get("WEB_PAGE").getAsJsonArray();
                        for(JsonElement webPage : webPageList)
                        {
                            mobileDataFields = webPage.getAsJsonObject().get("TEST_DATA").getAsJsonArray();

                        }
                    }
                }
            }
        }
        return mobileDataFields;
    }
    /**
     * accessMainframeData Method is used to access data against a test case's MAINFRAME component from it's corresponding Master data json file
     * @param testCaseId This is testng method name
     * @param i This is to denote which array element needs to be picked
     * @param typesList This is actual JsonArray which contains the data
     * @param day This is to denote which execution day's data needs to be picked
     * @return Returns a JsonObject containing the required mainframe data.
     */
    public synchronized JsonObject accessMainframeData(String testCaseId ,int i , JsonArray typesList, String day)
    {
        JsonObject mainFrameDataFields = null;
        JsonElement APItype= typesList.get(i);
        JsonObject jsonObject2 = APItype.getAsJsonObject();
        JsonArray iterationList = jsonObject2.get("ITERATION").getAsJsonArray();
        for(JsonElement iteration : iterationList)
        {
            int iterationNumber = iteration.getAsJsonObject().get("ITERATION_NUMBER").getAsInt();
            JsonArray iterationDetailsList = iteration.getAsJsonObject().get("ITERATION_DETAILS").getAsJsonArray();
            for(JsonElement iterationDetail : iterationDetailsList)
            {
                JsonArray testCaseDetailsList = iterationDetail.getAsJsonObject().get("TEST_CASE_DETAILS").getAsJsonArray();
                for(JsonElement testCaseDetails : testCaseDetailsList)
                {
                    JsonArray dayWiseDetailsList = testCaseDetails.getAsJsonObject().get("DAY_WISE_DETAILS").getAsJsonArray();
                    for(JsonElement dayWiseDetail : dayWiseDetailsList)
                    {
                        if(dayWiseDetail.getAsJsonObject().has("DAY") && dayWiseDetail.getAsJsonObject().get("DAY").getAsString()!=null) {
                            if (day.equalsIgnoreCase(dayWiseDetail.getAsJsonObject().get("DAY").getAsString())) {
                                JsonArray testDataList = dayWiseDetail.getAsJsonObject().get("TEST_DATA").getAsJsonArray();
                                for (JsonElement testData : testDataList) {
                                    mainFrameDataFields = testData.getAsJsonObject().get("CURRENT_DAY_TEST_DATA").getAsJsonObject();
                                }
                            }
                        }else {
                            System.out.println("Master data does not have DAY field / has null value for DAY field in test case :"+testCaseId);
                        }
                    }
                }
            }
        }

        return mainFrameDataFields;
    }
    /**
     * accessWebMobileMasterData Method is used to access data against a test case's WEB/MOBILE component from it's corresponding Master data json file
     * @param testCaseId This is testng method name
     * @param component This is to COMPONENT for which data is to be retrieved.
     * @param universalMasterDataDriverLocalObject This is actual json file which contains all the test case's data.
     * @see #accessWebData(String, int, JsonArray) This method is called to get the actual web JsonObject elements
     * @see #accessMobileData(String, int, JsonArray) This method is called to get the actual mobile JsonObject elements
     * @return Returns a JsonObject containing the required WEB/MOBILE data.
     */
    public synchronized  JsonArray accessWebMobileMasterData(String testCaseId, String component , JsonObject universalMasterDataDriverLocalObject) {
        JsonArray outputDataFields = null;
        JsonArray testCaseList = universalMasterDataDriverLocalObject.getAsJsonArray("TEST_CASES");
        for(JsonElement testCase : testCaseList)
        {
            JsonObject jsonObject1  = testCase.getAsJsonObject();
            if(jsonObject1.has("TEST_CASE_ID") && jsonObject1.get("TEST_CASE_ID").getAsString()!=null) {
                String testId = jsonObject1.get("TEST_CASE_ID").getAsString();

                if (testId.equalsIgnoreCase(testCaseId)) {
                    JsonArray typesList = jsonObject1.getAsJsonArray("TYPE");
                    for (int i = 0; i < typesList.size(); i++) {
                        if(typesList.get(i).getAsJsonObject().has("COMPONENT") && typesList.get(i).getAsJsonObject().get("COMPONENT").getAsString()!=null)
                        {
                            String componentName = typesList.get(i).getAsJsonObject().get("COMPONENT").getAsString();
                            if (componentName.equalsIgnoreCase("WEB") && component.equalsIgnoreCase("WEB"))
                            {
                                outputDataFields = accessWebData(testCaseId, i, typesList);
                            }
                            else if (componentName.equalsIgnoreCase("MOBILE") && component.equalsIgnoreCase("MOBILE"))
                            {
                                outputDataFields = accessMobileData(testCaseId, i, typesList);
                            }
                        }else {
                            System.out.println("Master data does not have COMPONENT field / has null value for COMPONENT field in test case :"+testCaseId);
                        }
                    }
                }
            }else {
                System.out.println("Master data does not have TEST_CASE_ID field/ has null value for TEST_CASE_ID field in test case :"+testCaseId);
            }
        }
        return outputDataFields;
    }
    /**
     * accessWebMobileMasterData Method is used to access data against a test data iteration
     * @param iterationObject This is actual iteration object which contains all the test case's page wise data.
     * @return Returns a JsonArray containing the required WEB/MOBILE's page wise data.
     */
    public synchronized  JsonArray accessWebMobileMasterData(JsonObject iterationObject) {
        JsonArray pageList = null;
        JsonArray iterationDetails  = iterationObject.get("ITERATION_DETAILS").getAsJsonArray();
        for(JsonElement iterationDetail : iterationDetails)
        {
            JsonArray webPages = iterationDetail.getAsJsonObject().get("WEB_PAGES").getAsJsonArray();
            for(JsonElement webPage : webPages)
            {
                JsonArray webPageList = webPage.getAsJsonObject().get("WEB_PAGE").getAsJsonArray();
                for(JsonElement page : webPageList){
                    pageList = page.getAsJsonObject().get("TEST_DATA").getAsJsonArray();

                }
            }
        }
        return pageList;
    }

    /**
     * accessMainframeMasterData Method is used to access data against a test case's WEB/MOBILE component from it's corresponding Master data json file
     * @param testCaseId This is testng method name
     * @param component This is to COMPONENT for which data is to be retrieved.
     * @param universalMasterDataDriverLocalObject This is actual json file which contains all the test case's data.
     * @param Day This is to denote the execution day for which data needs to be picked up.
     * @see #accessMainframeData(String, int, JsonArray, String)  This method is called to get the actual mainframe JsonObject elements
     * @return Returns a JsonObject containing the required mainframe data.
     */
    public synchronized  JsonObject accessMainframeMasterData(String testCaseId, String component , JsonObject universalMasterDataDriverLocalObject, String Day) {
        JsonObject outputDataFields = null;
        JsonArray testCaseList = universalMasterDataDriverLocalObject.getAsJsonArray("TEST_CASES");
        for(JsonElement testCase : testCaseList)
        {
            JsonObject jsonObject1  = testCase.getAsJsonObject();
            if(jsonObject1.has("TEST_CASE_ID") && jsonObject1.get("TEST_CASE_ID").getAsString()!=null) {
                String testId = jsonObject1.get("TEST_CASE_ID").getAsString();

                if (testId.equalsIgnoreCase(testCaseId)) {
                    JsonArray typesList = jsonObject1.getAsJsonArray("TYPE");
                    for (int i = 0; i < typesList.size(); i++) {
                        if(typesList.get(i).getAsJsonObject().has("COMPONENT") && typesList.get(i).getAsJsonObject().get("COMPONENT").getAsString()!=null)
                        {
                            String componentName = typesList.get(i).getAsJsonObject().get("COMPONENT").getAsString();
                            if (componentName.equalsIgnoreCase("MAINFRAME") && component.equalsIgnoreCase("MAINFRAME")) {
                                outputDataFields = accessMainframeData(testCaseId, i, typesList, Day);
                            }
                        }else {
                            System.out.println("Master data does not have COMPONENT field / has null value for COMPONENT field in test case :"+testCaseId);
                        }
                    }
                }
            }else {
                System.out.println("Master data does not have TEST_CASE_ID field/ has null value for TEST_CASE_ID field in test case :"+testCaseId);
            }
        }
        return outputDataFields;
    }
    /**
     * accessMainframeMasterData Method is used to access data against a test case's WEB/MOBILE component from it's corresponding Master data json file
     * @param testCaseId This is testng method name
     * @param component This is to COMPONENT for which data is to be retrieved.
     * @param universalMasterDataDriverLocalObject This is actual json file which contains all the test case's data.
     * @param endpointName This is actual ENDPOINT name based on which data is to be retrieved.
     * @see #accessRequestApiData(String, int, JsonArray, String)  This method is called to get the actual API request payload JsonObject elements
     * @return Returns a JsonObject containing the required API data.
     */
    public synchronized  JsonObject accessApiRequestData(String testCaseId, String component ,  JsonObject universalMasterDataDriverLocalObject,String endpointName) {
        JsonObject outputDataFields = null;
        JsonArray testCaseList = universalMasterDataDriverLocalObject.getAsJsonArray("TEST_CASES");
        for (JsonElement testCase : testCaseList) {
            JsonObject jsonObject1 = testCase.getAsJsonObject();
            if (jsonObject1.has("TEST_CASE_ID") && jsonObject1.get("TEST_CASE_ID").getAsString() != null) {
                String testId = jsonObject1.get("TEST_CASE_ID").getAsString();

                if (testId.equalsIgnoreCase(testCaseId)) {
                    JsonArray typesList = jsonObject1.getAsJsonArray("TYPE");
                    for (int i = 0; i < typesList.size(); i++) {
                        if (typesList.get(i).getAsJsonObject().has("COMPONENT") && typesList.get(i).getAsJsonObject().get("COMPONENT").getAsString() != null) {
                            String componentName = typesList.get(i).getAsJsonObject().get("COMPONENT").getAsString();
                            if (componentName.equalsIgnoreCase("API") && component.equalsIgnoreCase("API")) {
                                outputDataFields = accessRequestApiData(testCaseId, i, typesList, endpointName);

                            } else {
                                System.out.println("Master data does not have COMPONENT field / has null value for COMPONENT field in test case :" + testCaseId);
                            }
                        }
                    }
                }
            } else {
                System.out.println("Master data does not have TEST_CASE_ID field/ has null value for TEST_CASE_ID field in test case :" + testCaseId);
            }
        }

        return outputDataFields;
    }
    /**
     * accessMainframeMasterData Method is used to access data against a test case's WEB/MOBILE component from it's corresponding Master data json file
     * @param testCaseId This is testng method name
     * @param component This is to COMPONENT for which data is to be retrieved.
     * @param universalMasterDataDriverLocalObject This is actual json file which contains all the test case's data.
     * @param endpointName This is actual ENDPOINT name based on which data is to be retrieved.
     * @see #accessReponseApiData(String, int, JsonArray, String)  This method is called to get the expected API response payload JsonObject elements
     * @return Returns a JsonObject containing the required API data.
     */
    public synchronized JsonObject accessApiResponsetData(String testCaseId, String component ,JsonObject universalMasterDataDriverLocalObject,  String endpointName) {
        JsonObject outputDataFields = null;
        JsonArray testCaseList = universalMasterDataDriverLocalObject.getAsJsonArray("TEST_CASES");
        for (JsonElement testCase : testCaseList) {
            JsonObject jsonObject1 = testCase.getAsJsonObject();
            if (jsonObject1.has("TEST_CASE_ID") && jsonObject1.get("TEST_CASE_ID").getAsString() != null) {
                String testId = jsonObject1.get("TEST_CASE_ID").getAsString();

                if (testId.equalsIgnoreCase(testCaseId)) {
                    JsonArray typesList = jsonObject1.getAsJsonArray("TYPE");
                    for (int i = 0; i < typesList.size(); i++) {
                        if (typesList.get(i).getAsJsonObject().has("COMPONENT") && typesList.get(i).getAsJsonObject().get("COMPONENT").getAsString() != null) {
                            String componentName = typesList.get(i).getAsJsonObject().get("COMPONENT").getAsString();
                            if (componentName.equalsIgnoreCase("API") && component.equalsIgnoreCase("API")) {
                                outputDataFields = accessReponseApiData(testCaseId, i, typesList, endpointName);

                            } else {
                                System.out.println("Master data does not have COMPONENT field / has null value for COMPONENT field in test case :" + testCaseId);
                            }
                        }
                    }
                }
            } else {
                System.out.println("Master data does not have TEST_CASE_ID field/ has null value for TEST_CASE_ID field in test case :" + testCaseId);
            }
        }

        return outputDataFields;
    }
    /**
     * modifyExpectedResponseBeforeComparing Method is used to access data against a test case's WEB/MOBILE component from it's corresponding Master data json file
     * @param actualResponse This is JsonObject which contains actual json data.
     * @param expectedResponsePayloadDataFields This is JsonObject which contains actual json data fields to be modified.
     * @return Returns a JsonObject containing the modified expectedResponsePayloadDataFields data.
     */
    public JsonObject modifyExpectedResponseBeforeComparing(JsonObject actualResponse,JsonObject expectedResponsePayloadDataFields) {
        Iterator itr = expectedResponsePayloadDataFields.entrySet().iterator();
        while(itr.hasNext()) {
            Map.Entry<String, JsonElement> entry = (Map.Entry)itr.next();
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            if(jsonCompareUtils.getValueFromComplexJson(actualResponse,value.getAsString(),"")!=null)
            {
                if(!jsonCompareUtils.getValueFromComplexJson(expectedResponsePayloadDataFields,key,"").getAsString().equals("not null")) {
                    expectedResponsePayloadDataFields.add(key, jsonCompareUtils.getValueFromComplexJson(actualResponse,key,""));
                }
            }

        }
        return expectedResponsePayloadDataFields;
    }
    /**
     * modifyExpectedResponseBeforeComparing Method is used to access data against a test case's WEB/MOBILE component from it's corresponding Master data json file
     * @param actualResponse This is JsonElement which contains actual json data.
     * @param expectedResponsePayloadDataFields This is JsonObject which contains actual json data fields to be modified.
     * @return Returns a JsonObject containing the modified expectedResponsePayloadDataFields data.
     */
    public JsonObject modifyExpectedResponseBeforeComparing(JsonElement actualResponse,JsonObject expectedResponsePayloadDataFields) {
        Iterator itr = expectedResponsePayloadDataFields.entrySet().iterator();
        while(itr.hasNext()) {
            Map.Entry<String, JsonElement> entry = (Map.Entry)itr.next();
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            if(jsonCompareUtils.getValueFromComplexJson(actualResponse,value.getAsString(),"")!=null)
            {
                if(!jsonCompareUtils.getValueFromComplexJson(expectedResponsePayloadDataFields,key,"").getAsString().equals("not null")) {
                    expectedResponsePayloadDataFields.add(key, jsonCompareUtils.getValueFromComplexJson(actualResponse,key,""));
                }
            }

        }
        return expectedResponsePayloadDataFields;
    }
    /**
     * modifyExpectedResponseBeforeComparing Method is used to access data against a test case's WEB/MOBILE component from it's corresponding Master data json file
     * @param unalteredPayload This is JsonObject which contains unaltered payload.
     * @param dynamicRequestPayloadDataFields This is JsonObject which contains json data fields to be modified.
     * @param previousResponse This is the Response object of previous API call.
     * @return Returns a JsonObject containing the modified/non-modifed payload data.
     */
    public synchronized JsonObject readRequestPayloadDynamicFieldsAndMerge(JsonObject unalteredPayload, JsonObject dynamicRequestPayloadDataFields, Response... previousResponse) throws IOException {
        JsonElement value = null;
        if (previousResponse.length > 0)
        {
            Response response = previousResponse[0];
            JsonObject responseObject = jsonOperations.convertResponseToJsonObject(response);
            Iterator itr1 = dynamicRequestPayloadDataFields.keySet().iterator();
            while(itr1.hasNext()) {
                String key = (String)itr1.next();
                if (jsonCompareUtils.getValueFromComplexJson(unalteredPayload,key,"")!=null) {
                    if (jsonCompareUtils.getValueFromComplexJson(unalteredPayload,key,"").getAsString().contains("{{static}}"))
                    {
                        jsonOperations.modifyKeyValueInJson(unalteredPayload,key,jsonCompareUtils.getValueFromComplexJson(dynamicRequestPayloadDataFields,key,""));
                    }
                    else if (unalteredPayload.get(key).toString().contains("{{dynamic}}") && jsonCompareUtils.getValueFromComplexJson(responseObject,dynamicRequestPayloadDataFields.get(key).getAsString(),"") != null)
                    {
                        String responseKey = dynamicRequestPayloadDataFields.get(key).getAsString();
                        // JsonElement value = JsonParser.parseString(response.jsonPath().get(responseKey));
                        if(!responseKey.contains("."))
                        {
                            value = jsonCompareUtils.getValueFromComplexJson(responseObject,responseKey,"");
                        }else{
                            value = jsonCompareUtils.getValueFromComplexJson(responseObject,responseKey.split("\\.")[responseKey.split("\\.").length-1],"",0,responseKey);
                        }
                        unalteredPayload.add(key, value);
                    }
                }
            }
        }
        else
        {
            Iterator itr2 = dynamicRequestPayloadDataFields.keySet().iterator();
            while(itr2.hasNext())
            {
                String key = (String) itr2.next();

                    if(!key.contains("."))
                    {
                        value = jsonCompareUtils.getValueFromComplexJson(dynamicRequestPayloadDataFields,key,"");
                    }else{
                        value = jsonCompareUtils.getValueFromComplexJson(dynamicRequestPayloadDataFields,key.split("\\.")[key.split("\\.").length-1],"",0,key);
                    }
                    unalteredPayload = jsonOperations.modifyKeyValueInJson(unalteredPayload,key,value).getAsJsonObject();
                    //unalteredPayload.add(jsonCompareUtils.getKeyPathFromComplexJson(unalteredPayload,key,""), jsonCompareUtils.getValueFromComplexJson(dynamicRequestPayloadDataFields,key,""));

            }
        }
        return unalteredPayload;
    }
    public synchronized JsonObject readRequestPayloadDynamicFieldsAndMerge(JsonElement unalteredPayload, JsonObject dynamicRequestPayloadDataFields,Map<String,JsonElement> frameworkKeyVaules,Response... previousResponse) throws IOException {
        if(frameworkKeyVaules.size()>0)
        {
            if (previousResponse.length > 0)
            {
                Response response = previousResponse[0];
                JsonObject responseObject = jsonOperations.convertResponseToJsonObject(response);
                Iterator itr1 = dynamicRequestPayloadDataFields.keySet().iterator();
                while(itr1.hasNext()) {
                    String key = (String)itr1.next();
                    if (dynamicRequestPayloadDataFields.get(key).getAsString().equalsIgnoreCase("")) {
                        jsonOperations.modifyKeyValueInJson(unalteredPayload, key, frameworkKeyVaules.get(key));
                    }else {
                        if (jsonCompareUtils.getValueFromComplexJson(unalteredPayload, key, "") != null) {
                            if (jsonCompareUtils.getValueFromComplexJson(unalteredPayload, key, "").getAsString().contains("{{static}}")) {
                                jsonOperations.modifyKeyValueInJson(unalteredPayload, key, jsonCompareUtils.getValueFromComplexJson(dynamicRequestPayloadDataFields, key, ""));
                            } else if (unalteredPayload.getAsJsonObject().get(key).toString().contains("{{dynamic}}") && jsonCompareUtils.getValueFromComplexJson(responseObject, dynamicRequestPayloadDataFields.get(key).getAsString(), "") != null) {
                                String responseKey = dynamicRequestPayloadDataFields.get(key).getAsString();
                                // JsonElement value = JsonParser.parseString(response.jsonPath().get(responseKey));
                                JsonElement value = jsonCompareUtils.getValueFromComplexJson(responseObject, responseKey, "");
                                unalteredPayload.getAsJsonObject().add(key, value);
                            }
                        }
                    }
                }
            }
            else
            {
                Iterator itr2 = dynamicRequestPayloadDataFields.keySet().iterator();
                while (itr2.hasNext()) {
                    String key = (String) itr2.next();
                    if (dynamicRequestPayloadDataFields.get(key).getAsString().equalsIgnoreCase("")) {
                        jsonOperations.modifyKeyValueInJson(unalteredPayload, key, frameworkKeyVaules.get(key));
                    } else {
                        if (jsonCompareUtils.getValueFromComplexJson(unalteredPayload, key, "") != null) {
                            JsonElement modifyValue = jsonCompareUtils.getValueFromComplexJson(dynamicRequestPayloadDataFields, key, "");
                            unalteredPayload = jsonOperations.modifyKeyValueInJson(unalteredPayload, key, modifyValue).getAsJsonObject();
                            //unalteredPayload.add(jsonCompareUtils.getKeyPathFromComplexJson(unalteredPayload,key,""), jsonCompareUtils.getValueFromComplexJson(dynamicRequestPayloadDataFields,key,""));
                        }
                    }
                }
            }
        }
        else
        {
            if (previousResponse.length > 0)
            {
                Response response = previousResponse[0];
                JsonObject responseObject = jsonOperations.convertResponseToJsonObject(response);
                Iterator itr1 = dynamicRequestPayloadDataFields.keySet().iterator();
                while(itr1.hasNext()) {
                    String key = (String)itr1.next();
                    if (jsonCompareUtils.getValueFromComplexJson(unalteredPayload,key,"")!=null) {
                        if (jsonCompareUtils.getValueFromComplexJson(unalteredPayload,key,"").getAsString().contains("{{static}}"))
                        {
                            jsonOperations.modifyKeyValueInJson(unalteredPayload,key,jsonCompareUtils.getValueFromComplexJson(dynamicRequestPayloadDataFields,key,""));
                        }
                        else if (unalteredPayload.getAsJsonObject().get(key).toString().contains("{{dynamic}}") && jsonCompareUtils.getValueFromComplexJson(responseObject,dynamicRequestPayloadDataFields.get(key).getAsString(),"") != null)
                        {
                            String responseKey = dynamicRequestPayloadDataFields.get(key).getAsString();
                            // JsonElement value = JsonParser.parseString(response.jsonPath().get(responseKey));
                            JsonElement value = jsonCompareUtils.getValueFromComplexJson(responseObject,responseKey,"");
                            unalteredPayload.getAsJsonObject().add(key, value);
                        }
                    }
                }
            }
            else {
                Iterator itr2 = dynamicRequestPayloadDataFields.keySet().iterator();
                while (itr2.hasNext())
                {
                    String key = (String) itr2.next();
                    if (jsonCompareUtils.getValueFromComplexJson(unalteredPayload, key, "") != null)
                    {
                        JsonElement modifyValue = jsonCompareUtils.getValueFromComplexJson(dynamicRequestPayloadDataFields, key, "");
                        unalteredPayload = jsonOperations.modifyKeyValueInJson(unalteredPayload, key, modifyValue).getAsJsonObject();
                        //unalteredPayload.add(jsonCompareUtils.getKeyPathFromComplexJson(unalteredPayload,key,""), jsonCompareUtils.getValueFromComplexJson(dynamicRequestPayloadDataFields,key,""));
                    }
                }
            }
        }
        return unalteredPayload.getAsJsonObject();
    }
     public synchronized JsonArray getIterationDataSetsFromMasterData(String testCaseId, String component ,  JsonObject universalMasterDataDriverLocalObject) {
        JsonArray iterationList = null;
        JsonArray testCaseList = universalMasterDataDriverLocalObject.getAsJsonArray("TEST_CASES");
        for (JsonElement testCase : testCaseList) {
            JsonObject jsonObject1 = testCase.getAsJsonObject();
            if (jsonObject1.has("TEST_CASE_ID") && jsonObject1.get("TEST_CASE_ID").getAsString() != null) {
                String testId = jsonObject1.get("TEST_CASE_ID").getAsString();

                if (testId.equalsIgnoreCase(testCaseId)) {
                    JsonArray typesList = jsonObject1.getAsJsonArray("TYPE");
                    for (int i = 0; i < typesList.size(); i++) {
                        if (typesList.get(i).getAsJsonObject().has("COMPONENT") && typesList.get(i).getAsJsonObject().get("COMPONENT").getAsString() != null) {
                            String componentName = typesList.get(i).getAsJsonObject().get("COMPONENT").getAsString();
                            if (componentName.equalsIgnoreCase("API") && component.equalsIgnoreCase("API")) {
                                JsonElement APItype= typesList.get(i);
                                JsonObject jsonObject2 = APItype.getAsJsonObject();
                                iterationList = jsonObject2.get("ITERATION").getAsJsonArray();

                            } else if (componentName.equalsIgnoreCase("WEB") && component.equalsIgnoreCase("WEB")) {
                                JsonElement APItype= typesList.get(i);
                                JsonObject jsonObject2 = APItype.getAsJsonObject();
                                iterationList = jsonObject2.get("ITERATION").getAsJsonArray();
                            } else {
                                System.out.println("Master data does not have COMPONENT field / has null value for COMPONENT field in test case :" + testCaseId);
                            }
                        }
                    }
                }
            } else {
                System.out.println("Master data does not have TEST_CASE_ID field/ has null value for TEST_CASE_ID field in test case :" + testCaseId);
            }
        }

        return iterationList;
    }
    public synchronized  JsonObject accessApiRequestData(String testCaseId ,  JsonObject iterationData, String endpointName) {
        JsonObject dynamicRequestPayloadDataFields=null;
        {
            JsonArray iterationDetailsList = iterationData.get("ITERATION_DETAILS").getAsJsonArray();
            for (JsonElement iterationDetail : iterationDetailsList) {
                JsonArray endpointDetailsList = iterationDetail.getAsJsonObject().get("ENDPOINT_DETAILS").getAsJsonArray();
                for (JsonElement endpointDetail : endpointDetailsList) {
                    JsonArray endpointsList = endpointDetail.getAsJsonObject().get("ENDPOINTS").getAsJsonArray();
                    for (JsonElement endpoints : endpointsList) {
                        if (endpoints.getAsJsonObject().has("ENDPOINT_NAME") && endpoints.getAsJsonObject().get("ENDPOINT_NAME").getAsString() != null) {
                            if (endpointName.equalsIgnoreCase(endpoints.getAsJsonObject().get("ENDPOINT_NAME").getAsString())) {
                                JsonArray testDataList = endpoints.getAsJsonObject().get("TEST_DATA").getAsJsonArray();
                                for (JsonElement testData : testDataList) {
                                    dynamicRequestPayloadDataFields = testData.getAsJsonObject().get("DYNAMIC_REQUEST_PAYLOAD_FIELDS").getAsJsonObject();
                                }
                            }
                        } else {
                            System.out.println("Master data does not have COMPONENT field / has null value for COMPONENT field in test case :" + testCaseId);
                        }
                    }
                }
            }

        }

        return dynamicRequestPayloadDataFields;
    }
    public synchronized  JsonObject accessWebMobileIterationData(String testCaseId ,  JsonObject iterationData, String pageName) {
        JsonObject dynamicRequestPayloadDataFields=null;
        {
            JsonArray iterationDetailsList = iterationData.get("ITERATION_DETAILS").getAsJsonArray();
            for (JsonElement iterationDetail : iterationDetailsList) {
                JsonArray endpointDetailsList = iterationDetail.getAsJsonObject().get("WEB_PAGES").getAsJsonArray();
                for (JsonElement endpointDetail : endpointDetailsList) {
                    JsonArray endpointsList = endpointDetail.getAsJsonObject().get("WEB_PAGE").getAsJsonArray();
                    for (JsonElement endpoints : endpointsList) {
                        if (endpoints.getAsJsonObject().has("TEST_DATA") && endpoints.getAsJsonObject().get("TEST_DATA").getAsString() != null) {
                                JsonObject testDataList = endpoints.getAsJsonObject().get("TEST_DATA").getAsJsonObject();


                        } else {
                            System.out.println("Master data does not have COMPONENT field / has null value for COMPONENT field in test case :" + testCaseId);
                        }
                    }
                }
            }

        }

        return dynamicRequestPayloadDataFields;
    }
    public synchronized  JsonObject accessApiResponseData(String testCaseId , JsonObject iterationData, String endpointName) {
        JsonObject  expectedResponsePayloadDataFields =null;
        {
            JsonArray iterationDetailsList = iterationData.get("ITERATION_DETAILS").getAsJsonArray();
            for (JsonElement iterationDetail : iterationDetailsList) {
                JsonArray endpointDetailsList = iterationDetail.getAsJsonObject().get("ENDPOINT_DETAILS").getAsJsonArray();
                for (JsonElement endpointDetail : endpointDetailsList) {
                    JsonArray endpointsList = endpointDetail.getAsJsonObject().get("ENDPOINTS").getAsJsonArray();
                    for (JsonElement endpoints : endpointsList) {
                        if (endpoints.getAsJsonObject().has("ENDPOINT_NAME") && endpoints.getAsJsonObject().get("ENDPOINT_NAME").getAsString() != null) {
                            if (endpointName.equalsIgnoreCase(endpoints.getAsJsonObject().get("ENDPOINT_NAME").getAsString())) {
                                JsonArray testDataList = endpoints.getAsJsonObject().get("TEST_DATA").getAsJsonArray();
                                for (JsonElement testData : testDataList) {
                                    expectedResponsePayloadDataFields = testData.getAsJsonObject().get("EXPECTED_RESPONSE_PAYLOAD").getAsJsonObject();
                                }
                            }
                        } else {
                            System.out.println("Master data does not have COMPONENT field / has null value for COMPONENT field in test case :" + testCaseId);
                        }
                    }
                }
            }


        }
        return expectedResponsePayloadDataFields;
}
    public synchronized  JsonObject accessPageWiseWebMobileData(JsonElement iterationData,String pageConstant) {
        JsonObject  pageData =null;
        String pageName;
        {
            JsonObject iteration = iterationData.getAsJsonObject();
            for(JsonElement iterationDetail: iteration.getAsJsonArray("ITERATION_DETAILS")){
                for(JsonElement webPages : iterationDetail.getAsJsonObject().getAsJsonArray("WEB_PAGES")){
                    for(JsonElement webPage: webPages.getAsJsonObject().getAsJsonArray("WEB_PAGE")){
                        for(JsonElement testData : webPage.getAsJsonObject().getAsJsonArray("TEST_DATA")){
                            pageName = testData.getAsJsonObject().get("PAGE_NAME").getAsString();
                            if(pageName.equalsIgnoreCase(pageConstant)){
                                pageData =testData.getAsJsonObject().get("DATA").getAsJsonObject();
                            }
                        }
                    }
                }
            }
        }
        return pageData;
    }
}