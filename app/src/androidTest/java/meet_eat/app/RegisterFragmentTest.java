package meet_eat.app;

import android.widget.DatePicker;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RegisterFragmentTest {

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
    public void registerWithInvalidEmailTest() {
        onView(withId(R.id.etRegisterEmail)).perform(clearText());
        onView(withId(R.id.etRegisterEmail)).perform(typeText("a"));
        closeSoftKeyboard();
        onView(withId(R.id.btRegister)).perform(click());
    }

    @Test
    public void registerWithInvalidPasswordTest() {
        onView(withId(R.id.etRegisterPassword)).perform(clearText());
        onView(withId(R.id.etRegisterPassword)).perform(typeText("a"));
        closeSoftKeyboard();
        onView(withId(R.id.etRegisterPasswordConfirm)).perform(clearText());
        onView(withId(R.id.etRegisterPasswordConfirm)).perform(typeText("a"));
        closeSoftKeyboard();
        onView(withId(R.id.btRegister)).perform(click());
    }

    @Test
    public void registerWithNotMatchingPasswordsTest() {
        onView(withId(R.id.etRegisterPassword)).perform(clearText());
        onView(withId(R.id.etRegisterPassword)).perform(typeText("123##Test1"));
        closeSoftKeyboard();
        onView(withId(R.id.etRegisterPasswordConfirm)).perform(clearText());
        onView(withId(R.id.etRegisterPasswordConfirm)).perform(typeText("123##Test2"));
        closeSoftKeyboard();
        onView(withId(R.id.btRegister)).perform(click());
    }

    @Test
    public void registerWithEmptyUsernameTest() {
        onView(withId(R.id.etRegisterUsername)).perform(clearText());
        onView(withId(R.id.btRegister)).perform(click());
    }

    @Test
    public void registerWithEmptyBirthdayTest() {
        onView(withId(R.id.ibtBack)).perform(click());
        onView(withId(R.id.btLoginRegister)).perform(click());
        onView(withId(R.id.etRegisterEmail)).perform(typeText("tester@example.com"));
        closeSoftKeyboard();
        onView(withId(R.id.etRegisterPassword)).perform(typeText("123##Test"));
        closeSoftKeyboard();
        onView(withId(R.id.etRegisterPasswordConfirm)).perform(typeText("123##Test"));
        closeSoftKeyboard();
        onView(withId(R.id.etRegisterUsername)).perform(typeText("Tester"));
        closeSoftKeyboard();
        onView(withId(R.id.etRegisterPhone)).perform(typeText("123456789"));
        closeSoftKeyboard();
        onView(withId(R.id.etRegisterHome)).perform(typeText("New York City"));
        closeSoftKeyboard();
        onView(withId(R.id.etRegisterDescription)).perform(typeText("Test description"));
        closeSoftKeyboard();
        onView(withId(R.id.btRegister)).perform(click());
    }

    @Test
    public void registerWithFutureBirthdayTest() {
        onView(withId(R.id.tvRegisterBirthday)).perform(click());
        Calendar birthday = GregorianCalendar.getInstance();
        birthday.set(2030, 1, 1);
        // Invoke date picker, set date, then click ok
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions
                .setDate(birthday.get(Calendar.YEAR), birthday.get(Calendar.MONTH),
                        birthday.get(Calendar.DAY_OF_MONTH)));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.btRegister)).perform(click());
    }

    @Test
    public void registerWithEmptyHomeTest() {
        onView(withId(R.id.etRegisterHome)).perform(clearText());
        onView(withId(R.id.btRegister)).perform(click());
    }

    @Test
    public void registerWithInvalidHomeTest() {
        onView(withId(R.id.etRegisterHome)).perform(clearText());
        onView(withId(R.id.etRegisterHome)).perform(typeText("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
        closeSoftKeyboard();
        onView(withId(R.id.btRegister)).perform(click());
    }

    @Before
    public void fillRegisterFormula() {
        onView(withId(R.id.btLoginRegister)).perform(click());
        onView(withId(R.id.etRegisterEmail)).perform(typeText("tester@example.com"));
        closeSoftKeyboard();
        onView(withId(R.id.etRegisterPassword)).perform(typeText("123##Test"));
        closeSoftKeyboard();
        onView(withId(R.id.etRegisterPasswordConfirm)).perform(typeText("123##Test"));
        closeSoftKeyboard();
        onView(withId(R.id.etRegisterUsername)).perform(typeText("Tester"));
        closeSoftKeyboard();
        onView(withId(R.id.etRegisterPhone)).perform(typeText("123456789"));
        closeSoftKeyboard();
        onView(withId(R.id.etRegisterHome)).perform(typeText("New York City"));
        closeSoftKeyboard();
        onView(withId(R.id.etRegisterDescription)).perform(typeText("Test description"));
        closeSoftKeyboard();
        onView(withId(R.id.tvRegisterBirthday)).perform(click());
        Calendar birthday = GregorianCalendar.getInstance();
        birthday.set(2000, 1, 1);
        // Invoke date picker, set date, then click ok
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions
                .setDate(birthday.get(Calendar.YEAR), birthday.get(Calendar.MONTH),
                        birthday.get(Calendar.DAY_OF_MONTH)));
        onView(withText("OK")).perform(click());
    }
}