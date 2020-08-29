package meet_eat.app;

import android.os.SystemClock;
import android.view.Gravity;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
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
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class OfferEditFragmentTest {
    private static final RegisterViewModel registerVM = new RegisterViewModel();
    private static final SettingsViewModel settingsVM = new SettingsViewModel();
    private static final LoginViewModel loginVM = new LoginViewModel();
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
        loginVM.login(timestamp + "@example.com", password);
        Intents.init();
    }

    @AfterClass
    public static void cleanUp() throws RequestHandlerException {
        Intents.release();
        settingsVM.deleteUser();
    }


    @Test
    public void navigateBackTest() {
        onView(withId(R.id.ibtOfferListCreate)).perform(click());
        onView(withId(R.id.ibtBack)).perform(click());
    }

    @Test
    public void publishSaveAndDeleteOfferTest() {
        onView(withId(R.id.ibtOfferListCreate)).perform(click());
        // Check for empty location
        onView(withId(R.id.btOfferEditPublish)).perform(click());
        onView(withText(R.string.missing_location)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        // Check for invalid location
        SystemClock.sleep(1000);
        onView(withId(R.id.etOfferEditCity)).perform(typeText("................................................."));
        closeSoftKeyboard();
        onView(withId(R.id.btOfferEditPublish)).perform(click());
        onView(withText(R.string.invalid_location)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        onView(withId(R.id.etOfferEditCity)).perform(clearText());
        onView(withId(R.id.etOfferEditCity)).perform(typeText("New York City"));
        closeSoftKeyboard();
        // Check for empty price
        SystemClock.sleep(1000);
        onView(withId(R.id.btOfferEditPublish)).perform(click());
        onView(withText(R.string.missing_price)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        onView(withId(R.id.etOfferEditPrice)).perform(typeText("10"));
        closeSoftKeyboard();
        // Check for empty participants
        SystemClock.sleep(1000);
        onView(withId(R.id.btOfferEditPublish)).perform(click());
        onView(withText(R.string.missing_participants)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        // Check for invalid participants
        SystemClock.sleep(1000);
        onView(withId(R.id.etOfferEditParticipants)).perform(typeText("0"));
        closeSoftKeyboard();
        onView(withId(R.id.btOfferEditPublish)).perform(click());
        onView(withText(R.string.invalid_max_participants)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        onView(withId(R.id.etOfferEditParticipants)).perform(clearText());
        onView(withId(R.id.etOfferEditParticipants)).perform(typeText("42"));
        closeSoftKeyboard();
        // Check for empty title
        SystemClock.sleep(1000);
        onView(withId(R.id.btOfferEditPublish)).perform(click());
        onView(withText(R.string.missing_title)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        onView(withId(R.id.etOfferEditTitle)).perform(typeText("Offer"));
        closeSoftKeyboard();
        // Check for empty description
        SystemClock.sleep(1000);
        onView(withId(R.id.btOfferEditPublish)).perform(click());
        onView(withText(R.string.missing_description)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        onView(withId(R.id.etOfferEditDescription)).perform(typeText("Offer Description"));
        closeSoftKeyboard();
        // Check for empty datetime
        SystemClock.sleep(1000);
        onView(withId(R.id.btOfferEditPublish)).perform(click());
        onView(withText(R.string.missing_date_time)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        // Check for invalid datetime
        SystemClock.sleep(1000);
        onView(withId(R.id.tvOfferEditDate)).perform(click());
        Calendar offerDateInvalid = GregorianCalendar.getInstance();
        offerDateInvalid.set(2000, 1, 1);
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions
                .setDate(offerDateInvalid.get(Calendar.YEAR), offerDateInvalid.get(Calendar.MONTH),
                        offerDateInvalid.get(Calendar.DAY_OF_MONTH)));
        onView(withText("OK")).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(20, 0));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.btOfferEditPublish)).perform(click());
        onView(withText(R.string.invalid_date_time_past)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        // Publish valid offer
        SystemClock.sleep(1000);
        onView(withId(R.id.tvOfferEditDate)).perform(click());
        Calendar offerDateValid = GregorianCalendar.getInstance();
        offerDateValid.set(2100, 1, 1);
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions
                .setDate(offerDateValid.get(Calendar.YEAR), offerDateValid.get(Calendar.MONTH),
                        offerDateValid.get(Calendar.DAY_OF_MONTH)));
        onView(withText("OK")).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(20, 0));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.btOfferEditPublish)).perform(click());
        onView(withText(R.string.request_sent)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        // Navigate to own offers
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.drawer_menu_my_offers));
        // Wait, so espresso doesn't cause an error
        SystemClock.sleep(500);
        // Edit offer
        // Open offer detailed view
        onView(withId(R.id.rvOfferList)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // Open offer edit view
        onView(withId(R.id.ibtOfferDetailedEdit)).perform(click());
        onView(withId(R.id.btOfferEditSave)).perform(click());
        onView(withText(R.string.request_sent)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
        // Navigate to own offers
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.drawer_menu_my_offers));
        // Wait, so espresso doesn't cause an error
        SystemClock.sleep(500);
        // Delete offer
        // Open offer detailed view
        onView(withId(R.id.rvOfferList)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // Open offer edit view
        onView(withId(R.id.ibtOfferDetailedEdit)).perform(click());
        onView(withId(R.id.btOfferEditDelete)).perform(click());
        onView(withText(R.string.request_sent)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }
}