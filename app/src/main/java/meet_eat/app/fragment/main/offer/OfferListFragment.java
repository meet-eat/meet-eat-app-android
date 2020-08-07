package meet_eat.app.fragment.main.offer;

import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Objects;

import meet_eat.app.R;
import meet_eat.app.databinding.FragmentOfferListBinding;
import meet_eat.app.fragment.ListType;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.main.OfferViewModel;
import meet_eat.data.comparator.OfferComparableField;
import meet_eat.data.comparator.OfferComparator;

import static android.view.View.GONE;
import static meet_eat.app.fragment.ListType.STANDARD;
import static meet_eat.app.fragment.ListType.SUBSCRIBED;
import static meet_eat.app.fragment.NavigationArgumentKey.LIST_TYPE;
import static meet_eat.app.fragment.NavigationArgumentKey.SORT_CRITERION;

public class OfferListFragment extends Fragment {

    private FragmentOfferListBinding binding;
    private OfferViewModel offerVM;
    private NavController navController;
    private OfferListAdapter offerListAdapter;
    private OfferComparator comparator;
    private ListType type;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentOfferListBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        offerVM = new ViewModelProvider(this).get(OfferViewModel.class);
        navController = NavHostFragment.findNavController(this);
        offerListAdapter = new OfferListAdapter(offerVM, new ArrayList<>());
        binding.rvOfferList.setAdapter(offerListAdapter);
        binding.rvOfferList
                .setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        if (Objects.isNull(getArguments())) {
            type = STANDARD;
        } else {
            type = Objects.isNull(getArguments().getSerializable(LIST_TYPE.name())) ? STANDARD :
                    (ListType) getArguments().getSerializable(LIST_TYPE.name());
            offerVM.getCurrentUser().setOfferComparator(comparator =
                    Objects.isNull(getArguments().getSerializable(SORT_CRITERION.name())) ?
                            new OfferComparator(OfferComparableField.TIME, offerVM.getCurrentUser().getLocalizable()) :
                            (OfferComparator) getArguments().getSerializable(SORT_CRITERION.name()));
        }

        updateUI();
        setButtonOnClickListener();
        return binding.getRoot();
    }

    private void updateUI() {
        updateOffers();

        if (!type.equals(SUBSCRIBED)) {
            binding.ibtOfferListSubscribed.setVisibility(GONE);
        }

    }

    private void setButtonOnClickListener() {
        binding.ibtOfferListFilter.setOnClickListener(event -> navigateToOfferFilter());
        binding.ibtOfferListCreate.setOnClickListener(event -> navigateToOfferEdit());
        binding.ibtOfferListSubscribed.setOnClickListener(event -> navigateToProfileSubscribed());
    }

    private void navigateToOfferFilter() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(LIST_TYPE.name(), type);
        navController.navigate(R.id.offerFilterFragment, bundle);
    }

    private void navigateToProfileSubscribed() {
        navController.navigate(R.id.profileSubscribedFragment);
    }

    private void navigateToOfferEdit() {
        navController.navigate(R.id.offerEditFragment);
    }

    private void updateOffers() {

        try {

            switch (type) {
                case OWN:
                    offerListAdapter.updateOffers(offerVM.fetchOffers(offerVM.getCurrentUser()));
                    break;
                case BOOKMARKED:
                    offerListAdapter.updateOffers(offerVM.fetchBookmarkedOffers());
                    break;
                case SUBSCRIBED:
                    offerListAdapter.updateOffers(offerVM.fetchOffersOfSubscriptions());
                    break;
                default:
                    offerListAdapter.updateOffers(offerVM.fetchOffers(new ArrayList<>()));
            }

        } catch (RequestHandlerException e) {
            Toast.makeText(getActivity(), R.string.request_handler_exception_toast_error_message, Toast.LENGTH_LONG)
                    .show();
            Log.i("DEBUG", "In OfferListFragment.updateOffers: " + e.getMessage());
        }

    }
}