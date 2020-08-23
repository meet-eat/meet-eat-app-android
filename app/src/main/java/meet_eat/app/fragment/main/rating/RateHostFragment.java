package meet_eat.app.fragment.main.rating;

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

import java.util.Objects;

import meet_eat.app.R;
import meet_eat.app.databinding.FragmentRateHostBinding;
import meet_eat.app.fragment.ContextFormatter;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.main.RatingViewModel;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.relation.rating.Rating;
import meet_eat.data.entity.relation.rating.RatingValue;

/**
 * This is the host rating fragment. Here the user can rate the host of the offer he participated.
 */
public class RateHostFragment extends Fragment {

    private static final float RATING_STEP_SIZE = 1;
    private static final int DEFAULT_NUM_STARS = 3;

    private FragmentRateHostBinding binding;
    private RatingViewModel ratingVM;
    private Offer offer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRateHostBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        ratingVM = new ViewModelProvider(this).get(RatingViewModel.class);
        NavController navController = NavHostFragment.findNavController(this);

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
        ContextFormatter contextFormatter = new ContextFormatter(binding.getRoot().getContext());
        binding.tvRateHostOfferTitle.setText(offer.getName());
        //binding.tvRateHostOfferCity.setText(contextFormatter.getStringFromLocalizable(offer
        // .getLocation()));
        binding.tvRateHostOfferDate.setText(contextFormatter.formatTime(offer.getDateTime().toLocalTime()));
        binding.tvRateHostOfferDescription.setText(offer.getDescription());
        binding.tvRateHostOfferPrice.setText(String.valueOf(offer.getPrice()));
        binding.tvRateHostUsername.setText(offer.getCreator().getName());
        // add image: binding.ivRateHostOfferPicture.setImageResource(offer.getImage());
        // binding.tvRateHostOfferPicture.setText("");
        // add tag: binding.tvRateHostTag
        binding.rbRateHost.setNumStars(DEFAULT_NUM_STARS);
        binding.rbRateHost.setStepSize(RATING_STEP_SIZE);
    }

    /**
     * Sets various click listeners.
     */
    private void setButtonOnClickListener() {
        binding.btRateHostRate.setOnClickListener(event -> rateGuests());
    }

    /**
     * TODO
     * Tries to rate the host.
     */
    private void rateGuests() {
        int numStars = (int) binding.rbRateHost.getRating();
        RatingValue ratingValue = RatingValue.getRatingValueByInteger(numStars);
        // TODO Add offer to factory method
        Rating rating = Rating.createHostRating(ratingVM.getCurrentUser(), null, ratingValue);

        try {
            ratingVM.send(rating);
        } catch (RequestHandlerException exception) {
            Toast.makeText(getActivity(), R.string.toast_error_message, Toast.LENGTH_SHORT).show();
        }
    }
}