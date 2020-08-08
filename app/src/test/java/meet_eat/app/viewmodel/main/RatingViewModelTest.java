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
import meet_eat.data.entity.user.rating.Rating;
import meet_eat.data.location.SphericalLocation;
import meet_eat.data.location.SphericalPosition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Not yet implemented
 */
public class RatingViewModelTest {

    private static final String registeredEmail = "@example.com";
    private static final String password = "ABcd12§$";
    private static final String username = "Registered User";
    private static final String phoneNumber = "0123456789";
    private static final String profileDescription = "JUnit Test User";

    private static SettingsViewModel settingsVM;
    private static RatingViewModel ratingVM;
    private static User registeredUser;

    @BeforeClass
    public static void initialize() throws RequestHandlerException {
        RegisterViewModel registerVM = new RegisterViewModel();
        LoginViewModel loginVM = new LoginViewModel();
        settingsVM = new SettingsViewModel();
        ratingVM = new RatingViewModel();

        String uniqueIdentifier = String.valueOf(System.currentTimeMillis() % 100000);
        registeredUser =
                new User(new Email(uniqueIdentifier + registeredEmail), Password.createHashedPassword(password),
                        LocalDate.of(2000, 1, 1), username, phoneNumber, profileDescription, true,
                        new SphericalLocation(new SphericalPosition(0, 0)));
        registerVM.register(registeredUser);
        loginVM.login(uniqueIdentifier + registeredEmail, password);
    }

    @AfterClass
    public static void cleanUp() throws RequestHandlerException {
        settingsVM.deleteUser(settingsVM.getCurrentUser());
    }

    @Test
    public void testGetCurrentUser() {
        assertNotNull(ratingVM.getCurrentUser().getIdentifier());
    }

    @Test(expected = NullPointerException.class)
    public void testSendWithNull() throws RequestHandlerException {
        ratingVM.send((Rating) null);
    }

    @Test(expected = NullPointerException.class)
    public void testSendMultipleWithNull() throws RequestHandlerException {
        ratingVM.send(new Rating[2]);
    }
}
