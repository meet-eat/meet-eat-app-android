package meet_eat.app.fragment.login;

import android.app.DatePickerDialog;
import android.location.Address;
import android.location.Geocoder;
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
import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import meet_eat.app.R;
import meet_eat.app.databinding.FragmentRegisterBinding;
import meet_eat.app.fragment.ContextFormatter;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.login.RegisterViewModel;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;

/**
 * Manages registration-related information.
 */
public class RegisterFragment extends Fragment {

    private static final int MONTH_CORRECTION = 1;

    private FragmentRegisterBinding binding;
    private RegisterViewModel registerVM;
    private NavController navController;
    private LocalDate birthDay;
    private String email;
    private String home;
    private String password;
    private String username;
    private String phoneNumber;
    private String profileDescription;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        registerVM = new ViewModelProvider(this).get(RegisterViewModel.class);
        navController = NavHostFragment.findNavController(this);
        setButtonOnClickListener();
        return binding.getRoot();
    }

    private void setButtonOnClickListener() {
        binding.ibtBack.setOnClickListener(event -> navController.navigateUp());
        binding.tvRegisterBirthdate.setOnClickListener(event -> showDatePicker());
        binding.btRegister.setOnClickListener(event -> register());
    }

    private void showDatePicker() {
        Calendar cal = new GregorianCalendar();
        new DatePickerDialog(binding.getRoot().getContext(), (datePicker, year, month,
                                                              dayOfMonth) -> {
            birthDay = LocalDate.of(year, month + MONTH_CORRECTION, dayOfMonth);
            ContextFormatter contextFormatter =
                    new ContextFormatter(binding.getRoot().getContext());
            binding.tvRegisterBirthdate.setText(contextFormatter.formatDate(birthDay));
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void register() {
        ContextFormatter contextFormatter =
                new ContextFormatter(binding.getRoot().getContext());
        // TODO profile image

        if (!Email.isLegalEmailAddress(email)) {
            Toast.makeText(getActivity(), R.string.bad_email, Toast.LENGTH_SHORT).show();
            return;
        } else if (!Password.isLegalPassword(password)) {
            Toast.makeText(getActivity(), R.string.bad_password, Toast.LENGTH_SHORT).show();
            return;
        } else if ((username == null) || username.isEmpty()) {
            Toast.makeText(getActivity(), R.string.missing_username, Toast.LENGTH_SHORT).show();
            return;
        }

        Email emailParam = new Email(this.email);
        Password hashedPassword = Password.createHashedPassword(this.password);
        User user = new User(emailParam, hashedPassword, birthDay, username, phoneNumber,
                profileDescription, false);
        Address address = contextFormatter.getLocationFromString(home);

        if (address == null) {
            Toast.makeText(getActivity(), R.string.invalid_location, Toast.LENGTH_SHORT).show();
            return;
        }

        home = contextFormatter.getStringFromLocation(address);
        // TODO location of user

        try {
            registerVM.register(user);
        } catch (RequestHandlerException e) {
            // TODO resolve error code
            Toast.makeText(getActivity(),
                    "DEBUG RegisterFragment.java -> register(): " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        navigateToLogin();
        Toast.makeText(getActivity(), R.string.request_send, Toast.LENGTH_SHORT).show();
    }

    private void navigateToLogin() {
        navController.navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment());
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfileDescription() {
        return profileDescription;
    }

    public void setProfileDescription(String profileDescription) {
        this.profileDescription = profileDescription;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }
}