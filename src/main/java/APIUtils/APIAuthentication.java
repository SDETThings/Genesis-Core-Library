package APIUtils;

import LogUtils.LibraryLoggingUtils;
import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenRequestContext;
import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
/**
 * APIAuthentication class contains the methods for handling different types of authentication mechanism during API calls
 */
public class APIAuthentication {
    LibraryLoggingUtils libraryLoggingUtils;
    String className = Thread.currentThread().getStackTrace()[1].getClassName();

    /**
     * getBearerTokenWithAzureDefaultCredentials method is used to fetch bearer token from azure functionApp
     * @param tenantId This is the id which recognises an instance of the azure active directory
     * @param functionAppName This is the functionApp name where the application is hosted
     * @return  bearerToken This is the bearer token in string format which can be used as the authentication token during API calls
     */
    public synchronized String getBearerTokenWithAzureDefaultCredentials(String tenantId , String functionAppName) // tested OK
    {
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
}
