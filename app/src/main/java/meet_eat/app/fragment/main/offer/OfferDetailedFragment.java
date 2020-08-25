package meet_eat.app.fragment.main.offer;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.io.IOException;
import java.util.Objects;

import meet_eat.app.MainActivity;
import meet_eat.app.R;
import meet_eat.app.databinding.FragmentOfferDetailedBinding;
import meet_eat.app.fragment.ContextFormatter;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.main.OfferViewModel;
import meet_eat.app.viewmodel.main.UserViewModel;
import meet_eat.data.entity.Offer;
import meet_eat.data.location.Localizable;
import meet_eat.data.location.UnlocalizableException;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static meet_eat.app.fragment.NavigationArgumentKey.OFFER;
import static meet_eat.app.fragment.NavigationArgumentKey.USER;

/**
 * This is the detailed offer page. It shows an offer in detail and gives the user the opportunity to interact with
 * it.
 */
public class OfferDetailedFragment extends Fragment {

    private static final String PARTICIPANTS_SEPARATOR = "/";

    private FragmentOfferDetailedBinding binding;
    private OfferViewModel offerVM;
    private UserViewModel userVM;
    private NavController navController;
    private Bundle bundle;
    private Offer offer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).selectMenuItem(1);
        binding = FragmentOfferDetailedBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        offerVM = new ViewModelProvider(requireActivity()).get(OfferViewModel.class);
        userVM = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        navController = NavHostFragment.findNavController(this);

        // Checks if the previous page sent a bundle of arguments containing an offer
        if (Objects.isNull(getArguments()) || Objects.isNull(getArguments().getSerializable(OFFER.name()))) {
            Toast.makeText(getActivity(), R.string.toast_error_message, Toast.LENGTH_LONG).show();
            navController.navigateUp();
        } else {
            offer = (Offer) getArguments().getSerializable(OFFER.name());
        }

        bundle = new Bundle();
        initUI();
        setButtonOnClickListener();
        return binding.getRoot();
    }

    /**
     * Set various click listeners.
     */
    private void setButtonOnClickListener() {
        binding.ibtBack.setOnClickListener(event -> navController.navigateUp());
        binding.ibtOfferDetailedEdit.setOnClickListener(event -> navigateToOfferEdit());
        binding.ibtOfferDetailedBookmark.setOnClickListener(event -> bookmark());
        binding.btOfferDetailedParticipants.setOnClickListener(event -> navigateToOfferParticipants());
        binding.btOfferDetailedParticipate.setOnClickListener(event -> participateOffer());
        binding.ivOfferDetailedProfile.setOnClickListener(event -> navigateToProfile());
        binding.tvOfferDetailedUsername.setOnClickListener(event -> navigateToProfile());
        binding.ibtOfferDetailedReport.setOnClickListener(event -> navigateToOfferReport());
        binding.srlOfferDetailedSwipe.setOnRefreshListener(this::reloadAfterSwipe);
        // Currently disabled features
        // binding.btOfferDetailedContact.setOnClickListener(event -> navigateToOfferContact());
    }

    private void reloadAfterSwipe() {
        try {
            offer = offerVM.fetchOfferById(offer.getIdentifier());
        } catch (RequestHandlerException e) {
            Toast.makeText(getActivity(), R.string.toast_error_message, Toast.LENGTH_LONG).show();
            Log.e("HILFE", e.getMessage());
        }
        initUI();
        binding.srlOfferDetailedSwipe.setRefreshing(false);
    }

    /**
     * Navigates to the contact page. Currently disabled feature.
     */
    private void navigateToOfferContact() {
        // Adds the currently displayed offer to the arguments bundle
        bundle.putSerializable(OFFER.name(), offer);
        navController.navigate(R.id.offerContactFragment, bundle);
    }

    /**
     * Checks if the user wants to participate or cancel his participation to an offer. Depending on which state the
     * bookmark is toggled.
     */
    private void participateOffer() {
        try {
            if (offerVM.isParticipating(offer)) {
                offerVM.cancelParticipation(offerVM.getCurrentUser(), offer);
            } else {
                offerVM.participate(offer);
            }
            updateUI();
        } catch (RequestHandlerException exception) {
            Toast.makeText(getActivity(), R.string.toast_error_message, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Navigates to the profile page of the creator of the current offer.
     */
    private void navigateToProfile() {
        // Adds the creator of the currently displayed offer to the arguments bundle
        bundle.putSerializable(USER.name(), offer.getCreator());
        navController.navigate(R.id.profileFragment, bundle);
    }

    /**
     * Navigates to the participants page.
     */
    private void navigateToOfferParticipants() {
        // Adds the currently displayed offer to the arguments bundle
        bundle.putSerializable(OFFER.name(), offer);
        navController.navigate(R.id.offerParticipantsFragment, bundle);
    }

    /**
     * Checks for the current bookmark state and depending on that bookmarks the offer or removes the bookmark.
     */
    private void bookmark() {
        try {
            if (offerVM.isBookmarked(offer)) {
                offerVM.removeBookmark(offer);
            } else {
                offerVM.addBookmark(offer);
            }

            updateUI();
        } catch (RequestHandlerException exception) {
            Toast.makeText(getActivity(), R.string.toast_error_message, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Navigates to the report page. Currently disabled feature.
     */
    private void navigateToOfferReport() {
        // Adds the currently displayed offer to the arguments bundle
        bundle.putSerializable(OFFER.name(), offer);
        navController.navigate(R.id.offerReportFragment, bundle);
    }

    /**
     * Navigates to the edit page.
     */
    private void navigateToOfferEdit() {
        // Adds the currently displayed offer to the arguments bundle
        bundle.putSerializable(OFFER.name(), offer);
        navController.navigate(R.id.offerEditFragment, bundle);
    }

    /**
     * Initializes the GUI depending on the relations between the current user and the offer.
     */
    private void initUI() {
        binding.srlOfferDetailedSwipe
                .setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorPrimary, null));

        ContextFormatter contextFormatter = new ContextFormatter(binding.getRoot().getContext());
        binding.tvOfferDetailedTitle.setText(offer.getName());
        binding.tvOfferDetailedDate.setText(contextFormatter.formatDateTime(offer.getDateTime()));
        Localizable location = offer.getLocation();

        // Get the host rating of the offer creator and show it in the UI
        try {
            binding.tvOfferDetailedRating.setText(String.valueOf(userVM.getNumericHostRating(offer.getCreator())));
        } catch (RequestHandlerException exception) {
            binding.tvOfferDetailedRating.setVisibility(INVISIBLE);
        }

        try {
            binding.tvOfferDetailedDistance.setText(
                    contextFormatter.formatDistance(location.getDistance(offerVM.getCurrentUser().getLocalizable())));
        } catch (UnlocalizableException exception) {
            Toast.makeText(getActivity(), getString(R.string.invalid_location), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            binding.tvOfferDetailedCity.setText(contextFormatter.formatStringFromLocalizable(location));
        } catch (IOException | UnlocalizableException exception) {
            Toast.makeText(getActivity(), getString(R.string.invalid_location), Toast.LENGTH_SHORT).show();
            return;
        }

        binding.tvOfferDetailedPrice.setText(contextFormatter.formatPrice(offer.getPrice()));

        try {
            int participantsCount = offerVM.getParticipants(offer).size();
            String participantsText = participantsCount + PARTICIPANTS_SEPARATOR + offer.getMaxParticipants();
            binding.tvOfferDetailedParticipants.setText(participantsText);
        } catch (RequestHandlerException exception) {
            binding.tvOfferDetailedParticipants.setVisibility(GONE);
        }

        binding.tvOfferDetailedUsername.setText(offer.getCreator().getName());
        binding.tvOfferDetailedDescription.setText(offer.getDescription());

        if (offerVM.isCreator(offer)) {
            binding.ibtOfferDetailedBookmark.setVisibility(GONE);
            binding.ibtOfferDetailedReport.setVisibility(GONE);
            binding.btOfferDetailedParticipate.setVisibility(GONE);
            binding.btOfferDetailedContact.setVisibility(GONE);
            binding.tvOfferDetailedParticipating.setVisibility(INVISIBLE);
        } else {
            binding.ibtOfferDetailedEdit.setVisibility(GONE);
            binding.btOfferDetailedParticipants.setVisibility(GONE);
        }

        updateUI();
    }

    /**
     * Updates the GUI after the user has interacted with the offer.
     */
    private void updateUI() {

        if (!offerVM.isCreator(offer)) {
            // Handle exception while fetching bookmarks by removing the bookmark button from UI.
            try {
                if (offerVM.isBookmarked(offer)) {
                    binding.ibtOfferDetailedBookmark
                            .setColorFilter(ContextCompat.getColor(binding.getRoot().getContext(), R.color.bookmarked),
                                    PorterDuff.Mode.SRC_IN);
                } else {
                    binding.ibtOfferDetailedBookmark
                            .setColorFilter(ContextCompat.getColor(binding.getRoot().getContext(), R.color.symbol),
                                    PorterDuff.Mode.SRC_IN);
                }
                binding.ibtOfferDetailedBookmark.setVisibility(VISIBLE);
            } catch (RequestHandlerException exception) {
                binding.ibtOfferDetailedBookmark.setVisibility(GONE);
            }

            try {
                int participantsCount = offerVM.getParticipants(offer).size();
                String participantsText = participantsCount + PARTICIPANTS_SEPARATOR + offer.getMaxParticipants();
                binding.tvOfferDetailedParticipants.setText(participantsText);

                if (offerVM.isParticipating(offer)) {
                    binding.btOfferDetailedParticipate.setVisibility(VISIBLE);
                    binding.btOfferDetailedParticipate.setText(R.string.cancel);
                    binding.tvOfferDetailedParticipating.setVisibility(VISIBLE);
                } else {
                    binding.btOfferDetailedParticipate
                            .setVisibility(participantsCount >= offer.getMaxParticipants() ? GONE : VISIBLE);
                    binding.btOfferDetailedParticipate.setText(R.string.participate);
                    binding.tvOfferDetailedParticipating.setVisibility(INVISIBLE);
                }
            } catch (RequestHandlerException exception) {
                Toast.makeText(getActivity(), R.string.toast_error_message, Toast.LENGTH_SHORT).show();
                // TODO Handle error correctly
                binding.tvOfferDetailedParticipants.setVisibility(GONE);
                binding.tvOfferDetailedParticipating.setVisibility(GONE);
                binding.btOfferDetailedParticipate.setVisibility(GONE);
            }
        }
    }
}