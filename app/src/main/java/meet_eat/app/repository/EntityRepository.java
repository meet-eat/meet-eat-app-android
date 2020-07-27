package meet_eat.app.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import meet_eat.data.RequestHeaderField;
import meet_eat.data.entity.Token;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.util.LinkedMultiValueMap;

import meet_eat.data.entity.Entity;

import java.net.URI;
import java.util.Objects;

public abstract class EntityRepository<T extends Entity> {

    private static final String ERROR_MESSAGE_NOT_LOGGED_IN = "Request failed due to wrong login state (null token).";
    private final String entityPath;

    protected EntityRepository(String entityPath) {
         this.entityPath = entityPath;
    }

    public String getEntityPath() {
        return entityPath;
    }

    public T addEntity(T entity) throws RequestHandlerException {
        RequestEntity<T> requestEntity = new RequestEntity<T>(Objects.requireNonNull(entity), getTokenHeaders(),
                HttpMethod.POST, URI.create(RequestHandler.SERVER_PATH + entityPath));
        return new RequestHandler<T, T>().handle(requestEntity, HttpStatus.CREATED);
    }

    public T updateEntity(T entity) throws RequestHandlerException {
        RequestEntity<T> requestEntity = new RequestEntity<T>(Objects.requireNonNull(entity), getTokenHeaders(),
                HttpMethod.PUT, URI.create(RequestHandler.SERVER_PATH + entityPath));
        return new RequestHandler<T, T>().handle(requestEntity, HttpStatus.OK);
    }

    public void deleteEntity(T entity) throws RequestHandlerException {
        RequestEntity<T> requestEntity = new RequestEntity<T>(Objects.requireNonNull(entity), getTokenHeaders(),
                HttpMethod.DELETE, URI.create(RequestHandler.SERVER_PATH + entityPath));
        new RequestHandler<T, Void>().handle(requestEntity, HttpStatus.NO_CONTENT);
    }

    public T getEntityById(String identifier) throws RequestHandlerException {
        RequestEntity<Void> requestEntity = new RequestEntity<Void>(getTokenHeaders(), HttpMethod.GET,
                URI.create(RequestHandler.SERVER_PATH + entityPath + Objects.requireNonNull(identifier)));
        return new RequestHandler<Void, T>().handle(requestEntity, HttpStatus.OK);
    }

    protected LinkedMultiValueMap<String, String> getTokenHeaders() {
        Token token = Session.getInstance().getToken();
        if (Objects.isNull(token)) {
            throw new IllegalStateException(ERROR_MESSAGE_NOT_LOGGED_IN);
        }
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add(RequestHeaderField.TOKEN, toJSON(token));
        return headers;
    }

    protected <U> String toJSON(U obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(obj);
        } catch(JsonProcessingException jsonProcessingException) {
            throw new RuntimeException(jsonProcessingException.getMessage());
        }
    }
}