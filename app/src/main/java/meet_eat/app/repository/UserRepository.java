package meet_eat.app.repository;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;

import java.net.URI;
import java.util.Objects;

import meet_eat.data.EndpointPath;
import meet_eat.data.entity.relation.Bookmark;
import meet_eat.data.entity.relation.Subscription;
import meet_eat.data.entity.relation.rating.Rating;
import meet_eat.data.entity.relation.rating.RatingBasis;
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
        RequestEntity<User> requestEntity = new RequestEntity<>(Objects.requireNonNull(entity), HttpMethod.POST,
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
        RequestEntity<Void> requestEntity = new RequestEntity<>(HttpMethod.POST, URI.create(
                RequestHandler.SERVER_PATH + getEntityPath() +
                        String.format(URL_RESET_PASSWORD, Objects.requireNonNull(email).toString())));
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
     * Adds a {@link Subscription subscription} between two {@link User users} in the repository.
     *
     * @param subscription the subscription to be added to the repository
     * @return the subscription added to the repository
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public Subscription addSubscription(Subscription subscription) throws RequestHandlerException {
        String userIdentifier = subscription.getSource().getIdentifier();
        String uriUserIdentifier = "/" + Objects.requireNonNull(userIdentifier);

        // Handle request
        RequestEntity<Subscription> requestEntity = new RequestEntity<>(subscription, getTokenHeaders(), HttpMethod.POST,
                URI.create(RequestHandler.SERVER_PATH + getEntityPath() + uriUserIdentifier + EndpointPath.SUBSCRIPTIONS));
        return new RequestHandler<Subscription, Subscription>().handle(requestEntity, HttpStatus.CREATED);
    }

    /**
     * Removes a {@link Subscription subscription} between two {@link User users} in the repository.
     *
     * @param subscriber     the subscribing user
     * @param subscribedUser the subscribed user
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public void removeSubscriptionByUser(User subscriber, User subscribedUser) throws RequestHandlerException {
        String uriUserIdentifier = "/" + Objects.requireNonNull(subscriber.getIdentifier());

        // Handle request
        RequestEntity<User> requestEntity =
                new RequestEntity<>(Objects.requireNonNull(subscribedUser), getTokenHeaders(), HttpMethod.DELETE,
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
        RequestEntity<Void> requestEntity = new RequestEntity<>(getTokenHeaders(), HttpMethod.GET,
                URI.create(RequestHandler.SERVER_PATH + getEntityPath() + uriUserIdentifier + EndpointPath.SUBSCRIPTIONS));
        return new RequestHandler<Void, Subscription>().handleIterable(requestEntity, HttpStatus.OK);
    }

    /**
     * Returns the {@link Bookmark bookmarks} of a {@link User user} from the repository.
     *
     * @param user the user whose bookmarks are to be returned from the repository
     * @return the bookmarks of a user from the repository
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public Iterable<Bookmark> getBookmarksByUser(User user) throws RequestHandlerException {
        String uriUserIdentifier = "/" + Objects.requireNonNull(user.getIdentifier());

        // Handle request
        RequestEntity<Void> requestEntity = new RequestEntity<>(getTokenHeaders(), HttpMethod.GET,
                URI.create(RequestHandler.SERVER_PATH + getEntityPath() + uriUserIdentifier + EndpointPath.BOOKMARKS));
        return new RequestHandler<Void, Bookmark>().handleIterable(requestEntity, HttpStatus.OK);
    }

    /**
     * Removes a {@link Bookmark bookmark} from the repository.
     *
     * @param bookmark the bookmark to be deleted from the repository
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public void removeBookmark(Bookmark bookmark) throws RequestHandlerException {
        String uriUserIdentifier = "/" + Objects.requireNonNull(bookmark.getSource().getIdentifier());

        // Handle request
        RequestEntity<Bookmark> requestEntity = new RequestEntity<>(bookmark, getTokenHeaders(), HttpMethod.DELETE,
                URI.create(RequestHandler.SERVER_PATH + getEntityPath() + uriUserIdentifier + EndpointPath.BOOKMARKS));
        new RequestHandler<Bookmark, Void>().handle(requestEntity, HttpStatus.NO_CONTENT);
    }

    /**
     * Adds a {@link Bookmark bookmark} to the repository.
     *
     * @param bookmark the bookmark to be added to the repository
     * @return the bookmark added to the repository
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public Bookmark addBookmark(Bookmark bookmark) throws RequestHandlerException {
        String uriUserIdentifier = "/" + Objects.requireNonNull(bookmark.getSource().getIdentifier());

        // Handle request
        RequestEntity<Bookmark> requestEntity = new RequestEntity<>(bookmark, getTokenHeaders(), HttpMethod.POST,
                URI.create(RequestHandler.SERVER_PATH + getEntityPath() + uriUserIdentifier + EndpointPath.BOOKMARKS));
        return new RequestHandler<Bookmark, Bookmark>().handle(requestEntity, HttpStatus.CREATED);
    }

    /**
     * Adds a {@link Rating rating} to the repository.
     *
     * @param rating the rating to be added to the repository
     * @return the rating added to the repository
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public Rating addRating(Rating rating) throws RequestHandlerException {
        String uriUserIdentifier = "/" + Objects.requireNonNull(rating.getTarget().getIdentifier());

        // Handle request
        RequestEntity<Rating> requestEntity = new RequestEntity<>(rating, getTokenHeaders(), HttpMethod.POST,
                URI.create(RequestHandler.SERVER_PATH + getEntityPath() + uriUserIdentifier + EndpointPath.RATINGS));
        return new RequestHandler<Rating, Rating>().handle(requestEntity, HttpStatus.CREATED);
    }

    /**
     * Gets the numeric {@link RatingBasis#HOST host} {@link Rating rating} value of a user.
     *
     * @param user to get the numeric rating value from
     * @return the numeric host rating value of a user
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public Double getNumericHostRating(User user) throws RequestHandlerException {
        String uriUserIdentifier = "/" + Objects.requireNonNull(user.getIdentifier());

        // Handle request
        RequestEntity<Void> requestEntity = new RequestEntity<>(getTokenHeaders(), HttpMethod.GET,
                URI.create(RequestHandler.SERVER_PATH + getEntityPath() + uriUserIdentifier + EndpointPath.RATINGS + EndpointPath.HOST));
        return new RequestHandler<Void, Double>().handle(requestEntity, HttpStatus.OK);
    }

    /**
     * Gets the numeric {@link RatingBasis#GUEST guest} {@link Rating rating} value of a user.
     *
     * @param user to get the numeric rating value from
     * @return the numeric guest rating value of a user
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public Double getNumericGuestRating(User user) throws RequestHandlerException {
        String uriUserIdentifier = "/" + Objects.requireNonNull(user.getIdentifier());

        // Handle request
        RequestEntity<Void> requestEntity = new RequestEntity<>(getTokenHeaders(), HttpMethod.GET,
                URI.create(RequestHandler.SERVER_PATH + getEntityPath() + uriUserIdentifier + EndpointPath.RATINGS + EndpointPath.GUEST));
        return new RequestHandler<Void, Double>().handle(requestEntity, HttpStatus.OK);
    }
}