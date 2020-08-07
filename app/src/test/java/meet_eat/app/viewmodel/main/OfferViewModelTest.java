package meet_eat.app.viewmodel.main;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.login.LoginViewModel;
import meet_eat.app.viewmodel.login.RegisterViewModel;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Tag;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;
import meet_eat.data.location.SphericalLocation;
import meet_eat.data.location.SphericalPosition;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class OfferViewModelTest {

    private static final String testEmail = "@example.com";
    private static final String password = "ABcd12§$";
    private static final String username = "Registered User";
    private static final String phoneNumber = "0123456789";
    private static final String profileDescription = "JUnit Test User";
    private static final String title = "Offer";
    private static final String offerDescription = "JUnit Test Offer";

    private static LoginViewModel loginVM;
    private static RegisterViewModel registerVM;
    private static SettingsViewModel settingsVM;
    private static OfferViewModel offerVM;
    private static User registeredUser;
    private static User userWithOffer;
    private static Offer existingOffer;
    private static Offer newOffer;

    @BeforeClass
    public static void initialize() throws RequestHandlerException {
        loginVM = new LoginViewModel();
        registerVM = new RegisterViewModel();
        settingsVM = new SettingsViewModel();
        offerVM = new OfferViewModel();

        String uniqueIdentifier = String.valueOf(System.currentTimeMillis() % 100000);
        registeredUser = new User(new Email(uniqueIdentifier + testEmail), Password.createHashedPassword(password),
                LocalDate.of(2000, 1, 1), username, phoneNumber, profileDescription, true,
                new SphericalLocation(new SphericalPosition(0, 0)));
        registerVM.register(registeredUser);
        loginVM.login(uniqueIdentifier + testEmail, password);

        userWithOffer = new User(new Email(uniqueIdentifier + 1 + testEmail), Password.createHashedPassword(password),
                LocalDate.of(2000, 1, 1), username, phoneNumber, profileDescription, true,
                new SphericalLocation(new SphericalPosition(0, 0)));
        existingOffer = new Offer(offerVM.getCurrentUser(), new HashSet<Tag>(), title, offerDescription, 1.0, 2,
                LocalDateTime.of(2100, 1, 1, 0, 0), new SphericalLocation(new SphericalPosition(0, 0)));
        offerVM.add(existingOffer);

        newOffer = new Offer(userWithOffer, new HashSet<Tag>(), title, offerDescription, 1.0, 2,
                LocalDateTime.of(2100, 1, 1, 0, 0), new SphericalLocation(new SphericalPosition(0, 0)));
    }

    @AfterClass
    public static void cleanUp() throws RequestHandlerException {
        settingsVM.deleteUser(settingsVM.getCurrentUser());
    }

    @Test
    public void testGetCurrentUser() {
        assertNotNull(offerVM.getCurrentUser().getIdentifier());
    }

    @Test
    public void testFetchOffersByCreatorWithValidUser() throws RequestHandlerException {
        offerVM.fetchOffers(offerVM.getCurrentUser());
    }

    @Test(expected = NullPointerException.class)
    public void testFetchOffersByCreatorWithNullUser() throws RequestHandlerException {
        offerVM.fetchOffers((User) null);
    }

    @Test
    public void testFetchOffersWithValidPredicates() throws RequestHandlerException {
        offerVM.fetchOffers(new ArrayList<>());
    }

    @Test(expected = NullPointerException.class)
    public void testFetchOffersWithNullPredicates() throws RequestHandlerException {
        offerVM.fetchOffers((ArrayList) null);
    }

    @Test
    public void testFetchSubscribedOffers() throws RequestHandlerException {
        offerVM.fetchOffersOfSubscriptions();
    }

    @Test
    public void testFetchBookmarkedOffers() throws RequestHandlerException {
        offerVM.fetchBookmarkedOffers();
    }

    @Test
    public void testUpdatePredicatesWithValidPredicates() throws RequestHandlerException {
        offerVM.updatePredicates(new ArrayList<>());
    }

    @Test(expected = NullPointerException.class)
    public void testUpdatePredicatesWithNullPredicates() throws RequestHandlerException {
        offerVM.updatePredicates(null);
    }

    @Ignore("Add not working")
    @Test
    public void testAddEditDeleteWithValidOffer() throws RequestHandlerException {
        offerVM.add(newOffer);
        offerVM.edit(newOffer);
        offerVM.delete(newOffer);
    }

    @Test(expected = NullPointerException.class)
    public void testAddWithNullOffer() throws RequestHandlerException {
        offerVM.add(null);
    }

    @Test(expected = RequestHandlerException.class)
    public void testDeleteWithInvalidOffer() throws RequestHandlerException {
        offerVM.delete(newOffer);
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteWithNullOffer() throws RequestHandlerException {
        offerVM.delete(null);
    }

    @Test(expected = RequestHandlerException.class)
    public void testEditWithInvalidOffer() throws RequestHandlerException {
        offerVM.edit(newOffer);
    }

    @Test(expected = NullPointerException.class)
    public void testEditWithNullOffer() throws RequestHandlerException {
        offerVM.edit(null);
    }

    @Ignore("Recursion error")
    @Test
    public void testParticipateAndCancelParticipationWithValidOffer() throws RequestHandlerException {
        offerVM.participate(existingOffer);
        offerVM.cancelParticipation(offerVM.getCurrentUser(), existingOffer);
    }

    @Ignore("Switching between exceptions?!")
    @Test(expected = RequestHandlerException.class)
    public void testParticipateWithInvalidOffer() throws RequestHandlerException {
        offerVM.participate(newOffer);
    }

    @Test(expected = NullPointerException.class)
    public void testParticipateWithNullOffer() throws RequestHandlerException {
        offerVM.participate(null);
    }

    @Test(expected = RequestHandlerException.class)
    public void testCancelParticipationWithInvalidParticipant() throws RequestHandlerException {
        offerVM.cancelParticipation(offerVM.getCurrentUser(), existingOffer);
    }

    @Test(expected = NullPointerException.class)
    public void testCancelParticipationWithNullParticipant() throws RequestHandlerException {
        offerVM.cancelParticipation(null, existingOffer);
    }

    @Test(expected = RequestHandlerException.class)
    public void testCancelParticipationWithInvalidOffer() throws RequestHandlerException {
        offerVM.cancelParticipation(offerVM.getCurrentUser(), newOffer);
    }

    @Test(expected = NullPointerException.class)
    public void testCancelParticipationWithNullOffer() throws RequestHandlerException {
        offerVM.cancelParticipation(offerVM.getCurrentUser(), null);
    }

    @Ignore("Can't bookmark own offer")
    @Test
    public void testAddAndRemoveBookmarkIsBookmarkedWithValidOffer() throws RequestHandlerException {
        offerVM.addBookmark(existingOffer);
        assertTrue(offerVM.isBookmarked(existingOffer));
        offerVM.removeBookmark(existingOffer);
    }

    @Test(expected = RequestHandlerException.class)
    public void testAddBookmarkWithInvalidOffer() throws RequestHandlerException {
        offerVM.addBookmark(newOffer);
    }

    @Test(expected = NullPointerException.class)
    public void testAddBookmarkWithNullOffer() throws RequestHandlerException {
        offerVM.addBookmark(null);
    }

    @Ignore("Should throw exception")
    @Test(expected = RequestHandlerException.class)
    public void testRemoveBookmarkWithInvalidOffer() throws RequestHandlerException {
        offerVM.removeBookmark(newOffer);
    }

    @Ignore("Should throw exception")
    @Test(expected = NullPointerException.class)
    public void testRemoveBookmarkWithNullOffer() throws RequestHandlerException {
        offerVM.removeBookmark(null);
    }

    @Test
    public void testIsBookmarkedWithInvalidOffer() {
        assertFalse(offerVM.isBookmarked(newOffer));
    }

    @Ignore("Should throw exception")
    @Test(expected = NullPointerException.class)
    public void testIsBookmarkedWithNullOffer() {
        offerVM.isBookmarked(null);
    }
}