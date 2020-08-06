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

import java.util.Objects;

import meet_eat.app.R;
import meet_eat.app.databinding.FragmentOfferContactBinding;
import meet_eat.app.viewmodel.main.OfferViewModel;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.user.contact.ContactRequest;

import static meet_eat.app.fragment.NavigationArgumentKey.OFFER;

public class OfferContactFragment extends Fragment {

    private FragmentOfferContactBinding binding;
    private OfferViewModel offerVM;
    private NavController navController;
    private Offer offer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentOfferContactBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        offerVM = new ViewModelProvider(this).get(OfferViewModel.class);
        navController = NavHostFragment.findNavController(this);

        if (Objects.isNull(getArguments()) || Objects.isNull(getArguments().getSerializable(OFFER.name()))) {
            // TODO remove debug toast
            Toast.makeText(getActivity(), "DEBUG OfferContactFragment.java -> getArguments", Toast.LENGTH_LONG).show();
            navController.navigateUp();
        } else {
            offer = (Offer) getArguments().getSerializable(OFFER.name());
        }

        initUI();
        setButtonOnClickListener();
        return binding.getRoot();
    }

    private void setButtonOnClickListener() {
        binding.ibtBack.setOnClickListener(event -> navController.navigateUp());
        binding.btOfferContact.setOnClickListener(event -> contact());
    }

    private void contact() {
        ContactRequest contactRequest = new ContactRequest(offerVM.getCurrentUser(), offer.getCreator());
        offerVM.requestContact(contactRequest);
        Toast.makeText(getActivity(), R.string.request_sent, Toast.LENGTH_SHORT).show();
        navController.navigateUp();
    }

    private void initUI() {
        binding.tvOfferContactInfo.setText(offer.getCreator().getName());
    }
}