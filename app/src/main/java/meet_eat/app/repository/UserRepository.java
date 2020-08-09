package meet_eat.app.repository;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;

import java.net.URI;
import java.util.Objects;

import meet_eat.data.EndpointPath;
import meet_eat.data.Report;
import meet_eat.data.entity.Subscription;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;
import meet_eat.data.entity.user.contact.ContactData;
import meet_eat.data.entity.user.contact.ContactRequest;

/**
 * Represents the administrative unit that controls access and manipulation of {@link User users} within a repository.
 */
public class UserRepository extends EntityRepository<User> {

    private static final String URL_RESET_PASSWORD = "/%s/password/reset";
    private static final String ERROR_MESSAGE_NOT_IMPLEMENTED = "Not implemented yet.";

    /**
     * Creates a {@link User user} repository.
     */
    public UserRepository() {
        super(EndpointPath.USERS);
    }

    @Override
    public User addEntity(User entity) throws RequestHandlerException {
        // No token for registration
        RequestEntity<User> requestEntity = new RequestEntity<User>(Objects.requireNonNull(entity), HttpMethod.POST,
                URI.create(RequestHandler.SERVER_PATH + getEntityPath()));
        return new RequestHandler<User, User>().handle(requestEntity, HttpStatus.CREATED);
    }

    /**
     * Resets the {@link User user}'s {@link Password password} with the corresponding
     * {@link Email e-mail address}.
     *
     * @param email the e-mail address of the user whose password is to be reset
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public void resetPassword(Email email) throws RequestHandlerException {
        RequestEntity<Void> requestEntity = new RequestEntity<Void>(HttpMethod.POST, URI.create(RequestHandler.SERVER_PATH
                + getEntityPath() + String.format(URL_RESET_PASSWORD, Objects.requireNonNull(email).toString())));
        new RequestHandler<Void, Void>().handle(requestEntity, HttpStatus.ACCEPTED);
    }

    /**
     * Executes a {@link ContactRequest contact request}.
     *
     * @param contactRequest the contact request to be executed
     * @throws UnsupportedOperationException because the method is not implemented yet
     */
    public void requestContact(ContactRequest contactRequest) throws UnsupportedOperationException {
        throw new UnsupportedOperationException(ERROR_MESSAGE_NOT_IMPLEMENTED);
    }

    /**
     * Transmits {@link ContactData contact information} to the contact request {@link User creator}.
     *
     * @param data the contact information to be transmitted
     * @throws UnsupportedOperationException because the method is not implemented yet
     */
    public void sendContactData(ContactData data) throws UnsupportedOperationException {
        throw new UnsupportedOperationException(ERROR_MESSAGE_NOT_IMPLEMENTED);
    }

    /**
     * Reports a {@link User user} in the repository by submitting a {@link Report report}.
     *
     * @param user   the user to be reported
     * @param report the report to be submitted
     * @return the user that was reported within the repository
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public User report(User user, Report report) throws RequestHandlerException {
        Objects.requireNonNull(user);
        user.addReport(Objects.requireNonNull(report));
        return updateEntity(user);
    }

    /**
     * Adds a {@link Subscription subscription} between two {@link User users} in the repository.
     *
     * @param subscription the subscription to be added to the repository
     * @return the subscription added to the repository
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public Subscription addSubscription(Subscription subscription) throws RequestHandlerException {
        String userIdentifier = subscription.getSourceUser().getIdentifier();
        String uriUserIdentifier = "/" + Objects.requireNonNull(userIdentifier);

        // Handle request
        RequestEntity<Subscription> requestEntity = new RequestEntity<Subscription>(
                subscription,
                getTokenHeaders(),
                HttpMethod.POST,
                URI.create(RequestHandler.SERVER_PATH + getEntityPath() + uriUserIdentifier + EndpointPath.SUBSCRIPTIONS));
        return new RequestHandler<Subscription, Subscription>().handle(requestEntity, HttpStatus.CREATED);
    }

    /**
     * Removes a {@link Subscription subscription} between two {@link User users} in the repository.
     *
     * @param subscriber the subscribing user
     * @param subscribedUser the subscribed user
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public void removeSubscriptionByUser(User subscriber, User subscribedUser) throws RequestHandlerException {
        String uriUserIdentifier = "/" + Objects.requireNonNull(subscriber.getIdentifier());

        // Handle request
        RequestEntity<User> requestEntity = new RequestEntity<User>(
                Objects.requireNonNull(subscribedUser),
                getTokenHeaders(),
                HttpMethod.DELETE,
                URI.create(RequestHandler.SERVER_PATH + getEntityPath() + uriUserIdentifier + EndpointPath.SUBSCRIPTIONS));
        new RequestHandler<User, Void>().handle(requestEntity, HttpStatus.NO_CONTENT);
    }

    /**
     * Returns the {@link Subscription subscriptions} of a {@link User user} from the repository.
     *
     * @param user the user whose subscriptions are to be returned from the repository
     * @return the subscriptions of a user from the repository
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public Iterable<Subscription> getSubscriptionsOfUser(User user) throws RequestHandlerException {
        String uriUserIdentifier = "/" + Objects.requireNonNull(user.getIdentifier());

        // Handle request
        RequestEntity<Void> requestEntity = new RequestEntity<Void>(
                getTokenHeaders(),
                HttpMethod.GET,
                URI.create(RequestHandler.SERVER_PATH + getEntityPath() + uriUserIdentifier + EndpointPath.SUBSCRIPTIONS));
        return new RequestHandler<Void, Subscription>().handleIterable(requestEntity, HttpStatus.OK);
    }
}