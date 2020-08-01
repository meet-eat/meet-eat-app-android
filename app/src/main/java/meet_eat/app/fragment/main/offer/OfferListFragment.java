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
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import meet_eat.app.databinding.FragmentOfferListBinding;
import meet_eat.app.viewmodel.main.OfferViewModel;
import meet_eat.data.entity.Offer;

public class OfferListFragment extends Fragment {

    private FragmentOfferListBinding binding;
    private NavController navController;
    private OfferViewModel offerVM;
    private OfferListAdapter offerListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentOfferListBinding.inflate(inflater, container, false);
        offerVM = new ViewModelProvider(this).get(OfferViewModel.class);
        navController = NavHostFragment.findNavController(this);
        binding.rvOfferList.setAdapter(offerListAdapter);
        binding.rvOfferList.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        offerListAdapter = new OfferListAdapter(offerVM, new ArrayList<Offer>());
        setButtonOnClickListener();
        return binding.getRoot();
    }

    private void setButtonOnClickListener() {
        binding.ibtOfferListCreate.setOnClickListener(event -> navigateToOfferEdit());
        binding.ibtOfferListEdit.setOnClickListener(event -> navigateToProfileSubscribed());
    }

    private void navigateToProfileSubscribed() {
        navController.navigate(OfferListFragmentDirections
                .actionOfferListFragmentToProfileSubscribedFragment());
    }

    private void navigateToOfferEdit() {
        navController.navigate(OfferListFragmentDirections
                .actionOfferListFragmentToOfferEditFragment());
    }

    private void updateOffer() {
        // TODO offerListAdapter.updateOffers(...)
    }
}
