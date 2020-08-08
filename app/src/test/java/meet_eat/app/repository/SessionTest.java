package meet_eat.app.repository;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Objects;

import meet_eat.data.LoginCredential;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;
import meet_eat.data.location.CityLocation;
import meet_eat.data.location.Localizable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SessionTest {

    private static User user;
    private static LoginCredential loginCredential;

    @BeforeClass
    public static void setUpClass() throws RequestHandlerException {
        // Register
        Email email = new Email("test@example.com");
        Password password = Password.createHashedPassword("Str0ngPassw0rd!");
        LocalDate birthDay = LocalDate.of(1998, Month.FEBRUARY, 6);
        String name = "JUnit Test User";
        String phoneNumber = "0123456789";
        String description = "This is my test description";
        boolean isVerified = false;
        Localizable location = new CityLocation("Karlsruhe");
        User testUser = new User(email, password, birthDay, name, phoneNumber, description, isVerified, location);
        loginCredential = new LoginCredential(email, password);
        user = new UserRepository().addEntity(testUser);
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
            session.login(loginCredential);
        }
        new UserRepository().deleteEntity(user);
        user = null;
        loginCredential = null;
    }

    @Test
    public void testGetInstance() {
        // Execution
        Session session = Session.getInstance();
        Session sessionTwo = Session.getInstance();

        // Assertions
        assertNotNull(session);
        assertEquals(session, sessionTwo);
    }

    @Test(expected = NullPointerException.class)
    public void testGetUserNotLoggedIn() {
        // Execution
        Session.getInstance().getUser();
    }

    @Test
    public void testGetUserLoggedIn() throws RequestHandlerException {
        // Execution
        Session session = Session.getInstance();
        session.login(loginCredential);
        User testUser = session.getUser();

        // Assertions
        assertNotNull(testUser);
        assertEquals(user, testUser);
    }

    @Test
    public void testGetTokenNotLoggedIn() {
        // Assertions
        assertNull(Session.getInstance().getToken());
    }

    @Test
    public void testLogin() throws RequestHandlerException {
        Session session = Session.getInstance();
        assertNull(session.getToken());

        // Execution
        session.login(loginCredential);

        // Assertions
        assertNotNull(session.getToken());
    }

    @Test(expected = RequestHandlerException.class)
    public void testLoginWrongCredential() throws RequestHandlerException {
        // Execution
        Email email = new Email("abcdefghijklmnopqrstuvwxyz@example.com");
        Password password = Password.createHashedPassword("wv584zn958v5vZ6b9b2v!");
        Session.getInstance().login(new LoginCredential(email, password));
    }

    @Test(expected = NullPointerException.class)
    public void testLoginNullCredential() throws RequestHandlerException {
        // Execution
        Session.getInstance().login(null);
    }

    @Test
    public void testLogout() throws RequestHandlerException {
        // Execution
        Session session = Session.getInstance();
        session.login(loginCredential);

        // Assertions
        assertNotNull(session.getToken());

        session.logout();

        assertNull(session.getToken());
    }

    @Test(expected = IllegalStateException.class)
    public void testLoginWhenLoggedIn() throws RequestHandlerException {
        // Execution
        Session session = Session.getInstance();
        session.login(loginCredential);

        // Assertions
        assertNotNull(session.getToken());
        session.login(loginCredential);
    }

    @Test(expected = IllegalStateException.class)
    public void testLogoutWhenLoggedOut() throws RequestHandlerException {
        // Execution
        Session session = Session.getInstance();

        // Assertions
        assertNull(session.getToken());
        session.logout();
    }

    @Test
    public void testLoginAndLogoutMultipleTimes() throws RequestHandlerException {
        // Execution
        Session session = Session.getInstance();

        // Assertions
        assertNull(session.getToken());
        for (int i = 0; i < 2; i++) {
            session.login(loginCredential);
            assertNotNull(session.getToken());
            session.logout();
            assertNull(session.getToken());
        }
    }
}
