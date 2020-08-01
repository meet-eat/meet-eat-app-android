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

import meet_eat.app.R;
import meet_eat.app.databinding.FragmentOfferListBinding;
import meet_eat.app.viewmodel.main.OfferViewModel;
import meet_eat.data.entity.Offer;

public class OfferListFragment extends Fragment {

    public final static int OFFER_LIST_TYPE_STANDARD = 0;
    public final static int OFFER_LIST_TYPE_OWN = 1;
    public final static int OFFER_LIST_TYPE_BOOKMARKED = 2;
    public final static int OFFER_LIST_TYPE_SUBSCRIBED = 3;

    private FragmentOfferListBinding binding;
    private NavController navController;
    private OfferViewModel offerVM;
    private OfferListAdapter offerListAdapter;
    private int type = OFFER_LIST_TYPE_STANDARD;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentOfferListBinding.inflate(inflater, container, false);
        offerVM = new ViewModelProvider(this).get(OfferViewModel.class);
        navController = NavHostFragment.findNavController(this);
        binding.rvOfferList.setAdapter(offerListAdapter);
        binding.rvOfferList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        offerListAdapter = new OfferListAdapter(offerVM, new ArrayList<Offer>());

        if (getArguments() == null || getArguments().getInt("type") == 0) {
            type = OFFER_LIST_TYPE_STANDARD;
        } else {
            type = getArguments().getInt("type");
        }

        setButtonOnClickListener();
        updateUI();
        return binding.getRoot();
    }

    private void updateUI() {
        // TODO
    }

    private void setButtonOnClickListener() {
        binding.ibtOfferListCreate.setOnClickListener(event -> navigateToOfferEdit());
        binding.ibtOfferListEdit.setOnClickListener(event -> navigateToProfileSubscribed());
    }

    private void navigateToProfileSubscribed() {
        navController.navigate(R.id.profileSubscribedFragment);
    }

    private void navigateToOfferEdit() {
        navController.navigate(R.id.offerEditFragment);
    }

    private void updateOffer() {

        switch (type) {
            case OFFER_LIST_TYPE_STANDARD:
                break;
            case OFFER_LIST_TYPE_OWN:
                break;
            case OFFER_LIST_TYPE_BOOKMARKED:
                break;
            case OFFER_LIST_TYPE_SUBSCRIBED:
                break;
            default:
                break;
        }

        // TODO switch case for type, offerListAdapter.updateOffers(type filters)
    }
}
