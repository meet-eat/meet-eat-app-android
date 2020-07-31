package meet_eat.app.fragment.main.offer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import meet_eat.app.R;
import meet_eat.app.databinding.FragmentOfferContactBinding;
import meet_eat.app.repository.Session;
import meet_eat.app.viewmodel.main.OfferViewModel;
import meet_eat.app.viewmodel.main.UserViewModel;
import meet_eat.data.entity.user.User;
import meet_eat.data.entity.user.contact.ContactRequest;

public class OfferContactFragment extends Fragment {

    private FragmentOfferContactBinding binding;
    private OfferViewModel offerVM;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentOfferContactBinding.inflate(inflater, container, false);
        offerVM = new ViewModelProvider(this).get(OfferViewModel.class);
        updateUI();
        setButtonOnClickListener();
        return binding.getRoot();
    }

    private void setButtonOnClickListener() {
        binding.ibtBack.setOnClickListener(event -> goBack());
        binding.btOfferContact.setOnClickListener(event -> contact());
    }

    private void contact() {
        if (offerVM.getOffer() == null) {
            return;
        }

        ContactRequest contactRequest = new ContactRequest(offerVM.getUser(),
                offerVM.getOffer().getCreator());
        offerVM.requestContact(contactRequest);
    }

    private void updateUI() {
        if (offerVM.getOffer() == null) {
            return;
        }
        binding.tvOfferContactInfo.setText(offerVM.getOffer().getCreator().getName());
    }

    private void goBack() {
        Navigation.findNavController(binding.getRoot()).popBackStack();
    }
}
