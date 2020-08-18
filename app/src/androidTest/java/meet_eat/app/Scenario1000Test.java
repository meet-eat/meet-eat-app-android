package meet_eat.app;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.main.SettingsViewModel;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class Scenario1000Test {

    static long timestamp = System.currentTimeMillis();
    private final String password = "123##Tester";
    private ScenarioTestHelper scenarioTestHelper = new ScenarioTestHelper(timestamp, password);

    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @AfterClass
    public static void cleanUp() throws RequestHandlerException {
        new SettingsViewModel().deleteUser(new SettingsViewModel().getCurrentUser());
        Intents.release();
    }

    @Test
    public void testScenario1000() {
        // [TA 2000], [TA 1000], [TA 3000], [TA 2030]
        Intents.init();
        scenarioTestHelper.register();
        scenarioTestHelper.login();
        // [TA 3010]
        onView(withId(R.id.rvOfferList)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // [TA 3040]
        onView(withId(R.id.btOfferDetailedParticipate)).perform(click());
        // [TA 1010] (cannot log out and delete account, logging out is tested in unit tests though)
        // Also, the rating feature is not yet implemented, so the rest of this scenario is omitted
    }

}