package meet_eat.app.fragment.login;

import android.content.Intent;
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

import meet_eat.app.MainActivity;
import meet_eat.app.R;
import meet_eat.app.databinding.FragmentLoginBinding;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.login.LoginViewModel;
import meet_eat.data.entity.user.Email;

/**
 * This is the login page. It is the first page the user sees when opening the app. The user can
 * log in by providing his email and password or request to reset his password by providing only
 * his email address.
 *
 * @see LoginViewModel
 */
public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private NavController navController;
    private LoginViewModel loginVM;
    private String email, password;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        loginVM = new ViewModelProvider(this).get(LoginViewModel.class);
        navController = NavHostFragment.findNavController(this);
        setButtonOnClickListener();
        return binding.getRoot();
    }

    private void setButtonOnClickListener() {
        binding.btLoginRegister.setOnClickListener(event -> navigateToRegister());
        binding.btLogin.setOnClickListener(event -> login());
        binding.tvLoginReset.setOnClickListener(event -> reset());
    }

    private void navigateToRegister() {
        navController.navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment());
    }

    private void login() {

        /*if (!Email.isLegalEmailAddress(email) || !Password.isLegalPassword(password)) {
            Toast.makeText(getActivity(), R.string.bad_login, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            loginVM.login(email, password);
        } catch (RequestHandlerException e) {
            // TODO catch error on login
            Toast.makeText(getActivity(), "Exception " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }*/

        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    private void reset() {

        if (!Email.isLegalEmailAddress(email)) {
            Toast.makeText(getActivity(), R.string.bad_email, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            loginVM.resetPassword(email);
        } catch (RequestHandlerException e) {
            // TODO catch error on reset request
            Toast.makeText(getActivity(), "Exception " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(getActivity(), R.string.request_send, Toast.LENGTH_SHORT).show();
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
}