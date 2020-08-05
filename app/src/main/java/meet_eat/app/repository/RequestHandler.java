package meet_eat.app.repository;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Objects;

import meet_eat.data.ObjectJsonParser;

public class RequestHandler<T, S> {

    public static final String SERVER_PATH = "http://karlsruhe.gstuer.com:8443";
    private static final String ERROR_MESSAGE_REQUEST = "Request failed. ";
    private static final String ERROR_MESSAGE_STATUS_CODE = "Error code: ";
    private final RestTemplate restTemplate;

    public RequestHandler() {
        restTemplate = new RestTemplate();

        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setObjectMapper(ObjectJsonParser.getDefaultObjectMapper());

        restTemplate.setMessageConverters(Collections.singletonList(messageConverter));
    }

    protected S handle(RequestEntity<T> requestEntity, HttpStatus expectedStatus) throws RequestHandlerException {
        ResponseEntity<S> responseEntity;
        ParameterizedTypeReference<S> typeReference = new ParameterizedTypeReference<S>() {};
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
