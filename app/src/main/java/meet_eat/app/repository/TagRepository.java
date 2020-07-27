package meet_eat.app.repository;

import meet_eat.data.entity.Tag;
import meet_eat.data.entity.Token;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;

import java.net.URI;
import java.util.Objects;

public class TagRepository extends EntityRepository<Tag> {

    private static final String BASE_URL = "/tags"; //TODO

    public TagRepository() {
        super(BASE_URL);
    }

    public Iterable<Tag> getTags() throws RequestHandlerException {
        RequestEntity<Void> requestEntity = new RequestEntity<Void>(getTokenHeaders(), HttpMethod.GET,
                URI.create(RequestHandler.SERVER_PATH + BASE_URL));
        return new RequestHandler<Void, Iterable<Tag>>().handle(requestEntity, HttpStatus.OK);
    }
}