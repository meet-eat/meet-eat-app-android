package meet_eat.app.viewmodel.main;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;

import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.login.LoginViewModel;
import meet_eat.app.viewmodel.login.RegisterViewModel;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;
import meet_eat.data.entity.user.setting.ColorMode;
import meet_eat.data.entity.user.setting.DisplaySetting;
import meet_eat.data.entity.user.setting.NotificationSetting;
import meet_eat.data.location.SphericalLocation;
import meet_eat.data.location.SphericalPosition;

import static org.junit.Assert.assertNotNull;

/**
 * Logout is tested in {@link meet_eat.app.viewmodel.login.LoginViewModelTest}
 */
public class SettingsViewModelTest {

    private static final String registeredEmail = "@example.com";
    private static final String password = "ABcd12§$";
    private static final String username = "Registered User";
    private static final String phoneNumber = "0123456789";
    private static final String profileDescription = "JUnit Test User";

    private static SettingsViewModel settingsVM;

    @BeforeClass
    public static void initialize() throws RequestHandlerException {
        RegisterViewModel registerVM = new RegisterViewModel();
        LoginViewModel loginVM = new LoginViewModel();
        settingsVM = new SettingsViewModel();

        String uniqueIdentifier = String.valueOf(System.currentTimeMillis());
        User registeredUser =
                new User(new Email(uniqueIdentifier + registeredEmail), Password.createHashedPassword(password),
                        LocalDate.of(2000, 1, 1), username, phoneNumber, profileDescription, true,
                        new SphericalLocation(new SphericalPosition(0, 0)));
        registerVM.register(registeredUser);
        loginVM.login(uniqueIdentifier + registeredEmail, password);
    }

    @AfterClass
    public static void cleanUp() throws RequestHandlerException {
        settingsVM.deleteUser();
    }

    @Test
    public void testGetCurrentUserWhileLoggedIn() {
        assertNotNull(settingsVM.getCurrentUser().getIdentifier());
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateNotificationSettingsWithNull() throws RequestHandlerException {
        settingsVM.updateNotificationSettings(null);
    }

    @Test
    public void testUpdateNotificationSettings() throws RequestHandlerException {
        NotificationSetting notificationSetting = new NotificationSetting(true, 59);
        settingsVM.updateNotificationSettings(notificationSetting);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateNotificationSettingsNegativeTime() throws RequestHandlerException {
        NotificationSetting notificationSetting = new NotificationSetting(true, -1);
        settingsVM.updateNotificationSettings(notificationSetting);
    }

    @Test
    public void testUpdateNotificationSettingsWithMaxIntegerTime() throws RequestHandlerException {
        NotificationSetting notificationSetting = new NotificationSetting(true, Integer.MAX_VALUE);
        settingsVM.updateNotificationSettings(notificationSetting);
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateDisplaySettingsWithNull() throws RequestHandlerException {
        settingsVM.updateDisplaySettings(null);
    }

    @Test
    public void testUpdateDisplaySettings() throws RequestHandlerException {
        DisplaySetting displaySetting = new DisplaySetting(ColorMode.SYSTEM);
        settingsVM.updateDisplaySettings(displaySetting);
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteUserWithNull() throws RequestHandlerException {
        settingsVM.deleteUser(null);
    }
}
