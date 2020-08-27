package meet_eat.app.repository;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

import meet_eat.data.LoginCredential;
import meet_eat.data.entity.relation.Subscription;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;
import meet_eat.data.location.CityLocation;
import meet_eat.data.location.Localizable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class UserRepositoryTest extends EntityRepositoryTest<UserRepository, User, String> {

    private static User persistentUser;
    private static LoginCredential persistentLoginCredential;

    public UserRepositoryTest() throws RequestHandlerException {
        super(new UserRepository(), getRegisteredUser(), getNewUser());
    }

    @BeforeClass
    public static void initializePersistentUser() throws RequestHandlerException {
        Email email = new Email("wergviuhgvt349tz@example.com");
        Password password = Password.createHashedPassword("Str0ngPassw0rd!");
        LocalDate birthDay = LocalDate.of(1998, Month.OCTOBER, 16);
        String name = "JUnit Test User";
        String phoneNumber = "0123456789";
        String description = "This is a test description";
        boolean isVerified = false;
        Localizable localizable = new CityLocation("Karlsruhe");
        persistentLoginCredential = new LoginCredential(email, password);
        persistentUser = new UserRepository().addEntity(
                new User(email, password, birthDay, name, phoneNumber, description, isVerified, localizable));
    }

    @AfterClass
    public static void deletePersistentUser() throws RequestHandlerException {
        Session.getInstance().login(persistentLoginCredential);
        new UserRepository().deleteEntity(persistentUser);
        Session.getInstance().logout();
    }

    private static User getNewUser() {
        Email email = new Email("423v8hg9fjg@example.com");
        Password password = Password.createHashedPassword("Str0ngPassw0rd!");
        LocalDate birthDay = LocalDate.of(1998, Month.OCTOBER, 16);
        String name = "JUnit Test User";
        String phoneNumber = "0123456789";
        String description = "This is a test description";
        boolean isVerified = false;
        Localizable localizable = new CityLocation("Karlsruhe");
        return new User(email, password, birthDay, name, phoneNumber, description, isVerified, localizable);
    }

    // Test addEntity

    @Test
    @Override
    public void testAddEntityNotLoggedIn() throws RequestHandlerException {
        assertNull(Session.getInstance().getToken());

        // Execution
        User fetchedUser = getEntityRepository().addEntity(getNewEntity());

        // Assertions
        assertNull(getNewEntity().getIdentifier());
        assertNotNull(fetchedUser);
        assertNotNull(fetchedUser.getIdentifier());
        assertEquals(fetchedUser.getEmail(), getNewEntity().getEmail());

        Session.getInstance().login(new LoginCredential(getNewUser().getEmail(), getNewUser().getPassword()));
        new UserRepository().deleteEntity(Session.getInstance().getUser());
        Session.getInstance().logout();
    }

    @Test
    @Override
    public void testAddEntityValid() throws RequestHandlerException {
    }

    // Test deleteEntity

    @Test
    @Override
    public void testDeleteEntityValid() throws RequestHandlerException {
        // Assertions
        User fetchedUser = getEntityRepository().addEntity(getNewUser());
        Session.getInstance().login(new LoginCredential(getNewUser().getEmail(), getNewUser().getPassword()));
        assertNotNull(Session.getInstance().getToken());
        assertNotNull(fetchedUser.getIdentifier());

        // Execution
        getEntityRepository().deleteEntity(fetchedUser);
    }

    // Test addSubscription

    @Test(expected = IllegalStateException.class)
    public void testAddSubscriptionNotLoggedIn() throws RequestHandlerException {
        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().addSubscription(new Subscription(getRegisteredUser(), persistentUser));
    }

    @Test(expected = NullPointerException.class)
    public void testAddSubscriptionWithNullSource() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().addSubscription(new Subscription(null, persistentUser));
    }

    @Test(expected = NullPointerException.class)
    public void testAddSubscriptionWithNullTarget() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().addSubscription(new Subscription(getRegisteredUser(), null));
    }

    // Test removeSubscriptionByUser

    @Test(expected = IllegalStateException.class)
    public void testRemoveSubscriptionByUserNotLoggedIn() throws RequestHandlerException {
        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().removeSubscriptionByUser(getRegisteredUser(), persistentUser);
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveSubscriptionByUserWithNullSubscriber() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().removeSubscriptionByUser(null, persistentUser);
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveSubscriptionByUserWithNullSubscribedUser() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().removeSubscriptionByUser(getRegisteredUser(), null);
    }

    // Test getSubscriptionsOfUser

    @Test(expected = IllegalStateException.class)
    public void testGetSubscriptionsOfUserNotLoggedIn() throws RequestHandlerException {
        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().getSubscriptionsOfUser(getRegisteredUser());
    }

    @Test(expected = NullPointerException.class)
    public void testGetSubscriptionsOfUserWithNullSubscriber() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().getSubscriptionsOfUser(null);
    }

    // Test resetPassword

    @Test(expected = NullPointerException.class)
    public void testResetPasswordWithNullEmail() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().resetPassword(null);
    }
}
