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
 * Repräsentiert die Anmeldeansicht der Applikation.
 */
public class LoginFragment extends Fragment {

    View view;

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
