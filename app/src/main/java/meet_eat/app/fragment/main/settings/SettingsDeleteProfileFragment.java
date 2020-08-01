package meet_eat.app.fragment.main.settings;

import android.os.Bundle;
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

import meet_eat.app.R;
import meet_eat.app.databinding.FragmentSettingsDeleteProfileBinding;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.main.SettingsViewModel;
import meet_eat.data.entity.user.Password;

public class SettingsDeleteProfileFragment extends Fragment {

    private FragmentSettingsDeleteProfileBinding binding;
    private SettingsViewModel settingsVM;
    private String password;
    private NavController navController;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsDeleteProfileBinding.inflate(inflater, container, false);
        settingsVM = new ViewModelProvider(this).get(SettingsViewModel.class);
        navController = NavHostFragment.findNavController(this);
        setButtonOnClickListener();
        return binding.getRoot();
    }

    private void setButtonOnClickListener() {
        binding.btSettingsDeleteProfile.setOnClickListener(event -> deleteProfile());
        binding.ibtBack.setOnClickListener(event -> navController.navigateUp());
    }

    private void deleteProfile() {

        if (!Password.isLegalPassword(password)) {
            Toast.makeText(getActivity(), R.string.bad_password, Toast.LENGTH_SHORT).show();
        }

        if (Password.createHashedPassword(password).equals(settingsVM.getCurrentUser().getPassword())) {

            try {
                settingsVM.deleteUser(settingsVM.getCurrentUser());
                // TODO toast?
            } catch (RequestHandlerException e) {
                // TODO
            }

        } else {
            Toast.makeText(getActivity(), R.string.invalid_password, Toast.LENGTH_SHORT).show();
        }

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}