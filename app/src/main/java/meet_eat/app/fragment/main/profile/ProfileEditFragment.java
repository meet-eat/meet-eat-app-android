package meet_eat.app.fragment.main.profile;

import android.location.Address;
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

import java.io.IOException;
import java.util.Objects;

import meet_eat.app.MainActivity;
import meet_eat.app.R;
import meet_eat.app.databinding.FragmentProfileEditBinding;
import meet_eat.app.fragment.ContextFormatter;
import meet_eat.app.fragment.MenuSection;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.main.UserViewModel;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;
import meet_eat.data.location.Localizable;
import meet_eat.data.location.SphericalLocation;
import meet_eat.data.location.SphericalPosition;
import meet_eat.data.location.UnlocalizableException;

/**
 * This is the profile edit page. Here the user can change his profile details.
 */
public class ProfileEditFragment extends Fragment {

    private FragmentProfileEditBinding binding;
    private UserViewModel userVM;
    private NavController navController;
    private String phone;
    private String oldPasswordString;
    private String newPasswordString;
    private String passwordConfirm;
    private String home;
    private String description;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((MainActivity) requireActivity()).selectMenuItem(MenuSection.PROFILE.ordinal());
        binding = FragmentProfileEditBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        userVM = new ViewModelProvider(this).get(UserViewModel.class);
        navController = NavHostFragment.findNavController(this);
        setButtonOnClickListener();
        initUI();
        return binding.getRoot();
    }

    /**
     * Initializes the GUI by inserting the current profile information into the edit text views.
     */
    private void initUI() {
        User currentUser = userVM.getCurrentUser();
        binding.tvProfileEditEmail.setText(currentUser.getEmail().toString());
        binding.tvProfileEditUsername.setText(currentUser.getName());
        ContextFormatter contextFormatter = new ContextFormatter(binding.getRoot().getContext());
        binding.tvProfileEditBirthday.setText(contextFormatter.formatDate(currentUser.getBirthDay()));
        phone = currentUser.getPhoneNumber();

        // Have to catch in case the location the user typed in is invalid
        try {
            home = contextFormatter.formatStringFromLocalizable(currentUser.getLocalizable());
        } catch (IOException | UnlocalizableException exception) {
            Toast.makeText(getActivity(), R.string.toast_error_message, Toast.LENGTH_LONG).show();
            navController.navigateUp();
        }

        description = currentUser.getDescription();
    }

    /**
     * Sets various click listeners.
     */
    private void setButtonOnClickListener() {
        binding.btProfileEditChangePassword.setOnClickListener(event -> changePassword());
        binding.btProfileEditSave.setOnClickListener(event -> saveProfile());
        binding.ibtBack.setOnClickListener(event -> navController.navigateUp());
        // Disabled feature: adding an image
        // binding.ibtProfileEditAdd.setOnClickListener(event -> addImage());
    }

    /**
     * Checks old and new {@link Password password} for semantic correctness and thus changes the
     * {@link Password password} if and only if the input is valid.
     */
    private void changePassword() {
        if (Objects.isNull(oldPasswordString) && Objects.isNull(newPasswordString) && Objects.isNull(passwordConfirm)) {
            return;
        } else if (!Password.isLegalPassword(oldPasswordString) || !Password.isLegalPassword(newPasswordString)) {
            Toast.makeText(getActivity(), R.string.bad_passwords, Toast.LENGTH_SHORT).show();
            return;
        } else if (!newPasswordString.equals(passwordConfirm)) {
            Toast.makeText(getActivity(), R.string.passwords_not_matching, Toast.LENGTH_SHORT).show();
            return;
        }

        Password oldPassword = Password.createHashedPassword(oldPasswordString);
        Password newPassword = Password.createHashedPassword(newPasswordString);

        try {
            if (oldPassword.matches(userVM.getCurrentUser().getPassword())) {
                if (oldPassword.matches(newPassword)) {
                    Toast.makeText(getActivity(), R.string.passwords_matching, Toast.LENGTH_SHORT).show();
                    return;
                }
                userVM.getCurrentUser().setPassword(newPassword);
                try {
                    userVM.edit(userVM.getCurrentUser());
                    Toast.makeText(getActivity(), R.string.password_changed, Toast.LENGTH_SHORT).show();
                } catch (RequestHandlerException exception) {
                    Toast.makeText(getActivity(), R.string.toast_error_message, Toast.LENGTH_LONG).show();
                    Toast.makeText(getActivity(),
                            "DEBUG ProfileEditFragment.java -> changePassword(): " + exception.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity(), R.string.invalid_old_password, Toast.LENGTH_SHORT).show();
            }
        } catch (IllegalStateException exception) {
            Toast.makeText(getActivity(), R.string.request_new_login, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Checks the given input for semantic correctness, then tries to update the user.
     */
    private void saveProfile() {
        User currentUser = userVM.getCurrentUser();

        if (Objects.nonNull(phone) && !phone.isEmpty()) {
            currentUser.setPhoneNumber(phone);
        }

        if (Objects.nonNull(home) && !home.isEmpty()) {
            Address address;
            ContextFormatter contextFormatter = new ContextFormatter(binding.getRoot().getContext());

            try {
                address = contextFormatter.formatAddressFromString(home);
            } catch (IOException exception) {
                Toast.makeText(getActivity(), R.string.missing_location, Toast.LENGTH_SHORT).show();
                return;
            }

            if (Objects.isNull(address)) {
                Toast.makeText(getActivity(), R.string.invalid_location, Toast.LENGTH_SHORT).show();
                return;
            }

            Localizable localizable =
                    new SphericalLocation(new SphericalPosition(address.getLatitude(), address.getLongitude()));
            home = contextFormatter.formatStringFromAddress(address);
            currentUser.setLocalizable(localizable);
        }

        if (Objects.nonNull(description) && !description.isEmpty()) {
            currentUser.setDescription(description);
        }

        try {
            userVM.edit(currentUser);
            Toast.makeText(getActivity(), R.string.profile_edit_success, Toast.LENGTH_SHORT).show();
        } catch (RequestHandlerException exception) {
            Toast.makeText(getActivity(), R.string.toast_error_message, Toast.LENGTH_LONG).show();
        }
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOldPasswordString() {
        return oldPasswordString;
    }

    public void setOldPasswordString(String oldPasswordString) {
        this.oldPasswordString = oldPasswordString;
    }

    public String getNewPasswordString() {
        return newPasswordString;
    }

    public void setNewPasswordString(String newPasswordString) {
        this.newPasswordString = newPasswordString;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}