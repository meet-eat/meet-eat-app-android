package meet_eat.app;

import android.widget.DatePicker;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.intent.Intents;

import org.hamcrest.Matchers;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class ScenarioTestHelper {

    long timestamp;
    String password;

    public ScenarioTestHelper(long timestamp, String password) {
        this.timestamp = timestamp;
        this.password = password;
    }

    void register() {
        onView(withId(R.id.btLoginRegister)).perform(click());
        onView(withId(R.id.etRegisterEmail)).perform(typeText(timestamp + "@example.com"));
        onView(withId(R.id.etRegisterPassword)).perform(typeText(password));
        onView(withId(R.id.etRegisterPasswordConfirm)).perform(typeText("123##Tester"));
        onView(withId(R.id.etRegisterUsername)).perform(typeText("Tester"));
        onView(withId(R.id.etRegisterPhone)).perform(typeText("123456789"));
        closeSoftKeyboard();
        onView(withId(R.id.etRegisterHome)).perform(typeText("New York City"));
        closeSoftKeyboard();
        onView(withId(R.id.etRegisterDescription)).perform(typeText("Test description"));
        closeSoftKeyboard();
        onView(withId(R.id.tvRegisterBirthday)).perform(click());

        Calendar birthday = GregorianCalendar.getInstance();
        birthday.set(2000, 1, 1);
        // invoke date picker, set date, then click ok
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions
                .setDate(birthday.get(Calendar.YEAR), birthday.get(Calendar.MONTH),
                        birthday.get(Calendar.DAY_OF_MONTH)));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.btRegister)).perform(click());

        // Check for the right toast to come up
        onView(withText(R.string.request_sent)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    void login() {
        onView(withId(R.id.etLoginEmail)).perform(typeText(timestamp + "@example.com"));
        closeSoftKeyboard();
        onView(withId(R.id.etLoginPassword)).perform(typeText(password));
        closeSoftKeyboard();
        // Start recording intents
        onView(withId(R.id.btLogin)).perform(click());

        // Check if mainActivity was reached (user is logged in)
        intended(hasComponent(MainActivity.class.getName()));
    }
}
