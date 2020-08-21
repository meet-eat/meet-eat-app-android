package meet_eat.app.viewmodel.login;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;

import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.repository.Session;
import meet_eat.app.viewmodel.main.SettingsViewModel;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;
import meet_eat.data.location.SphericalLocation;
import meet_eat.data.location.SphericalPosition;

import static org.junit.Assert.assertNotNull;

public class LoginViewModelTest {

    private static final String validRegisteredEmail = "@example.com";
    private static final String validUnregisteredEmail = "unregistered-email@example.com";
    private static final String invalidEmail = "email example";
    private static final String password = "ABcd12§$";
    private static final String username = "Registered User";
    private static final String phoneNumber = "0123456789";
    private static final String description = "JUnit Test User";

    private static LoginViewModel loginVM = new LoginViewModel();
    private static RegisterViewModel registerVM = new RegisterViewModel();
    private static SettingsViewModel settingsVM = new SettingsViewModel();
    private static String uniqueIdentifier = String.valueOf(System.currentTimeMillis() % 100000);

    @BeforeClass
    public static void initialize() throws RequestHandlerException {
        User registeredUser =
                new User(new Email(uniqueIdentifier + validRegisteredEmail), Password.createHashedPassword(password),
                        LocalDate.of(2000, 1, 1), username, phoneNumber, description, true,
                        new SphericalLocation(new SphericalPosition(0, 0)));
        registerVM.register(registeredUser);
        loginVM.login(uniqueIdentifier + validRegisteredEmail, password);
        System.out.println("Created user " + settingsVM.getCurrentUser().getEmail());
        settingsVM.logout();
    }

    @AfterClass
    public static void cleanUp() throws RequestHandlerException {
        // cleanup last test in case something went wrong
        if (Session.getInstance().getToken() == null) {
            loginVM.login(uniqueIdentifier + validRegisteredEmail, password);
        }
        settingsVM.deleteUser();
        uniqueIdentifier = "";
    }

    @Test
    public void testLoginLogout() throws RequestHandlerException {
        loginVM.login(uniqueIdentifier + validRegisteredEmail, password);
        assertNotNull(settingsVM.getCurrentUser());
        settingsVM.logout();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoginWithInvalidEmail() throws RequestHandlerException {
        loginVM.login(invalidEmail, password);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoginWithNullEmail() throws RequestHandlerException {
        loginVM.login(null, password);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoginWithNullPassword() throws RequestHandlerException {
        loginVM.login(validUnregisteredEmail, null);
    }
}