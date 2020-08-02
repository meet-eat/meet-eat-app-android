package meet_eat.app.fragment.main.profile;

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
import meet_eat.app.databinding.FragmentProfileEditBinding;
import meet_eat.app.fragment.ContextFormatter;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.main.UserViewModel;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;

public class ProfileEditFragment extends Fragment {

    private FragmentProfileEditBinding binding;
    private UserViewModel userVM;
    private String phone;
    private String oldPasswordString;
    private String newPasswordString;
    private String home;
    private String description;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileEditBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        userVM = new ViewModelProvider(this).get(UserViewModel.class);
        navController = NavHostFragment.findNavController(this);
        setButtonOnClickListener();
        initUI();
        return binding.getRoot();
    }

    private void initUI() {
        User currentUser = userVM.getCurrentUser();
        binding.tvProfileEditEmail.setText(currentUser.getEmail().toString());
        binding.tvProfileEditUsername.setText(currentUser.getName());
        ContextFormatter contextFormatter = new ContextFormatter(binding.getRoot().getContext());
        binding.tvProfileEditBirthday.setText(contextFormatter.formatDate(currentUser.getBirthDay()));
        phone = currentUser.getPhoneNumber();
        // TODO binding.etProfileEditHome.setText(currentUser.getHome());
        description = currentUser.getDescription();
    }

    private void setButtonOnClickListener() {
        binding.btProfileEditChangePassword.setOnClickListener(event -> changePassword());
        binding.btProfileEditSave.setOnClickListener(event -> saveProfile());
        binding.ibtProfileEditAdd.setOnClickListener(event -> addImage());
        binding.ibtBack.setOnClickListener(event -> navController.navigateUp());
    }

    private void changePassword() {

        if (!Password.isLegalPassword(oldPasswordString) || !Password.isLegalPassword(newPasswordString)) {
            Toast.makeText(getActivity(), R.string.bad_passwords,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Password oldPassword = Password.createHashedPassword(oldPasswordString);
        Password newPassword = Password.createHashedPassword(newPasswordString);

        if (oldPassword.equals(userVM.getCurrentUser().getPassword())) {
            userVM.getCurrentUser().setPassword(newPassword);

            try {
                userVM.edit(userVM.getCurrentUser());
            } catch (RequestHandlerException e) {
                // TODO resolve error code
                Toast.makeText(getActivity(),
                        "DEBUG ProfileEditFragment.java -> changePassword(): " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }

            Toast.makeText(getActivity(), R.string.password_changed, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), R.string.invalid_old_password, Toast.LENGTH_SHORT).show();
        }

    }

    private void addImage() {
        // TODO
    }

    private void saveProfile() {
        User currentUser = userVM.getCurrentUser();

        if (phone != null && !phone.isEmpty()) {
            currentUser.setPhoneNumber(phone);
        }

        if (home != null && !home.isEmpty()) {
            // TODO
        }

        if (description != null && !description.isEmpty()) {
            currentUser.setDescription(description);
        }

        try {
            userVM.edit(currentUser);
            Toast.makeText(getActivity(), R.string.profile_edit_success, Toast.LENGTH_SHORT).show();
        } catch (RequestHandlerException e) {
            // TODO resolve error code
            Toast.makeText(getActivity(),
                    "DEBUG RegisterFragment.java -> saveProfile(): " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
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
}
