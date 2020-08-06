package meet_eat.app.repository;

import meet_eat.data.entity.Tag;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;

import java.net.URI;

/**
 * Represents the administrative unit that controls access and manipulation of tags within a repository.
 */
public class TagRepository extends EntityRepository<Tag> {

    private static final String BASE_URL = "/tags"; //TODO

    /**
     * Creates an tag repository.
     */
    public TagRepository() {
        super(BASE_URL);
    }

    /**
     * Returns all available tags from the repository.
     *
     * @return all available tags
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public Iterable<Tag> getTags() throws RequestHandlerException {
        RequestEntity<Void> requestEntity = new RequestEntity<Void>(getTokenHeaders(), HttpMethod.GET,
                URI.create(RequestHandler.SERVER_PATH + BASE_URL));
        return new RequestHandler<Void, Tag>().handleIterable(requestEntity, HttpStatus.OK);
    }
}