package meet_eat.app.repository;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;

import java.net.URI;

import meet_eat.data.EndpointPath;
import meet_eat.data.entity.Tag;

/**
 * Represents the administrative unit that controls access and manipulation of {@link Tag tags} within a repository.
 */
public class TagRepository extends EntityRepository<Tag> {

    /**
     * Creates a {@link Tag tag} repository.
     */
    public TagRepository() {
        super(EndpointPath.TAGS);
    }

    /**
     * Returns all available {@link Tag tags} from the repository.
     *
     * @return all available tags
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public Iterable<Tag> getTags() throws RequestHandlerException {
        RequestEntity<Void> requestEntity = new RequestEntity<>(getTokenHeaders(), HttpMethod.GET,
                URI.create(RequestHandler.SERVER_PATH + getEntityPath()));
        return new RequestHandler<Void, Tag>().handleIterable(requestEntity, HttpStatus.OK);
    }
}