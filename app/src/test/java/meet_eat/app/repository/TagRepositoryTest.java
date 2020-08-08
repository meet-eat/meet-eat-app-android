package meet_eat.app.repository;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Objects;

import meet_eat.data.LoginCredential;
import meet_eat.data.entity.Tag;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;
import meet_eat.data.location.CityLocation;
import meet_eat.data.location.Localizable;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TagRepositoryTest {

    private static User user;
    private static LoginCredential loginCredential;
    private static TagRepository tagRepository;

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
        tagRepository = new TagRepository();
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

    @Test(expected = IllegalStateException.class)
    public void testAddEntityNotLoggedIn() throws RequestHandlerException {
        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        tagRepository.addEntity(new Tag("test"));
    }

    @Test(expected = IllegalStateException.class)
    public void testUpdateEntityNotLoggedIn() throws RequestHandlerException {
        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        tagRepository.addEntity(new Tag("test"));
    }

    @Test(expected = IllegalStateException.class)
    public void testDeleteEntityNotLoggedIn() throws RequestHandlerException {
        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        tagRepository.addEntity(new Tag("test"));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetEntityByIdNotLoggedIn() throws RequestHandlerException {
        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        tagRepository.addEntity(new Tag("test"));
    }

    @Test(expected = NullPointerException.class)
    public void testAddEntityWithNull() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(loginCredential);
        assertNotNull(Session.getInstance().getToken());

        // Execution
        tagRepository.addEntity(null);
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateEntityWithNull() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(loginCredential);
        assertNotNull(Session.getInstance().getToken());

        // Execution
        tagRepository.addEntity(null);
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteEntityWithNull() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(loginCredential);
        assertNotNull(Session.getInstance().getToken());

        // Execution
        tagRepository.addEntity(null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetEntityByIdWithNull() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(loginCredential);
        assertNotNull(Session.getInstance().getToken());

        // Execution
        tagRepository.addEntity(null);
    }


}
