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
 * Manages registration-related information.
 */
public class RegisterFragment extends Fragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);

        setButtonOnClickListener();

        return view;
    }

    private void setButtonOnClickListener() {
        view.findViewById(R.id.ibtBack).setOnClickListener(event ->
                Navigation.findNavController(view).popBackStack()
        );
        view.findViewById(R.id.btRegister).setOnClickListener(this::doRegister);
    }


    private void doRegister(View view) {
        // TODO: call ViewModel
    }
}
