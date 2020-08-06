package meet_eat.app.viewmodel.main;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;

import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.login.LoginViewModel;
import meet_eat.app.viewmodel.login.RegisterViewModel;
import meet_eat.data.Report;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.Role;
import meet_eat.data.entity.user.User;
import meet_eat.data.location.SphericalLocation;
import meet_eat.data.location.SphericalPosition;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.util.Assert.isTrue;

public class UserViewModelTest {

    private static final String registeredEmail = "registered-email@example.com";
    private static final String toBeSubscribedEmail = "tbs-email@example.com";
    private static final String toBeReportedEmail = "tbr-email@example.com";
    private static final String subscribedUserEmail = "su-email@example.com";
    private static final String password = "ABcd12§$";
    private static final String username = "Registered User";
    private static final String phoneNumber = "0123456789";
    private static final String profileDescription = "JUnit Test User";

    private static SettingsViewModel settingsVM;
    private static User registeredUser;
    private static User toBeReported;
    private static User toBeSubscribed;
    private static User subscribedUser;

    @BeforeClass
    public static void initialize() throws RequestHandlerException {
        LoginViewModel loginVM = new LoginViewModel();
        RegisterViewModel registerVM = new RegisterViewModel();
        settingsVM = new SettingsViewModel();

        toBeReported = new User(new Email(toBeReportedEmail), Password.createHashedPassword(password),
                LocalDate.of(2000, 1, 1), username, phoneNumber, profileDescription, true,
                new SphericalLocation(new SphericalPosition(0, 0)));

        toBeSubscribed = new User(new Email(toBeSubscribedEmail), Password.createHashedPassword(password),
                LocalDate.of(2000, 1, 1), username, phoneNumber, profileDescription, true,
                new SphericalLocation(new SphericalPosition(0, 0)));

        registeredUser =
                new User(new Email(registeredEmail), Password.createHashedPassword(password), LocalDate.of(2000, 1, 1),
                        username, phoneNumber, profileDescription, true,
                        new SphericalLocation(new SphericalPosition(0, 0)));

        subscribedUser = new User(new Email(subscribedUserEmail), Password.createHashedPassword(password),
                LocalDate.of(2000, 1, 1), username, phoneNumber, profileDescription, true,
                new SphericalLocation(new SphericalPosition(0, 0)));

        registerVM.register(toBeReported);
        registerVM.register(toBeSubscribed);
        registerVM.register(registeredUser);
        registerVM.register(subscribedUser);
        loginVM.login(registeredEmail, password);

        new UserViewModel().subscribe(subscribedUser);
    }

    @AfterClass
    public static void cleanUp() throws RequestHandlerException {
        settingsVM.deleteUser(registeredUser);
        settingsVM.deleteUser(toBeReported);
        settingsVM.deleteUser(toBeSubscribed);
        settingsVM.deleteUser(subscribedUser);
    }

    @Test
    public void testGetCurrentUser() {
        assertEquals(registeredUser, new UserViewModel().getCurrentUser());
    }

    @Test
    public void testEditUser() throws RequestHandlerException {
        // changing email is omitted, there is no such functionality in the app
        registeredUser.setPhoneNumber("");
        registeredUser.setDescription("");
        registeredUser.setBirthDay(LocalDate.MIN);
        registeredUser.setVerified(!registeredUser.isVerified());
        registeredUser.setName("");
        registeredUser.setRole(Role.ADMIN);
        registeredUser.setLocalizable(new SphericalLocation(new SphericalPosition(0.0, 0.0)));

        new UserViewModel().edit(registeredUser);

        testGetCurrentUser();
    }

    @Test
    public void testChangePassword() throws RequestHandlerException {
        UserViewModel userVM = new UserViewModel();
        Password newPassword = Password.createHashedPassword("HelloWorld1!");
        registeredUser.setPassword(newPassword);
        userVM.edit(registeredUser);

        assertTrue(newPassword.matches(userVM.getCurrentUser().getPassword()));
    }

    @Test(expected = NullPointerException.class)
    public void testEditUserWithNull() throws RequestHandlerException {
        new UserViewModel().edit(null);
    }

    @Test
    public void testReport() throws RequestHandlerException {
        Report report = new Report(new UserViewModel().getCurrentUser(), "");
        new UserViewModel().report(toBeReported, report);
    }

    @Test(expected = RequestHandlerException.class)
    public void testReportUnregisteredUser() throws RequestHandlerException {
        User unregisteredUser =
                new User(new Email(registeredEmail), Password.createHashedPassword(password), LocalDate.of(2000, 1, 1),
                        username, phoneNumber, profileDescription, true,
                        new SphericalLocation(new SphericalPosition(0, 0)));
        Report report = new Report(new UserViewModel().getCurrentUser(), "");
        new UserViewModel().report(unregisteredUser, report);
    }

    @Test(expected = NullPointerException.class)
    public void testReportWithNullReportObject() throws RequestHandlerException {
        new UserViewModel().report(toBeReported, null);
    }

    @Test(expected = NullPointerException.class)
    public void testReportWithNullUser() throws RequestHandlerException {
        Report report = new Report(new UserViewModel().getCurrentUser(), "");
        new UserViewModel().report(null, report);
    }

    @Test(expected = NullPointerException.class)
    public void testReportWithNull() throws RequestHandlerException {
        new UserViewModel().report(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testSubscribeWithNull() throws RequestHandlerException {
        new UserViewModel().subscribe(null);
    }

    @Test(expected = RequestHandlerException.class)
    public void testSubscribeWithUnregisteredUser() throws RequestHandlerException {
        // if exception is not thrown, check if unregisteredUser is in users subscriber list
        User unregisteredUser =
                new User(new Email(registeredEmail), Password.createHashedPassword(password), LocalDate.of(2000, 1, 1),
                        username, phoneNumber, profileDescription, true,
                        new SphericalLocation(new SphericalPosition(0, 0)));

        new UserViewModel().subscribe(unregisteredUser);
    }

    @Test
    public void testSubscribeAndUnsubscribe() throws RequestHandlerException {
        UserViewModel userVM = new UserViewModel();
        userVM.subscribe(toBeSubscribed);

        isTrue(userVM.getCurrentUser().getSubscriptions().contains(toBeSubscribed), "subscribing did not work");

        userVM.unsubscribe(toBeSubscribed);

        isTrue(!userVM.getCurrentUser().getSubscriptions().contains(toBeSubscribed), "unsubscribing did not work");
    }

    @Test(expected = NullPointerException.class)
    public void testUnsubscribeWithNull() throws RequestHandlerException {
        new UserViewModel().unsubscribe(null);
    }

    @Test(expected = RequestHandlerException.class)
    public void testUnsubscribeWithUnregisteredUser() throws RequestHandlerException {
        User unregisteredUser =
                new User(new Email(registeredEmail), Password.createHashedPassword(password), LocalDate.of(2000, 1, 1),
                        username, phoneNumber, profileDescription, true,
                        new SphericalLocation(new SphericalPosition(0, 0)));

        new UserViewModel().unsubscribe(unregisteredUser);
    }
}
