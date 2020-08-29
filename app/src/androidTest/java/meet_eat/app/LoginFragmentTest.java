package meet_eat.app;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class LoginFragmentTest {

    @Rule
    public ActivityTestRule<SplashActivity> activityTestRule = new ActivityTestRule<>(SplashActivity.class);

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
        onView(withText(R.string.bad_email)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    @Test
    public void resetPasswordWithValidEmail() {
        onView(withId(R.id.etLoginEmail)).perform(typeText("resetpasswordtest@example.com"));
        closeSoftKeyboard();
        onView(withId(R.id.tvLoginReset)).perform(click());
        onView(withText(R.string.request_sent)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    @Test
    public void loginWithInvalidEmail() {
        onView(withId(R.id.etLoginEmail)).perform(typeText("a"));
        closeSoftKeyboard();
        onView(withId(R.id.etLoginPassword)).perform(typeText("123##Test"));
        closeSoftKeyboard();
        onView(withId(R.id.btLogin)).perform(click());
        onView(withText(R.string.bad_login)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    @Test
    public void loginWithInvalidPassword() {
        onView(withId(R.id.etLoginEmail)).perform(typeText("invalidpassword@example.com"));
        closeSoftKeyboard();
        onView(withId(R.id.etLoginPassword)).perform(typeText("a"));
        closeSoftKeyboard();
        onView(withId(R.id.btLogin)).perform(click());
        onView(withText(R.string.bad_login)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    @Test
    public void loginWithUnregisteredEmail() {
        onView(withId(R.id.etLoginEmail)).perform(typeText("unregistered@example.com"));
        closeSoftKeyboard();
        onView(withId(R.id.etLoginPassword)).perform(typeText("123##Test"));
        closeSoftKeyboard();
        onView(withId(R.id.btLogin)).perform(click());
        onView(withText(R.string.toast_error_message)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }
}