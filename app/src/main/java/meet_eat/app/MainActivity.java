package meet_eat.app;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.navigation.NavigationView;

/**
 * Represents the main activity of the app, that is active when the user is in the logged in state.
 * It thus holds all the fragments except {@link meet_eat.app.fragment.login.RegisterFragment
 * RegisterFragment} and {@link meet_eat.app.fragment.login.LoginFragment LoginFragment}.
 */
public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navController = NavHostFragment.findNavController(getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_main));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this::onItemClicked);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private boolean onItemClicked(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            // TODO bundles
            case R.id.drawer_menu_profile:
                navController.navigate(R.id.profileFragment);
                break;
            case R.id.drawer_menu_main_offers:
                navController.navigate(R.id.offerListFragment);
                break;
            case R.id.drawer_menu_my_offers:
                navController.navigate(R.id.offerListFragment);
                break;
            case R.id.drawer_menu_bookmarked:
                navController.navigate(R.id.offerListFragment);
                break;
            case R.id.drawer_menu_subscriptions:
                navController.navigate(R.id.offerListFragment);
                break;
            case R.id.drawer_menu_settings:
                navController.navigate(R.id.settingsFragment);
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }
}