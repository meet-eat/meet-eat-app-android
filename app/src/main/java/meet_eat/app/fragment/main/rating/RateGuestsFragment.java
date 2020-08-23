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
import java.util.LinkedList;
import java.util.Objects;

import meet_eat.app.R;
import meet_eat.app.databinding.FragmentRateGuestsBinding;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.main.OfferViewModel;
import meet_eat.app.viewmodel.main.RatingViewModel;
import meet_eat.data.entity.Offer;

/**
 * This is the guest rating page. Here the user can rate his guests and send his ratings.
 */
public class RateGuestsFragment extends Fragment {

    private OfferViewModel offerVM;
    private FragmentRateGuestsBinding binding;
    private NavController navController;
    private RateGuestsAdapter rateGuestsAdapter;
    private Offer offer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRateGuestsBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        offerVM = new ViewModelProvider(requireActivity()).get(OfferViewModel.class);
        RatingViewModel ratingVM = new ViewModelProvider(this).get(RatingViewModel.class);
        rateGuestsAdapter = new RateGuestsAdapter(ratingVM, new ArrayList<>());
        binding.rvRateGuests.setAdapter(rateGuestsAdapter);
        binding.rvRateGuests
                .setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        navController = NavHostFragment.findNavController(this);

        if (Objects.isNull(getArguments())) {
            navController.navigate(R.id.offerListFragment);
        }

        // get offer associated with rating
        setButtonOnClickListener();
        initUI();
        return binding.getRoot();
    }

    /**
     * Initializes the GUI.
     */
    private void initUI() {
        binding.tvRateGuestsOfferTitle.setText(offer.getName());
        try {
            rateGuestsAdapter.updateGuests(offerVM.getParticipants(offer));
        } catch (RequestHandlerException exception) {
            rateGuestsAdapter.updateGuests(new LinkedList<>());
        }
    }

    /**
     * Sets various click listeners.
     */
    private void setButtonOnClickListener() {
        binding.btRateGuestsRate.setOnClickListener(event -> confirmRatings());
    }

    /**
     * TODO
     * Tries to send the guest ratings.
     */
    private void confirmRatings() {
        rateGuestsAdapter.sendRatings();
        navController.navigate(R.id.offerListFragment);
    }
}
