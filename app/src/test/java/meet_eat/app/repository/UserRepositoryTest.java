package meet_eat.app.repository;

import org.junit.AfterClass;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import meet_eat.data.LoginCredential;
import meet_eat.data.comparator.OfferComparableField;
import meet_eat.data.comparator.OfferComparator;
import meet_eat.data.entity.relation.Subscription;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.Role;
import meet_eat.data.entity.user.User;
import meet_eat.data.entity.user.setting.ColorMode;
import meet_eat.data.entity.user.setting.DisplaySetting;
import meet_eat.data.entity.user.setting.NotificationSetting;
import meet_eat.data.entity.user.setting.Setting;
import meet_eat.data.location.CityLocation;
import meet_eat.data.location.Localizable;
import meet_eat.data.predicate.OfferPredicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class UserRepositoryTest extends EntityRepositoryTest<UserRepository, User, String> {

    public UserRepositoryTest() {
        super(new UserRepository(), getUserWithId(), getUserWithoutId());
    }

    private static User getUserWithId() {
        String identifier = "oreub43gh43g";
        Set<Setting> settings = new HashSet<>();
        settings.add(new NotificationSetting(true, 60));
        settings.add(new DisplaySetting(ColorMode.DARK));
        Role role = Role.USER;
        Email email = new Email("wergviuhgvt349tz@example.com");
        Password password = Password.createHashedPassword("Str0ngPassw0rd!");
        LocalDate birthDay = LocalDate.of(1998, Month.OCTOBER, 16);
        String name = "JUnit Test User";
        String phoneNumber = "0123456789";
        String description = "This is a test description";
        boolean isVerified = false;
        Collection<OfferPredicate> offerPredicates = new LinkedList<>();
        Localizable localizable = new CityLocation("Karlsruhe");
        OfferComparator offerComparator = new OfferComparator(OfferComparableField.TIME, localizable);
        return new User(identifier, settings, role, email, password,
                birthDay, name, phoneNumber, description, isVerified, offerPredicates, offerComparator, localizable);
    }

    private static User getUserWithoutId() {
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

    @AfterClass
    public static void tearDownClass() throws RequestHandlerException {
        Session.getInstance().login(new LoginCredential(getUserWithoutId().getEmail(), getUserWithoutId().getPassword()));
        new UserRepository().deleteEntity(Session.getInstance().getUser());
        Session.getInstance().logout();
        Session.getInstance().login(getRegisteredLoginCredential());
        new UserRepository().deleteEntity(getRegisteredUser());
    }

    // Test addEntity

    @Test
    @Override
    public void testAddEntityNotLoggedIn() throws RequestHandlerException {
        assertNull(Session.getInstance().getToken());

        // Execution
        User registeredUser = getEntityRepository().addEntity(getEntityWithoutId());

        // Assertions
        assertNull(getUserWithoutId().getIdentifier());
        assertNotNull(registeredUser);
        assertNotNull(registeredUser.getIdentifier());
        assertEquals(registeredUser.getEmail(), getUserWithoutId().getEmail());
    }

    // Test report

    // TODO Rework tests
    /*
    @Test(expected = IllegalStateException.class)
    public void testReportNotLoggedIn() {
        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().report(getRegisteredUser(), new Report(getUserWithId(), "test"));
    }

    @Test(expected = NullPointerException.class)
    public void testReportWithNullUser() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().report(null, new Report(getUserWithId(), "test"));
    }

    @Test(expected = NullPointerException.class)
    public void testReportWithNullReport() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().report(getRegisteredUser(), null);
    }
    */

    // Test addSubscription

    @Test(expected = IllegalStateException.class)
    public void testAddSubscriptionNotLoggedIn() throws RequestHandlerException {
        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().addSubscription(new Subscription(getRegisteredUser(), getUserWithId()));
    }

    @Test(expected = NullPointerException.class)
    public void testAddSubscriptionWithNullSource() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().addSubscription(new Subscription(null, getUserWithId()));
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
        getEntityRepository().removeSubscriptionByUser(getRegisteredUser(), getUserWithId());
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveSubscriptionByUserWithNullSubscriber() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().removeSubscriptionByUser(null, getUserWithId());
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
