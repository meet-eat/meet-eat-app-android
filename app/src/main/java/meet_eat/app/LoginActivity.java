package meet_eat.app;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Holds the fragments {@link meet_eat.app.fragment.login.RegisterFragment RegisterFragment} and
 * {@link meet_eat.app.fragment.login.LoginFragment LoginFragment} and is the first activity called
 * on app startup.
 */
public class LoginActivity extends AppCompatActivity {

    private long timeInMillis = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public void onBackPressed() {

        if (timeInMillis == 0) {
            timeInMillis = System.currentTimeMillis();
            Toast.makeText(this, R.string.on_back_pressed_message, Toast.LENGTH_SHORT).show();
        } else {
            if (System.currentTimeMillis() - timeInMillis < 1200) {
                super.onBackPressed();
                timeInMillis = 0;
            } else {
                timeInMillis = System.currentTimeMillis();
                Toast.makeText(this, R.string.on_back_pressed_message, Toast.LENGTH_SHORT).show();
            }
        }
    }
}