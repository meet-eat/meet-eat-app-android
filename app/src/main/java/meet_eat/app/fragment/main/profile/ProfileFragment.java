package meet_eat.app.fragment.main.profile;

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

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

import meet_eat.app.MainActivity;
import meet_eat.app.R;
import meet_eat.app.databinding.FragmentProfileBinding;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.main.UserViewModel;
import meet_eat.data.entity.user.User;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static meet_eat.app.fragment.NavigationArgumentKey.USER;

/**
 * This is the profile page. Here the user can see either his own or another users profile.
 */
public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private UserViewModel userVM;
    private NavController navController;
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((MainActivity) requireActivity()).selectMenuItem(0);
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        userVM = new ViewModelProvider(this).get(UserViewModel.class);
        navController = NavHostFragment.findNavController(this);

        // Checks if the previous page sent a bundle of arguments containing the user whose profile is to be shown
        if (Objects.isNull(getArguments())) {
            // If no user is given by the previous fragment, show the currently logged in user
            user = userVM.getCurrentUser();
        } else if (Objects.isNull(getArguments().getSerializable(USER.name()))) {
            // If a user was given, but is null
            Toast.makeText(getActivity(), getString(R.string.toast_error_message), Toast.LENGTH_SHORT).show();
            navController.navigateUp();
        } else {
            user = (User) getArguments().getSerializable(USER.name());
        }

        updateUI();
        setButtonOnClickListener();
        return binding.getRoot();
    }

    /**
     * Sets various click listeners.
     */
    private void setButtonOnClickListener() {
        binding.ibtBack.setOnClickListener(event -> navController.navigateUp());
        binding.btProfileSubscribe.setOnClickListener(event -> changeSubscription());
        binding.ibtProfileEdit.setOnClickListener(event -> navController.navigate(R.id.profileEditFragment));
        binding.ibtProfileReport.setOnClickListener(event -> navigateToProfileReport());
    }

    /**
     * Initializes the GUI when opening a profile by setting the text views, and showing/hiding buttons. Also updates
     * the UI when a button has been clicked.
     */
    private void updateUI() {
        binding.tvProfileUsername.setText(user.getName());
        binding.tvProfileDescription.setText(user.getDescription());
        binding.tvProfileBirthday.setText(getAge());

        // Get the host rating of the user and show it in the UI
        try {
            binding.tvProfileHostRating.setText(String.valueOf(userVM.getNumericHostRating(user)));
        } catch (RequestHandlerException exception) {
            binding.tvProfileHostRating.setVisibility(INVISIBLE);
        }

        // Get the guest rating of the user and show it in the UI
        try {
            binding.tvProfileGuestRating.setText(String.valueOf(userVM.getNumericGuestRating(user)));
        } catch (RequestHandlerException exception) {
            binding.tvProfileGuestRating.setVisibility(INVISIBLE);
        }

        // add profile image

        if (user.getIdentifier().equals(userVM.getCurrentUser().getIdentifier())) {
            binding.ibtProfileReport.setVisibility(GONE);
            binding.btProfileSubscribe.setVisibility(INVISIBLE);
        } else {
            binding.ibtProfileEdit.setVisibility(GONE);

            try {
                if (userVM.getSubscribedUsers().contains(user)) {
                    binding.btProfileSubscribe.setText(getResources().getString(R.string.unsubscribe));
                } else {
                    binding.btProfileSubscribe.setText((getResources().getString(R.string.subscribe)));
                }
            } catch (RequestHandlerException exception) {
                binding.btProfileSubscribe.setVisibility(GONE);
                Toast.makeText(getActivity(), R.string.toast_error_message, Toast.LENGTH_SHORT).show();
                Log.i("DEBUG", "In ProfileFragment.updateUI: " + exception.getMessage());
            }
        }
    }

    /**
     * Calculates the age of the user shown in the profile page.
     *
     * @return the age of the user in the profile page
     */
    private String getAge() {
        return String.valueOf(Period.between(user.getBirthDay(), LocalDate.now()).getYears());
    }

    /**
     * Currently disabled feature.
     * Navigates to the profile report page, giving the shown user as an argument.
     */
    private void navigateToProfileReport() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER.name(), user);
        navController.navigate(R.id.profileReportFragment, bundle);
    }

    /**
     * Tries to subscribe the currently logged in user to the user shown in the page.
     * If they are already subscribed, unsubscribe.
     * After (un)subscribing, calls updateUI() to change the buttons text.
     */
    private void changeSubscription() {
        try {
            if (!userVM.getSubscribedUsers().contains(user)) {
                userVM.subscribe(user);
            } else {
                userVM.unsubscribe(user);
            }

            updateUI();
        } catch (RequestHandlerException exception) {
            Toast.makeText(getActivity(), R.string.toast_error_message, Toast.LENGTH_LONG).show();
        }
    }
}