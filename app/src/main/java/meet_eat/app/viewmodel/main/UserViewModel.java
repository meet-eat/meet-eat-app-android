package meet_eat.app.viewmodel.main;

import androidx.lifecycle.ViewModel;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.stream.Collectors;

import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.repository.Session;
import meet_eat.app.repository.UserRepository;
import meet_eat.data.Report;
import meet_eat.data.entity.Subscription;
import meet_eat.data.entity.user.User;

/**
 * Manages user-related information.
 */
public class UserViewModel extends ViewModel {

    private final UserRepository userRepository = new UserRepository();
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
     * Updates the user in the {@link UserRepository}.
     *
     * @param editedUser the user to be updated
     */
    public void edit(User editedUser) throws RequestHandlerException {
        userRepository.updateEntity(editedUser);
    }

    /**
     * Reports a user in the {@link UserRepository}.
     *
     * @param toBeReported the user that is to be reported
     * @param report       the report
     */
    public void report(User toBeReported, Report report) throws RequestHandlerException {
        userRepository.report(toBeReported, report);
    }

    /**
     * Adds the given {@link User} to the current user's subcriptions, then
     * updates the current user in the {@link UserRepository}
     *
     * @param toBeSubscribed the user to be subscribed to
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
     */
    public void unsubscribe(User toBeUnsubscribed) throws RequestHandlerException {
        userRepository.removeSubscriptionByUser(getCurrentUser(), toBeUnsubscribed);
    }

    /**
     * Checks if the current user is subscribed to the given {@link User}.
     *
     * @param user the user which is to be compared
     * @return true if the current user subscribed the user
     */
    public boolean isSubscribed(User user) throws RequestHandlerException {
        return getSubscribedUsers().contains(user);
    }

    // TODO JAVADOC
    public Collection<Subscription> getSubscriptions() throws RequestHandlerException {
        return Lists.newLinkedList(userRepository.getSubscriptionsOfUser(getCurrentUser()));
    }

    // TODO JAVADOC
    public Collection<User> getSubscribedUsers() throws RequestHandlerException {
        return getSubscriptions().stream().map(Subscription::getTargetUser).collect(Collectors.toList());
    }
}
