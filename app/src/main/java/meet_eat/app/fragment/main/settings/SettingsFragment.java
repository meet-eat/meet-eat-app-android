package meet_eat.app.fragment.main.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Set;

import meet_eat.app.LoginActivity;
import meet_eat.app.R;
import meet_eat.app.databinding.FragmentSettingsBinding;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.main.SettingsViewModel;
import meet_eat.data.entity.user.setting.NotificationSetting;
import meet_eat.data.entity.user.setting.Setting;

/**
 * The settings page. Here the user can choose between different settings to change.
 */
public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private SettingsViewModel settingsVM;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        settingsVM = new ViewModelProvider(this).get(SettingsViewModel.class);
        navController = NavHostFragment.findNavController(this);
        setButtonOnClickListener();
        updateUI();
        return binding.getRoot();
    }

    /**
     * Sets various click listeners.
     */
    private void setButtonOnClickListener() {
        binding.ibtBack.setOnClickListener(event -> navController.navigateUp());
        binding.btSettingsDelete
                .setOnClickListener(event -> navController.navigate(R.id.settingsDeleteProfileFragment));
        binding.btSettingsLogout.setOnClickListener(event -> logout());
        // Currently disabled features
        // binding.swSettingsNotification.setOnClickListener(event -> toggleNotification());
        // binding.tvSettingsAppearanceMenu.setOnClickListener(event -> navController.navigate(R.id
        // .settingsDisplayFragment));
        // binding.tvSettingsNotificationMenu.setOnClickListener(event -> navController.navigate(R.id
        // .settingsNotificationFragment));
    }

    /**
     * Sets the notification setting switch accordingly to the users given notification setting.
     */
    private void updateUI() {
        Set<Setting> settings = settingsVM.getCurrentUser().getSettings();
        // Get the notification setting off the settings set, then updates the notification switch accordingly
        settings.forEach(s -> {
            if (s.getClass().equals(NotificationSetting.class)) {
                binding.swSettingsNotification.setChecked(((NotificationSetting) s).isEnabled());
            }
        });
    }

    /**
     * Tries to log out the user, then goes back to the login page
     *
     * @see meet_eat.app.fragment.login.LoginFragment
     */
    private void logout() {
        try {
            settingsVM.logout();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        } catch (RequestHandlerException exception) {
            Toast.makeText(getActivity(), R.string.toast_error_message, Toast.LENGTH_LONG).show();
            Toast.makeText(getActivity(), "DEBUG SettingsFragment.java -> logout(): " + exception.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Currently disabled feature
     * Toggles the notification setting
     */
    private void toggleNotification() {
        NotificationSetting newNotificationSetting = new NotificationSetting();
        newNotificationSetting.setEnabled(binding.swSettingsNotification.isEnabled());

        try {
            settingsVM.updateNotificationSettings(newNotificationSetting);
        } catch (RequestHandlerException exception) {
            Toast.makeText(getActivity(), R.string.toast_error_message, Toast.LENGTH_LONG).show();
            Log.i("DEBUG", "SettingsFragment.toggleNotification: " + exception.getMessage());
        }
    }
}