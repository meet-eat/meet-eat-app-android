package meet_eat.app.fragment.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import meet_eat.app.R;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);


        setButtonOnClickListener();

        return view;
    }

    private void setButtonOnClickListener() {
        view.findViewById(R.id.btRegister).setOnClickListener(event ->
                Navigation.findNavController(view).navigate(
                        LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                )
        );
    }
}
