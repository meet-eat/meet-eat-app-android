package meet_eat.app.viewmodel.main;

import com.google.common.collect.Iterables;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.login.LoginViewModel;
import meet_eat.app.viewmodel.login.RegisterViewModel;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.relation.Report;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;
import meet_eat.data.entity.user.contact.ContactData;
import meet_eat.data.entity.user.contact.ContactRequest;
import meet_eat.data.location.Localizable;
import meet_eat.data.location.SphericalLocation;
import meet_eat.data.location.SphericalPosition;
import meet_eat.data.location.UnlocalizableException;
import meet_eat.data.predicate.OfferPredicate;
import meet_eat.data.predicate.string.NamePredicate;
import meet_eat.data.predicate.string.StringOperation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class OfferViewModelTest {

    private static final String testEmail = "@example.com";
    private static final String password = "ABcd12§$";
    private static final String username = "Greetings from OfferViewModelTest!";
    private static final String phoneNumber = "0";
    private static final String profileDescription = "JUnit Test User. Greetings from OfferViewModelTest!";
    private static final SphericalPosition position = new SphericalPosition(48.9969871, 11.2092129);
    private static final Localizable location = new SphericalLocation(position);
    private static final String offerTitle = "Greetings from OfferViewModelTest!";
    private static final String offerDescription = "JUnit Test Offer. Greetings from OfferViewModelTest!";

    private static LoginViewModel loginVM;
    private static SettingsViewModel settingsVM;
    private static OfferViewModel offerVM;
    private static User secondUser;
    private static String uniqueIdentifier;
    private static final Collection<OfferPredicate> predicates = new ArrayList<>();

    @BeforeClass
    public static void initialize() throws RequestHandlerException {
        loginVM = new LoginViewModel();
        RegisterViewModel registerVM = new RegisterViewModel();
        settingsVM = new SettingsViewModel();
        offerVM = new OfferViewModel();
        uniqueIdentifier = String.valueOf(System.currentTimeMillis());

        // Register contactRequestUser
        secondUser = new User(new Email(uniqueIdentifier + 1 + testEmail), Password.createHashedPassword(password),
                LocalDate.of(2000, 1, 1), username, phoneNumber, profileDescription, true, location);
        registerVM.register(secondUser);
        System.out.println("Created " + secondUser.getEmail());
        // Register registeredUser:
        User registeredUser = new User(new Email(uniqueIdentifier + testEmail), Password.createHashedPassword(password),
                LocalDate.of(2000, 1, 1), username, phoneNumber, profileDescription, true, location);
        registerVM.register(registeredUser);
        System.out.println("Created " + registeredUser.getEmail());

        loginVM.login(uniqueIdentifier + testEmail, password);
        System.out.println("Logged in " + registeredUser.getEmail());
    }

    @AfterClass
    public static void cleanUp() throws RequestHandlerException {
        settingsVM.deleteUser();
        loginVM.login(uniqueIdentifier + 1 + testEmail, password);
        settingsVM.deleteUser();
        uniqueIdentifier = "";
    }

    @Test
    public void testGetCurrentUser() {
        assertNotNull(offerVM.getCurrentUser());
    }

    @Test
    public void testFetchOffersWithCurrentUser() throws RequestHandlerException {
        offerVM.fetchOffers(offerVM.getCurrentUser());
    }

    @Test(expected = NullPointerException.class)
    public void testFetchOffersWithNullUser() throws RequestHandlerException {
        offerVM.fetchOffers((User) null);
    }

    @Test
    public void testFetchOffersWithPredicates() throws RequestHandlerException {
        NamePredicate newPredicate = new NamePredicate(StringOperation.CONTAIN, username);
        predicates.add(newPredicate);
        offerVM.fetchOffers(predicates);
        predicates.remove(newPredicate);
    }

    @Test
    public void testFetchBookmarkedOffers() throws RequestHandlerException {
        offerVM.fetchBookmarkedOffers();
    }

    @Test
    public void testFetchOffersOfSubscriptions() throws RequestHandlerException {
        offerVM.fetchOffersOfSubscriptions();
    }

    @Test
    public void testUpdatePredicates() throws RequestHandlerException {
        NamePredicate newPredicate = new NamePredicate(StringOperation.CONTAIN, username);
        int amountPredicates = offerVM.getCurrentUser().getOfferPredicates().size();
        predicates.add(newPredicate);
        User user = offerVM.updatePredicates(predicates);
        predicates.remove(newPredicate);
        // logout and login to ensure the predicates have been updated
        settingsVM.logout();
        loginVM.login(uniqueIdentifier + testEmail, password);
        assertEquals(++amountPredicates, user.getOfferPredicates().size());
    }

    @Test(expected = NullPointerException.class)
    public void testUpdatePredicatesWithNull() throws RequestHandlerException {
        offerVM.updatePredicates(null);
    }

    @Test
    public void testAddEditDeleteOffer() throws RequestHandlerException, UnlocalizableException {
        // ~~~ ADDING ~~~
        Offer toBeAdded = new Offer(offerVM.getCurrentUser(), new HashSet<>(), offerTitle, offerDescription, 0, 1,
                LocalDateTime.of(2030, Month.DECEMBER, 31, 23, 59), location);
        assertEquals(0, (Iterables.size(offerVM.fetchOffers(offerVM.getCurrentUser()))));
        offerVM.add(toBeAdded);
        // logout and login to ensure the offer was added
        logoutThenLogin(uniqueIdentifier + testEmail);
        // no other offer has been added, so total must be 1
        offerVM.getCurrentUser().clearOfferPredicates();
        assertEquals(1, (Iterables.size(offerVM.fetchOffers(offerVM.getCurrentUser()))));

        // ~~~ EDITING ~~~
        Localizable editedLocation = new SphericalLocation(
                new SphericalPosition(location.getSphericalPosition().getLatitude() + 1,
                        location.getSphericalPosition().getLongitude() + 1));
        final String editedDescription = "Hello JUnit test!";
        Offer editedOffer = offerVM.fetchOffers(offerVM.getCurrentUser()).iterator().next();
        // Should not throw UnlocalizableException, have defined it before
        editedOffer.setLocation(editedLocation);
        editedOffer.setDescription(editedDescription);

        offerVM.edit(editedOffer);
        // logout and login to ensure the offer was removed
        logoutThenLogin(uniqueIdentifier + testEmail);
        Offer checkOffer = offerVM.fetchOffers(offerVM.getCurrentUser()).iterator().next();
        assertEquals(checkOffer.getLocation(), editedLocation);
        assertEquals(checkOffer.getDescription(), editedDescription);

        // ~~ DELETING ~~~
        Offer offer = offerVM.fetchOffers(offerVM.getCurrentUser()).iterator().next();
        offerVM.delete(offer);
        // logout and login to ensure the offer was removed
        logoutThenLogin(uniqueIdentifier + testEmail);
        assertEquals(0, (Iterables.size(offerVM.fetchOffers(offerVM.getCurrentUser()))));
    }

    @Test
    public void testParticipateThenCancelParticipation() throws RequestHandlerException {
        // ~~~ ADDING AN OFFER (see testAddEditDeleteOffer) ~~~
        Offer offerToAdd = new Offer(offerVM.getCurrentUser(), new HashSet<>(), offerTitle, offerDescription, 0, 1,
                LocalDateTime.of(2030, Month.DECEMBER, 31, 23, 59), location);
        offerVM.add(offerToAdd);
        logoutThenLogin(uniqueIdentifier + testEmail);
        Offer toBeParticipating = offerVM.fetchOffers(offerVM.getCurrentUser()).iterator().next();
        logoutThenLogin(uniqueIdentifier + 1 + testEmail);
        User participatingUser = offerVM.getCurrentUser();
        offerVM.getCurrentUser().clearOfferPredicates();

        // ~~~ PARTICIPATE ~~~
        offerVM.participate(toBeParticipating);
        logoutThenLogin(uniqueIdentifier + testEmail);
        Offer offerWithParticipant = offerVM.fetchOffers(offerVM.getCurrentUser()).iterator().next();
        assertTrue(offerVM.getParticipants(offerWithParticipant).contains(participatingUser));

        // ~~~ CANCELLING PARTICIPATION ~~~
        logoutThenLogin(uniqueIdentifier + 1 + testEmail);
        offerVM.cancelParticipation(offerVM.getCurrentUser(), offerWithParticipant);
        logoutThenLogin(uniqueIdentifier + testEmail);
        Offer offerWithCancelledParticipation = offerVM.fetchOffers(offerVM.getCurrentUser()).iterator().next();
        assertFalse(offerVM.getParticipants(offerWithCancelledParticipation).contains(participatingUser));

        // ~~~ DELETING ~~~
        Offer offer = offerVM.fetchOffers(offerVM.getCurrentUser()).iterator().next();
        offerVM.delete(offer);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRequestContact() {
        ContactRequest contactRequest = new ContactRequest(offerVM.getCurrentUser(), secondUser);
        offerVM.requestContact(contactRequest);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSendContact() {
        ContactRequest contactRequest = new ContactRequest(offerVM.getCurrentUser(), secondUser);
        ContactData contactData = new ContactData(contactRequest);
        offerVM.sendContact(contactData);
    }

    @Ignore("Asserting that the report was sent is not possible anymore.")
    @Test(expected = RequestHandlerException.class)
    public void testReport() throws RequestHandlerException {
        // Not yet implemented
        String reportMessage = "This is a report message";
        Report report = new Report(offerVM.getCurrentUser(), secondUser, reportMessage);
        offerVM.report(report);
        // Logout and login to get current user's reports
        settingsVM.logout();
        loginVM.login(uniqueIdentifier + 1 + testEmail, password);
        // log standard user in again
        logoutThenLogin(uniqueIdentifier + testEmail);
    }

    @Test
    public void testAddRemoveBookmarks() throws RequestHandlerException {
        // ~~~ CREATE OFFER ~~~
        Offer toBeBookmarked = new Offer(offerVM.getCurrentUser(), new HashSet<>(), offerTitle, offerDescription, 0, 1,
                LocalDateTime.of(2030, Month.DECEMBER, 31, 23, 59), location);
        offerVM.add(toBeBookmarked);
        logoutThenLogin(uniqueIdentifier + testEmail);
        Offer addedOffer = offerVM.fetchOffers(offerVM.getCurrentUser()).iterator().next();

        // ~~~ ADD BOOKMARK ~~~
        logoutThenLogin(uniqueIdentifier + 1 + testEmail);
        offerVM.addBookmark(addedOffer);
        logoutThenLogin(uniqueIdentifier + 1 + testEmail);
        assertTrue((Iterables.size(offerVM.fetchBookmarkedOffers())) > 0);

        // ~~~ REMOVE BOOKMARK ~~~
        Offer bookmarkedOffer = (offerVM.fetchBookmarkedOffers()).iterator().next();
        offerVM.removeBookmark(bookmarkedOffer);
        logoutThenLogin(uniqueIdentifier + 1 + testEmail);

        assertEquals(0, (Iterables.size(offerVM.fetchBookmarkedOffers())));
        logoutThenLogin(uniqueIdentifier + testEmail);
    }

    @Test
    public void testIsParticipating() throws RequestHandlerException {
        // ~~~ CREATE OFFER ~~~
        Offer toBeBookmarked = new Offer(offerVM.getCurrentUser(), new HashSet<>(), offerTitle, offerDescription, 0, 1,
                LocalDateTime.of(2030, Month.DECEMBER, 31, 23, 59), location);
        offerVM.add(toBeBookmarked);
        logoutThenLogin(uniqueIdentifier + testEmail);
        Offer addedOffer = offerVM.fetchOffers(offerVM.getCurrentUser()).iterator().next();

        // ~~~ CHECK IF BOOKMARKED ~~~
        assertFalse(offerVM.isParticipating(addedOffer));
    }

    @Test
    public void testIsCreator() throws RequestHandlerException {
        // ~~~ CREATE OFFER ~~~
        Offer toBeBookmarked = new Offer(offerVM.getCurrentUser(), new HashSet<>(), offerTitle, offerDescription, 0, 1,
                LocalDateTime.of(2030, Month.DECEMBER, 31, 23, 59), location);
        offerVM.add(toBeBookmarked);
        logoutThenLogin(uniqueIdentifier + testEmail);
        Offer addedOffer = offerVM.fetchOffers(offerVM.getCurrentUser()).iterator().next();

        // ~~~ CHECK IF OWNER ~~~
        assertTrue(offerVM.isCreator(addedOffer));
    }

    @Test
    public void testGetAllTags() throws RequestHandlerException {
        offerVM.getAllTags().forEach(tag -> System.out.println(tag.getName()));
    }

    private void logoutThenLogin(String email) throws RequestHandlerException {
        settingsVM.logout();
        loginVM.login(email, password);
    }
}