package meet_eat.app.viewmodel.login;

import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;

import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.main.SettingsViewModel;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;
import meet_eat.data.location.SphericalLocation;
import meet_eat.data.location.SphericalPosition;

public class LoginViewModelTest {

    private static final String validRegisteredEmail = "@example.com";
    private static final String validUnregisteredEmail = "unregistered-email@example.com";
    private static final String invalidEmail = "email example";
    private static final String password = "ABcd12§$";
    private static final String username = "Registered User";
    private static final String phoneNumber = "0123456789";
    private static final String description = "JUnit Test User";

    private static LoginViewModel loginVM;
    private static RegisterViewModel registerVM;
    private static SettingsViewModel settingsVM;

    @BeforeClass
    public static void initialize() {
        loginVM = new LoginViewModel();
        registerVM = new RegisterViewModel();
        settingsVM = new SettingsViewModel();
    }

    @Test
    public void testRegisterLoginDeleteUser() throws RequestHandlerException {
        String uniqueIdentifier = String.valueOf(System.currentTimeMillis() % 100000);
        User registeredUser = new User(new Email(uniqueIdentifier + validRegisteredEmail),
                Password.createHashedPassword(password),
                LocalDate.of(2000, 1, 1), username, phoneNumber, description, true,
                new SphericalLocation(new SphericalPosition(0, 0)));
        registerVM.register(registeredUser);
        loginVM.login(uniqueIdentifier + validRegisteredEmail, password);
        settingsVM.deleteUser(settingsVM.getCurrentUser());
    }

    @Test(expected = RequestHandlerException.class)
    public void testLoginWithUnregisteredCredentials() throws RequestHandlerException {
        loginVM.login(validUnregisteredEmail, password);
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

    @Test(expected = RequestHandlerException.class)
    public void testResetPasswordWithValidEmail() throws RequestHandlerException {
        loginVM.resetPassword(validUnregisteredEmail);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetPasswordWithInvalidEmail() throws RequestHandlerException {
        loginVM.resetPassword(invalidEmail);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetPasswordWithNullEmail() throws RequestHandlerException {
        loginVM.resetPassword(null);
    }
}