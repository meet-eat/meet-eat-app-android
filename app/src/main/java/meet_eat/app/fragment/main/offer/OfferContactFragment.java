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

import meet_eat.app.databinding.FragmentOfferContactBinding;
import meet_eat.app.viewmodel.main.OfferViewModel;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.user.contact.ContactRequest;

public class OfferContactFragment extends Fragment {

    private FragmentOfferContactBinding binding;
    private NavController navController;
    private OfferViewModel offerVM;
    private Offer offer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentOfferContactBinding.inflate(inflater, container, false);
        offerVM = new ViewModelProvider(this).get(OfferViewModel.class);
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

    private void setButtonOnClickListener() {
        binding.ibtBack.setOnClickListener(event -> navController.navigateUp());
        binding.btOfferContact.setOnClickListener(event -> contact());
    }

    private void contact() {
        ContactRequest contactRequest = new ContactRequest(offerVM.getCurrentUser(),
                offer.getCreator());
        offerVM.requestContact(contactRequest);
        // TODO navigation and toast
    }

    private void updateUI() {
        binding.tvOfferContactInfo.setText(offer.getCreator().getName());
    }
}