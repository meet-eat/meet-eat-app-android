package meet_eat.app.fragment.main.offer;

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

import meet_eat.app.R;
import meet_eat.app.databinding.FragmentOfferDetailedBinding;
import meet_eat.app.viewmodel.main.OfferViewModel;
import meet_eat.data.entity.Offer;

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

        if (getArguments() == null || getArguments().getSerializable("offer") == null) {
            Toast.makeText(getActivity(), "DEBUG: Offer not given", Toast.LENGTH_SHORT).show();
            navController.navigateUp();
        } else {
            offer = (Offer) getArguments().getSerializable("offer");
        }

        updateUI();
        setButtonOnClickListener();
        return binding.getRoot();
    }

    private void updateUI() {
        // TODO
    }

    private void setButtonOnClickListener() {
        binding.ibtBack.setOnClickListener(event -> navController.navigateUp());
        binding.ibtOfferDetailedEdit.setOnClickListener(event -> navigateToOfferEdit());
        binding.ibtOfferDetailedReport.setOnClickListener(event -> navigateToOfferReport());
        binding.ibtOfferDetailedBookmark.setOnClickListener(this::bookmark);
        binding.btOfferDetailedParticipants.setOnClickListener(event -> navigateToOfferParticipants());
        binding.ivOfferDetailedProfile.setOnClickListener(event -> navigateToProfile());
        binding.tvOfferDetailedUsername.setOnClickListener(event -> navigateToProfile());
        binding.btOfferDetailedCancel.setOnClickListener(event -> cancelOffer());
        binding.btOfferDetailedContact.setOnClickListener(event -> navigateToOfferContact());
    }

    private void navigateToOfferContact() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("offer", offer);
        navController.navigate(R.id.offerContactFragment, bundle);
    }

    private void cancelOffer() {
        offer.removeParticipant(offerVM.getCurrentUser());
        // TODO toast?
        updateUI();
    }

    private void navigateToProfile() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", offer.getCreator());
        navController.navigate(R.id.profileFragment, bundle);
    }

    private void navigateToOfferParticipants() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("offer", offer);
        navController.navigate(R.id.offerParticipantsFragment, bundle);
    }

    private void bookmark(View view) {

        if (offerVM.getCurrentUser().getBookmarks().contains(offer)) {
            offerVM.getCurrentUser().removeBookmark(offer);
        } else {
            offerVM.getCurrentUser().addBookmark(offer);
        }

        // TODO toast?
        updateUI();
    }

    private void navigateToOfferReport() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("offer", offer);
        navController.navigate(R.id.offerReportFragment, bundle);
    }

    private void navigateToOfferEdit() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("offer", offer);
        navController.navigate(R.id.offerEditFragment, bundle);
    }
}