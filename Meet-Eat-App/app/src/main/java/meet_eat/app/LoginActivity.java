package meet_eat.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * Hält die Fragmente RegisterFragment und LoginFragment und dient als initiale Aktivität
 * beim Start der Applikation.
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}