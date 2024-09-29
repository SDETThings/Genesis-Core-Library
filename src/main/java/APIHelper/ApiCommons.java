package APIHelper;

import LogUtils.LibraryLoggingUtils;
import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenRequestContext;
import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApiCommons {
    LibraryLoggingUtils libraryLoggingUtils;
    String className = Thread.currentThread().getStackTrace()[1].getClassName();
    Gson gson;
    /******************************************************************************************************************************************
     *                                                URI CONSTRUCTION METHODS
     ******************************************************************************************************************************************/
    public String getBaseUrl(String baseUrlsJsonFilePath ,String environment,String baseUrlType) throws FileNotFoundException {
        gson = new Gson();
        String baseUrl = null;
        JsonObject baseUrlsFile = gson.fromJson(new FileReader(baseUrlsJsonFilePath),JsonObject.class);
        if(baseUrlsFile.has("Environments")){
            for(JsonElement environments:baseUrlsFile.getAsJsonArray("Environments")){
                if(environments.getAsJsonObject().get("Environment").getAsString().equalsIgnoreCase(environment))
                {
                    for(JsonElement baseUrlDetails: environments.getAsJsonObject().get("BaseUrlDetails").getAsJsonArray()){
                        if(baseUrlDetails.getAsJsonObject().get("BaseUrlType").getAsString().equalsIgnoreCase(baseUrlType)){
                            baseUrl= baseUrlDetails.getAsJsonObject().get("BaseUrl").getAsString();
                        }
                    }
                }
            }
        }
        return baseUrl;
    }
    public String getApiVersion(String apiVersionJsonFilePath , String environment,String apiVersionType) throws FileNotFoundException {
        gson = new Gson();
        String apiVersion = null;
        JsonObject apiVersionsFile = gson.fromJson(new FileReader(apiVersionJsonFilePath),JsonObject.class);
        if(apiVersionsFile.has("Environments")){
            for(JsonElement environments:apiVersionsFile.getAsJsonArray("Environments")){
                if(environments.getAsJsonObject().get("Environment").getAsString().equalsIgnoreCase(environment))
                {
                    for(JsonElement apiVersionDetails: environments.getAsJsonObject().get("ApiVersionDetails").getAsJsonArray()){
                        if(apiVersionDetails.getAsJsonObject().get("ApiVersionType").getAsString().equalsIgnoreCase(apiVersionType)){
                            apiVersion= apiVersionDetails.getAsJsonObject().get("ApiVersion").getAsString();
                        }
                    }
                }
            }
        }
        return apiVersion;
    }
    public String getEndpoint(String endpointJsonFilePath ,String endpointName) throws FileNotFoundException {
        gson = new Gson();
        String endpointUrl = null;
        JsonObject endpointDetailsFile = gson.fromJson(new FileReader(endpointJsonFilePath),JsonObject.class);
        if(endpointDetailsFile.has("EndpointDetails")){
            for(JsonElement endpointDetails:endpointDetailsFile.getAsJsonArray("EndpointDetails")){
                    if(endpointDetails.getAsJsonObject().get("EndpointName").getAsString().equalsIgnoreCase(endpointName)){
                            endpointUrl= endpointDetails.getAsJsonObject().get("Url").getAsString();
                    }
                }
            }
        return endpointUrl;
    }


    /******************************************************************************************************************************************
     *                                                HEADER CONSTRUCTION METHOD
     ******************************************************************************************************************************************/
    public Map<String, String> getHeaders(String endpointJsonFilePath , String endpointName) throws FileNotFoundException {
        gson = new Gson();
        Map<String, String> headers = new ConcurrentHashMap<>();
        JsonObject headersFile = gson.fromJson(new FileReader(endpointJsonFilePath),JsonObject.class);
        if(headersFile.has("HeaderDetails")){
            for(JsonElement headerDetails:headersFile.getAsJsonArray("HeaderDetails")){
                if(headerDetails.getAsJsonObject().get("EndpointName").getAsString().equalsIgnoreCase(endpointName)){
                    headers= gson.fromJson(headerDetails.getAsJsonObject().get("Headers"),Map.class);
                }
            }
        }
        return headers;
    }

    /******************************************************************************************************************************************
     *                                                AUTHENTICATION METHODS
     ******************************************************************************************************************************************/
    public SSLConfig loadPfxCertificate(String pfxFilePath,String pfxPassword) throws KeyStoreException {

        KeyStore clientStore = KeyStore.getInstance("PKCS12");
        FileInputStream clientStoreFile = null;
        SSLConfig config;
        try {
            clientStoreFile = new FileInputStream(pfxFilePath);
            org.apache.http.conn.ssl.SSLSocketFactory clientAuthFactory = new org.apache.http.conn.ssl.SSLSocketFactory(clientStore,pfxPassword);
            clientStore.load(clientStoreFile,pfxPassword.toCharArray());
            config = new SSLConfig().with().sslSocketFactory(clientAuthFactory).and().allowAllHostnames();
        } catch (IOException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException |
                 KeyManagementException e) {
            throw new RuntimeException(e);
        }

        return config;
    }
    public String fetchAzureKeyVaultPassword(String keyVaultUri,String passwordKey) {
        SecretClient secretClient = new SecretClientBuilder().vaultUrl(keyVaultUri).credential(new DefaultAzureCredentialBuilder().build()).buildClient();
        KeyVaultSecret secretBundle1 = secretClient.getSecret(passwordKey);
        return secretBundle1.getValue();
    }
    /**
     * getBearerTokenWithAzureDefaultCredentials method is used to fetch bearer token from azure functionApp
     * @param tenantId This is the id which recognises an instance of the azure active directory
     * @param functionAppName This is the functionApp name where the application is hosted
     * @return  bearerToken This is the bearer token in string format which can be used as the authentication token during API calls
     */
    public String getBearerTokenWithAzureDefaultCredentials(String tenantId , String functionAppName) {// tested OK
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        libraryLoggingUtils = new LibraryLoggingUtils();
        String bearerToken = null;
        try {
            String azureResourceId ="api://"+tenantId+"/"+functionAppName;
            DefaultAzureCredential defaultAzureCredential = new DefaultAzureCredentialBuilder().build();
            TokenRequestContext tokenRequestContext = new TokenRequestContext();
            tokenRequestContext.addScopes(azureResourceId);
            AccessToken accessToken = defaultAzureCredential.getToken(tokenRequestContext).block();
            bearerToken = accessToken.getToken();
        } catch (Exception actualException) {
            libraryLoggingUtils.getCurrentMethodException(className,methodName,actualException);
        }
        return bearerToken;
    }

    /******************************************************************************************************************************************
     *                                                API CALL METHOD
     ******************************************************************************************************************************************/
    public Response requestAsync(Method method , String contentType , String completeUrl , Map<String,String> defaultHeaders, Map<String ,String> formParams, String requestPayload, Map<String ,String> queryParams, Map<String ,String> pathParams, SSLConfig sslConfig) {
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.filter(new TestListener());
        if(contentType!=null)
        {
            requestSpecification.contentType(contentType);
        }
        if(requestPayload!=null)
        {
            requestSpecification.body(requestPayload);
        }
        if(defaultHeaders!=null)
        {
            requestSpecification.headers(defaultHeaders);
        }
        if(formParams!=null)
        {
            requestSpecification.formParams(formParams);
        }
        if(queryParams!=null)
        {
            requestSpecification.queryParams(queryParams);
        }
        if(pathParams!=null)
        {
            requestSpecification.pathParams(pathParams);
        }
        if(sslConfig!=null)
        {
            requestSpecification.config(RestAssured.config().sslConfig(sslConfig));
        }
        Response response =requestSpecification.request(method,completeUrl);
        return response;
    }

}
