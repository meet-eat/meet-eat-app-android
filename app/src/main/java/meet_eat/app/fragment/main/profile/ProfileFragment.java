package meet_eat.app.fragment.main.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import meet_eat.app.databinding.FragmentProfileBinding;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.repository.Session;
import meet_eat.app.viewmodel.main.UserViewModel;
import meet_eat.data.entity.user.User;

import static android.view.View.INVISIBLE;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private NavController navController;
    private UserViewModel userVM;
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        userVM = new ViewModelProvider(this).get(UserViewModel.class);
        navController = Navigation.findNavController(binding.getRoot());
        if (getArguments() != null)
            user = (User) getArguments().get("user");
        else
            goBack();

        updateUI();
        setButtonOnClickListener();
        return binding.getRoot();
    }

    private void updateUI() {
        binding.tvProfileUsername.setText(user.getName());
        binding.tvProfileDescription.setText(user.getDescription());
        binding.tvProfileBirthday.setText(user.getBirthDay().toString());
        binding.tvProfileRating.setText(String.format("%s", user.getHostRating()));
        // add image
        if (!user.equals(userVM.getCurrentUser())) {
            binding.ibtProfileEdit.setVisibility(INVISIBLE);
            binding.ibtProfileEdit.setClickable(false);
        } else {
            binding.ibtProfileReport.setVisibility(INVISIBLE);
            binding.ibtProfileReport.setClickable(false);
            binding.btProfileSubscribe.setVisibility(INVISIBLE);
            binding.btProfileSubscribe.setClickable(false);
        }
    }

    private void setButtonOnClickListener() {
        binding.ibtBack.setOnClickListener(event -> goBack());
        binding.btProfileSubscribe.setOnClickListener(event -> subscribe());
        binding.ibtProfileReport.setOnClickListener(event -> navigateToProfileReport());
        binding.ibtProfileEdit.setOnClickListener(event -> navigateToProfileEdit());
    }

    private void navigateToProfileEdit() {
        navController.navigate(ProfileFragmentDirections.actionProfileFragmentToProfileEditFragment());
    }

    private void navigateToProfileReport() {
        navController.navigate(ProfileFragmentDirections
                .actionProfileFragmentToProfileReportFragment(user));
    }

    private void subscribe() {
        try {
            userVM.subscribe(user);
        } catch (RequestHandlerException e) {
            // TODO
            e.printStackTrace();
        }
    }


    private void goBack() {
        navController.popBackStack();
    }
}
