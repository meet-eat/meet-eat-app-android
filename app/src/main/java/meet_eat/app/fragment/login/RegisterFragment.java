package meet_eat.app.fragment.login;

import android.app.DatePickerDialog;
import android.location.Address;
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

import com.google.common.base.Strings;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

import meet_eat.app.R;
import meet_eat.app.databinding.FragmentRegisterBinding;
import meet_eat.app.fragment.ContextFormatter;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.login.RegisterViewModel;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;
import meet_eat.data.location.Localizable;
import meet_eat.data.location.SphericalLocation;
import meet_eat.data.location.SphericalPosition;

/**
 * This is the registration page. Here the user can register with his personal data or go back to the login page.
 *
 * @see RegisterViewModel
 */
public class RegisterFragment extends Fragment {

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

    /**
     * Set various click listeners.
     */
    private void setButtonOnClickListener() {
        binding.ibtBack.setOnClickListener(event -> navController.navigateUp());
        binding.tvRegisterBirthdate.setOnClickListener(event -> showDatePicker());
        binding.btRegister.setOnClickListener(event -> register());
    }

    /**
     * Shows a dialog where the user can pick his birthday. Updates the GUI.
     */
    private void showDatePicker() {
        Calendar cal = new GregorianCalendar();
        new DatePickerDialog(binding.getRoot().getContext(), (datePicker, year, month, dayOfMonth) -> {
            birthDay = LocalDate.of(year, month + ContextFormatter.MONTH_CORRECTION, dayOfMonth);
            ContextFormatter contextFormatter = new ContextFormatter(binding.getRoot().getContext());
            binding.tvRegisterBirthdate.setText(contextFormatter.formatDate(birthDay));
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * Checks the given input for semantic correctness and tries to register a new user.
     */
    private void register() {
        ContextFormatter contextFormatter = new ContextFormatter(binding.getRoot().getContext());

        if (!Email.isLegalEmailAddress(email)) {
            Toast.makeText(getActivity(), R.string.bad_email, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Password.isLegalPassword(password)) {
            Toast.makeText(getActivity(), R.string.bad_password, Toast.LENGTH_SHORT).show();
            return;
        }

        if (Objects.isNull(username) || username.isEmpty()) {
            Toast.makeText(getActivity(), R.string.missing_username, Toast.LENGTH_SHORT).show();
            return;
        }

        if (Objects.isNull(birthDay)) {
            Toast.makeText(getActivity(), R.string.missing_date, Toast.LENGTH_SHORT).show();
            return;
        } else if (birthDay.isAfter(LocalDateTime.now().toLocalDate())) {
            Toast.makeText(getActivity(), R.string.invalid_date_time_future, Toast.LENGTH_SHORT).show();
            return;
        }

        Address address;

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

        phoneNumber = Strings.nullToEmpty(phoneNumber);
        profileDescription = Strings.nullToEmpty(profileDescription);
        Email emailParam = new Email(this.email);
        Password hashedPassword = Password.createHashedPassword(this.password);
        User user = new User(emailParam, hashedPassword, birthDay, username, phoneNumber, profileDescription, false,
                localizable);

        try {
            registerVM.register(user);
            navController.navigate(R.id.loginFragment);
            Toast.makeText(getActivity(), R.string.request_sent, Toast.LENGTH_SHORT).show();
        } catch (RequestHandlerException exception) {
            Toast.makeText(getActivity(), R.string.toast_error_message, Toast.LENGTH_LONG).show();
        }
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