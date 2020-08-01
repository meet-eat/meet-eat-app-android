package meet_eat.app.viewmodel.main;

import androidx.lifecycle.ViewModel;

import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.repository.Session;
import meet_eat.app.repository.UserRepository;
import meet_eat.data.Report;
import meet_eat.data.entity.user.User;

/**
 * Manages user-related information.
 */
public class UserViewModel extends ViewModel {

    private final UserRepository userRepository = new UserRepository();
    private final Session session = Session.getInstance();

    private User user;

    /**
     * Requests the object of the user currently logged in to the device from the
     * {@link meet_eat.app.repository.Session Session}.
     *
     * @return The current user.
     */
    public User getCurrentUser() {
        return session.getUser();
    }

    /**
     * Sends a user update request to the
     * {@link meet_eat.app.repository.UserRepository UserRepository}.
     *
     * @param editedUser The user that is to be updated.
     */
    public void edit(User editedUser) throws RequestHandlerException {
        userRepository.updateEntity(editedUser);
    }

    /**
     * Sends a report request to the {@link meet_eat.app.repository.UserRepository UserRepository}.
     *
     * @param user   The user that is to be reported.
     * @param report The report.
     */
    public void report(User user, Report report) throws RequestHandlerException {
        userRepository.report(user, report);
    }

    /**
     * Sends an update request to the {@link meet_eat.app.repository.UserRepository UserRepository},
     * containing the modified user object.
     *
     * @param user The user object with an updated subscriber list.
     */
    public void subscribe(User user) throws RequestHandlerException {
        userRepository.updateEntity(user);
    }

    /**
     * Sends an update request to the {@link meet_eat.app.repository.UserRepository UserRepository},
     * containing the modified user object.
     *
     * @param user The user object with an updated subscriber list.
     */
    public void unsubscribe(User user) throws RequestHandlerException {
        userRepository.updateEntity(user);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
