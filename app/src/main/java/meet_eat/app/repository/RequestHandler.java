package meet_eat.app.repository;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class RequestHandler<T, S> {

    public static final String SERVER_PATH = ""; //TODO
    private static final String ERROR_MESSAGE_REQUEST = "Request failed. ";
    private static final String ERROR_MESSAGE_STATUS_CODE = "Error code: ";
    private final RestTemplate restTemplate;

    public RequestHandler() {
        restTemplate = new RestTemplate();
    }

    protected S handle(RequestEntity<T> requestEntity, HttpStatus expectedStatus) throws RequestHandlerException {
        ResponseEntity<S> responseEntity;
        ParameterizedTypeReference<S> typeReference = new ParameterizedTypeReference<S>() {};
        try {
            responseEntity = new RestTemplate().exchange(requestEntity, typeReference);
        } catch (RestClientException exception) {
            throw new RequestHandlerException(ERROR_MESSAGE_REQUEST + exception.getMessage());
        }
        if (responseEntity.getStatusCode().equals(expectedStatus)) {
            return responseEntity.getBody();
        }
        throw new RequestHandlerException(ERROR_MESSAGE_REQUEST + ERROR_MESSAGE_STATUS_CODE + responseEntity.getStatusCodeValue());
    }
}
