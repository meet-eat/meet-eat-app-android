package meet_eat.app.fragment.main.profile;

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

import java.time.LocalDate;
import java.time.Period;

import meet_eat.app.R;
import meet_eat.app.databinding.FragmentProfileBinding;
import meet_eat.app.repository.RequestHandlerException;
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
        navController = NavHostFragment.findNavController(this);

        if (getArguments() == null) {
            user = userVM.getCurrentUser();
        } else if (getArguments().getSerializable("user") == null) {
            Toast.makeText(getActivity(), "DEBUG: User not given", Toast.LENGTH_SHORT).show();
            navController.navigateUp();
        } else {
            user = (User) getArguments().getSerializable("user");
        }

        updateUI();
        setButtonOnClickListener();
        return binding.getRoot();
    }

    private void setButtonOnClickListener() {
        binding.ibtBack.setOnClickListener(event -> navController.navigateUp());
        binding.btProfileSubscribe.setOnClickListener(event -> subscribe());
        binding.ibtProfileReport.setOnClickListener(event -> navigateToProfileReport());
        binding.ibtProfileEdit.setOnClickListener(event -> navigateToProfileEdit());
    }

    private void updateUI() {
        binding.tvProfileUsername.setText(user.getName());
        binding.tvProfileDescription.setText(user.getDescription());
        binding.tvProfileBirthday.setText(getAge());
        binding.tvProfileRating.setText(String.format("%s", user.getHostRating()));
        // TODO profile image

        if (user.equals(userVM.getCurrentUser())) {
            binding.ibtProfileReport.setVisibility(INVISIBLE);
            binding.ibtProfileReport.setClickable(false);
            binding.btProfileSubscribe.setVisibility(INVISIBLE);
            binding.btProfileSubscribe.setClickable(false);
        } else {
            binding.ibtProfileEdit.setVisibility(INVISIBLE);
            binding.ibtProfileEdit.setClickable(false);

            if (userVM.getCurrentUser().getSubscriptions().contains(user)) {
                binding.btProfileSubscribe.setText(getResources().getString(R.string.unsubscribe));
            } else {
                binding.btProfileSubscribe.setText((getResources().getString(R.string.subscribe)));
            }

        }

    }

    private int getAge() {
        return Period.between(user.getBirthDay(), LocalDate.now()).getYears();
    }

    private void navigateToProfileEdit() {
        navController.navigate(R.id.profileEditFragment);
    }

    private void navigateToProfileReport() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        navController.navigate(R.id.profileReportFragment, bundle);
    }

    private void subscribe() {

        try {
            userVM.subscribe(user);
            updateUI();
        } catch (RequestHandlerException e) {
            // TODO non runtime exceptions
            e.printStackTrace();
        }

    }
}