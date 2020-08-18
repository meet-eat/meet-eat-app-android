package meet_eat.app;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import static meet_eat.app.fragment.ListType.BOOKMARKED;
import static meet_eat.app.fragment.ListType.OWN;
import static meet_eat.app.fragment.ListType.STANDARD;
import static meet_eat.app.fragment.ListType.SUBSCRIBED;
import static meet_eat.app.fragment.NavigationArgumentKey.LIST_TYPE;

/**
 * Represents the main activity of the app, that is active when the user is in the logged in state.
 * It thus holds all the fragments except {@link meet_eat.app.fragment.login.RegisterFragment
 * RegisterFragment} and {@link meet_eat.app.fragment.login.LoginFragment LoginFragment}.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * The maximum time to wait in between two clicks of the back button to quit the application
     */
    private static final int MAX_WAIT_TIME = 1200;

    private DrawerLayout drawerLayout;
    private NavController navController;
    private long timeInMillis;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this::onItemClicked);
        navigationView.getMenu().getItem(1).setChecked(true);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void selectMenuItem(int item) {
        navigationView.getMenu().getItem(item).setChecked(true);
        String title = getResources().getString(R.string.app_name);
        switch (item) {
            case 0:
                title = getResources().getString(R.string.drawer_menu_profile);
                break;
            case 1:
                title = getResources().getString(R.string.drawer_menu_main_offers);
                break;
            case 2:
                title = getResources().getString(R.string.drawer_menu_my_offers);
                break;
            case 3:
                title = getResources().getString(R.string.drawer_menu_bookmarked);
                break;
            case 4:
                title = getResources().getString(R.string.drawer_menu_subscriptions);
                break;
            case 5:
                title = getResources().getString(R.string.drawer_menu_settings);
                break;
        }
        getSupportActionBar().setTitle(title);
    }

    /**
     * When an item of the navDrawer is clicked, navigates to the specified destination
     *
     * @param menuItem The item that was clicked in the navDrawer
     * @return true, if the item that was clicked was valid
     */
    private boolean onItemClicked(MenuItem menuItem) {
        Bundle bundle = new Bundle();

        switch (menuItem.getItemId()) {
            case R.id.drawer_menu_profile:
                navController.navigate(R.id.profileFragment);
                break;
            case R.id.drawer_menu_main_offers:
                bundle.putSerializable(LIST_TYPE.name(), STANDARD);
                navController.navigate(R.id.offerListFragment, bundle);
                break;
            case R.id.drawer_menu_my_offers:
                bundle.putSerializable(LIST_TYPE.name(), OWN);
                navController.navigate(R.id.offerListFragment, bundle);
                break;
            case R.id.drawer_menu_bookmarked:
                bundle.putSerializable(LIST_TYPE.name(), BOOKMARKED);
                navController.navigate(R.id.offerListFragment, bundle);
                break;
            case R.id.drawer_menu_subscriptions:
                bundle.putSerializable(LIST_TYPE.name(), SUBSCRIBED);
                navController.navigate(R.id.offerListFragment, bundle);
                break;
            case R.id.drawer_menu_settings:
                navController.navigate(R.id.settingsFragment);
                break;
            default:
                return false;
        }
        drawerLayout.close();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        // if there is nothing to go back to
        if (Objects.isNull(navController.getPreviousBackStackEntry()) ||
                (navController.getPreviousBackStackEntry().getDestination().getId() == R.id.offerEditFragment ||
                        navController.getPreviousBackStackEntry().getDestination().getId() == R.id.offerEditFragment)) {
            if (timeInMillis == 0) {
                timeInMillis = System.currentTimeMillis();
                Toast.makeText(this, R.string.on_back_pressed_message, Toast.LENGTH_SHORT).show();
            } else {
                // make the user double click the back button within MAX_WAIT_TIME millis to quit
                if (System.currentTimeMillis() - timeInMillis < MAX_WAIT_TIME) {
                    super.onPause();
                    timeInMillis = 0;
                } else {
                    timeInMillis = System.currentTimeMillis();
                    Toast.makeText(this, R.string.on_back_pressed_message, Toast.LENGTH_SHORT).show();
                }
            }
            // if previous fragments should not be visited again i.e. after editing profile/offer
        } else {
            // pop the back stack: go back to the last fragment
            super.onBackPressed();
        }
    }
}