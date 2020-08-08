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
     * Requests the currently logged in user from the {@link Session}.
     *
     * @return the user that is currently logged in
     */
    public User getCurrentUser() {
        return session.getUser();
    }

    /**
     * Removes a {@link User} entity from the {@link UserRepository}.
     *
     * @param user the user to be deleted
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public void deleteUser(User user) throws RequestHandlerException {
        userRepository.deleteEntity(user);
    }

    /**
     * Logs out the user via the {@link Session}.
     *
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public void logout() throws RequestHandlerException {
        session.logout();
    }

    /**
     * Adds the given {@link NotificationSetting} to the user's settings, then updates the
     * {@link User} entity in the {@link UserRepository}.
     *
     * @param notificationSetting the new notification setting
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public void updateNotificationSettings(NotificationSetting notificationSetting) throws RequestHandlerException {
        getCurrentUser().addSetting(notificationSetting);
        userRepository.updateEntity(getCurrentUser());
    }

    /**
     * Adds the given {@link DisplaySetting} to the user's settings, then updates the
     * {@link User} entity in the {@link UserRepository}.
     *
     * @param displaySetting the new display setting
     * @throws RequestHandlerException if an error occurs when requesting the repository
     */
    public void updateDisplaySettings(DisplaySetting displaySetting) throws RequestHandlerException {
        getCurrentUser().addSetting(displaySetting);
        userRepository.updateEntity(getCurrentUser());
    }
}
