package meet_eat.app.fragment.main.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import meet_eat.app.databinding.FragmentSettingsDisplayBinding;
import meet_eat.app.viewmodel.main.SettingsViewModel;

/**
 * Not yet implemented.
 * This is the display settings fragment. The user can choose between light mode, dark mode and the system wide mode.
 */
public class SettingsDisplayFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        meet_eat.app.databinding.FragmentSettingsDisplayBinding binding =
                FragmentSettingsDisplayBinding.inflate(inflater, container, false);
        SettingsViewModel settingsVM = new ViewModelProvider(this).get(SettingsViewModel.class);
        return binding.getRoot();
    }
}