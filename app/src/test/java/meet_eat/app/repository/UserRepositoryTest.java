package meet_eat.app.repository;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

import meet_eat.data.LoginCredential;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Tag;
import meet_eat.data.entity.relation.Bookmark;
import meet_eat.data.entity.relation.Subscription;
import meet_eat.data.entity.relation.rating.Rating;
import meet_eat.data.entity.relation.rating.RatingBasis;
import meet_eat.data.entity.relation.rating.RatingValue;
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
    private static Offer persistentOffer;

    public UserRepositoryTest() throws RequestHandlerException {
        super(new UserRepository(), getRegisteredUser(), getNewUser());
    }

    @BeforeClass
    public static void initializePersistentEntities() throws RequestHandlerException {
        Email email = new Email("wergviuhgvt349tz@example.com");
        Password password = Password.createHashedPassword("Str0ngPassw0rd!");
        LocalDate birthDay = LocalDate.of(1998, Month.OCTOBER, 16);
        String userName = "JUnit Test User";
        String phoneNumber = "0123456789";
        String userDescription = "This is a test description";
        boolean isVerified = false;
        Localizable localizable = new CityLocation("Karlsruhe");
        persistentLoginCredential = new LoginCredential(email, password);
        persistentUser = new UserRepository().addEntity(
                new User(email, password, birthDay, userName, phoneNumber, userDescription, isVerified, localizable));

        User creator = persistentUser;
        Set<Tag> tags = new HashSet<>();
        String offerName = "JUnit Test Offer";
        String offerDescription = "This is a test offer";
        double price = 5d;
        int maxParticipants = 5;
        LocalDateTime dateTime = LocalDateTime.of(2020, Month.SEPTEMBER, 30, 18, 0);
        Localizable location = new CityLocation("Karlsruhe");
        Session.getInstance().login(persistentLoginCredential);
        Offer fetchedOffer = new OfferRepository().addEntity(
                new Offer(creator, tags, offerName, offerDescription, price, maxParticipants, dateTime, location));
        Session.getInstance().logout();
        persistentOffer = fetchedOffer;
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
        // Same as testAddEntityNotLoggedIn
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

    @Test
    public void testAddSubscriptionValid() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        Subscription subscription = getEntityRepository().addSubscription(
                new Subscription(getRegisteredUser(), persistentUser));
        assertEquals(subscription.getSource(), getRegisteredUser());
        assertEquals(subscription.getTarget(), persistentUser);

        getEntityRepository().removeSubscriptionByUser(getRegisteredUser(), persistentUser);
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

    @Test
    public void testRemoveSubscriptionByUserValid() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());
        getEntityRepository().addSubscription(new Subscription(getRegisteredUser(), persistentUser));

        // Execution
        getEntityRepository().removeSubscriptionByUser(getRegisteredUser(), persistentUser);
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

    @Test
    public void testGetSubscriptionsOfUserValid() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        assertNotNull(getEntityRepository().getSubscriptionsOfUser(getRegisteredUser()));
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

    // Test getBookmarksByUser

    @Test(expected = IllegalStateException.class)
    public void testGetBookmarksByUserNotLoggedIn() throws RequestHandlerException {
        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().getBookmarksByUser(getRegisteredUser());
    }

    @Test(expected = NullPointerException.class)
    public void testGetBookmarksByUserWithNullUser() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().getBookmarksByUser(null);
    }

    @Test
    public void testGetBookmarksByUserValid() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        assertNotNull(getEntityRepository().getBookmarksByUser(getRegisteredUser()));
    }

    // Test addBookmark

    @Test(expected = IllegalStateException.class)
    public void testAddBookmarkNotLoggedIn() throws RequestHandlerException {
        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().addBookmark(new Bookmark(getRegisteredUser(), persistentOffer));
    }

    @Test(expected = NullPointerException.class)
    public void testAddBookmarkWithNullBookmark() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().addBookmark(null);
    }

    @Test
    public void testAddBookmarkValid() throws RequestHandlerException {
        // Execution
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());
        Bookmark bookmark = getEntityRepository().addBookmark(new Bookmark(getRegisteredUser(), persistentOffer));

        // Assertions
        assertEquals(bookmark.getSource(), getRegisteredUser());
        assertEquals(bookmark.getTarget(), persistentOffer);

        getEntityRepository().removeBookmark(bookmark);
    }

    // Test removeBookmark

    @Test(expected = IllegalStateException.class)
    public void testRemoveBookmarkNotLoggedIn() throws RequestHandlerException {
        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().removeBookmark(new Bookmark(getRegisteredUser(), persistentOffer));
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveBookmarkWithNullBookmark() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().addBookmark(null);
    }

    @Test
    public void testRemoveBookmarkValid() throws RequestHandlerException {
        // Execution
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());
        Bookmark bookmark = getEntityRepository().addBookmark(new Bookmark(getRegisteredUser(), persistentOffer));
        getEntityRepository().removeBookmark(bookmark);
    }

    // Test addRating

    @Test(expected = IllegalStateException.class)
    public void testAddRatingNotLoggedIn() throws RequestHandlerException {
        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().addRating(
                Rating.createHostRating(getRegisteredUser(), persistentOffer, RatingValue.POINTS_5));
    }

    @Test(expected = NullPointerException.class)
    public void testAddRatingWithNullRating() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().addRating(null);
    }

    @Test
    public void testAddRatingValid() throws RequestHandlerException {
        // Execution
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());
        Rating rating = getEntityRepository().addRating(
                Rating.createHostRating(getRegisteredUser(), persistentOffer, RatingValue.POINTS_5));
        assertEquals(rating.getOffer(), persistentOffer);
        assertEquals(rating.getValue(), RatingValue.POINTS_5);
        assertEquals(rating.getBasis(), RatingBasis.HOST);
    }

    // Test getNumericHostRating

    @Test(expected = IllegalStateException.class)
    public void testGetNumericHostRatingNotLoggedIn() throws RequestHandlerException {
        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().getNumericHostRating(getRegisteredUser());
    }

    @Test(expected = NullPointerException.class)
    public void testGetNumericHostRatingWithNullUser() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().getNumericHostRating(null);
    }

    @Test
    public void testGetNumericHostRatingValid() throws RequestHandlerException {
        // Execution
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());
        assertNotNull(getEntityRepository().getNumericHostRating(getRegisteredUser()));
    }

    // Test getNumericGuestRating

    @Test(expected = IllegalStateException.class)
    public void testGetNumericGuestRatingNotLoggedIn() throws RequestHandlerException {
        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().getNumericGuestRating(getRegisteredUser());
    }

    @Test(expected = NullPointerException.class)
    public void testGetNumericGuestRatingWithNullUser() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().getNumericGuestRating(null);
    }

    @Test
    public void testGetNumericGuestRatingValid() throws RequestHandlerException {
        // Execution
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());
        assertNotNull(getEntityRepository().getNumericGuestRating(getRegisteredUser()));
    }
}
