package meet_eat.app.fragment.main.rating;

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

import java.util.Objects;

import meet_eat.app.R;
import meet_eat.app.databinding.FragmentRateHostBinding;
import meet_eat.app.fragment.ContextFormatter;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.main.RatingViewModel;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.user.rating.Rating;
import meet_eat.data.entity.user.rating.RatingBasis;
import meet_eat.data.entity.user.rating.RatingValue;

public class RateHostFragment extends Fragment {

    private static final float RATING_STEP_SIZE = 1;

    private FragmentRateHostBinding binding;
    private NavController navController;
    private RatingViewModel ratingVM;
    private Offer offer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRateHostBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        ratingVM = new ViewModelProvider(this).get(RatingViewModel.class);
        navController = NavHostFragment.findNavController(this);

        if (Objects.isNull(getArguments())) {
            navController.navigate(R.id.offerListFragment);
        }
        //TODO get offer associated with rating

        setButtonOnClickListener();
        initUI();
        return binding.getRoot();
    }

    private void initUI() {
        ContextFormatter contextFormatter = new ContextFormatter(binding.getRoot().getContext());
        binding.tvRateHostOfferTitle.setText(offer.getName());
        //binding.tvRateHostOfferCity.setText(contextFormatter.getStringFromLocalizable(offer
        // .getLocation()));
        binding.tvRateHostOfferDate.setText(contextFormatter.formatTime(offer.getDateTime().toLocalTime()));
        binding.tvRateHostOfferDescription.setText(offer.getDescription());
        binding.tvRateHostOfferPrice.setText(String.valueOf(offer.getPrice()));
        binding.tvRateHostUsername.setText(offer.getCreator().getName());
        // TODO image binding.ivRateHostOfferPicture.setImageResource(offer.getImage());
        // binding.tvRateHostOfferPicture.setText("");
        // TODO binding.tvRateHostTag
        binding.rbRateHost.setNumStars(3);
        binding.rbRateHost.setStepSize(RATING_STEP_SIZE);
    }

    private void setButtonOnClickListener() {
        binding.btRateHostRate.setOnClickListener(event -> rateGuests());
    }

    private void rateGuests() {
        int numStars = (int) binding.rbRateHost.getRating();
        Rating rating;

        switch (numStars) {
            case 1:
                rating = new Rating(RatingBasis.HOST, RatingValue.POINTS_1, ratingVM.getCurrentUser());
                break;
            case 2:
                rating = new Rating(RatingBasis.HOST, RatingValue.POINTS_2, ratingVM.getCurrentUser());
                break;
            case 3:
                rating = new Rating(RatingBasis.HOST, RatingValue.POINTS_3, ratingVM.getCurrentUser());
                break;
            case 4:
                rating = new Rating(RatingBasis.HOST, RatingValue.POINTS_4, ratingVM.getCurrentUser());
                break;
            case 5:
                rating = new Rating(RatingBasis.HOST, RatingValue.POINTS_5, ratingVM.getCurrentUser());
                break;
            default:
                Log.i("DEBUG", "RateHostFragment.rateGuests");
                return;

        }

        try {
            ratingVM.send(rating);
        } catch (RequestHandlerException e) {
            e.printStackTrace();
        }
    }
}