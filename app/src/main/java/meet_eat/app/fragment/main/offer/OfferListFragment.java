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

/**
 * This is an offer list page. Can display all offers, offer which are bookmarked by the user, offers which are
 * created by the user and offers from users which have been subscribed by the user.
 */
public class OfferListFragment extends Fragment {

    private FragmentOfferListBinding binding;
    private OfferViewModel offerVM;
    private NavController navController;
    private OfferListAdapter offerListAdapter;
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

        // Checks if the previous page sent a bundle of arguments containing an offer list type
        if (Objects.isNull(getArguments()) || Objects.isNull(getArguments().getSerializable(LIST_TYPE.name()))) {
            type = STANDARD;
        } else {
            type = (ListType) getArguments().getSerializable(LIST_TYPE.name());
            // Updates the comparators for the current user
            offerVM.getCurrentUser().setOfferComparator(
                    Objects.isNull(getArguments().getSerializable(SORT_CRITERION.name())) ?
                            new OfferComparator(OfferComparableField.TIME, offerVM.getCurrentUser().getLocalizable()) :
                            (OfferComparator) getArguments().getSerializable(SORT_CRITERION.name()));
        }

        updateUI();
        setButtonOnClickListener();
        return binding.getRoot();
    }

    /**
     * Sets various click listeners.
     */
    private void setButtonOnClickListener() {
        binding.srlOfferListSwipe.setOnRefreshListener(this::reloadAfterSwipe);
        binding.ibtOfferListFilter.setOnClickListener(event -> navigateToOfferFilter());
        binding.ibtOfferListCreate.setOnClickListener(event -> navController.navigate(R.id.offerEditFragment));
        binding.ibtOfferListSubscribed
                .setOnClickListener(event -> navController.navigate(R.id.profileSubscribedFragment));
    }

    /**
     * After pulling down in the offer list, a reload of the offers is happening here.
     */
    private void reloadAfterSwipe() {
        updateOffers();
        binding.srlOfferListSwipe.setRefreshing(false);
    }

    /**
     * Navigates to the filter page.
     */
    private void navigateToOfferFilter() {
        Bundle bundle = new Bundle();
        // Adds the current offer list type to the arguments bundle
        bundle.putSerializable(LIST_TYPE.name(), type);
        navController.navigate(R.id.offerFilterFragment, bundle);
    }

    /**
     * Tries to update the offers depending on which offer list type is selected.
     */
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

        } catch (RequestHandlerException exception) {
            Toast.makeText(getActivity(), R.string.toast_error_message, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Updates the GUI when changes are made. Is also used to initialize the GUI.
     */
    private void updateUI() {
        // Set the color for the progress spinner disc of the refresher
        binding.srlOfferListSwipe.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorPrimary, null));
        updateOffers();

        if (!type.equals(SUBSCRIBED)) {
            binding.ibtOfferListSubscribed.setVisibility(GONE);
        }
    }
}