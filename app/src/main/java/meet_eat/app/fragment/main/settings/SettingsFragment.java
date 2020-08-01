package meet_eat.app.fragment.main.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private void setButtonOnClickListener() {
        binding.swSettingsNotification.setOnClickListener(event -> toggleNotification());
        binding.tvSettingsAppearanceMenu.setOnClickListener(event -> navigateToSettingsDisplay());
        binding.tvSettingsNotificationMenu.setOnClickListener(event -> navigateToSettingsNotification());
        binding.btSettingsDelete.setOnClickListener(event -> navigateToSettingsDeleteProfile());
        binding.btSettingsLogout.setOnClickListener(event -> logout());
        binding.ibtBack.setOnClickListener(event -> navController.navigateUp());
    }

    private void updateUI() {
        Set<Setting> settings = settingsVM.getCurrentUser().getSettings();

        for (Setting s : settings) {

            if (s instanceof NotificationSetting) {
                binding.swSettingsNotification.setChecked(((NotificationSetting) s).isEnabled());
                break;
            }

        }

    }

    private void logout() {

        try {
            settingsVM.logout();
            // TODO toast?
            startActivity(new Intent(getActivity(), LoginActivity.class));
        } catch (RequestHandlerException e) {
            // TODO
        }

    }

    private void navigateToSettingsDeleteProfile() {
        navController.navigate(R.id.settingsDeleteProfileFragment);
    }

    private void navigateToSettingsNotification() {
        navController.navigate(R.id.settingsNotificationFragment);
    }

    private void navigateToSettingsDisplay() {
        navController.navigate(R.id.settingsDisplayFragment);
    }

    private void toggleNotification() {
        NotificationSetting newNotificationSetting = new NotificationSetting();
        newNotificationSetting.setEnabled(binding.swSettingsNotification.isEnabled());

        try {
            settingsVM.updateNotificationSettings(newNotificationSetting);
        } catch (RequestHandlerException e) {
            // TODO
        }

    }
}