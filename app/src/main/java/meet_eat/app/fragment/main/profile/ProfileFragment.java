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
import androidx.navigation.fragment.NavHostFragment;

import meet_eat.app.databinding.FragmentProfileBinding;
import meet_eat.app.viewmodel.main.OfferViewModel;
import meet_eat.data.entity.user.User;

import static android.view.View.INVISIBLE;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private NavController navController;
    private OfferViewModel offerVM;
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        offerVM = new ViewModelProvider(this).get(OfferViewModel.class);
        navController = NavHostFragment.findNavController(this);
        user = offerVM.getUser();

        if (user == null) {
            navController.popBackStack();
        }

        updateUI();
        setButtonOnClickListener();
        return binding.getRoot();
    }

    private void setButtonOnClickListener() {
        binding.ibtBack.setOnClickListener(event -> goBack());
        //binding.btProfileSubscribe.setOnClickListener(event -> subscribe());
        binding.ibtProfileReport.setOnClickListener(event -> navigateToProfileReport());
        binding.ibtProfileEdit.setOnClickListener(event -> navigateToProfileEdit());
    }

    private void updateUI() {
        binding.tvProfileUsername.setText(user.getName());
        binding.tvProfileDescription.setText(user.getDescription());
        binding.tvProfileBirthday.setText(user.getBirthDay().toString());
        binding.tvProfileRating.setText(String.format("%s", user.getHostRating()));
        // add image

        if (!user.equals(offerVM.getCurrentUser())) {
            binding.ibtProfileEdit.setVisibility(INVISIBLE);
            binding.ibtProfileEdit.setClickable(false);
        } else {
            binding.ibtProfileReport.setVisibility(INVISIBLE);
            binding.ibtProfileReport.setClickable(false);
            binding.btProfileSubscribe.setVisibility(INVISIBLE);
            binding.btProfileSubscribe.setClickable(false);
        }

        // TODO if subscribed change text to "deabonnieren" oder so
    }

    private void navigateToProfileEdit() {
        navController.navigate(ProfileFragmentDirections
                .actionProfileFragmentToProfileEditFragment());
    }

    private void navigateToProfileReport() {
        navController.navigate(ProfileFragmentDirections
                .actionProfileFragmentToProfileReportFragment());
    }

    /*private void subscribe() {

        try {
            offerVM.subscribe(user);
        } catch (RequestHandlerException e) {
            // TODO
            e.printStackTrace();
        }

    }*/


    private void goBack() {
        navController.popBackStack();
    }
}