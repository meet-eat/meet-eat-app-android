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
import androidx.navigation.Navigation;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import meet_eat.app.R;
import meet_eat.app.databinding.FragmentRegisterBinding;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.login.RegisterViewModel;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;

/**
 * Manages registration-related information.
 */
public class RegisterFragment extends Fragment {

    // TODO Extract EUROPEAN_DATE_FORMAT to strings.xml
    private static final String EUROPEAN_DATE_FORMAT = "dd.MM.uuuu";
    private static final int MONTH_CORRECTION = 1;

    private FragmentRegisterBinding binding;
    private RegisterViewModel registerVM;
    private String email;
    private String password;
    private String username;
    private String phoneNumber;
    private String profileDescription;
    private LocalDate birthDay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        registerVM = new ViewModelProvider(this).get(RegisterViewModel.class);
        setButtonOnClickListener();
        return binding.getRoot();
    }

    private void setButtonOnClickListener() {
        binding.ibtBack.setOnClickListener(event -> Navigation.findNavController(binding.getRoot())
                .popBackStack());
        binding.tvRegisterBirthdate.setOnClickListener(event -> showDatePicker());
        binding.btRegister.setOnClickListener(event -> register());
    }

    private void showDatePicker() {
        Calendar cal = new GregorianCalendar();
        new DatePickerDialog(binding.getRoot().getContext(), (datePicker, year, month, dayOfMonth)
                -> {
            birthDay = LocalDate.of(year, month + MONTH_CORRECTION, dayOfMonth);
            binding.tvRegisterBirthdate.setText(birthDay.format(DateTimeFormatter
                    .ofPattern(EUROPEAN_DATE_FORMAT)));
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void register() {

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

        Address address = getLocationFromUI();
        if (address == null) {
            Toast.makeText(getActivity(), "Heimatort gibt es nicht (laut google)", Toast.LENGTH_SHORT).show();
            return;
        }
        // TODO add location to user object

        try {
            registerVM.register(user);
        } catch (RequestHandlerException e) {
            // TODO catch error on user create
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        navigateToLogin();
        Toast.makeText(getActivity(), R.string.request_send, Toast.LENGTH_SHORT).show();
    }

    private Address getLocationFromUI() {
        // TODO cleanup
        Geocoder geocoder = new Geocoder(binding.getRoot().getContext(), Locale.GERMANY);
        List<Address> addressList = null;
        Address address = null;
        try {

            if (geocoder.getFromLocationName(binding.etRegisterHome.getText().toString(), 1) != null)
                addressList = geocoder.getFromLocationName(binding.etRegisterHome.getText().toString(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addressList != null && addressList.size() > 0)
            address = addressList.get(0);

        if (address != null && address.getSubAdminArea() != null) {
            if (address.getPostalCode() != null)
                binding.etRegisterHome.setText(new StringBuilder().append(address.getSubAdminArea()).append(", ").append(address.getPostalCode()).toString());
            else
                binding.etRegisterHome.setText(new StringBuilder().append(address.getFeatureName()).append(", ").append(address.getSubAdminArea()));
        } else {
            return null;
        }
        return address;
    }

    private void navigateToLogin() {
        Navigation.findNavController(binding.getRoot()).navigate(RegisterFragmentDirections
                .actionRegisterFragmentToLoginFragment());
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
}