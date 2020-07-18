package meet_eat.app.viewmodel.main;

import meet_eat.data.Report;
import meet_eat.data.entity.user.User;

/**
 * Manages user-related information.
 */
public class UserViewModel {

    /**
     * Requests the object of the user currently logged in to the device from the
     * {@link meet_eat.app.repository.Session Session}.
     *
     * @return The current user.
     */
    public User getCurrentUser() {
        return null;
    }

    /**
     * TODO rename to update?
     * Sends a user update request to the
     * {@link meet_eat.app.repository.UserRepository UserRepository}.
     *
     * @param editedUser The user that is to be updated.
     */
    public void edit(User editedUser) {

    }

    /**
     * Sends a report request to the {@link meet_eat.app.repository.UserRepository UserRepository}.
     *
     * @param user The user that is to be reported.
     * @param report The report.
     */
    public void report(User user, Report report) {

    }

    /**
     * Sends an update request to the {@link meet_eat.app.repository.UserRepository UserRepository},
     * containing the modified user object.
     *
     * @param user The user object with an updated subscriber list.
     */
    public void subscribe(User user) {

    }

    /**
     *  Sends an update request to the {@link meet_eat.app.repository.UserRepository UserRepository},
     *  containing the modified user object.
     *
     * @param user The user object with an updated subscriber list.
     */
    public void unsubscribe(User user) {

    }
}
