package meet_eat.app;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;

import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.login.LoginViewModel;
import meet_eat.app.viewmodel.login.RegisterViewModel;
import meet_eat.app.viewmodel.main.OfferViewModel;
import meet_eat.app.viewmodel.main.SettingsViewModel;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;
import meet_eat.data.location.Localizable;
import meet_eat.data.location.SphericalLocation;
import meet_eat.data.location.SphericalPosition;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class OfferListFragmentTest {
    private static final LoginViewModel loginVM = new LoginViewModel();
    private static final RegisterViewModel registerVM = new RegisterViewModel();
    private static final OfferViewModel offerVM = new OfferViewModel();
    private static final SettingsViewModel settingsVM = new SettingsViewModel();
    private static final String password = "123##Test";
    private static final long timestamp = System.currentTimeMillis();

    private final ScenarioTestHelper scenarioTestHelper = new ScenarioTestHelper(timestamp, password);
    private static final Localizable home = new SphericalLocation(new SphericalPosition(49.0082285, 8.3978892));
    private static boolean isLoggedIn = false;

    @Rule
    public ActivityTestRule<SplashActivity> activityTestRule = new ActivityTestRule<>(SplashActivity.class);

    @BeforeClass
    public static void initialize() throws RequestHandlerException {
        User newUser = new User(new Email(timestamp + "@example.com"), Password.createHashedPassword(password),
                LocalDate.of(2000, 1, 1), "Tester", "0123456789", "Test description", true, home);
        registerVM.register(newUser);
        addOffers();
        Intents.init();
    }

    @AfterClass
    public static void cleanUp() throws RequestHandlerException {
        Intents.release();
        settingsVM.deleteUser();
        loginVM.login(timestamp + 1 + "@example.com", password);
        settingsVM.deleteUser();
    }

    @Before
    public void login() {
        if (!isLoggedIn) {
            scenarioTestHelper.login();
            isLoggedIn = true;
        }
    }

    private static void addOffers() throws RequestHandlerException {
        Localizable home = new SphericalLocation(new SphericalPosition(49.0082285, 8.3978892));
        User newUser = new User(new Email(timestamp + 1 + "@example.com"), Password.createHashedPassword(password),
                LocalDate.of(2000, 1, 1), "Tester", "0123456789", "Test description", true, home);
        registerVM.register(newUser);

        // Create an foreign offer
        loginVM.login(timestamp + 1 + "@example.com", password);
        Offer foreignOffer =
                new Offer(offerVM.getCurrentUser(), new HashSet<>(), "Foreign Offer", "offerDescription", 0, 2,
                        LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth(),
                                LocalDate.now().getDayOfMonth(), LocalTime.now().getHour(),
                                (LocalTime.now().getMinute() + 2) % 60), home);
        offerVM.add(foreignOffer);

        // Create an bookmarked offer
        Offer bookmarkedOffer =
                new Offer(offerVM.getCurrentUser(), new HashSet<>(), "Bookmarked Offer", "offerDescription", 0, 2,
                        LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth(),
                                LocalDate.now().getDayOfMonth(), LocalTime.now().getHour(),
                                (LocalTime.now().getMinute() + 2) % 60), home);
        offerVM.add(bookmarkedOffer);

        // Create an participating offer
        Offer participatingOffer =
                new Offer(offerVM.getCurrentUser(), new HashSet<>(), "Participating Offer", "offerDescription", 0, 2,
                        LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth(),
                                LocalDate.now().getDayOfMonth(), LocalTime.now().getHour(),
                                (LocalTime.now().getMinute() + 2) % 60), home);
        offerVM.add(participatingOffer);

        // Get offer IDs
        String bookmarkedOfferID = bookmarkedOffer.getIdentifier();
        String participatingOfferID = participatingOffer.getIdentifier();
        for (Offer offer : offerVM.fetchOffers(offerVM.getCurrentUser())) {
            if (offer.getName().equals(bookmarkedOffer.getName())) {
                bookmarkedOfferID = offer.getIdentifier();
            } else if (offer.getName().equals(participatingOffer.getName())) {
                participatingOfferID = offer.getIdentifier();
            }
        }
        settingsVM.logout();

        // Create own offer
        loginVM.login(timestamp + "@example.com", password);
        Offer ownOffer = new Offer(offerVM.getCurrentUser(), new HashSet<>(), "1Offer", "offerDescription", 0, 2,
                LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth(),
                        LocalTime.now().getHour(), (LocalTime.now().getMinute() + 2) % 60), home);
        offerVM.add(ownOffer);

        // Add bookmark
        offerVM.addBookmark(offerVM.fetchOfferById(bookmarkedOfferID));

        // Add participation to offer
        offerVM.participate(offerVM.fetchOfferById(participatingOfferID));
        settingsVM.logout();
    }

    @Test
    public void fetchOffersTest() {
        onView(withId(R.id.rvOfferList)).perform(swipeUp());
        onView(withId(R.id.rvOfferList)).perform(swipeDown());
        onView(withId(R.id.rvOfferList)).perform(swipeDown());
    }
}