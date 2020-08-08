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

import meet_eat.app.LoginActivity;
import meet_eat.app.R;
import meet_eat.app.databinding.FragmentSettingsDeleteProfileBinding;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.main.SettingsViewModel;
import meet_eat.data.entity.user.Password;

/**
 * This is the page where the user can delete his profile.
 * After deleting the profile, returns to the login page.
 *
 * @see meet_eat.app.fragment.login.LoginFragment
 */
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
        binding.setFragment(this);
        settingsVM = new ViewModelProvider(this).get(SettingsViewModel.class);
        navController = NavHostFragment.findNavController(this);
        setButtonOnClickListener();
        return binding.getRoot();
    }

    /**
     * Sets various click listeners.
     */
    private void setButtonOnClickListener() {
        binding.btSettingsDeleteProfile.setOnClickListener(event -> deleteProfile());
        binding.ibtBack.setOnClickListener(event -> navController.navigateUp());
    }

    /**
     * Checks the password for semantic correctness, checks if the password is correct (the users actual password),
     * then tries to delete the profile and go back to the login page
     */
    private void deleteProfile() {
        if (!Password.isLegalPassword(password)) {
            Toast.makeText(getActivity(), R.string.bad_password, Toast.LENGTH_SHORT).show();
            return;
        }

        if (Password.createHashedPassword(password).matches(settingsVM.getCurrentUser().getPassword())) {

            try {
                settingsVM.deleteUser(settingsVM.getCurrentUser());
                startActivity(new Intent(getActivity(), LoginActivity.class));
            } catch (RequestHandlerException e) {
                Toast.makeText(getActivity(), R.string.request_handler_exception_toast_error_message, Toast.LENGTH_LONG)
                        .show();
                Log.i("DEBUG", "SettingsDeleteProfileFragment.deleteProfile: " + e.getMessage());
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