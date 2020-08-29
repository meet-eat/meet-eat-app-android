package meet_eat.app;

import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matcher;
import org.junit.AfterClass;
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
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class Scenario1060Test {

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
        User newUser = new User(new Email(timestamp + "@example.com"), Password.createHashedPassword(password),
                LocalDate.of(2000, 1, 1), "Tester", "0123456789", "Test description", true,
                new SphericalLocation(new SphericalPosition(0, 0)));
        registerVM.register(newUser);
        addForeignOffer();
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
        Offer toBeAdded2 = new Offer(offerVM.getCurrentUser(), new HashSet<>(), "1Offer", "offerDescription", 0, 2,
                LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth(),
                        LocalTime.now().getHour(), (LocalTime.now().getMinute() + 2) % 60), home);
        offerVM.add(toBeAdded);
        offerVM.add(toBeAdded2);
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
    public void testScenario1060() {
        Intents.init();
        scenarioTestHelper.login();
        // Bookmark first 2 offers the user sees
        onView(withId(R.id.rvOfferList)).perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId()));
        onView(withId(R.id.rvOfferList)).perform(RecyclerViewActions.actionOnItemAtPosition(1, clickChildViewWithId()));
        // Remove and add bookmark for extra coverage
        onView(withId(R.id.rvOfferList)).perform(RecyclerViewActions.actionOnItemAtPosition(1, clickChildViewWithId()));
        onView(withId(R.id.rvOfferList)).perform(RecyclerViewActions.actionOnItemAtPosition(1, clickChildViewWithId()));

        // Open navigation drawer
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());
        // Navigate to bookmarked offers
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.drawer_menu_bookmarked));
        // wait, so espresso doesn't cause an error
        SystemClock.sleep(500);

        // Chooses first offer
        onView(withId(R.id.rvOfferList)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.btOfferDetailedParticipate)).perform(click());
    }

    /**
     * Returns viewAction, meaning an action to click a button within an item within our recycler view.
     *
     * @return The viewAction to be performed
     */
    private ViewAction clickChildViewWithId() {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(R.id.ibtOfferCardBookmark);
                v.performClick();
            }
        };
    }
}
