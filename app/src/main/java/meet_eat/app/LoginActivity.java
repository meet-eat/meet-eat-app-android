package meet_eat.app;

import android.os.Bundle;
import android.os.StrictMode;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Holds the fragments {@link meet_eat.app.fragment.login.RegisterFragment RegisterFragment} and
 * {@link meet_eat.app.fragment.login.LoginFragment LoginFragment} and is the first activity called
 * on app startup.
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}