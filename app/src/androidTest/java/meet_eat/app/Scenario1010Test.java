package meet_eat.app;

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
import java.time.Month;
import java.util.Collection;
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

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class Scenario1010Test {
    private static OfferViewModel offerVM = new OfferViewModel();

    static long timestamp = System.currentTimeMillis();
    private static final String password = "123##Tester";
    private ScenarioTestHelper scenarioTestHelper = new ScenarioTestHelper(timestamp, password);

    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @BeforeClass
    public static void initialize() throws RequestHandlerException {
        User newUser = new User(new Email(timestamp + "@example.com"), Password.createHashedPassword(password),
                LocalDate.of(2000, 1, 1), "Tester", "0123456789", "Test description", true,
                new SphericalLocation(new SphericalPosition(0, 0)));
        new RegisterViewModel().register(newUser);

        // Create an offer for this test
        new LoginViewModel().login(timestamp + "@example.com", password);
        Offer toBeAdded = new Offer(offerVM.getCurrentUser(), new HashSet<>(), "Offer", "offerDescription", 0, 1,
                LocalDateTime.of(2030, Month.DECEMBER, 31, 23, 59), new SphericalLocation(new SphericalPosition(6, 6)));
        assertEquals(0, ((Collection<Offer>) offerVM.fetchOffers(offerVM.getCurrentUser())).size());
        offerVM.add(toBeAdded);
        new SettingsViewModel().logout();
    }

    @AfterClass
    public static void cleanUp() throws RequestHandlerException {
        Intents.release();
        offerVM.delete(offerVM.fetchOffers(offerVM.getCurrentUser()).iterator().next());
        new SettingsViewModel().deleteUser(new SettingsViewModel().getCurrentUser());
    }

    @Test
    public void testScenario1010() {
        Intents.init();
        scenarioTestHelper.login();
        onView(withId(R.id.ibtOfferListFilter)).perform(click());
        // Tags are not yet implemented, leaving [TA 3020] out of this test
        // [TA 3000], [TA 3030]
        onView(withId(R.id.sOfferFilterSort)).perform(click());
        // For other language, change magic number (english: Distance)
        onData(allOf(is(instanceOf(String.class)), is("Entfernung"))).perform(click());
        onView(withId(R.id.btOfferFilterSave)).perform(click());
        // [TA 1010] omitted, as it is tested in UnitTests
    }
}
