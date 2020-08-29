package meet_eat.app.repository;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.time.LocalDate;
import java.time.Month;
import java.util.Objects;

import meet_eat.data.LoginCredential;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;
import meet_eat.data.location.CityLocation;
import meet_eat.data.location.Localizable;

public class RepositoryTestEnvironment {

    private static final String UNIQUE_IDENTIFIER = String.valueOf(System.currentTimeMillis() % 100000);
    private static final String UNREGISTERED_EMAIL = "test@example.com";
    private static final String PASSWORD = "Str0ngPassword!";
    private static final LocalDate BIRTH_DAY = LocalDate.of(1998, Month.FEBRUARY, 6);
    private static final String USER_NAME = "JUnit Test User";
    private static final String PHONE_NUMBER = "0123456789";
    private static final String DESCRIPTION = "JUnit description";
    private static final boolean IS_VERIFIED = false;
    private static final Localizable LOCATION = new CityLocation("Stuttgart");

    private static User registeredUser;
    private static LoginCredential registeredLoginCredential;

    @BeforeClass
    public static void setUpClass() throws RequestHandlerException {
        // Register
        Email email = new Email(UNIQUE_IDENTIFIER + UNREGISTERED_EMAIL);
        Password password = Password.createHashedPassword(PASSWORD);
        User testUser = new User(email, password, BIRTH_DAY, USER_NAME, PHONE_NUMBER, DESCRIPTION, IS_VERIFIED, LOCATION);
        registeredLoginCredential = new LoginCredential(email, password);
        registeredUser = new UserRepository().addEntity(testUser);

        // Delete token of deleted user from tests before
        Session session = Session.getInstance();
        if (Objects.nonNull(session.getToken())) {
            session.logout();
        }
    }

    @After
    public void logout() throws RequestHandlerException {
        Session session = Session.getInstance();
        if (Objects.nonNull(session.getToken())) {
            session.logout();
        }
    }

    @AfterClass
    public static void tearDownClass() throws RequestHandlerException {
        Session session = Session.getInstance();
        if (Objects.isNull(session.getToken())) {
            session.login(registeredLoginCredential);
        }
        new UserRepository().deleteEntity(registeredUser);
        session.logout();
        registeredUser = null;
        registeredLoginCredential = null;
    }

    protected static User getRegisteredUser() {
        return registeredUser;
    }

    protected static LoginCredential getRegisteredLoginCredential() {
        return registeredLoginCredential;
    }
}
