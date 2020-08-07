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

        if (Objects.isNull(getArguments()) || Objects.isNull(getArguments().getSerializable(OFFER.name()))) {
            Toast.makeText(getActivity(), R.string.request_handler_exception_toast_error_message, Toast.LENGTH_LONG)
                    .show();
            Log.i("DEBUG", "OfferDetailedFragment.getArguments: " + "getArguments() null or getArguments()" +
                    ".getSerializable() null");
            navController.navigateUp();
        } else {
            offer = (Offer) getArguments().getSerializable(OFFER.name());
        }

        bundle = new Bundle();
        initUI();
        setButtonOnClickListener();
        return binding.getRoot();
    }

    private void setButtonOnClickListener() {
        binding.ibtBack.setOnClickListener(event -> navController.navigateUp());
        binding.ibtOfferDetailedEdit.setOnClickListener(event -> navigateToOfferEdit());
        // binding.ibtOfferDetailedReport.setOnClickListener(event -> navigateToOfferReport());
        binding.ibtOfferDetailedBookmark.setOnClickListener(event -> bookmark());
        binding.btOfferDetailedParticipants.setOnClickListener(event -> navigateToOfferParticipants());
        binding.ivOfferDetailedProfile.setOnClickListener(event -> navigateToProfile());
        binding.tvOfferDetailedUsername.setOnClickListener(event -> navigateToProfile());
        binding.btOfferDetailedParticipate.setOnClickListener(event -> participateOffer());
        // binding.btOfferDetailedContact.setOnClickListener(event -> navigateToOfferContact());
    }

    private void navigateToOfferContact() {
        bundle.putSerializable(OFFER.name(), offer);
        navController.navigate(R.id.offerContactFragment, bundle);
    }

    private void participateOffer() {

        try {

            if (offer.getParticipants().contains(offerVM.getCurrentUser())) {
                offer.removeParticipant(offerVM.getCurrentUser());
                offerVM.cancelParticipation(offerVM.getCurrentUser(), offer);
                binding.ibtOfferDetailedBookmark.setVisibility(VISIBLE);
            } else {
                offer.addParticipant(offerVM.getCurrentUser());
                offerVM.participate(offer);
                binding.ibtOfferDetailedBookmark.setVisibility(GONE);
            }

            updateUI();
        } catch (RequestHandlerException e) {
            Toast.makeText(getActivity(), R.string.request_handler_exception_toast_error_message, Toast.LENGTH_LONG)
                    .show();
            Log.i("DEBUG", "In OfferDetailedFragment.participateOffer: " + e.getMessage());
        }

    }

    private void navigateToProfile() {
        bundle.putSerializable(USER.name(), offer.getCreator());
        navController.navigate(R.id.profileFragment, bundle);
    }

    private void navigateToOfferParticipants() {
        bundle.putSerializable(OFFER.name(), offer);
        navController.navigate(R.id.offerParticipantsFragment, bundle);
    }

    private void bookmark() {

        try {

            if (offerVM.getCurrentUser().getBookmarks().contains(offer)) {
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

    private void navigateToOfferReport() {
        bundle.putSerializable(OFFER.name(), offer);
        navController.navigate(R.id.offerReportFragment, bundle);
    }

    private void navigateToOfferEdit() {
        bundle.putSerializable(OFFER.name(), offer);
        navController.navigate(R.id.offerEditFragment, bundle);
    }

    private void initUI() {
        ContextFormatter contextFormatter = new ContextFormatter(binding.getRoot().getContext());
        // add offer image
        binding.tvOfferDetailedTitle.setText(offer.getName());
        binding.tvOfferDetailedDate.setText(contextFormatter.formatDateTime(offer.getDateTime()));
        Localizable location = offer.getLocation();

        try {
            binding.tvOfferDetailedDistance.setText(
                    contextFormatter.formatDistance(location.getDistance(offerVM.getCurrentUser().getLocalizable())));
        } catch (UnlocalizableException e) {
            // TODO remove debug toast
            Log.i("DEBUG", "In OfferDetailedFragment.initUI: " + e.getMessage());
            return;
        }

        try {
            binding.tvOfferDetailedCity.setText(contextFormatter.formatStringFromLocalizable(location));
        } catch (IOException | UnlocalizableException e) {
            // TODO remove debug toast
            Log.i("DEBUG", "In OfferDetailedFragment.initUI: " + e.getMessage());
            return;
        }

        binding.tvOfferDetailedPrice.setText(contextFormatter.formatPrice(offer.getPrice()));
        binding.tvOfferDetailedParticipants.setText(offer.getParticipants().size() + "/" + offer.getMaxParticipants());
        // add profile image
        binding.tvOfferDetailedUsername.setText(offer.getCreator().getName());
        binding.tvOfferDetailedRating.setText(String.valueOf(offer.getCreator().getHostRating()));
        binding.tvOfferDetailedDescription.setText(offer.getDescription());
        // add tags

        if (offerVM.getCurrentUser().getIdentifier().equals(offer.getCreator().getIdentifier())) {
            binding.ibtOfferDetailedReport.setVisibility(GONE);
            binding.btOfferDetailedParticipate.setVisibility(GONE);
            binding.btOfferDetailedContact.setVisibility(GONE);
            binding.tvOfferDetailedParticipating.setVisibility(GONE);
        } else {
            binding.ibtOfferDetailedEdit.setVisibility(GONE);
            binding.btOfferDetailedParticipants.setVisibility(GONE);
        }

        if (!offerVM.getCurrentUser().getIdentifier().equals(offer.getCreator().getIdentifier()) &&
                !offer.getParticipants().contains(offerVM.getCurrentUser())) {
            updateUI();
        } else {
            binding.ibtOfferDetailedBookmark.setVisibility(GONE);
        }
    }

    private void updateUI() {

        if (offerVM.getCurrentUser().getBookmarks().contains(offer)) {
            binding.ibtOfferDetailedBookmark
                    .setColorFilter(ContextCompat.getColor(binding.getRoot().getContext(), R.color.bookmarked),
                            PorterDuff.Mode.SRC_IN);
        } else {
            binding.ibtOfferDetailedBookmark
                    .setColorFilter(ContextCompat.getColor(binding.getRoot().getContext(), R.color.symbol),
                            PorterDuff.Mode.SRC_IN);
        }

        if (offer.getParticipants().contains(offerVM.getCurrentUser())) {
            binding.btOfferDetailedParticipate.setText(R.string.cancel);
            binding.tvOfferDetailedParticipating.setVisibility(VISIBLE);
        } else {
            binding.btOfferDetailedParticipate.setText(R.string.participate);
            binding.tvOfferDetailedParticipating.setVisibility(GONE);
        }

        binding.tvOfferDetailedParticipants.setText(offer.getParticipants().size() + "/" + offer.getMaxParticipants());
    }
}