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

import com.google.common.collect.Lists;

import org.hamcrest.Matcher;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
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
public class ProfileSubscribedFragmentTest {
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
    public ActivityTestRule<LoginActivity> activityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @BeforeClass
    public static void initialize() throws RequestHandlerException {
        User newUser = new User(new Email(timestamp + "@example.com"), Password.createHashedPassword(password),
                LocalDate.of(2000, 1, 1), "Tester", "0123456789", "Test description", true, home);
        registerVM.register(newUser);
        addToBeSubscribedWithOffer();
        Intents.init();
    }

    private static void addToBeSubscribedWithOffer() throws RequestHandlerException {
        User toBeSubscribedUser =
                new User(new Email(timestamp + 1 + "@example.com"), Password.createHashedPassword(password),
                        LocalDate.of(2000, 1, 1), "Tester", "0123456789", "Test description", true, home);
        registerVM.register(toBeSubscribedUser);

        // Create an offer for this test
        loginVM.login(timestamp + 1 + "@example.com", password);
        Offer toBeSubscribedUsersOffer =
                new Offer(offerVM.getCurrentUser(), new HashSet<>(), "Offer", "offerDescription", 1, 2,
                        LocalDateTime.of(2030, Month.DECEMBER, 31, 23, 59), home);
        offerVM.add(toBeSubscribedUsersOffer);
        settingsVM.logout();
    }

    @AfterClass
    public static void cleanUp() throws RequestHandlerException {
        Intents.release();
        settingsVM.deleteUser();
        loginVM.login(timestamp + 1 + "@example.com", password);

        // Remove offers
        ArrayList<Offer> toBeRemoved = Lists.newArrayList(offerVM.fetchOffers(offerVM.getCurrentUser()));
        if (!toBeRemoved.isEmpty()) {
            offerVM.delete(toBeRemoved.remove(0));
        }

        settingsVM.deleteUser();
    }

    @Before
    public void login() {
        if(!isLoggedIn) {
            scenarioTestHelper.login();
            isLoggedIn = true;
        }
    }

    @Test
    public void subsribeAndUnsubscribeTest() {
        // Select first offer, mustn't be own (user has never created an offer, so shouldn't be problem)
        // Open offer detailed view
        onView(withId(R.id.rvOfferList)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.tvOfferDetailedUsername)).perform(click());

        // Subscribe to user
        onView(withId(R.id.btProfileSubscribe)).perform(click());

        // Unsubscribe the user
        onView(withId(R.id.btProfileSubscribe)).perform(click());

        // Subscribe back to user for next test
        onView(withId(R.id.btProfileSubscribe)).perform(click());
    }

    @Test
    public void openSubscriptionInProfileSubscribedFragmentTest() {
        // Navigate to subscribed offers list
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.drawer_menu_subscriptions));

        // wait, so espresso doesn't cause an error
        SystemClock.sleep(500);

        // Navigate to subscription profile
        onView(withId(R.id.btOfferListSubscribed)).perform(click());
        onView(withId(R.id.rvProfileSubscriptions))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewUsername()));
    }

    @Test
    public void removeSubscriptionInProfileSubscribedFragmentTest() {
        // Navigate to subscribed offers list
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.drawer_menu_subscriptions));

        // wait, so espresso doesn't cause an error
        SystemClock.sleep(500);

        // Remove subscription
        onView(withId(R.id.btOfferListSubscribed)).perform(click());
        onView(withId(R.id.rvProfileSubscriptions))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewRemove()));
    }

    private ViewAction clickChildViewUsername() {
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
                View v = view.findViewById(R.id.tvOfferSubscriptionUsername);
                v.performClick();
            }
        };
    }

    private ViewAction clickChildViewRemove() {
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
                View v = view.findViewById(R.id.ibtOfferSubscriptionRemove);
                v.performClick();
            }
        };
    }
}