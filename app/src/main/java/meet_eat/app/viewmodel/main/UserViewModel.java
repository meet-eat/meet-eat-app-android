package meet_eat.app.viewmodel.main;

import androidx.lifecycle.ViewModel;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.stream.Collectors;

import meet_eat.app.repository.ReportRepository;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.repository.Session;
import meet_eat.app.repository.UserRepository;
import meet_eat.data.entity.relation.Report;
import meet_eat.data.entity.relation.Subscription;
import meet_eat.data.entity.relation.rating.Rating;
import meet_eat.data.entity.relation.rating.RatingBasis;
import meet_eat.data.entity.user.User;

/**
 * Manages user-related information.
 */
public class UserViewModel extends ViewModel {

    private final UserRepository userRepository = new UserRepository();
    private final ReportRepository reportRepository = new ReportRepository();
    private final Session session = Session.getInstance();

    /**
     * Requests the currently logged in user from the {@link Session}.
     *
     * @return the user that is currently logged in
     */
    public User getCurrentUser() {
        return session.getUser();
    }

    /**
     * Edits the user in the {@link UserRepository}.
     *
     * @param editedUser the user to be updated
     * @return the edited user
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public User edit(User editedUser) throws RequestHandlerException {
        return userRepository.updateEntity(editedUser);
    }

    /**
     * Reports a user in the {@link ReportRepository}.
     *
     * @param report the report
     * @return the report
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public Report report(Report report) throws RequestHandlerException {
        return reportRepository.addEntity(report);
    }

    /**
     * Adds the given {@link User} to the current user's subscriptions, then
     * updates the current user in the {@link UserRepository}
     *
     * @param toBeSubscribed the user to be subscribed to
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public void subscribe(User toBeSubscribed) throws RequestHandlerException {
        Subscription subscription = new Subscription(getCurrentUser(), toBeSubscribed);
        userRepository.addSubscription(subscription);
    }

    /**
     * Sends an update request to the {@link meet_eat.app.repository.UserRepository UserRepository},
     * containing the modified user object.
     *
     * @param toBeUnsubscribed the user to be unsubscribed from
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public void unsubscribe(User toBeUnsubscribed) throws RequestHandlerException {
        userRepository.removeSubscriptionByUser(getCurrentUser(), toBeUnsubscribed);
    }

    /**
     * Checks if the current user is subscribed to the given {@link User}.
     *
     * @param user the user which is to be compared
     * @return true if the current user subscribed the user
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public boolean isSubscribed(User user) throws RequestHandlerException {
        return getSubscribedUsers().contains(user);
    }

    /**
     * Gets a list of all subscriptions.
     *
     * @return all subscriptions
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public Collection<Subscription> getSubscriptions() throws RequestHandlerException {
        return Lists.newLinkedList(userRepository.getSubscriptionsOfUser(getCurrentUser()));
    }

    /**
     * Gets the users subscribed by the current user.
     *
     * @return all subscriptions of the current user
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public Collection<User> getSubscribedUsers() throws RequestHandlerException {
        return getSubscriptions().stream().map(Subscription::getTarget).collect(Collectors.toList());
    }

    /**
     * Gets the numeric {@link RatingBasis#HOST host} {@link Rating rating} value of a user.
     *
     * @param user to get the numeric rating value from
     * @return the numeric host rating value of a user
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public Double getNumericHostRating(User user) throws RequestHandlerException {
        return userRepository.getNumericHostRating(user);
    }

    /**
     * Gets the numeric {@link RatingBasis#GUEST guest} {@link Rating rating} value of a user.
     *
     * @param user to get the numeric rating value from
     * @return the numeric guest rating value of a user
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public Double getNumericGuestRating(User user) throws RequestHandlerException {
        return userRepository.getNumericGuestRating(user);
    }
}
