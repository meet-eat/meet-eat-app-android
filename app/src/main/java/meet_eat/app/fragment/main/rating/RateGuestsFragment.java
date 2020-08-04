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
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import meet_eat.app.R;
import meet_eat.app.databinding.FragmentRateGuestsBinding;
import meet_eat.app.viewmodel.main.RatingViewModel;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.user.User;

public class RateGuestsFragment extends Fragment {

    private FragmentRateGuestsBinding binding;
    private RatingViewModel ratingVM;
    private RateGuestsAdapter rateGuestsAdapter;
    private NavController navController;
    private Offer offer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentRateGuestsBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        ratingVM = new ViewModelProvider(this).get(RatingViewModel.class);
        rateGuestsAdapter = new RateGuestsAdapter(ratingVM, new ArrayList<User>());
        binding.rvRateGuests.setAdapter(rateGuestsAdapter);
        binding.rvRateGuests
                .setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        navController = NavHostFragment.findNavController(this);

        if (getArguments() == null) {
            navController.navigate(R.id.offerListFragment);
        }
        //TODO get offer associated with rating

        setButtonOnClickListener();
        initUI();
        return binding.getRoot();
    }

    private void initUI() {
        binding.tvRateGuestsOfferTitle.setText(offer.getName());
        rateGuestsAdapter.updateGuests(offer.getParticipants());
    }

    private void setButtonOnClickListener() {
        binding.btRateGuestsRate.setOnClickListener(event -> confirmRatings());
    }

    private void confirmRatings() {
        rateGuestsAdapter.sendRatings();

        // go to standard offer list view
        navController.navigate(R.id.offerListFragment);

    }


}
