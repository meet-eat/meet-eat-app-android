package meet_eat.app.fragment.main.offer;

import android.location.Geocoder;
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

import java.io.IOException;

import meet_eat.app.R;
import meet_eat.app.databinding.FragmentOfferDetailedBinding;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.main.OfferViewModel;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.user.User;
import meet_eat.data.location.Localizable;
import meet_eat.data.location.UnlocalizableException;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class OfferDetailedFragment extends Fragment {

    private final static String KEY_OFFER = "offer";
    private final static String KEY_USER = "user";

    private FragmentOfferDetailedBinding binding;
    private OfferViewModel offerVM;
    private NavController navController;
    private Offer offer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentOfferDetailedBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        offerVM = new ViewModelProvider(requireActivity()).get(OfferViewModel.class);
        navController = NavHostFragment.findNavController(this);

        if (getArguments() == null || getArguments().getSerializable(KEY_OFFER) == null) {
            // TODO remove debug toast
            Toast.makeText(getActivity(),
                    "DEBUG OfferDetailedFragment.java -> getArguments", Toast.LENGTH_LONG).show();
            navController.navigateUp();
        } else {
            offer = (Offer) getArguments().getSerializable(KEY_OFFER);
        }

        initUI();
        setButtonOnClickListener();
        return binding.getRoot();
    }

    private void setButtonOnClickListener() {
        binding.ibtBack.setOnClickListener(event -> navController.navigateUp());
        binding.ibtOfferDetailedEdit.setOnClickListener(event -> navigateToOfferEdit());
        binding.ibtOfferDetailedReport.setOnClickListener(event -> navigateToOfferReport());
        binding.ibtOfferDetailedBookmark.setOnClickListener(this::bookmark);
        binding.btOfferDetailedParticipants.setOnClickListener(event -> navigateToOfferParticipants());
        binding.ivOfferDetailedProfile.setOnClickListener(event -> navigateToProfile());
        binding.tvOfferDetailedUsername.setOnClickListener(event -> navigateToProfile());
        binding.btOfferDetailedParticipate.setOnClickListener(event -> participateOffer());
        binding.btOfferDetailedContact.setOnClickListener(event -> navigateToOfferContact());
    }

    private void navigateToOfferContact() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_OFFER, offer);
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
            Toast.makeText(getActivity(),
                    "DEBUG OfferDetailedFragment.java -> participateOffer(): " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

    }

    private void navigateToProfile() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_USER, offer.getCreator());
        navController.navigate(R.id.profileFragment, bundle);
    }

    private void navigateToOfferParticipants() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_OFFER, offer);
        navController.navigate(R.id.offerParticipantsFragment, bundle);
    }

    private void bookmark(View view) {

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
            Toast.makeText(getActivity(),
                    "DEBUG OfferDetailedFragment.java -> participateOffer(): " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

    }

    private void navigateToOfferReport() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_OFFER, offer);
        navController.navigate(R.id.offerReportFragment, bundle);
    }

    private void navigateToOfferEdit() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_OFFER, offer);
        navController.navigate(R.id.offerEditFragment, bundle);
    }

    private void initUI() {
        // TODO offer image
        binding.tvOfferDetailedTitle.setText(offer.getName());
        binding.tvOfferDetailedDate.setText(offer.getDateTime().toString());
        Localizable location = offer.getLocation();

        try {
            // TODO distance
            binding.tvOfferDetailedCity.setText(new Geocoder(binding.getRoot().getContext()).getFromLocation(location.getSphericalPosition().getLatitude(),
                    location.getSphericalPosition().getLongitude(), 1).get(0).getSubAdminArea());
        } catch (IOException e) {
            // TODO remove debug toast
            Toast.makeText(getActivity(),
                    "DEBUG OfferDetailedFragment.java -> updateUI(): " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        } catch (UnlocalizableException e) {
            // TODO remove debug toast
            Toast.makeText(getActivity(),
                    "DEBUG OfferDetailedFragment.java -> updateUI(): " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        binding.tvOfferDetailedPrice.setText(String.valueOf(offer.getPrice()) + R.string.currency);
        binding.tvOfferDetailedParticipants.setText(String.valueOf(offer.getMaxParticipants()));
        // TODO profile image
        binding.tvOfferDetailedUsername.setText(offer.getCreator().getName());
        binding.tvOfferDetailedRating.setText(String.valueOf(offer.getCreator().getHostRating()));
        binding.tvOfferDetailedDescription.setText(offer.getDescription());
        // TODO tags

        if (offerVM.getCurrentUser().equals(offer.getCreator())) {
            binding.ibtOfferDetailedReport.setVisibility(INVISIBLE);
            binding.ibtOfferDetailedReport.setClickable(false);
            binding.btOfferDetailedParticipate.setVisibility(INVISIBLE);
            binding.btOfferDetailedParticipate.setClickable(false);
            binding.btOfferDetailedContact.setVisibility(INVISIBLE);
            binding.btOfferDetailedContact.setClickable(false);
            binding.tvOfferDetailedParticipating.setVisibility(INVISIBLE);
        } else {
            binding.ibtOfferDetailedEdit.setVisibility(INVISIBLE);
            binding.ibtOfferDetailedEdit.setClickable(false);
            binding.btOfferDetailedParticipants.setVisibility(INVISIBLE);
            binding.btOfferDetailedParticipants.setClickable(false);
        }

        updateUI();
    }

    private void updateUI() {

        if (offerVM.getCurrentUser().getBookmarks().contains(offer)) {
            binding.ibtOfferDetailedBookmark.setColorFilter(R.color.bookmarked);
        } else {
            binding.ibtOfferDetailedBookmark.setColorFilter(R.color.symbol);
        }

        if (offer.getParticipants().contains(offerVM.getCurrentUser())) {
            binding.btOfferDetailedParticipate.setText(R.string.cancel);
            binding.tvOfferDetailedParticipating.setVisibility(VISIBLE);
        } else {
            binding.btOfferDetailedParticipate.setText(R.string.participate);
            binding.tvOfferDetailedParticipating.setVisibility(INVISIBLE);
        }
    }
}