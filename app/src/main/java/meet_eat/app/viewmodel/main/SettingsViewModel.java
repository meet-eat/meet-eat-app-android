package meet_eat.app.viewmodel.main;

import androidx.lifecycle.ViewModel;

import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.repository.Session;
import meet_eat.app.repository.UserRepository;
import meet_eat.data.entity.user.User;
import meet_eat.data.entity.user.setting.DisplaySetting;
import meet_eat.data.entity.user.setting.NotificationSetting;

/**
 * Manages settings-related information.
 */
public class SettingsViewModel extends ViewModel {

    private final UserRepository userRepository = new UserRepository();
    private final Session session = Session.getInstance();

    /**
     * Sends a user deletion request to the
     * {@link meet_eat.app.repository.UserRepository UserRepository}.
     *
     * @param user The user to be deleted.
     */
    public void deleteUser(User user) throws RequestHandlerException {
        userRepository.deleteEntity(user);
    }

    /**
     * Sends a logout request to the {@link meet_eat.app.repository.Session Session}.
     */
    public void logout() throws RequestHandlerException {
        session.logout();
    }

    /**
     * Sends a setting update request to the
     * {@link meet_eat.app.repository.UserRepository UserRepository}.
     *
     * @param notificationSetting The new notification setting
     *                            TODO @throws (all viewmodels)
     */
    public void updateNotificationSettings(NotificationSetting notificationSetting) throws RequestHandlerException {
        User currentUser = session.getUser();
        currentUser.addSetting(notificationSetting);
        userRepository.updateEntity(currentUser);
    }

    /**
     * Sends a setting update request to the
     * {@link meet_eat.app.repository.UserRepository UserRepository}.
     *
     * @param displaySetting The new display setting.
     */
    public void updateDisplaySettings(DisplaySetting displaySetting) throws RequestHandlerException {
        User currentUser = session.getUser();
        currentUser.addSetting(displaySetting);
        userRepository.updateEntity(currentUser);
    }
}
