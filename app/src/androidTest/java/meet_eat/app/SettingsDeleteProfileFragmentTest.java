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
import org.springframework.core.annotation.Order;

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
import meet_eat.app.viewmodel.main.UserViewModel;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;
import meet_eat.data.location.Localizable;
import meet_eat.data.location.SphericalLocation;
import meet_eat.data.location.SphericalPosition;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class SettingsDeleteProfileFragmentTest {
    private static final RegisterViewModel registerVM = new RegisterViewModel();
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
        Intents.init();
    }

    @AfterClass
    public static void cleanUp() throws RequestHandlerException {
        Intents.release();
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
    public void deleteWithInvalidPasswordTest() {
        // Navigate to settings
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.drawer_menu_settings));

        // Wait, so espresso doesn't cause an error
        SystemClock.sleep(500);

        // Delete Profile
        onView(withId(R.id.btSettingsDelete)).perform(click());
        onView(withId(R.id.etSettingsDeleteProfilePassword)).perform(typeText("a"));
        closeSoftKeyboard();
        onView(withId(R.id.btSettingsDeleteProfile)).perform(click());
    }

    @Test
    public void deleteWithWrongPasswordTest() {
        // Navigate to settings
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.drawer_menu_settings));

        // Wait, so espresso doesn't cause an error
        SystemClock.sleep(500);

        // Delete Profile
        onView(withId(R.id.btSettingsDelete)).perform(click());
        onView(withId(R.id.etSettingsDeleteProfilePassword)).perform(typeText("123##TesterWrong"));
        closeSoftKeyboard();
        onView(withId(R.id.btSettingsDeleteProfile)).perform(click());
    }
}