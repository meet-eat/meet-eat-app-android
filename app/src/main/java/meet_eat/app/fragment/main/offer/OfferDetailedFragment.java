package meet_eat.app.fragment.main.offer;

import android.graphics.PorterDuff;
import android.os.Bundle;
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
            // TODO remove debug toast
            Toast.makeText(getActivity(), "DEBUG OfferDetailedFragment.java -> getArguments", Toast.LENGTH_LONG).show();
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
        binding.ibtOfferDetailedReport.setOnClickListener(event -> navigateToOfferReport());
        binding.ibtOfferDetailedBookmark.setOnClickListener(event -> bookmark());
        binding.btOfferDetailedParticipants.setOnClickListener(event -> navigateToOfferParticipants());
        binding.ivOfferDetailedProfile.setOnClickListener(event -> navigateToProfile());
        binding.tvOfferDetailedUsername.setOnClickListener(event -> navigateToProfile());
        binding.btOfferDetailedParticipate.setOnClickListener(event -> participateOffer());
        binding.btOfferDetailedContact.setOnClickListener(event -> navigateToOfferContact());
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
            } else {
                offer.addParticipant(offerVM.getCurrentUser());
                offerVM.participate(offer);
            }

            updateUI();
        } catch (RequestHandlerException e) {
            // TODO resolve error code
            Toast.makeText(getActivity(), "DEBUG OfferDetailedFragment.java -> participateOffer(): " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
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
                offerVM.getCurrentUser().removeBookmark(offer);
                offerVM.removeBookmark(offer);
            } else {
                offerVM.getCurrentUser().addBookmark(offer);
                offerVM.addBookmark(offer);
            }

            updateUI();
        } catch (RequestHandlerException e) {
            // TODO resolve error code
            Toast.makeText(getActivity(), "DEBUG OfferDetailedFragment.java -> participateOffer(): " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
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
        // TODO offer image
        binding.tvOfferDetailedTitle.setText(offer.getName());
        binding.tvOfferDetailedDate.setText(contextFormatter.formatDateTime(offer.getDateTime()));
        Localizable location = offer.getLocation();

        try {
            binding.tvOfferDetailedDistance.setText(
                    contextFormatter.formatDistance(location.getDistance(offerVM.getCurrentUser().getLocalizable())));
        } catch (UnlocalizableException e) {
            // TODO remove debug toast
            Toast.makeText(getActivity(), "DEBUG OfferDetailedFragment.java -> initUI(): " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
            return;
        }

        try {
            binding.tvOfferDetailedCity.setText(contextFormatter.formatStringFromLocalizable(location));
        } catch (IOException | UnlocalizableException e) {
            // TODO remove debug toast
            Toast.makeText(getActivity(), "DEBUG OfferDetailedFragment.java -> initUI(): " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
            return;
        }

        binding.tvOfferDetailedPrice.setText(contextFormatter.formatPrice(offer.getPrice()));
        binding.tvOfferDetailedParticipants.setText(String.valueOf(offer.getMaxParticipants()));
        // TODO profile image
        binding.tvOfferDetailedUsername.setText(offer.getCreator().getName());
        binding.tvOfferDetailedRating.setText(String.valueOf(offer.getCreator().getHostRating()));
        binding.tvOfferDetailedDescription.setText(offer.getDescription());
        // TODO tags

        if (offerVM.getCurrentUser().equals(offer.getCreator())) {
            binding.ibtOfferDetailedReport.setVisibility(GONE);
            binding.btOfferDetailedParticipate.setVisibility(GONE);
            binding.btOfferDetailedContact.setVisibility(GONE);
            binding.tvOfferDetailedParticipating.setVisibility(GONE);
        } else {
            binding.ibtOfferDetailedEdit.setVisibility(GONE);
            binding.btOfferDetailedParticipants.setVisibility(GONE);
        }

        updateUI();
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

    }
}