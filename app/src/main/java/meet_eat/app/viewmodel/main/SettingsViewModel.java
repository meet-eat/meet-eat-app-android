package meet_eat.app.viewmodel.main;

import meet_eat.data.entity.user.User;
import meet_eat.data.entity.user.setting.DisplaySetting;
import meet_eat.data.entity.user.setting.NotificationSetting;

/**
 * Manages settings-related information.
 */
public class SettingsViewModel {

    /**
     * Sends a user deletion request to the
     * {@link meet_eat.app.repository.UserRepository UserRepository}.
     *
     * @param user The user to be deleted.
     */
    public void deleteUser(User user) {

    }

    /**
     * Sends a logout request to the {@link meet_eat.app.repository.Session Session}.
     */
    public void logout() {

    }

    /**
     * Sends a setting update request to the
     * {@link meet_eat.app.repository.UserRepository UserRepository}.
     *
     * @param notificationSetting The new notification setting
     */
    public void updateNotificationSettings(NotificationSetting notificationSetting) {

    }

    /**
     * Sends a setting update request to the
     * {@link meet_eat.app.repository.UserRepository UserRepository}.
     *
     * @param displaySetting The new display setting.
     */
    public void updateDisplaySettings(DisplaySetting displaySetting) {

    }
}
