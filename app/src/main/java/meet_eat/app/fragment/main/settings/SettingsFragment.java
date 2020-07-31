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
import androidx.navigation.Navigation;

import java.util.Set;

import meet_eat.app.LoginActivity;
import meet_eat.app.R;
import meet_eat.app.databinding.FragmentSettingsBinding;
import meet_eat.app.databinding.FragmentSettingsDeleteProfileBinding;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.repository.Session;
import meet_eat.app.viewmodel.main.UserViewModel;
import meet_eat.data.entity.user.setting.NotificationSetting;
import meet_eat.data.entity.user.setting.Setting;

public class SettingsFragment extends Fragment {


    private FragmentSettingsBinding binding;
    private UserViewModel userVM;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        userVM = new ViewModelProvider(this).get(UserViewModel.class);
        navController = Navigation.findNavController(binding.getRoot());

        setButtonOnClickListener();
        setUI();

        return binding.getRoot();
    }

    private void setButtonOnClickListener() {
        binding.swSettingsNotification.setOnClickListener(event -> toggleNotification());
        binding.tvSettingsAppearanceMenu.setOnClickListener(event -> navigateToSettingsDisplay());
        binding.tvSettingsNotificationMenu.setOnClickListener(event -> navigateToSettingsNotification());
        binding.btSettingsDelete.setOnClickListener(event -> navigateToSettingsDeleteProfile());
        binding.btSettingsLogout.setOnClickListener(event -> logout());
        binding.ibtBack.setOnClickListener(event -> goBack());
    }

    private void setUI() {
        NotificationSetting currentNotificationSetting = getCurrentNotificationSetting();
        if (currentNotificationSetting != null)
            binding.swSettingsNotification.setChecked(currentNotificationSetting.isEnabled());
    }

    private void logout() {
        try {
            Session.getInstance().logout();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        } catch (RequestHandlerException e) {
            // TODO was ist los
        }
    }

    private void navigateToSettingsDeleteProfile() {
        navController.navigate(SettingsFragmentDirections.actionSettingsFragmentToSettingsDeleteProfileFragment());
    }

    private void navigateToSettingsNotification() {
        navController.navigate(SettingsFragmentDirections.actionSettingsFragmentToNotificationFragment());
    }

    private void navigateToSettingsDisplay() {
        navController.navigate(SettingsFragmentDirections.actionSettingsFragmentToDisplayFragment());
    }

    private void goBack() {
        navController.popBackStack();
    }

    private void toggleNotification() {
        NotificationSetting newNotificationSetting = new NotificationSetting();
        NotificationSetting currentNotificationSetting = getCurrentNotificationSetting();
        newNotificationSetting.setEnabled(binding.swSettingsNotification.isChecked());

        if (currentNotificationSetting != null) {
            userVM.getCurrentUser().getSettings().remove(currentNotificationSetting);
            newNotificationSetting.setMinutesUntilOffer(currentNotificationSetting.getMinutesUntilOffer());
        }
        userVM.getCurrentUser().getSettings().add(newNotificationSetting);

    }

    private NotificationSetting getCurrentNotificationSetting() {
        for (Setting s : userVM.getCurrentUser().getSettings())
            if (s instanceof NotificationSetting)
                return (NotificationSetting) s;
        return null;
    }
}

