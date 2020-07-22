package meet_eat.app.fragment.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import java.time.LocalDate;

import meet_eat.app.R;
import meet_eat.app.viewmodel.login.RegisterViewModel;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.Role;
import meet_eat.data.entity.user.User;

/**
 * Manages registration-related information.
 */
public class RegisterFragment extends Fragment {
    private View view;
    private RegisterViewModel registerVM;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_register, container, false);
        registerVM = new ViewModelProvider(this).get(RegisterViewModel.class);
        setButtonOnClickListener();
        return view;
    }

    private void setButtonOnClickListener() {
        view.findViewById(R.id.ibtBack).setOnClickListener(event -> Navigation.findNavController(view).popBackStack());
        view.findViewById(R.id.btRegister).setOnClickListener(event -> register());
    }

    private void navigateToLogin() {
        Navigation.findNavController(view).navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment());
    }

    private void register() {
        /* TODO DatePicker */
        int year = 1;
        int month = 1;
        int dayOfMonth = 1;
        LocalDate birthDay = LocalDate.of(year, month, dayOfMonth);

        String emailString = ((EditText) view.findViewById(R.id.etEmail)).getText().toString();
        String passwordString =
                ((EditText) view.findViewById(R.id.etPassword)).getText().toString();
        String name = ((EditText) view.findViewById(R.id.etName)).getText().toString();
        String description =
                ((EditText) view.findViewById(R.id.etDescription)).getText().toString();
        String phoneNumber = ((EditText) view.findViewById(R.id.etPhone)).getText().toString();

        /* TODO Home with Google MapView */
        String home = ((EditText) view.findViewById(R.id.etHome)).getText().toString();

        try {
            Email email = new Email(emailString);
            Password password = new Password(passwordString);
            User user = new User(email, password, birthDay, name, phoneNumber, description, false);
            /* TODO user.addPredicate(home); */
            registerVM.register(user);
            navigateToLogin();
        } catch (IllegalArgumentException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}