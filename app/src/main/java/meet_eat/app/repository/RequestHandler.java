package meet_eat.app.repository;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import meet_eat.data.ObjectJsonParser;

/**
 * Represents the unit for handling HTTP requests to the server.
 *
 * @param <T> the element type of the input
 * @param <S> the type of the output
 */
public class RequestHandler<T, S> {

    /**
     * The URL for the server path.
     */
    public static final String SERVER_PATH = "http://karlsruhe.gstuer.com:8443";

    private static final String ERROR_MESSAGE_REQUEST = "Request failed. ";
    private static final String ERROR_MESSAGE_STATUS_CODE = "Error code: ";
    private final RestTemplate restTemplate;

    /**
     * Creates an request handler.
     */
    public RequestHandler() {
        restTemplate = new RestTemplate();

        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setObjectMapper(ObjectJsonParser.getDefaultObjectMapper());

        restTemplate.setMessageConverters(Collections.singletonList(messageConverter));
    }

    /**
     * Handles a HTTP request to the server for a single element.
     *
     * @param requestEntity the request entity to be transmitted to the server
     * @param expectedStatus the http status that is expected on success
     * @return the body of the response entity to the server request
     * @throws RequestHandlerException if an error occurs when requesting the server
     */
    protected S handle(RequestEntity<T> requestEntity, HttpStatus expectedStatus) throws RequestHandlerException {
        ParameterizedTypeReference<S> typeReference = new ParameterizedTypeReference<S>() {};
        return executeExchange(requestEntity, expectedStatus, typeReference);
    }

    /**
     * Handles a HTTP request to the server for multiple elements.
     *
     * @param requestEntity the request entity to be transmitted to the server
     * @param expectedStatus the http status that is expected on success
     * @return the body of the response entity to the server request
     * @throws RequestHandlerException if an error occurs when requesting the server
     */
    protected Iterable<S> handleIterable(RequestEntity<T> requestEntity, HttpStatus expectedStatus)
            throws RequestHandlerException {
        ParameterizedTypeReference<S[]> typeReference = new ParameterizedTypeReference<S[]>() {};
        return Arrays.asList(executeExchange(requestEntity, expectedStatus, typeReference));
    }

    /**
     * Executes the request to the server.
     *
     * @param requestEntity the request entity to be transmitted to the server
     * @param expectedStatus the http status that is expected on success
     * @param typeReference return type reference of the server request
     * @param <U> the return type of the server request
     * @return the body of the response entity to the server request
     * @throws RequestHandlerException if an error occurs when requesting the server
     */
    private <U> U executeExchange(RequestEntity<T> requestEntity, HttpStatus expectedStatus,
                                  ParameterizedTypeReference<U> typeReference) throws RequestHandlerException {
        ResponseEntity<U> responseEntity;
        try {
            responseEntity = restTemplate.exchange(Objects.requireNonNull(requestEntity), typeReference);
        } catch (RestClientException exception) {
            throw new RequestHandlerException(ERROR_MESSAGE_REQUEST + exception.getMessage());
        }
        if (responseEntity.getStatusCode().equals(Objects.requireNonNull(expectedStatus))) {
            return responseEntity.getBody();
        }
        throw new RequestHandlerException(ERROR_MESSAGE_REQUEST + ERROR_MESSAGE_STATUS_CODE
                + responseEntity.getStatusCodeValue());
    }
}
