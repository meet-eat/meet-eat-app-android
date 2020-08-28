package meet_eat.app;

import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.google.common.collect.Lists;

import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import meet_eat.app.repository.RequestHandlerException;
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

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class Scenario1020Test {

    private static final OfferViewModel offerVM = new OfferViewModel();
    private static final SettingsViewModel settingsVM = new SettingsViewModel();
    private static final RegisterViewModel registerVM = new RegisterViewModel();
    private static final long timestamp = System.currentTimeMillis();
    private static final String password = "123##Tester";

    private final ScenarioTestHelper scenarioTestHelper = new ScenarioTestHelper(timestamp, password);

    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @BeforeClass
    public static void initialize() throws RequestHandlerException {
        Localizable home = new SphericalLocation(new SphericalPosition(49.0082285, 8.3978892));
        User newUser = new User(new Email(timestamp + "@example.com"), Password.createHashedPassword(password),
                LocalDate.of(2000, 1, 1), "Tester", "0123456789", "Test description", true, home);
        registerVM.register(newUser);
    }

    @AfterClass
    public static void cleanUp() throws RequestHandlerException {
        Intents.release();

        settingsVM.deleteUser();
    }

    @Test
    public void testScenario1020() {
        Intents.init();
        scenarioTestHelper.login();
        onView(withId(R.id.ibtOfferListCreate)).perform(click());
        // [TA 3060]
        onView(withId(R.id.etOfferEditTitle)).perform(typeText("Offer"));
        onView(withId(R.id.etOfferEditCity)).perform(typeText("New York City"));
        onView(withId(R.id.etOfferEditPrice)).perform(typeText("10"));
        onView(withId(R.id.etOfferEditDescription)).perform(typeText("Offer Description"));
        onView(withId(R.id.etOfferEditParticipants)).perform(typeText("42"));
        closeSoftKeyboard();

        onView(withId(R.id.tvOfferEditDate)).perform(click());
        Calendar offerDate = GregorianCalendar.getInstance();
        offerDate.set(2100, 1, 1);
        // invoke date picker, set date, then click ok
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions
                .setDate(offerDate.get(Calendar.YEAR), offerDate.get(Calendar.MONTH),
                        offerDate.get(Calendar.DAY_OF_MONTH)));
        onView(withText("OK")).perform(click());

        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(20, 0));
        onView(withText("OK")).perform(click());

        closeSoftKeyboard();
        onView(withId(R.id.btOfferEditPublish)).perform(click());

        onView(withText(R.string.request_sent)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));

        // Rating is not yet implemented, can't test
    }

}
