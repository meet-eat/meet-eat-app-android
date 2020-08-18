package meet_eat.app;

import android.os.SystemClock;
import android.view.Gravity;

import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;

import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.login.RegisterViewModel;
import meet_eat.app.viewmodel.main.SettingsViewModel;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;
import meet_eat.data.location.SphericalLocation;
import meet_eat.data.location.SphericalPosition;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class Scenario1030Test {
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
    }

    @AfterClass
    public static void cleanUp() throws RequestHandlerException {
        Intents.release();
        new SettingsViewModel().deleteUser(new SettingsViewModel().getCurrentUser());
    }

    @Test
    public void testScenario1030() {
        Intents.init();
        scenarioTestHelper.login();
        // [TA 3000]
        // Open navigation drawer
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());
        // Navigate to settings
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.drawer_menu_settings));
        // wait, so espresso doesn't cause an error
        SystemClock.sleep(500);
        onView(withId(R.id.btSettingsDelete)).perform(click());
        onView(withId(R.id.etSettingsDeleteProfilePassword)).perform(typeText(password));
        closeSoftKeyboard();
        onView(withId(R.id.btSettingsDeleteProfile)).perform(click());
        // Check if logged out
        intended(hasComponent(LoginActivity.class.getName()));
    }
}
