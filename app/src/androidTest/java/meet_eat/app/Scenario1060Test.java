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
import java.time.Month;
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
import meet_eat.data.location.SphericalLocation;
import meet_eat.data.location.SphericalPosition;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class Scenario1060Test {

    private static final String password = "123##Tester";

    private static final long timestamp = System.currentTimeMillis();
    private static final OfferViewModel offerVM = new OfferViewModel();

    private final ScenarioTestHelper scenarioTestHelper = new ScenarioTestHelper(timestamp, password);

    // U: 10 O: 0 B: 8
    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @BeforeClass
    public static void initialize() throws RequestHandlerException {
        User newUser = new User(new Email(timestamp + "@example.com"), Password.createHashedPassword(password),
                LocalDate.of(2000, 1, 1), "Tester", "0123456789", "Test description", true,
                new SphericalLocation(new SphericalPosition(0, 0)));
        new RegisterViewModel().register(newUser);
        addForeignOffer();
    }

    private static void addForeignOffer() throws RequestHandlerException {
        User newUser = new User(new Email(timestamp + 1 + "@example.com"), Password.createHashedPassword(password),
                LocalDate.of(2000, 1, 1), "Tester", "0123456789", "Test description", true,
                new SphericalLocation(new SphericalPosition(0, 0)));
        new RegisterViewModel().register(newUser);

        // Create an offer for this test
        new LoginViewModel().login(timestamp + 1 + "@example.com", password);
        Offer toBeAdded = new Offer(offerVM.getCurrentUser(), new HashSet<>(), "Offer 1", "offerDescription", 0, 1,
                LocalDateTime.of(2030, Month.DECEMBER, 31, 23, 59), new SphericalLocation(new SphericalPosition(6, 6)));
        Offer toBeAdded2 = new Offer(offerVM.getCurrentUser(), new HashSet<>(), "Offer 2", "offerDescription", 0, 1,
                LocalDateTime.of(2030, Month.DECEMBER, 31, 23, 59), new SphericalLocation(new SphericalPosition(6, 6)));
        offerVM.add(toBeAdded);
        offerVM.add(toBeAdded2);
        new SettingsViewModel().logout();
    }

    @AfterClass
    public static void cleanUp() throws RequestHandlerException {
        Intents.release();

        // Remove bookmarks
        for (Offer bookmarkedOffer : offerVM.fetchBookmarkedOffers()) {
            offerVM.removeBookmark(bookmarkedOffer);
        }

        new LoginViewModel().login(timestamp + 1 + "@example.com", password);

        // Remove offers
        for (Offer offer : offerVM.fetchOffers(offerVM.getCurrentUser())) {
            offerVM.delete(offer);
        }

        new SettingsViewModel().deleteUser(new SettingsViewModel().getCurrentUser());
        new LoginViewModel().login(timestamp + "@example.com", password);
        new SettingsViewModel().deleteUser(new SettingsViewModel().getCurrentUser());
    }

    @Test
    public void testScenario1060() {
        Intents.init();
        scenarioTestHelper.login();
        // Bookmark first 2 offers the user sees
        onView(withId(R.id.rvOfferList)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.ibtOfferCardBookmark)));
        onView(withId(R.id.rvOfferList)).perform(
                RecyclerViewActions.actionOnItemAtPosition(1, clickChildViewWithId(R.id.ibtOfferCardBookmark)));

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
     * @param id The button to be clicked
     * @return The viewAction to be performed
     */
    private ViewAction clickChildViewWithId(final int id) {
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
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }
}
