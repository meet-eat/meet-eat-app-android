package meet_eat.app.fragment.main.rating;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import meet_eat.app.R;
import meet_eat.app.databinding.FragmentProfileSubscribedBinding;
import meet_eat.app.databinding.FragmentRateGuestsBinding;
import meet_eat.app.fragment.main.profile.ProfileSubscribedAdapter;
import meet_eat.app.viewmodel.main.UserViewModel;
import meet_eat.data.entity.user.User;

public class RateGuestsFragment extends Fragment {

    private FragmentRateGuestsBinding binding;
    private UserViewModel userVM;
    private RateGuestsAdapter rateGuestsAdapter;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRateGuestsBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        userVM = new ViewModelProvider(this).get(UserViewModel.class);
        binding.rvRateGuests.setAdapter(rateGuestsAdapter);
        binding.rvRateGuests.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        rateGuestsAdapter = new RateGuestsAdapter(userVM, new ArrayList<User>());
        navController = Navigation.findNavController(binding.getRoot());
        setButtonOnClickListener();
        return binding.getRoot();
    }

    private void setButtonOnClickListener() {
    }
}
