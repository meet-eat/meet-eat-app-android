package meet_eat.app.fragment.main.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import meet_eat.app.MainActivity;
import meet_eat.app.databinding.FragmentSettingsNotificationBinding;
import meet_eat.app.fragment.MenuSection;
import meet_eat.app.viewmodel.main.SettingsViewModel;

/**
 * This is the notification settings fragment. The user can choose (for example) the notification time before a meeting.
 */
public class SettingsNotificationFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((MainActivity) requireActivity()).selectMenuItem(MenuSection.SETTINGS.ordinal());
        meet_eat.app.databinding.FragmentSettingsNotificationBinding binding =
                FragmentSettingsNotificationBinding.inflate(inflater, container, false);
        SettingsViewModel settingsVM = new ViewModelProvider(this).get(SettingsViewModel.class);
        return binding.getRoot();
    }
}