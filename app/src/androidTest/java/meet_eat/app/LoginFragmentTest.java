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
public class LoginFragmentTest {

    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @BeforeClass
    public static void initialize() {
        Intents.init();
    }

    @AfterClass
    public static void cleanUp() {
        Intents.release();
    }

    @Test
    public void resetPasswordWithInvalidEmail() {
        onView(withId(R.id.etLoginEmail)).perform(typeText("a"));
        closeSoftKeyboard();
        onView(withId(R.id.tvLoginReset)).perform(click());
    }

    @Test
    public void resetPasswordWithValidEmail() {
        onView(withId(R.id.etLoginEmail)).perform(typeText("resetpasswordtest@example.com"));
        closeSoftKeyboard();
        onView(withId(R.id.tvLoginReset)).perform(click());
    }

    @Test
    public void loginWithInvaldEmail() {
        onView(withId(R.id.etLoginEmail)).perform(typeText("a"));
        closeSoftKeyboard();
        onView(withId(R.id.etLoginPassword)).perform(typeText("123##Test"));
        closeSoftKeyboard();
        onView(withId(R.id.btLogin)).perform(click());
    }

    @Test
    public void loginWithInvaldiPassword() {
        onView(withId(R.id.etLoginEmail)).perform(typeText("invalidpassword@example.com"));
        closeSoftKeyboard();
        onView(withId(R.id.etLoginPassword)).perform(typeText("a"));
        closeSoftKeyboard();
        onView(withId(R.id.btLogin)).perform(click());
    }

    @Test
    public void loginWithUnregisteredEmail() {
        onView(withId(R.id.etLoginEmail)).perform(typeText("unregistered@example.com"));
        closeSoftKeyboard();
        onView(withId(R.id.etLoginPassword)).perform(typeText("123##Test"));
        closeSoftKeyboard();
        onView(withId(R.id.btLogin)).perform(click());
    }
}