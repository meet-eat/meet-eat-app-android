package meet_eat.app.repository;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import meet_eat.data.LoginCredential;
import meet_eat.data.Page;
import meet_eat.data.comparator.OfferComparableField;
import meet_eat.data.comparator.OfferComparator;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Tag;
import meet_eat.data.entity.relation.Participation;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;
import meet_eat.data.location.CityLocation;
import meet_eat.data.location.Localizable;
import meet_eat.data.predicate.OfferPredicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class OfferRepositoryTest extends EntityRepositoryTest<OfferRepository, Offer, String> {

    private static Page page;
    private static ArrayList<OfferPredicate> predicates;
    private static OfferComparator comparator;
    private static User persistentUser;
    private static LoginCredential persistentLoginCredential;

    public OfferRepositoryTest() throws RequestHandlerException {
        super(new OfferRepository(), getPersistentOffer(), getNewOffer());
    }

    private static Offer getPersistentOffer() throws RequestHandlerException {
        User creator = getRegisteredUser();
        Set<Tag> tags = new HashSet<>();
        String name = "JUnit Test Offer";
        String description = "This is a test offer";
        double price = 5d;
        int maxParticipants = 5;
        LocalDateTime dateTime = LocalDateTime.of(2020, Month.SEPTEMBER, 30, 18, 0);
        Localizable location = new CityLocation("Karlsruhe");
        Session.getInstance().login(getRegisteredLoginCredential());
        Offer fetchedOffer = new OfferRepository().addEntity(
                new Offer(creator, tags, name, description, price, maxParticipants, dateTime, location));
        Session.getInstance().logout();
        return fetchedOffer;
    }

    private static Offer getNewOffer() {
        User creator = getRegisteredUser();
        Set<Tag> tags = new HashSet<>();
        String name = "JUnit Test Offer";
        String description = "This is a test offer";
        double price = 5d;
        int maxParticipants = 5;
        LocalDateTime dateTime = LocalDateTime.of(2020, Month.SEPTEMBER, 30, 18, 0);
        Localizable location = new CityLocation("Karlsruhe");
        return new Offer(creator, tags, name, description, price, maxParticipants, dateTime, location);
    }

    @BeforeClass
    public static void initialize() throws RequestHandlerException {
        page = new Page(0, 10);
        predicates = new ArrayList<>();
        CityLocation cityLocation = new CityLocation("Karlsruhe");
        comparator = new OfferComparator(OfferComparableField.DISTANCE, cityLocation);

        // Persistent user
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

    // Test getOffers

    @Test(expected = IllegalStateException.class)
    public void testGetOffersNotLoggedIn() throws RequestHandlerException {
        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().getOffers(page, predicates, comparator);
    }

    @Test(expected = NullPointerException.class)
    public void testGetOffersWithNullPage() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().getOffers(null, predicates, comparator);
    }

    @Test(expected = NullPointerException.class)
    public void testGetOffersWithNullPredicates() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().getOffers(page, null, comparator);
    }

    @Test(expected = NullPointerException.class)
    public void testGetOffersWithNullComparator() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().getOffers(page, predicates, null);
    }

    @Test
    public void testGetOffersValid() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        assertNotNull(getEntityRepository().getOffers(page, predicates, comparator));
    }

    // Test getOffersByCreator

    @Test(expected = IllegalStateException.class)
    public void testGetOffersByCreatorNotLoggedIn() throws RequestHandlerException {
        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().getOffersByCreator(getRegisteredUser(), page, predicates, comparator);
    }

    @Test(expected = NullPointerException.class)
    public void testGetOffersByCreatorWithNullPage() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().getOffersByCreator(getRegisteredUser(), null, predicates, comparator);
    }

    @Test(expected = NullPointerException.class)
    public void testGetOffersByCreatorWithNullPredicates() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().getOffersByCreator(getRegisteredUser(), page, null, comparator);
    }

    @Test(expected = NullPointerException.class)
    public void testGetOffersByCreatorWithNullComparator() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().getOffersByCreator(getRegisteredUser(), page, predicates, null);
    }

    @Test
    public void testGetOffersByCreatorValid() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        assertNotNull(getEntityRepository().getOffersByCreator(getRegisteredUser(), page, predicates, comparator));
    }

    // Test getOffersBySubscriptions

    @Test(expected = IllegalStateException.class)
    public void testGetOffersBySubscriptionsNotLoggedIn() throws RequestHandlerException {
        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().getOffersBySubscriptions(getRegisteredUser(), page, predicates, comparator);
    }

    @Test(expected = NullPointerException.class)
    public void testGetOffersBySubscriptionsWithNullPage() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().getOffersBySubscriptions(getRegisteredUser(), null, predicates, comparator);
    }

    @Test(expected = NullPointerException.class)
    public void testGetOffersBySubscriptionsWithNullPredicates() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().getOffersBySubscriptions(getRegisteredUser(), page, null, comparator);
    }

    @Test(expected = NullPointerException.class)
    public void testGetOffersBySubscriptionsWithNullComparator() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().getOffersBySubscriptions(getRegisteredUser(), page, predicates, null);
    }

    @Test
    public void testGetOffersBySubscriptionsValid() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        assertNotNull(getEntityRepository().getOffersBySubscriptions(getRegisteredUser(), page, predicates, comparator));
    }

    // Test addParticipation

    @Test(expected = IllegalStateException.class)
    public void testAddParticipationNotLoggedIn() throws RequestHandlerException {
        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().addParticipation(new Participation(persistentUser, getPersistentEntity()));
    }

    @Test(expected = NullPointerException.class)
    public void testAddParticipationWithNullOffer() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().addParticipation(null);
    }

    @Test
    public void testAddParticipationValid() throws RequestHandlerException {
        // Execution
        Session.getInstance().login(persistentLoginCredential);
        assertNotNull(Session.getInstance().getToken());
        Participation participation = getEntityRepository().addParticipation(
                new Participation(persistentUser, getPersistentEntity()));

        // Assertions
        assertEquals(participation.getSource(), persistentUser);
        assertEquals(participation.getTarget(), getPersistentEntity());

        getEntityRepository().removeParticipation(participation);
    }

    // Test removeParticipation

    @Test(expected = IllegalStateException.class)
    public void testRemoveParticipationNotLoggedIn() throws RequestHandlerException {
        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().removeParticipation(new Participation(persistentUser, getPersistentEntity()));
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveParticipationWithNullOffer() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().removeParticipation(null);
    }

    @Test
    public void testRemoveParticipationValid() throws RequestHandlerException {
        // Execution
        Session.getInstance().login(persistentLoginCredential);
        assertNotNull(Session.getInstance().getToken());
        Participation participation = getEntityRepository().addParticipation(
                new Participation(persistentUser, getPersistentEntity()));
        getEntityRepository().removeParticipation(participation);
    }

    // Test getParticipationsByOffer

    @Test(expected = IllegalStateException.class)
    public void testGetParticipationsByOfferNotLoggedIn() throws RequestHandlerException {
        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().getParticipationsByOffer(getPersistentEntity());
    }

    @Test(expected = NullPointerException.class)
    public void testGetParticipationsByOfferWithNullOffer() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().getParticipationsByOffer(null);
    }

    @Test
    public void testGetParticipationsByOfferValid() throws RequestHandlerException {
        // Execution
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Assertions
        assertNotNull(getEntityRepository().getParticipationsByOffer(getPersistentEntity()));
    }
}
