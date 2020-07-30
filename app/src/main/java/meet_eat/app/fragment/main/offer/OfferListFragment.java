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
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import meet_eat.app.databinding.FragmentOfferListBinding;
import meet_eat.app.viewmodel.main.OfferViewModel;
import meet_eat.data.entity.Offer;

public class OfferListFragment extends Fragment {

    private FragmentOfferListBinding binding;
    private OfferViewModel offerVM;
    private OfferListAdapter offerListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        offerListAdapter = new OfferListAdapter(new ArrayList<Offer>());
        binding = FragmentOfferListBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        offerVM = new ViewModelProvider(this).get(OfferViewModel.class);
        binding.rvOfferList.setAdapter(offerListAdapter);
        binding.rvOfferList.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        setButtonOnClickListener();
        return binding.getRoot();
    }

    private void setButtonOnClickListener() {
        binding.ibtCreate.setOnClickListener(event -> navigateToOfferEdit());
    }

    private void navigateToOfferEdit() {
        Navigation.findNavController(binding.getRoot()).navigate(OfferListFragmentDirections
                .actionOfferListFragmentToOfferEditFragment());
    }

    private void updateOffer() {
        // TODO offerListAdapter.updateOffers(...)
    }
}