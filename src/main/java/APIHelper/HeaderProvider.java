package APIHelper;

import com.google.gson.JsonElement;
import org.openqa.selenium.json.Json;

import java.io.FileNotFoundException;
import java.util.Map;

@FunctionalInterface
public interface HeaderProvider {

    Map<String, String> getHeaders(String endpointName, String headersJsonFilePath) throws FileNotFoundException;

    static HeaderProvider getProvider() {
        return (endpointName, headersJsonFilePath) -> {
            ApiCommons apiCommons = new ApiCommons();
            return apiCommons.getHeaders(headersJsonFilePath, endpointName);
        };
    }
}
