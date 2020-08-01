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
import androidx.navigation.Navigation;

import meet_eat.app.databinding.FragmentProfileEditBinding;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileEditBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        userVM = new ViewModelProvider(this).get(UserViewModel.class);
        setButtonOnClickListener();
        return binding.getRoot();
    }

    private void setButtonOnClickListener() {
        binding.btProfileEditChangePassword.setOnClickListener(event -> changePassword());
        binding.btProfileEditSave.setOnClickListener(event -> saveProfile());
        binding.ibtBack.setOnClickListener(event -> goBack());
    }

    private void changePassword() {

        if (!Password.isLegalPassword(oldPasswordString) || !Password
                .isLegalPassword(newPasswordString)) {
            Toast.makeText(getActivity(), "Altes Passwort falsch oder neues ungültig",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Password oldPassword = Password.createHashedPassword(oldPasswordString);
        Password newPassword = Password.createHashedPassword(newPasswordString);

        if (oldPassword.equals(userVM.getCurrentUser().getPassword())) {
            userVM.getCurrentUser().setPassword(newPassword);
            Toast.makeText(getActivity(), "Passwort erfolgreich geändert", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Altes Passwort falsch", Toast.LENGTH_SHORT).show();
        }

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
        } catch (RequestHandlerException e) {
            // TODO timeout etc.
        }

    }

    private void goBack() {
        Navigation.findNavController(binding.getRoot()).popBackStack();
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
