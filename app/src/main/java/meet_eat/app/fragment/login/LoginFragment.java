package meet_eat.app.fragment.login;

import android.content.Intent;
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

import meet_eat.app.MainActivity;
import meet_eat.app.R;
import meet_eat.app.viewmodel.login.LoginViewModel;

/**
 * This is the login page. It is the first page the user sees when opening the app. The user can
 * log in by providing his email and password or request to reset his password by providing only
 * his email address.
 *
 * @see LoginViewModel
 */
public class LoginFragment extends Fragment {

    private View view;
    private LoginViewModel loginVM;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        loginVM = new ViewModelProvider(this).get(LoginViewModel.class);
        setButtonOnClickListener();
        return view;
    }

    private void setButtonOnClickListener() {
        view.findViewById(R.id.btRegister).setOnClickListener(event -> navigateToRegister());
        view.findViewById(R.id.btLogin).setOnClickListener(event -> login());
        view.findViewById(R.id.tvReset).setOnClickListener(event -> reset());
    }

    private void navigateToRegister() {
        Navigation.findNavController(view).navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment());
    }

    private void login() {
        String email = ((EditText) view.findViewById(R.id.etEmail)).getText().toString();
        String password = ((EditText) view.findViewById(R.id.etPassword)).getText().toString();

        try {
            loginVM.login(email, password);
        } catch (IllegalArgumentException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        //Switch to main app with logged in state
        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    private void reset() {
        String email = ((EditText) view.findViewById(R.id.etEmail)).getText().toString();
        try {
            loginVM.resetPassword(email);
        } catch (IllegalArgumentException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}