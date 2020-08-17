package meet_eat.app;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

/**
 * Holds the fragments {@link meet_eat.app.fragment.login.RegisterFragment RegisterFragment} and
 * {@link meet_eat.app.fragment.login.LoginFragment LoginFragment} and is the first activity called
 * on app startup.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * The maximum time to wait in between two clicks of the back button to quit the application
     */
    private static final int MAX_WAIT_TIME = 1200;

    private NavController navController;
    private long timeInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_login);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public void onBackPressed() {
        // if there is nothing to go back to
        if (navController.getPreviousBackStackEntry() == null) {
            // make the user double click the back button within MAX_WAIT_TIME millis to quit
            if (timeInMillis == 0) {
                timeInMillis = System.currentTimeMillis();
                Toast.makeText(this, R.string.on_back_pressed_message, Toast.LENGTH_SHORT).show();
            } else {
                // The maximum time to wait in between two clicks of the back button to quit the application
                if (System.currentTimeMillis() - timeInMillis < MAX_WAIT_TIME) {
                    super.onBackPressed();
                    timeInMillis = 0;
                } else {
                    timeInMillis = System.currentTimeMillis();
                    Toast.makeText(this, R.string.on_back_pressed_message, Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            super.onBackPressed();
        }
    }
}