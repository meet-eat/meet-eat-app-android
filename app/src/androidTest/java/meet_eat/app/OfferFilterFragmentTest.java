package meet_eat.app;

import android.os.SystemClock;
import android.view.Gravity;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
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

import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;

import meet_eat.app.repository.RequestHandlerException;
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
public class OfferFilterFragmentTest {
    private static final RegisterViewModel registerVM = new RegisterViewModel();
    private static final SettingsViewModel settingsVM = new SettingsViewModel();
    private static final String password = "123##Test";
    private static final long timestamp = System.currentTimeMillis();

    private final ScenarioTestHelper scenarioTestHelper = new ScenarioTestHelper(timestamp, password);
    private static final Localizable home = new SphericalLocation(new SphericalPosition(49.0082285, 8.3978892));
    private static boolean isLoggedIn = false;

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
        settingsVM.deleteUser();
    }

    @Before
    public void login() {
        if (!isLoggedIn) {
            scenarioTestHelper.login();
            isLoggedIn = true;
        }
    }

    @Test
    public void saveRatingFiltersTest() {
        // Navigate to filter view
        onView(withId(R.id.ibtOfferListFilter)).perform(click());

        // Set invalid rating filters
        onView(withId(R.id.etOfferFilterRatingMin)).perform(typeText("2"));
        closeSoftKeyboard();
        onView(withId(R.id.etOfferFilterRatingMax)).perform(typeText("1"));
        closeSoftKeyboard();
        onView(withId(R.id.btOfferFilterSave)).perform(click());
        onView(withText(R.string.invalid_rating_interval)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));

        // Set valid rating filters
        onView(withId(R.id.etOfferFilterRatingMin)).perform(clearText());
        onView(withId(R.id.etOfferFilterRatingMin)).perform(typeText("1"));
        closeSoftKeyboard();
        onView(withId(R.id.etOfferFilterRatingMax)).perform(clearText());
        onView(withId(R.id.etOfferFilterRatingMax)).perform(typeText("2"));
        closeSoftKeyboard();
        onView(withId(R.id.btOfferFilterSave)).perform(click());
    }

    @Test
    public void saveParticipantsFiltersTest() {
        // Navigate to filter view
        onView(withId(R.id.ibtOfferListFilter)).perform(click());

        // Set invalid participants filters
        onView(withId(R.id.etOfferFilterParticipantsMin)).perform(typeText("2"));
        closeSoftKeyboard();
        onView(withId(R.id.etOfferFilterParticipantsMax)).perform(typeText("1"));
        closeSoftKeyboard();
        onView(withId(R.id.btOfferFilterSave)).perform(click());
        onView(withText(R.string.invalid_participants_interval)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));

        // Set valid participants filters
        onView(withId(R.id.etOfferFilterParticipantsMin)).perform(clearText());
        onView(withId(R.id.etOfferFilterParticipantsMin)).perform(typeText("1"));
        closeSoftKeyboard();
        onView(withId(R.id.etOfferFilterParticipantsMax)).perform(clearText());
        onView(withId(R.id.etOfferFilterParticipantsMax)).perform(typeText("2"));
        closeSoftKeyboard();
        onView(withId(R.id.btOfferFilterSave)).perform(click());
    }

    @Test
    public void saveDistanceFiltersTest() {
        // Navigate to filter view
        onView(withId(R.id.ibtOfferListFilter)).perform(click());

        // Set invalid distance filters
        onView(withId(R.id.etOfferFilterDistanceMin)).perform(typeText("2"));
        closeSoftKeyboard();
        onView(withId(R.id.etOfferFilterDistanceMax)).perform(typeText("1"));
        closeSoftKeyboard();
        onView(withId(R.id.btOfferFilterSave)).perform(click());
        onView(withText(R.string.invalid_distance_interval)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));

        // Set valid price filters
        onView(withId(R.id.etOfferFilterDistanceMin)).perform(clearText());
        onView(withId(R.id.etOfferFilterDistanceMin)).perform(typeText("1"));
        closeSoftKeyboard();
        onView(withId(R.id.etOfferFilterDistanceMax)).perform(clearText());
        onView(withId(R.id.etOfferFilterDistanceMax)).perform(typeText("2"));
        closeSoftKeyboard();
        onView(withId(R.id.btOfferFilterSave)).perform(click());
    }

    @Test
    public void savePriceFiltersTest() {
        // Navigate to filter view
        onView(withId(R.id.ibtOfferListFilter)).perform(click());

        // Set invalid price filters
        onView(withId(R.id.etOfferFilterPriceMin)).perform(typeText("2"));
        closeSoftKeyboard();
        onView(withId(R.id.etOfferFilterPriceMax)).perform(typeText("1"));
        closeSoftKeyboard();
        onView(withId(R.id.btOfferFilterSave)).perform(click());
        onView(withText(R.string.invalid_price_interval)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));

        // Set valid price filters
        onView(withId(R.id.etOfferFilterPriceMin)).perform(clearText());
        onView(withId(R.id.etOfferFilterPriceMin)).perform(typeText("1"));
        closeSoftKeyboard();
        onView(withId(R.id.etOfferFilterPriceMax)).perform(clearText());
        onView(withId(R.id.etOfferFilterPriceMax)).perform(typeText("2"));
        closeSoftKeyboard();
        onView(withId(R.id.btOfferFilterSave)).perform(click());
    }

    @Test
    public void saveTimeFiltersTest() {
        // Navigate to filter view
        onView(withId(R.id.ibtOfferListFilter)).perform(click());

        // Set invalid time filters
        onView(withId(R.id.tvOfferFilterDateMin)).perform(click());
        Calendar offerDateMin = GregorianCalendar.getInstance();
        offerDateMin.set(2100, 1, 1);
        // invoke date picker, set date, then click ok
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions
                .setDate(offerDateMin.get(Calendar.YEAR), offerDateMin.get(Calendar.MONTH),
                        offerDateMin.get(Calendar.DAY_OF_MONTH)));
        onView(withText("OK")).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(20, 0));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.tvOfferFilterDateMax)).perform(click());
        Calendar offerDateMax = GregorianCalendar.getInstance();
        offerDateMax.set(2000, 1, 1);
        // invoke date picker, set date, then click ok
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions
                .setDate(offerDateMax.get(Calendar.YEAR), offerDateMax.get(Calendar.MONTH),
                        offerDateMax.get(Calendar.DAY_OF_MONTH)));
        onView(withText("OK")).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(20, 0));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.btOfferFilterSave)).perform(click());
        onView(withText(R.string.invalid_date_time_interval)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));

        // Set valid time filters
        onView(withId(R.id.tvOfferFilterDateMin)).perform(click());
        Calendar offerDateMinValid = GregorianCalendar.getInstance();
        offerDateMinValid.set(2000, 1, 1);
        // invoke date picker, set date, then click ok
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions
                .setDate(offerDateMinValid.get(Calendar.YEAR), offerDateMinValid.get(Calendar.MONTH),
                        offerDateMinValid.get(Calendar.DAY_OF_MONTH)));
        onView(withText("OK")).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(20, 0));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.tvOfferFilterDateMax)).perform(click());
        Calendar offerDateMaxValid = GregorianCalendar.getInstance();
        offerDateMaxValid.set(2100, 1, 1);
        // invoke date picker, set date, then click ok
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions
                .setDate(offerDateMaxValid.get(Calendar.YEAR), offerDateMaxValid.get(Calendar.MONTH),
                        offerDateMaxValid.get(Calendar.DAY_OF_MONTH)));
        onView(withText("OK")).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(20, 0));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.btOfferFilterSave)).perform(click());
    }

    @Test
    public void navigateBackFromOwnOffersTest() {
        // Navigate to own offers
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.drawer_menu_my_offers));

        // Wait, so espresso doesn't cause an error
        SystemClock.sleep(500);

        onView(withId(R.id.ibtOfferListFilter)).perform(click());
        // Navigate back
        onView(withId(R.id.ibtBack)).perform(click());
    }

    @Test
    public void resetFiltersTest() {
        onView(withId(R.id.ibtOfferListFilter)).perform(click());
        onView(withId(R.id.btOfferFilterReset)).perform(click());
    }
}