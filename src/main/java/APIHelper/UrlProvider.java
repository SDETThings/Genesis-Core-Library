package APIHelper;

import java.io.FileNotFoundException;

@FunctionalInterface
public interface UrlProvider {

    String getUrl(String environment , String client , String endpointName, String baseUrlApiVersionType,String baseUrlsJsonFilePath,String endpointFilePath,String... apiVersionFilePath) throws FileNotFoundException;

    static UrlProvider getProvider(){
        return(environment,client,endpointName,baseUrlApiVersionType,baseUrlsJsonFilePath,endpointFilePath,apiVersionFilePath)->{
            ApiCommons apiCommons = new ApiCommons();
            String apiVersion;
            String baseUrl;
            String endpointUrl;
            baseUrl = apiCommons.getBaseUrl(baseUrlsJsonFilePath,environment,baseUrlApiVersionType);
            if(apiVersionFilePath.length>0)
            {
                apiVersion =apiCommons.getApiVersion(apiVersionFilePath[0],environment,baseUrlApiVersionType);
            }else{
                apiVersion="";
            }
            endpointUrl = apiCommons.getEndpoint(endpointFilePath,endpointName);
            if(apiVersion.equalsIgnoreCase("")){
                return baseUrl+endpointUrl;
            }else{
                return baseUrl+apiVersion+endpointUrl;

            }};
    }
}
