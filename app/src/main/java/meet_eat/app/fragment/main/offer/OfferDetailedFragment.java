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

import meet_eat.app.R;
import meet_eat.app.databinding.FragmentOfferDetailedBinding;
import meet_eat.app.fragment.ContextFormatter;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.main.OfferViewModel;
import meet_eat.data.entity.Offer;
import meet_eat.data.location.Localizable;
import meet_eat.data.location.UnlocalizableException;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static meet_eat.app.fragment.NavigationArgumentKey.OFFER;
import static meet_eat.app.fragment.NavigationArgumentKey.USER;

/**
 * This is the detailed offer page. It shows an offer in detail and gives the user the opportunity to interact with
 * it.
 */
public class OfferDetailedFragment extends Fragment {

    private FragmentOfferDetailedBinding binding;
    private OfferViewModel offerVM;
    private NavController navController;
    private Bundle bundle;
    private Offer offer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentOfferDetailedBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        offerVM = new ViewModelProvider(requireActivity()).get(OfferViewModel.class);
        navController = NavHostFragment.findNavController(this);

        // Checks if the previous page sent a bundle of arguments containing an offer
        if (Objects.isNull(getArguments()) || Objects.isNull(getArguments().getSerializable(OFFER.name()))) {
            Toast.makeText(getActivity(), R.string.request_handler_exception_toast_error_message, Toast.LENGTH_LONG)
                    .show();
            Log.i("DEBUG",
                    "OfferDetailedFragment.getArguments: getArguments() null or getArguments().getSerializable() null");
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
        // Currently disabled features
        // binding.ibtOfferDetailedReport.setOnClickListener(event -> navigateToOfferReport());
        // binding.btOfferDetailedContact.setOnClickListener(event -> navigateToOfferContact());
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
                offer = offerVM.cancelParticipation(offerVM.getCurrentUser(), offer);
                binding.ibtOfferDetailedBookmark.setVisibility(VISIBLE);
            } else {
                offer = offerVM.participate(offer);
                binding.ibtOfferDetailedBookmark.setVisibility(GONE);
            }

            updateUI();
        } catch (RequestHandlerException e) {
            Toast.makeText(getActivity(), R.string.request_handler_exception_toast_error_message, Toast.LENGTH_LONG)
                    .show();
            Log.i("DEBUG", "In OfferDetailedFragment.participateOffer: " + e.getMessage());
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
        } catch (RequestHandlerException e) {
            Toast.makeText(getActivity(), R.string.request_handler_exception_toast_error_message, Toast.LENGTH_LONG)
                    .show();
            Log.i("DEBUG", "In OfferDetailedFragment.bookmark: " + e.getMessage());
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
        ContextFormatter contextFormatter = new ContextFormatter(binding.getRoot().getContext());
        binding.tvOfferDetailedTitle.setText(offer.getName());
        binding.tvOfferDetailedDate.setText(contextFormatter.formatDateTime(offer.getDateTime()));
        Localizable location = offer.getLocation();

        try {
            binding.tvOfferDetailedDistance.setText(
                    contextFormatter.formatDistance(location.getDistance(offerVM.getCurrentUser().getLocalizable())));
        } catch (UnlocalizableException e) {
            Toast.makeText(getActivity(), getString(R.string.invalid_location), Toast.LENGTH_SHORT).show();
            Log.i("DEBUG", "In OfferDetailedFragment.initUI: " + e.getMessage());
            return;
        }

        try {
            binding.tvOfferDetailedCity.setText(contextFormatter.formatStringFromLocalizable(location));
        } catch (IOException | UnlocalizableException e) {
            Toast.makeText(getActivity(), getString(R.string.invalid_location), Toast.LENGTH_SHORT).show();
            Log.i("DEBUG", "In OfferDetailedFragment.initUI: " + e.getMessage());
            return;
        }

        binding.tvOfferDetailedPrice.setText(contextFormatter.formatPrice(offer.getPrice()));
        String participantsText = offer.getParticipants().size() + "/" + offer.getMaxParticipants();
        binding.tvOfferDetailedParticipants.setText(participantsText);
        binding.tvOfferDetailedUsername.setText(offer.getCreator().getName());
        binding.tvOfferDetailedRating.setText(String.valueOf(offer.getCreator().getHostRating()));
        binding.tvOfferDetailedDescription.setText(offer.getDescription());

        if (offerVM.isCreator(offer)) {
            binding.ibtOfferDetailedReport.setVisibility(GONE);
            binding.btOfferDetailedParticipate.setVisibility(GONE);
            binding.btOfferDetailedContact.setVisibility(GONE);
            binding.tvOfferDetailedParticipating.setVisibility(GONE);
        } else {
            binding.ibtOfferDetailedEdit.setVisibility(GONE);
            binding.btOfferDetailedParticipants.setVisibility(GONE);
        }

        if (!offerVM.isCreator(offer) && !offerVM.isParticipating(offer)) {
            updateUI();
        } else {
            binding.ibtOfferDetailedBookmark.setVisibility(GONE);
        }
    }

    /**
     * Updates the GUI after the user has interacted with the offer.
     */
    private void updateUI() {
        if (offerVM.isBookmarked(offer)) {
            binding.ibtOfferDetailedBookmark
                    .setColorFilter(ContextCompat.getColor(binding.getRoot().getContext(), R.color.bookmarked),
                            PorterDuff.Mode.SRC_IN);
        } else {
            binding.ibtOfferDetailedBookmark
                    .setColorFilter(ContextCompat.getColor(binding.getRoot().getContext(), R.color.symbol),
                            PorterDuff.Mode.SRC_IN);
        }

        if (offerVM.isParticipating(offer)) {
            binding.btOfferDetailedParticipate.setText(R.string.cancel);
            binding.tvOfferDetailedParticipating.setVisibility(VISIBLE);
        } else {
            binding.btOfferDetailedParticipate.setText(R.string.participate);
            binding.tvOfferDetailedParticipating.setVisibility(GONE);
        }

        String participantsText = offer.getParticipants().size() + "/" + offer.getMaxParticipants();
        binding.tvOfferDetailedParticipants.setText(participantsText);
    }
}