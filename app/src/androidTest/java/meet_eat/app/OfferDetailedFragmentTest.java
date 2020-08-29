package meet_eat.app;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class OfferDetailedFragmentTest {

    private static final OfferViewModel offerVM = new OfferViewModel();
    private static final SettingsViewModel settingsVM = new SettingsViewModel();
    private static final LoginViewModel loginVM = new LoginViewModel();
    private static final RegisterViewModel registerVM = new RegisterViewModel();
    private static final String password = "123##Tester";
    private static final long timestamp = System.currentTimeMillis();

    private final ScenarioTestHelper scenarioTestHelper = new ScenarioTestHelper(timestamp, password);

    @Rule
    public ActivityTestRule<SplashActivity> activityTestRule = new ActivityTestRule<>(SplashActivity.class);

    @BeforeClass
    public static void initialize() throws RequestHandlerException {
        Localizable home = new SphericalLocation(new SphericalPosition(49.0082285, 8.3978892));
        User newUser = new User(new Email(timestamp + "@example.com"), Password.createHashedPassword(password),
                LocalDate.of(2000, 1, 1), "Tester", "0123456789", "Test description", true, home);
        registerVM.register(newUser);
        addForeignOffer();
        Intents.init();
    }

    private static void addForeignOffer() throws RequestHandlerException {
        Localizable home = new SphericalLocation(new SphericalPosition(49.0082285, 8.3978892));
        User newUser = new User(new Email(timestamp + 1 + "@example.com"), Password.createHashedPassword(password),
                LocalDate.of(2000, 1, 1), "Tester", "0123456789", "Test description", true, home);
        registerVM.register(newUser);

        // Create an offer for this test
        loginVM.login(timestamp + 1 + "@example.com", password);
        Offer toBeAdded = new Offer(offerVM.getCurrentUser(), new HashSet<>(), "1Offer", "offerDescription", 0, 2,
                LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth(),
                        LocalTime.now().getHour(), (LocalTime.now().getMinute() + 2) % 60), home);
        offerVM.add(toBeAdded);
        settingsVM.logout();
    }

    @AfterClass
    public static void cleanUp() throws RequestHandlerException {
        Intents.release();
        settingsVM.deleteUser();
        loginVM.login(timestamp + 1 + "@example.com", password);
        settingsVM.deleteUser();
    }

    @Test
    public void offerDetailedFragmentTest() {
        scenarioTestHelper.login();
        onView(withId(R.id.rvOfferList)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Test participate
        onView(withId(R.id.btOfferDetailedParticipate)).perform(click());
        onView(withId(R.id.btOfferDetailedParticipate)).perform(click());

        // Test bookmark
        onView(withId(R.id.ibtOfferDetailedBookmark)).perform(click());
        onView(withId(R.id.ibtOfferDetailedBookmark)).perform(click());

        // Test profile
        onView(withId(R.id.ivOfferDetailedProfile)).perform(click());
        onView(withId(R.id.ibtBack)).perform(click());
        onView(withId(R.id.tvOfferDetailedUsername)).perform(click());
        onView(withId(R.id.ibtBack)).perform(click());

        // Test reload
        onView(withId(R.id.srlOfferDetailedSwipe)).perform(swipeDown());

        // Test back
        onView(withId(R.id.ibtBack)).perform(click());
    }
}