package APIHelper;

import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class ApiCallLogging  {
    private static final Logger log = LogManager.getLogger(ApiCallLogging.class);

    public synchronized void logRequest(Response response, FilterableRequestSpecification requestSpec) {
        boolean isResponseLarge = checkIfResponsePayloadIsTooLargeToBePrinted(response);
        boolean isRequestPayloadPresent = checkIfRequestPayloadIsPresent(requestSpec);
        boolean isResponsePayloadPresent = checkIfResponsePayloadIsPresent(response);
        log.info("=============================================================================================================================");
        log.info("==============================================EXECUTION START================================================================");
        log.info("=============================================================================================================================");
        if (requestSpec.getMethod().equalsIgnoreCase("POST")) {
            if (!isResponseLarge && isRequestPayloadPresent && isResponsePayloadPresent) {
                synchronized (this) {
                    log.info(
                            "\n\n MethodType => " + requestSpec.getMethod() +
                                    "\n URI => " + requestSpec.getURI() +
                                    "\n Request Headers => " + requestSpec.getHeaders() +
                                    "\n Request Body => " + requestSpec.getBody().toString() +
                                    "\n Status code => " + response.getStatusCode() +
                                    "\n Response Body => " + response.getBody().prettyPrint());
                }
            } else if (!isRequestPayloadPresent) {
                log.info(
                        "\n\n MethodType => " + requestSpec.getMethod() +
                                "\n URI => " + requestSpec.getURI() +
                                "\n Request Headers => " + requestSpec.getHeaders() +
                                "\n Status code => " + response.getStatusCode() +
                                "\n Response Body => " + response.getBody().prettyPrint());
            }
        } else if (requestSpec.getMethod().equalsIgnoreCase("GET")) {
            if (!isResponseLarge) {
                if (!(response.getHeader("Content-Type").equalsIgnoreCase("text/html") || response.getHeader("Content-Type").equalsIgnoreCase("application/pdf"))) {
                    log.info(
                            "\n\n MethodType => " + requestSpec.getMethod() +
                                    "\n URI => " + requestSpec.getURI() +
                                    "\n Request Headers => " + requestSpec.getHeaders() +
                                    "\n Status code => " + response.getStatusCode() +
                                    "\n Response Body => " + response.getBody().prettyPrint()
                    );
                } else {
                    log.info(
                            "\n\n MethodType => " + requestSpec.getMethod() +
                                    "\n URI => " + requestSpec.getURI() +
                                    "\n Request Headers => " + requestSpec.getHeaders() +
                                    "\n Status code => " + response.getStatusCode() +
                                    "\n Response Body => " + "Response is a document and cannot be printed on console"
                    );
                }

            } else if (isResponseLarge) {
                if (!(response.getHeader("Content-Type").equalsIgnoreCase("text/html") || response.getHeader("Content-Type").equalsIgnoreCase("application/pdf"))) {
                    log.info(
                            "\n\n MethodType => " + requestSpec.getMethod() +
                                    "\n URI => " + requestSpec.getURI() +
                                    "\n Request Headers => " + requestSpec.getHeaders() +
                                    "\n Status code => " + response.getStatusCode() +
                                    "\n Response Body => " + "Response is too large to print in console");
                } else {
                    log.info(
                            "\n\n MethodType => " + requestSpec.getMethod() +
                                    "\n URI => " + requestSpec.getURI() +
                                    "\n Request Headers => " + requestSpec.getHeaders() +
                                    "\n Status code => " + response.getStatusCode() +
                                    "\n Response Body => " + "Response is a document and cannot be printed on console"
                    );
                }
            }
        } else if (requestSpec.getMethod().equalsIgnoreCase("PUT") || requestSpec.getMethod().equalsIgnoreCase("PATCH")) {
            if (isResponsePayloadPresent) {
                log.info(
                        "\n\n MethodType => " + requestSpec.getMethod() +
                                "\n URI => " + requestSpec.getURI() +
                                "\n Request Headers => " + requestSpec.getHeaders() +
                                "\n Request Body => " + requestSpec.getBody().toString() +
                                "\n Status code => " + response.getStatusCode() +
                                "\n Response Body => " + response.getBody().prettyPrint()
                );
            } else {
                log.info(
                        "\n\n MethodType => " + requestSpec.getMethod() +
                                "\n URI => " + requestSpec.getURI() +
                                "\n Request Headers => " + requestSpec.getHeaders() +
                                "\n Request Body => " + requestSpec.getBody().toString() +
                                "\n Status code => " + response.getStatusCode() +
                                "\n Response Body => " + "Response body is not present for this request"
                );
            }
        }

    }

    public synchronized boolean checkIfRequestPayloadIsPresent(FilterableRequestSpecification requestSpec) {

        if (requestSpec.getBody() != null) {
            String requestPayload = requestSpec.getBody().toString();
            return Optional.ofNullable(requestPayload)
                    .filter(body -> !body.isEmpty())
                    .isPresent();
        } else {
            return false;
        }
    }

    public synchronized boolean checkIfResponsePayloadIsPresent(Response response) {
        String responsePayload = response.getBody().asString();
        return Optional.ofNullable(responsePayload)
                .filter(body -> !body.isEmpty())
                .isPresent();
    }

    public synchronized boolean checkIfResponsePayloadIsTooLargeToBePrinted(Response response) {
        return response.getBody().asPrettyString().length()>500;
    }
}
