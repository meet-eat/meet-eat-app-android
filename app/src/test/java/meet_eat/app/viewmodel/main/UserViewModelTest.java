package meet_eat.app.viewmodel.main;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Objects;

import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.repository.Session;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.util.Assert.isTrue;

public class UserViewModelTest {

    private static final String testEmail = "@example.com";
    private static final String password = "ABcd12§$";
    private static final String username = "Registered User";
    private static final String phoneNumber = "0123456789";
    private static final String profileDescription = "JUnit Test User";
    private static final String changedPassword = "HelloWorld1!";

    private static SettingsViewModel settingsVM;
    private static LoginViewModel loginVM;
    private static UserViewModel userVM;
    private static User registeredUser;
    private static User toBeReported;
    private static String uniqueIdentifier;

    @BeforeClass
    public static void initialize() throws RequestHandlerException {
        loginVM = new LoginViewModel();
        userVM = new UserViewModel();
        RegisterViewModel registerVM = new RegisterViewModel();
        settingsVM = new SettingsViewModel();

        uniqueIdentifier = String.valueOf(System.currentTimeMillis() % 100000);
        toBeReported = new User(new Email(uniqueIdentifier + 1 + testEmail), Password.createHashedPassword(password),
                LocalDate.of(2000, 1, 1), username, phoneNumber, profileDescription, true,
                new SphericalLocation(new SphericalPosition(0, 0)));

        User toBeSubscribed =
                new User(new Email(uniqueIdentifier + 2 + testEmail), Password.createHashedPassword(password),
                        LocalDate.of(2000, 1, 1), username, phoneNumber, profileDescription, true,
                        new SphericalLocation(new SphericalPosition(0, 0)));

        registeredUser = new User(new Email(uniqueIdentifier + testEmail), Password.createHashedPassword(password),
                LocalDate.of(2000, 1, 1), username, phoneNumber, profileDescription, true,
                new SphericalLocation(new SphericalPosition(0, 0)));

        registerVM.register(toBeReported);
        registerVM.register(toBeSubscribed);
        registerVM.register(registeredUser);
        loginVM.login(uniqueIdentifier + testEmail, password);
        System.out.println("Logged in with: " + settingsVM.getCurrentUser().getEmail() + "\n");
    }

    @AfterClass
    public static void cleanUp() throws RequestHandlerException {
        if (Objects.nonNull(settingsVM.getCurrentUser())) {
            settingsVM.deleteUser(settingsVM.getCurrentUser());
        }
        for (int i = 1; i <= 2; i++) {
            loginVM.login(uniqueIdentifier + i + testEmail, password);
            settingsVM.deleteUser(settingsVM.getCurrentUser());
            System.out.println("Deleted user " + settingsVM.getCurrentUser()+ "\n");

        }
    }

    @Test
    public void testGetCurrentUser() {
        assertNotNull(userVM.getCurrentUser().getIdentifier());
    }

    @Test
    public void testSubscribeAndUnsubscribe() throws RequestHandlerException {
        settingsVM.logout();
        loginVM.login(uniqueIdentifier + 2 + testEmail, password);
        User toBeSubscribedReally = userVM.getCurrentUser();
        settingsVM.logout();
        loginVM.login(uniqueIdentifier + testEmail, changedPassword);

        userVM.subscribe(toBeSubscribedReally);

        isTrue(userVM.isSubscribed(toBeSubscribedReally), "subscribing did not work");

        userVM.unsubscribe(toBeSubscribedReally);

        isTrue(!userVM.isSubscribed(toBeSubscribedReally), "unsubscribing did not work");
    }

    @Test
    public void testEditUser() throws RequestHandlerException {
        // changing email is omitted, there is no such functionality in the app
        // changing password is done in testChangePassword()
        registeredUser.setPhoneNumber(phoneNumber + "1");
        registeredUser.setDescription(profileDescription + "1");
        registeredUser.setBirthDay(LocalDate.MIN);
        registeredUser.setVerified(!registeredUser.isVerified());
        registeredUser.setName(username + "1");
        // set role to new role
        registeredUser.setRole(registeredUser.getRole().equals(Role.ADMIN) ? Role.USER : Role.ADMIN);
        registeredUser.setLocalizable(new SphericalLocation(new SphericalPosition(1, 1)));

        userVM.edit(userVM.getCurrentUser());
    }

    @Test
    public void testChangePassword() throws RequestHandlerException {
        Password newPassword = Password.createHashedPassword("HelloWorld1!");
        User user = userVM.getCurrentUser();
        user.setPassword(newPassword);
        userVM.edit(user);
        settingsVM.logout();
        loginVM.login(uniqueIdentifier + testEmail, changedPassword);
        assertTrue(newPassword.matches(userVM.getCurrentUser().getPassword()));
    }

    @Test(expected = NullPointerException.class)
    public void testEditUserWithNull() throws RequestHandlerException {
        userVM.edit(null);
    }

    @Ignore("Not yet implemented")
    @Test
    public void testReport() throws RequestHandlerException {
        Report report = new Report(userVM.getCurrentUser(), "");
        userVM.report(toBeReported, report);
    }

    @Test(expected = NullPointerException.class)
    public void testReportWithNullReportObject() throws RequestHandlerException {
        userVM.report(toBeReported, null);
    }

    @Test(expected = NullPointerException.class)
    public void testReportWithNullUser() throws RequestHandlerException {
        Report report = new Report(userVM.getCurrentUser(), "");
        userVM.report(null, report);
    }

    @Test(expected = NullPointerException.class)
    public void testReportWithNull() throws RequestHandlerException {
        userVM.report(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testSubscribeWithNull() throws RequestHandlerException {
        userVM.subscribe(null);
    }

    @Test(expected = NullPointerException.class)
    public void testUnsubscribeWithNull() throws RequestHandlerException {
        userVM.unsubscribe(null);
    }
}
