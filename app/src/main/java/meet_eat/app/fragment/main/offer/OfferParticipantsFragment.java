package meet_eat.app.fragment.main.offer;

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
import java.util.Objects;

import meet_eat.app.MainActivity;
import meet_eat.app.databinding.FragmentOfferParticipantsBinding;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.main.OfferViewModel;
import meet_eat.data.entity.Offer;

import static meet_eat.app.fragment.NavigationArgumentKey.OFFER;

/**
 * This is the page where the participants to an offer are listed.
 */
public class OfferParticipantsFragment extends Fragment {

    private FragmentOfferParticipantsBinding binding;
    private NavController navController;
    private Offer offer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((MainActivity) requireActivity()).selectMenuItem(1);
        binding = FragmentOfferParticipantsBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        OfferViewModel offerVM = new ViewModelProvider(this).get(OfferViewModel.class);
        OfferParticipantsAdapter offerParticipantsAdapter = new OfferParticipantsAdapter(new ArrayList<>());
        binding.rvOfferParticipants.setAdapter(offerParticipantsAdapter);
        binding.rvOfferParticipants
                .setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        navController = NavHostFragment.findNavController(this);

        // Checks if the previous page sent a bundle of arguments containing the offer
        if (Objects.isNull(getArguments()) || Objects.isNull(getArguments().getSerializable(OFFER.name()))) {
            navController.navigateUp();
        } else {
            offer = (Offer) getArguments().getSerializable(OFFER.name());
        }

        // Get the participants of the offer
        try {
            offerParticipantsAdapter.updateParticipants(new ArrayList<>(offerVM.getParticipants(offer)));
        } catch (RequestHandlerException exception) {
            // TODO Evaluate
            offerParticipantsAdapter.updateParticipants(new ArrayList<>());
        }

        setButtonOnClickListener();
        return binding.getRoot();
    }

    /**
     * Sets various click listeners.
     */
    private void setButtonOnClickListener() {
        binding.ibtBack.setOnClickListener(event -> navController.navigateUp());
    }
}