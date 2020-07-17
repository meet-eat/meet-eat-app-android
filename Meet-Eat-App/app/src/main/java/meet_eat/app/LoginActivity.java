package meet_eat.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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
    }
}