package meet_eat.app.repository;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import meet_eat.data.Page;
import meet_eat.data.comparator.OfferComparableField;
import meet_eat.data.comparator.OfferComparator;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Tag;
import meet_eat.data.entity.relation.Report;
import meet_eat.data.entity.user.User;
import meet_eat.data.location.CityLocation;
import meet_eat.data.location.Localizable;
import meet_eat.data.predicate.OfferPredicate;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class OfferRepositoryTest extends EntityRepositoryTest<OfferRepository, Offer, String> {

    public OfferRepositoryTest() {
        super(new OfferRepository(), getOfferWithId(), getOfferWithoutId());
    }

    private static Offer getOfferWithId() {
        String identifier = "gh30sifj02";
        Collection<Report> reports = new LinkedList<>();
        User creator = getRegisteredUser();
        Set<User> participants = new HashSet<>();
        Set<Tag> tags = new HashSet<>();
        String name = "JUnit Test Offer";
        String description = "This is a test offer";
        double price = 5d;
        int maxParticipants = 5;
        LocalDateTime dateTime = LocalDateTime.of(2020, Month.SEPTEMBER, 30, 18, 0);
        Localizable location = new CityLocation("Karlsruhe");
        return new Offer(identifier, reports, creator, participants, tags, name, description, price, maxParticipants,
                dateTime, location);
    }

    private static Offer getOfferWithoutId() {
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

    // Test getOffers

    @Test(expected = IllegalStateException.class)
    public void testGetOffersNotLoggedIn() throws RequestHandlerException {
        // Test data
        Page page = new Page(0, 10);
        ArrayList<OfferPredicate> predicates = new ArrayList<>();
        CityLocation cityLocation = new CityLocation("Karlsruhe");
        OfferComparator comparator = new OfferComparator(OfferComparableField.DISTANCE, cityLocation);

        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().getOffers(page, predicates, comparator);
    }

    @Test(expected = NullPointerException.class)
    public void testGetOffersWithNullPage() throws RequestHandlerException {
        // Test data
        ArrayList<OfferPredicate> predicates = new ArrayList<>();
        CityLocation cityLocation = new CityLocation("Karlsruhe");
        OfferComparator comparator = new OfferComparator(OfferComparableField.DISTANCE, cityLocation);

        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().getOffers(null, predicates, comparator);
    }

    @Test(expected = NullPointerException.class)
    public void testGetOffersWithNullPredicates() throws RequestHandlerException {
        // Test data
        Page page = new Page(0, 10);
        CityLocation cityLocation = new CityLocation("Karlsruhe");
        OfferComparator comparator = new OfferComparator(OfferComparableField.DISTANCE, cityLocation);

        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().getOffers(page, null, comparator);
    }

    @Test(expected = NullPointerException.class)
    public void testGetOffersWithNullComparator() throws RequestHandlerException {
        // Test data
        Page page = new Page(0, 10);
        ArrayList<OfferPredicate> predicates = new ArrayList<>();

        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().getOffers(page, predicates, null);
    }

    // Test getOffersByCreator

    @Test(expected = IllegalStateException.class)
    public void testGetOffersByCreatorNotLoggedIn() throws RequestHandlerException {
        // Test data
        Page page = new Page(0, 10);
        ArrayList<OfferPredicate> predicates = new ArrayList<>();
        CityLocation cityLocation = new CityLocation("Karlsruhe");
        OfferComparator comparator = new OfferComparator(OfferComparableField.DISTANCE, cityLocation);

        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().getOffersByCreator(getRegisteredUser(), page, predicates, comparator);
    }

    @Test(expected = NullPointerException.class)
    public void testGetOffersByCreatorWithNullPage() throws RequestHandlerException {
        // Test data
        ArrayList<OfferPredicate> predicates = new ArrayList<>();
        CityLocation cityLocation = new CityLocation("Karlsruhe");
        OfferComparator comparator = new OfferComparator(OfferComparableField.DISTANCE, cityLocation);

        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().getOffersByCreator(getRegisteredUser(), null, predicates, comparator);
    }

    @Test(expected = NullPointerException.class)
    public void testGetOffersByCreatorWithNullPredicates() throws RequestHandlerException {
        // Test data
        Page page = new Page(0, 10);
        CityLocation cityLocation = new CityLocation("Karlsruhe");
        OfferComparator comparator = new OfferComparator(OfferComparableField.DISTANCE, cityLocation);

        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().getOffersByCreator(getRegisteredUser(), page, null, comparator);
    }

    @Test(expected = NullPointerException.class)
    public void testGetOffersByCreatorWithNullComparator() throws RequestHandlerException {
        // Test data
        Page page = new Page(0, 10);
        ArrayList<OfferPredicate> predicates = new ArrayList<>();

        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().getOffersByCreator(getRegisteredUser(), page, predicates, null);
    }

    // Test getOffersBySubscriptions

    @Test(expected = IllegalStateException.class)
    public void testGetOffersBySubscriptionsNotLoggedIn() throws RequestHandlerException {
        // Test data
        Page page = new Page(0, 10);
        ArrayList<OfferPredicate> predicates = new ArrayList<>();
        CityLocation cityLocation = new CityLocation("Karlsruhe");
        OfferComparator comparator = new OfferComparator(OfferComparableField.DISTANCE, cityLocation);

        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().getOffersBySubscriptions(getRegisteredUser(), page, predicates, comparator);
    }

    @Test(expected = NullPointerException.class)
    public void testGetOffersBySubscriptionsWithNullPage() throws RequestHandlerException {
        // Test data
        ArrayList<OfferPredicate> predicates = new ArrayList<>();
        CityLocation cityLocation = new CityLocation("Karlsruhe");
        OfferComparator comparator = new OfferComparator(OfferComparableField.DISTANCE, cityLocation);

        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().getOffersBySubscriptions(getRegisteredUser(), null, predicates, comparator);
    }

    @Test(expected = NullPointerException.class)
    public void testGetOffersBySubscriptionsWithNullPredicates() throws RequestHandlerException {
        // Test data
        Page page = new Page(0, 10);
        CityLocation cityLocation = new CityLocation("Karlsruhe");
        OfferComparator comparator = new OfferComparator(OfferComparableField.DISTANCE, cityLocation);

        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().getOffersBySubscriptions(getRegisteredUser(), page, null, comparator);
    }

    @Test(expected = NullPointerException.class)
    public void testGetOffersBySubscriptionsWithNullComparator() throws RequestHandlerException {
        // Test data
        Page page = new Page(0, 10);
        ArrayList<OfferPredicate> predicates = new ArrayList<>();

        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().getOffersBySubscriptions(getRegisteredUser(), page, predicates, null);
    }

    // Test report

    @Test(expected = IllegalStateException.class)
    public void testReportNotLoggedIn() {
        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().report(getOfferWithId(), new Report(getRegisteredUser(), "test"));
    }

    @Test(expected = NullPointerException.class)
    public void testReportWithNullOffer() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().report(null, new Report(getRegisteredUser(), "test"));
    }

    @Test(expected = NullPointerException.class)
    public void testReportWithNullReport() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().report(getOfferWithId(), null);
    }

    // Test addParticipant

    @Test(expected = IllegalStateException.class)
    public void testAddParticipantNotLoggedIn() {
        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().addParticipant(getOfferWithId(), getRegisteredUser());
    }

    @Test(expected = NullPointerException.class)
    public void testAddParticipantWithNullOffer() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().addParticipant(null, getRegisteredUser());
    }

    @Test(expected = NullPointerException.class)
    public void testAddParticipantWithNullParticipant() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().addParticipant(getOfferWithId(), null);
    }

    // Test removeParticipant

    @Test(expected = IllegalStateException.class)
    public void testRemoveParticipantNotLoggedIn() {
        // Assertions
        assertNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().removeParticipant(getOfferWithId(), getRegisteredUser());
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveParticipantWithNullOffer() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().removeParticipant(null, getRegisteredUser());
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveParticipantWithNullParticipant() throws RequestHandlerException {
        // Assertions
        Session.getInstance().login(getRegisteredLoginCredential());
        assertNotNull(Session.getInstance().getToken());

        // Execution
        getEntityRepository().removeParticipant(getOfferWithId(), null);
    }
}
