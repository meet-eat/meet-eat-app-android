package meet_eat.app;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.google.common.collect.Lists;

import org.junit.AfterClass;
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
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class Scenario1050Test {

    private static final OfferViewModel offerVM = new OfferViewModel();
    private static final SettingsViewModel settingsVM = new SettingsViewModel();
    private static final LoginViewModel loginVM = new LoginViewModel();
    private static final RegisterViewModel registerVM = new RegisterViewModel();
    private static final String password = "123##Tester";
    private static final long timestamp = System.currentTimeMillis();

    private final ScenarioTestHelper scenarioTestHelper = new ScenarioTestHelper(timestamp, password);
    private static final Localizable home = new SphericalLocation(new SphericalPosition(49.0082285, 8.3978892));

    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @BeforeClass
    public static void initialize() throws RequestHandlerException {
        User newUser = new User(new Email(timestamp + "@example.com"), Password.createHashedPassword(password),
                LocalDate.of(2000, 1, 1), "Tester", "0123456789", "Test description", true, home);
        registerVM.register(newUser);
        addForeignOffer();
    }

    private static void addForeignOffer() throws RequestHandlerException {
        User newUser = new User(new Email(timestamp + 1 + "@example.com"), Password.createHashedPassword(password),
                LocalDate.of(2000, 1, 1), "Tester", "0123456789", "Test description", true, home);
        registerVM.register(newUser);

        // Create an offer for this test
        loginVM.login(timestamp + 1 + "@example.com", password);
        Offer toBeAdded = new Offer(offerVM.getCurrentUser(), new HashSet<>(), "Offer", "offerDescription", 1, 2,
                LocalDateTime.of(2030, Month.DECEMBER, 31, 23, 59), home);
        offerVM.add(toBeAdded);
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

    @Test
    public void testScenario1050() {
        Intents.init();
        scenarioTestHelper.login();
        // Select first offer, mustn't be own (user has never created an offer, so shouldn't be problem)
        // Open offer detailed view
        onView(withId(R.id.rvOfferList)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.tvOfferDetailedUsername)).perform(click());
        //Subscribe to user
        onView(withId(R.id.btProfileSubscribe)).perform(click());
    }
}