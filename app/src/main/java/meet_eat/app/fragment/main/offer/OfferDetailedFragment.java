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

import meet_eat.app.R;
import meet_eat.app.databinding.FragmentOfferDetailedBinding;
import meet_eat.app.viewmodel.main.OfferViewModel;
import meet_eat.app.viewmodel.main.UserViewModel;
import meet_eat.data.entity.Offer;

public class OfferDetailedFragment extends Fragment {

    private FragmentOfferDetailedBinding binding;
    private UserViewModel userVM;
    private Offer offer;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentOfferDetailedBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        userVM = new ViewModelProvider(this).get(UserViewModel.class);
        navController = Navigation.findNavController(binding.getRoot());
        if (getArguments() != null) {
            offer = (Offer) getArguments().get("offer");
        } else {
            navController.popBackStack();
        }
        updateUI();
        setButtonOnClickListener();

        return binding.getRoot();
    }


    private void updateUI() {
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
        offer.removeParticipant(userVM.getCurrentUser());
        updateUI();
    }


    private void navigateToProfile() {
        navController.navigate(OfferDetailedFragmentDirections
                .actionOfferDetailedFragmentToProfileFragment(offer.getCreator()));
    }

    private void navigateToOfferParticipants() {
        navController.navigate(OfferDetailedFragmentDirections
                .actionOfferDetailedFragmentToOfferParticipantsFragment());
    }

    private void bookmark(View view) {
        if (userVM.getCurrentUser().getBookmarks().contains(offer))
            userVM.getCurrentUser().removeBookmark(offer);
        else
            userVM.getCurrentUser().addBookmark(offer);
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
