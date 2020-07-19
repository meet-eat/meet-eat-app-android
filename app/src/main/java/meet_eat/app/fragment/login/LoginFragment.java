package meet_eat.app.fragment.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);

        setButtonOnClickListener();
        return view;
    }

    private void setButtonOnClickListener() {
        view.findViewById(R.id.btRegister)
                .setOnClickListener(event ->
                        Navigation.findNavController(view).navigate(
                                LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                        )
                );
        view.findViewById(R.id.btLogin).setOnClickListener(this::login);
        view.findViewById(R.id.tvReset).setOnClickListener(this::reset);
    }

    private void login(View v) {
        // TODO

        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    private void reset(View v) {
        // TODO
    }

    /*
     * method is used for checking valid email id format.
     */
    private boolean isEmailValid(String email) {

        String expression = ""; // TODO include regex
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
