package meet_eat.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;

import androidx.appcompat.app.AppCompatActivity;

import meet_eat.app.repository.Session;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);

        new Handler().post(() -> {
            Intent intent;
            // Checks if user was logged in previously
            if (Session.getInstance().isLoggedIn()) {
                intent = new Intent(getApplicationContext(), MainActivity.class);
            } else {
                intent = new Intent(getApplicationContext(), LoginActivity.class);
            }
            startActivity(intent);
            finish();
        });
    }
}
