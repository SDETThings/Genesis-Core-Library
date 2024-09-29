package APIHelper;

import io.restassured.config.SSLConfig;
import io.restassured.http.Method;
import io.restassured.response.Response;

import java.io.FileNotFoundException;
import java.util.Map;

@FunctionalInterface
public interface RequestProvider {

    Response request(Method method , String contentType , String completeUrl , Map<String,String> defaultHeaders, Map<String ,String> formParams, String requestPayload, Map<String ,String> queryParams, Map<String ,String> pathParams, SSLConfig sslConfig) ;

    static RequestProvider getProvider() {
        return (method, contentType, completeUrl, defaultHeaders, formParams, requestPayload, queryParams, pathParams, sslConfig) -> {
            ApiCommons apiCommons = new ApiCommons();

            return apiCommons.requestAsync(method, contentType, completeUrl, defaultHeaders, formParams, requestPayload, queryParams, pathParams, sslConfig);

        };
    }
}
