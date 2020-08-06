package meet_eat.app.repository;

import meet_eat.data.ObjectJsonParser;
import meet_eat.data.RequestHeaderField;
import meet_eat.data.entity.Token;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.util.LinkedMultiValueMap;

import meet_eat.data.entity.Entity;

import java.net.URI;
import java.util.Objects;

/**
 * Represents the administrative unit that controls access and manipulation of entities within a repository.
 *
 * @param <T> the type of entities to be managed
 */
public abstract class EntityRepository<T extends Entity> {

    private static final String ERROR_MESSAGE_NOT_LOGGED_IN = "Request failed due to wrong login state (null token).";
    private final String entityPath;

    /**
     * Creates an entity repository.
     *
     * @param entityPath the path of an entity specific endpoint on the server
     */
    protected EntityRepository(String entityPath) {
         this.entityPath = entityPath;
    }

    /**
     * Returns the path of an entity specific endpoint on the server.
     *
     * @return the path of an entity specific endpoint on the server
     */
    public String getEntityPath() {
        return entityPath;
    }

    /**
     * Creates a persistent entity by adding it to the repository.
     *
     * @param entity the entity to be added to the repository
     * @return the entity added to the repository
     * @throws RequestHandlerException
     */
    public T addEntity(T entity) throws RequestHandlerException {
        RequestEntity<T> requestEntity = new RequestEntity<T>(Objects.requireNonNull(entity), getTokenHeaders(),
                HttpMethod.POST, URI.create(RequestHandler.SERVER_PATH + entityPath));
        return new RequestHandler<T, T>().handle(requestEntity, HttpStatus.CREATED);
    }

    /**
     * Updates an entity within the repository.
     *
     * @param entity the entity that should be updated within the repository
     * @return the entity that was updated within the repository
     * @throws RequestHandlerException
     */
    public T updateEntity(T entity) throws RequestHandlerException {
        RequestEntity<T> requestEntity = new RequestEntity<T>(Objects.requireNonNull(entity), getTokenHeaders(),
                HttpMethod.PUT, URI.create(RequestHandler.SERVER_PATH + entityPath));
        return new RequestHandler<T, T>().handle(requestEntity, HttpStatus.OK);
    }

    /**
     * Deletes an entity from the repository.
     *
     * @param entity the entity to be deleted from the repository
     * @throws RequestHandlerException
     */
    public void deleteEntity(T entity) throws RequestHandlerException {
        RequestEntity<T> requestEntity = new RequestEntity<T>(Objects.requireNonNull(entity), getTokenHeaders(),
                HttpMethod.DELETE, URI.create(RequestHandler.SERVER_PATH + entityPath));
        new RequestHandler<T, Void>().handle(requestEntity, HttpStatus.NO_CONTENT);
    }

    /**
     * Returns the user with the corresponding identifier from the repository.
     *
     * @param identifier the identifier of the user to be returned from the repository
     * @return the user with the corresponding identifier from the repository
     * @throws RequestHandlerException
     */
    public T getEntityById(String identifier) throws RequestHandlerException {
        RequestEntity<Void> requestEntity = new RequestEntity<Void>(getTokenHeaders(), HttpMethod.GET,
                URI.create(RequestHandler.SERVER_PATH + entityPath + Objects.requireNonNull(identifier)));
        return new RequestHandler<Void, T>().handle(requestEntity, HttpStatus.OK);
    }

    /**
     * Returns a map representing headers initialized with token of the logged in user.
     *
     * @return the map representing headers initialized with token of the logged in user
     */
    protected LinkedMultiValueMap<String, String> getTokenHeaders() {
        Token token = Session.getInstance().getToken();
        if (Objects.isNull(token)) {
            throw new IllegalStateException(ERROR_MESSAGE_NOT_LOGGED_IN);
        }
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add(RequestHeaderField.TOKEN, new ObjectJsonParser().parseObjectToJsonString(token));
        return headers;
    }
}