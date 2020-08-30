package meet_eat.app;

import android.os.SystemClock;
import android.widget.DatePicker;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;

import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.login.LoginViewModel;
import meet_eat.app.viewmodel.login.RegisterViewModel;
import meet_eat.app.viewmodel.main.SettingsViewModel;
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
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RegisterFragmentTest {
    private static final LoginViewModel loginVM = new LoginViewModel();
    private static final RegisterViewModel registerVM = new RegisterViewModel();
    private static final SettingsViewModel settingsVM = new SettingsViewModel();
    private static final String password = "123##Test";
    private static final long timestamp = System.currentTimeMillis();

    private static final Localizable home = new SphericalLocation(new SphericalPosition(49.0082285, 8.3978892));

    @Rule
    public ActivityTestRule<SplashActivity> activityTestRule = new ActivityTestRule<>(SplashActivity.class);

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
        loginVM.login(timestamp + "@example.com", password);
        settingsVM.deleteUser();
    }

    @Test
    public void registerWithInvalidEmailTest() {
        onView(withId(R.id.btLoginRegister)).perform(click());

        // Invalid email
        onView(withId(R.id.etRegisterEmail)).perform(typeText("a"));
        closeSoftKeyboard();
        onView(withId(R.id.btRegister)).perform(click());
        onView(withText(R.string.bad_email)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        onView(withId(R.id.etRegisterEmail)).perform(clearText());
        onView(withId(R.id.etRegisterEmail)).perform(typeText(timestamp + "@example.com"));
        closeSoftKeyboard();
        SystemClock.sleep(1000);

        // No matching passwords
        onView(withId(R.id.etRegisterPassword)).perform(typeText("123##Test1"));
        closeSoftKeyboard();
        onView(withId(R.id.etRegisterPasswordConfirm)).perform(typeText("123##Test2"));
        closeSoftKeyboard();
        onView(withId(R.id.btRegister)).perform(click());
        onView(withText(R.string.passwords_not_matching)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        SystemClock.sleep(1000);

        // Invalid password
        onView(withId(R.id.etRegisterPassword)).perform(clearText());
        onView(withId(R.id.etRegisterPassword)).perform(typeText("a"));
        closeSoftKeyboard();
        onView(withId(R.id.etRegisterPasswordConfirm)).perform(clearText());
        onView(withId(R.id.etRegisterPasswordConfirm)).perform(typeText("a"));
        closeSoftKeyboard();
        onView(withId(R.id.btRegister)).perform(click());
        onView(withText(R.string.bad_password)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        onView(withId(R.id.etRegisterPassword)).perform(clearText());
        onView(withId(R.id.etRegisterPassword)).perform(typeText(password));
        closeSoftKeyboard();
        onView(withId(R.id.etRegisterPasswordConfirm)).perform(clearText());
        onView(withId(R.id.etRegisterPasswordConfirm)).perform(typeText(password));
        closeSoftKeyboard();
        SystemClock.sleep(1000);

        // Empty username
        onView(withId(R.id.btRegister)).perform(click());
        onView(withText(R.string.missing_username)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        onView(withId(R.id.etRegisterUsername)).perform(typeText("Tester"));
        closeSoftKeyboard();
        SystemClock.sleep(1000);

        // Empty birthday
        onView(withId(R.id.btRegister)).perform(click());
        onView(withText(R.string.missing_date)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        SystemClock.sleep(1000);

        // Future birthday
        onView(withId(R.id.tvRegisterBirthday)).perform(click());
        Calendar birthday = GregorianCalendar.getInstance();
        birthday.set(2030, 1, 1);
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions
                .setDate(birthday.get(Calendar.YEAR), birthday.get(Calendar.MONTH),
                        birthday.get(Calendar.DAY_OF_MONTH)));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.btRegister)).perform(click());
        onView(withText(R.string.invalid_date_time_future)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        onView(withId(R.id.tvRegisterBirthday)).perform(click());
        Calendar validBirthday = GregorianCalendar.getInstance();
        validBirthday.set(2000, 1, 1);
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions
                .setDate(validBirthday.get(Calendar.YEAR), validBirthday.get(Calendar.MONTH),
                        validBirthday.get(Calendar.DAY_OF_MONTH)));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.btRegister)).perform(click());
        onView(withText(R.string.invalid_date_time_future)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        SystemClock.sleep(1000);

        // Empty home
        onView(withId(R.id.btRegister)).perform(click());
        onView(withText(R.string.missing_location)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        SystemClock.sleep(1000);

        // Invalid home
        onView(withId(R.id.etRegisterHome)).perform(typeText("................................................."));
        closeSoftKeyboard();
        onView(withId(R.id.btRegister)).perform(click());
        onView(withText(R.string.invalid_location)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        onView(withId(R.id.etRegisterHome)).perform(clearText());
        onView(withId(R.id.etRegisterHome)).perform(typeText("Stuttgart"));
        closeSoftKeyboard();
        SystemClock.sleep(1000);

        // Already registered email
        onView(withId(R.id.btRegister)).perform(click());
        onView(withText(R.string.toast_error_message)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));

        // Navigate back
        onView(withId(R.id.ibtBack)).perform(click());
    }
}