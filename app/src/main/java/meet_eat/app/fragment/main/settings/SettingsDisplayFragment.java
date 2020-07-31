package meet_eat.app.fragment.main.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import meet_eat.app.R;
import meet_eat.app.databinding.FragmentSettingsDisplayBinding;
import meet_eat.app.viewmodel.main.SettingsViewModel;

public class SettingsDisplayFragment extends Fragment {

    private FragmentSettingsDisplayBinding binding;
    private SettingsViewModel settingsVM;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsDisplayBinding.inflate(inflater, container, false);
        settingsVM = new ViewModelProvider(this).get(SettingsViewModel.class);
        return binding.getRoot();
    }
}
