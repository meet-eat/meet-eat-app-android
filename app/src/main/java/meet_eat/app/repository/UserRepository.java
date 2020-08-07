package meet_eat.app.repository;

import meet_eat.data.EndpointPath;
import meet_eat.data.entity.Tag;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;

import java.net.URI;
import java.util.Objects;

import meet_eat.data.Report;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.User;
import meet_eat.data.entity.user.contact.ContactData;
import meet_eat.data.entity.user.contact.ContactRequest;

public class UserRepository extends EntityRepository<User> {
    
    private static final String URL_RESET_PASSWORD = "/%s/password/reset"; //TODO
    private static final String ERROR_MESSAGE_NOT_IMPLEMENTED = "Not implemented yet.";

    public UserRepository() {
        super(EndpointPath.USERS);
    }

    @Override
    public User addEntity(User entity) throws RequestHandlerException {
        //No token for registration
        RequestEntity<User> requestEntity = new RequestEntity<User>(Objects.requireNonNull(entity), HttpMethod.POST,
                URI.create(RequestHandler.SERVER_PATH + EndpointPath.USERS));
        return new RequestHandler<User, User>().handle(requestEntity, HttpStatus.CREATED);
    }

    public Void resetPassword(Email email) throws RequestHandlerException {
        RequestEntity<Void> requestEntity = new RequestEntity<Void>(HttpMethod.POST, URI.create(RequestHandler.SERVER_PATH
                + EndpointPath.USERS + String.format(URL_RESET_PASSWORD, Objects.requireNonNull(email).toString())));
        return new RequestHandler<Void, Void>().handle(requestEntity, HttpStatus.ACCEPTED);
    }

    public void requestContact(ContactRequest contactRequest) {
        throw new UnsupportedOperationException(ERROR_MESSAGE_NOT_IMPLEMENTED);
    }

    public void sendContactData(ContactData data) {
        throw new UnsupportedOperationException(ERROR_MESSAGE_NOT_IMPLEMENTED);
    }

    public User report(User user, Report report) throws RequestHandlerException {
        Objects.requireNonNull(user);
        user.addReport(Objects.requireNonNull(report));
        return updateEntity(user);
    }
}