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

    private static User registeredUser;
    private static LoginCredential registeredLoginCredential;

    @BeforeClass
    public static void setUpClass() throws RequestHandlerException {
        // Register
        Email email = new Email("asoinasidnsiadn@example.com");
        Password password = Password.createHashedPassword("Str0ngPassw0rd!");
        LocalDate birthDay = LocalDate.of(1998, Month.FEBRUARY, 6);
        String name = "JUnit Test User";
        String phoneNumber = "0123456789";
        String description = "This is my test description";
        boolean isVerified = false;
        Localizable location = new CityLocation("Karlsruhe");
        User testUser = new User(email, password, birthDay, name, phoneNumber, description, isVerified, location);
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
