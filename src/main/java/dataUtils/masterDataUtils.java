package dataUtils;

import DataHandlerUtils.JsonCompareUtils;
import LogUtils.LibraryLoggingUtils;
import com.google.gson.*;
import io.restassured.*;
import io.restassured.response.Response;
import DataHandlerUtils.JsonOperations;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import java.util.Map;
    /**
     * MasterDataUtils class contains the methods related to retrieving data from json file or JsonObject
     */
    public class masterDataUtils {
        LibraryLoggingUtils libraryLoggingUtils;
        String className = Thread.currentThread().getStackTrace()[1].getClassName();
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
            String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
            libraryLoggingUtils = new LibraryLoggingUtils();
            JsonObject dynamicRequestPayloadDataFields=null;
            try{
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
            } catch (Exception e) {
                libraryLoggingUtils.getCurrentMethodException(className, methodName, e);
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
        public synchronized JsonObject accessReponseApiData(String testCaseId , int i , JsonArray typesList, String endpointName) {
            String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
            libraryLoggingUtils = new LibraryLoggingUtils();
            JsonObject expectedResponsePayloadDataFields = null;
            try {
                JsonElement APItype = typesList.get(i);
                JsonObject jsonObject2 = APItype.getAsJsonObject();
                JsonArray iterationList = jsonObject2.get("ITERATION").getAsJsonArray();
                for (JsonElement iteration : iterationList) {
                    int iterationNumber = iteration.getAsJsonObject().get("ITERATION_NUMBER").getAsInt();
                    JsonArray iterationDetailsList = iteration.getAsJsonObject().get("ITERATION_DETAILS").getAsJsonArray();
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
            } catch (Exception e) {
                libraryLoggingUtils.getCurrentMethodException(className, methodName, e);
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
        public synchronized JsonObject accessWebData(String testCaseId , int i , JsonArray typesList) {
            String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
            libraryLoggingUtils = new LibraryLoggingUtils();
            JsonObject webPageDataFields = null;
            try{
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
                                webPageDataFields = webPage.getAsJsonObject().get("TEST_DATA").getAsJsonObject();

                            }
                        }
                    }
                }
            } catch (Exception e) {
                libraryLoggingUtils.getCurrentMethodException(className, methodName, e);
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
        public synchronized JsonObject accessMobileData(String testCaseId ,int i , JsonArray typesList) {
            String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
            libraryLoggingUtils = new LibraryLoggingUtils();
            JsonObject mobileDataFields = null;
            try {
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
                                mobileDataFields = webPage.getAsJsonObject().get("TEST_DATA").getAsJsonObject();

                            }
                        }
                    }
                }
            } catch (Exception e) {
                libraryLoggingUtils.getCurrentMethodException(className, methodName, e);
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
        public synchronized JsonObject accessMainframeData(String testCaseId ,int i , JsonArray typesList, String day) {
            String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
            libraryLoggingUtils = new LibraryLoggingUtils();
            JsonObject mainFrameDataFields = null;
            try {
                mainFrameDataFields = null;
                JsonElement APItype = typesList.get(i);
                JsonObject jsonObject2 = APItype.getAsJsonObject();
                JsonArray iterationList = jsonObject2.get("ITERATION").getAsJsonArray();
                for (JsonElement iteration : iterationList) {
                    int iterationNumber = iteration.getAsJsonObject().get("ITERATION_NUMBER").getAsInt();
                    JsonArray iterationDetailsList = iteration.getAsJsonObject().get("ITERATION_DETAILS").getAsJsonArray();
                    for (JsonElement iterationDetail : iterationDetailsList) {
                        JsonArray testCaseDetailsList = iterationDetail.getAsJsonObject().get("TEST_CASE_DETAILS").getAsJsonArray();
                        for (JsonElement testCaseDetails : testCaseDetailsList) {
                            JsonArray dayWiseDetailsList = testCaseDetails.getAsJsonObject().get("DAY_WISE_DETAILS").getAsJsonArray();
                            for (JsonElement dayWiseDetail : dayWiseDetailsList) {
                                if (dayWiseDetail.getAsJsonObject().has("DAY") && dayWiseDetail.getAsJsonObject().get("DAY").getAsString() != null) {
                                    if (day.equalsIgnoreCase(dayWiseDetail.getAsJsonObject().get("DAY").getAsString())) {
                                        JsonArray testDataList = dayWiseDetail.getAsJsonObject().get("TEST_DATA").getAsJsonArray();
                                        for (JsonElement testData : testDataList) {
                                            mainFrameDataFields = testData.getAsJsonObject().get("CURRENT_DAY_TEST_DATA").getAsJsonObject();
                                        }
                                    }
                                } else {
                                    System.out.println("Master data does not have DAY field / has null value for DAY field in test case :" + testCaseId);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                libraryLoggingUtils.getCurrentMethodException(className, methodName, e);
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
        public synchronized  JsonObject accessWebMobileMasterData(String testCaseId, String component , JsonObject universalMasterDataDriverLocalObject)        {
            String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
            libraryLoggingUtils = new LibraryLoggingUtils();
            JsonObject outputDataFields = null;
            try {
                outputDataFields = null;
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
                                    if (componentName.equalsIgnoreCase("WEB") && component.equalsIgnoreCase("WEB")) {
                                        outputDataFields = accessWebData(testCaseId, i, typesList);
                                    } else if (componentName.equalsIgnoreCase("MOBILE") && component.equalsIgnoreCase("MOBILE")) {
                                        outputDataFields = accessMobileData(testCaseId, i, typesList);
                                    }
                                } else {
                                    System.out.println("Master data does not have COMPONENT field / has null value for COMPONENT field in test case :" + testCaseId);
                                }
                            }
                        }
                    } else {
                        System.out.println("Master data does not have TEST_CASE_ID field/ has null value for TEST_CASE_ID field in test case :" + testCaseId);
                    }
                }
            } catch (Exception e) {
                libraryLoggingUtils.getCurrentMethodException(className, methodName, e);
            }
            return outputDataFields;
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
            String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
            libraryLoggingUtils = new LibraryLoggingUtils();
            JsonObject outputDataFields = null;
            try {
                outputDataFields = null;
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
                                    if (componentName.equalsIgnoreCase("MAINFRAME") && component.equalsIgnoreCase("MAINFRAME")) {
                                        outputDataFields = accessMainframeData(testCaseId, i, typesList, Day);
                                    }
                                } else {
                                    System.out.println("Master data does not have COMPONENT field / has null value for COMPONENT field in test case :" + testCaseId);
                                }
                            }
                        }
                    } else {
                        System.out.println("Master data does not have TEST_CASE_ID field/ has null value for TEST_CASE_ID field in test case :" + testCaseId);
                    }
                }
            } catch (Exception e) {
                libraryLoggingUtils.getCurrentMethodException(className, methodName, e);
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
            String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
            libraryLoggingUtils = new LibraryLoggingUtils();
            JsonObject outputDataFields = null;
            try {
                outputDataFields = null;
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
            } catch (Exception e) {
                libraryLoggingUtils.getCurrentMethodException(className, methodName, e);
            }
            return outputDataFields;
        }

        /**
         * accessMainframeMasterData Method is used to access data against a test case's WEB/MOBILE component from it's corresponding Master data json file
         * @param testCaseId This is testng method name
         * @param component This is to COMPONENT for which data is to be retrieved.
         * @param universalMasterDataDriverLocalObject This is actual json file which contains all the test case's data.
         * @param endpointName This is actual ENDPOINT name based on which data is to be retrieved.
         * @see #accessRequestApiData(String, int, JsonArray, String)  This method is called to get the expected API response payload JsonObject elements
         * @return Returns a JsonObject containing the required API data.
         */
        public synchronized JsonObject accessApiResponsetData(String testCaseId, String component ,JsonObject universalMasterDataDriverLocalObject,  String endpointName) {

            String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
            libraryLoggingUtils = new LibraryLoggingUtils();
            JsonObject outputDataFields = null;
            try {
                outputDataFields = null;
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
            } catch (Exception e) {
                libraryLoggingUtils.getCurrentMethodException(className, methodName, e);
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
            String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
            libraryLoggingUtils = new LibraryLoggingUtils();
            try {
                Iterator itr = expectedResponsePayloadDataFields.entrySet().iterator();
                while (itr.hasNext()) {
                    Map.Entry<String, JsonElement> entry = (Map.Entry) itr.next();
                    String key = entry.getKey();
                    JsonElement value = entry.getValue();
                    if (jsonCompareUtils.getValueFromComplexJson(actualResponse, value.getAsString(), "") != null) {
                        if (!jsonCompareUtils.getValueFromComplexJson(expectedResponsePayloadDataFields, key, "").getAsString().equals("not null")) {
                            expectedResponsePayloadDataFields.add(key, jsonCompareUtils.getValueFromComplexJson(actualResponse, key, ""));
                        }
                    }
                }
            } catch (Exception e) {
                libraryLoggingUtils.getCurrentMethodException(className,methodName,e);
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
            String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
            libraryLoggingUtils = new LibraryLoggingUtils();
            try {
                if (previousResponse.length > 0) {
                    Response response = previousResponse[0];
                    if(response.getBody().asString().startsWith("{"))
                    {
                        JsonObject responseObject = jsonOperations.convertResponseToJsonObject(response);
                        Iterator itr1 = dynamicRequestPayloadDataFields.keySet().iterator();
                        while (itr1.hasNext()) {
                            String key = (String) itr1.next();
                            if (jsonCompareUtils.getValueFromComplexJson(unalteredPayload, key, "") != null && jsonCompareUtils.getValueFromComplexJson(unalteredPayload, key, "").getAsString().contains("{{static}}"))
                            {
                                jsonOperations.modifyKeyValueInJson(unalteredPayload, key, jsonCompareUtils.getValueFromComplexJson(dynamicRequestPayloadDataFields, key, ""));
                                } else if (jsonCompareUtils.getValueFromComplexJson(unalteredPayload, key, "") != null && unalteredPayload.get(key).toString().contains("{{dynamic}}") && jsonCompareUtils.getValueFromComplexJson(responseObject, dynamicRequestPayloadDataFields.get(key).getAsString(), "") != null) {
                                    String responseKey = dynamicRequestPayloadDataFields.get(key).getAsString();
                                    // JsonElement value = JsonParser.parseString(response.jsonPath().get(responseKey));
                                    JsonElement value = jsonCompareUtils.getValueFromComplexJson(responseObject, responseKey, "");
                                    unalteredPayload.add(key, value);
                                }
                            }
                    } else if (response.getBody().asString().startsWith("[")) {
                        JsonArray responseArray = jsonOperations.convertResponseToJsonArray(response);
                        for(JsonElement arrayElement:responseArray)
                        {
                            JsonObject arrayObject = arrayElement.getAsJsonObject();
                            Iterator itr1 = dynamicRequestPayloadDataFields.keySet().iterator();
                            while (itr1.hasNext()) {
                                String key = (String) itr1.next();
                                if (jsonCompareUtils.getValueFromComplexJson(unalteredPayload, key, "") != null && jsonCompareUtils.getValueFromComplexJson(unalteredPayload, key, "").getAsString().contains("{{static}}"))
                                {
                                    jsonOperations.modifyKeyValueInJson(unalteredPayload, key, jsonCompareUtils.getValueFromComplexJson(dynamicRequestPayloadDataFields, key, ""));
                                } else if (jsonCompareUtils.getValueFromComplexJson(unalteredPayload, key, "") != null && unalteredPayload.get(key).toString().contains("{{dynamic}}") && jsonCompareUtils.getValueFromComplexJson(arrayObject, dynamicRequestPayloadDataFields.get(key).getAsString(), "") != null) {
                                    String responseKey = dynamicRequestPayloadDataFields.get(key).getAsString();
                                    // JsonElement value = JsonParser.parseString(response.jsonPath().get(responseKey));
                                    JsonElement value = jsonCompareUtils.getValueFromComplexJson(arrayObject, responseKey, "");
                                    unalteredPayload.add(key, value);
                                }
                            }
                        }

                    }
                }
                else {
                    Iterator itr2 = dynamicRequestPayloadDataFields.keySet().iterator();
                    while (itr2.hasNext()) {
                        String key = (String) itr2.next();
                        if (jsonCompareUtils.getValueFromComplexJson(unalteredPayload, key, "") != null) {
                            JsonElement modifyValue = jsonCompareUtils.getValueFromComplexJson(dynamicRequestPayloadDataFields, key, "");
                            unalteredPayload = jsonOperations.modifyKeyValueInJson(unalteredPayload, key, modifyValue).getAsJsonObject();
                            //unalteredPayload.add(jsonCompareUtils.getKeyPathFromComplexJson(unalteredPayload,key,""), jsonCompareUtils.getValueFromComplexJson(dynamicRequestPayloadDataFields,key,""));
                        }
                    }
                }
            } catch (Exception e) {
                libraryLoggingUtils.getCurrentMethodException(className,methodName,e);
            }
            return unalteredPayload;
}
    }

