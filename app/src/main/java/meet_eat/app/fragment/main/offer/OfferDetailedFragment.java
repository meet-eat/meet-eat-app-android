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
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import meet_eat.app.databinding.FragmentOfferDetailedBinding;
import meet_eat.app.viewmodel.main.OfferViewModel;
import meet_eat.app.viewmodel.main.UserViewModel;
import meet_eat.data.entity.Offer;

import static android.view.View.INVISIBLE;

public class OfferDetailedFragment extends Fragment {

    private FragmentOfferDetailedBinding binding;
    private OfferViewModel offerVM;
    private Offer offer;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentOfferDetailedBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        offerVM = new ViewModelProvider(requireActivity()).get(OfferViewModel.class);
        navController = NavHostFragment.findNavController(this);

        offer = offerVM.getOffer();
        if (offer == null)
            navController.popBackStack();

        updateUI();
        setButtonOnClickListener();

        return binding.getRoot();
    }

    private void updateUI() {
        // TODO
        /*if (offer.getCreator().equals(offerVM.getCurrentUser())) {
            binding.tvOfferDetailedParticipating.setVisibility(INVISIBLE);
            binding.btOfferDetailedParticipate.setVisibility(INVISIBLE);
            binding.btOfferDetailedParticipate.setClickable(false);
            binding.btOfferDetailedCancel.setVisibility(INVISIBLE);
            binding.btOfferDetailedCancel.setClickable(false);
            binding.btOfferDetailedContact.setVisibility(INVISIBLE);
            binding.btOfferDetailedContact.setClickable(false);
        } else {
            if (!offer.getParticipants().contains(offerVM.getCurrentUser())
                    && offer.getMaxParticipants() > offer.getParticipants().size()) {
                binding.btOfferDetailedParticipate.setVisibility(INVISIBLE);
                binding.btOfferDetailedParticipate.setClickable(false);
            }
            if (!offer.getParticipants().contains(offerVM.getCurrentUser())) {
                binding.btOfferDetailedCancel.setVisibility(INVISIBLE);
                binding.btOfferDetailedCancel.setClickable(false);
            }
            binding.btOfferDetailedParticipants.setVisibility(INVISIBLE);
            binding.btOfferDetailedParticipants.setClickable(false);
            binding.ibtOfferDetailedEdit.setVisibility(INVISIBLE);
            binding.ibtOfferDetailedEdit.setClickable(false);
        }*/
    }

    private void setButtonOnClickListener() {
        binding.ibtBack.setOnClickListener(event -> navController.popBackStack());
        binding.ibtOfferDetailedEdit.setOnClickListener(event -> navigateToOfferEdit());
        binding.ibtOfferDetailedReport.setOnClickListener(event -> navigateToOfferReport());
        binding.ibtOfferDetailedBookmark.setOnClickListener(this::bookmark);
        binding.btOfferDetailedParticipants
                .setOnClickListener(event -> navigateToOfferParticipants());
        binding.ivOfferDetailedProfile.setOnClickListener(event -> navigateToProfile());
        binding.tvOfferDetailedUsername.setOnClickListener(event -> navigateToProfile());
        binding.btOfferDetailedCancel.setOnClickListener(event -> cancelOffer());
        binding.btOfferDetailedContact.setOnClickListener(event -> navigateToOfferContact());
    }

    private void navigateToOfferContact() {
        navController.navigate(OfferDetailedFragmentDirections
                .actionOfferDetailedFragmentToOfferContactFragment());
    }

    private void cancelOffer() {
        offer.removeParticipant(offerVM.getCurrentUser());
        updateUI();
    }

    private void navigateToProfile() {
        offerVM.setUser(offer.getCreator());
        navController.navigate(OfferDetailedFragmentDirections
                .actionOfferDetailedFragmentToProfileFragment());
    }

    private void navigateToOfferParticipants() {
        offerVM.setOffer(offer);
        navController.navigate(OfferDetailedFragmentDirections
                .actionOfferDetailedFragmentToOfferParticipantsFragment());
    }

    private void bookmark(View view) {
        if (offerVM.getCurrentUser().getBookmarks().contains(offer))
            offerVM.getCurrentUser().removeBookmark(offer);
        else
            offerVM.getCurrentUser().addBookmark(offer);
        updateUI();
    }

    private void navigateToOfferReport() {
        navController.navigate(OfferDetailedFragmentDirections
                .actionOfferDetailedFragmentToOfferReportFragment());
    }

    private void navigateToOfferEdit() {
        navController.navigate(OfferDetailedFragmentDirections
                .actionOfferDetailedFragmentToOfferEditFragment());
    }
}
