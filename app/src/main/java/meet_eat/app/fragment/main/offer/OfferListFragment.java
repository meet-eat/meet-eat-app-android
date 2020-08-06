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

import com.fasterxml.jackson.core.json.async.NonBlockingJsonParser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import meet_eat.app.R;
import meet_eat.app.databinding.FragmentOfferListBinding;
import meet_eat.app.fragment.ListType;
import meet_eat.app.fragment.SortCriterion;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.main.OfferViewModel;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Tag;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;
import meet_eat.data.location.Localizable;
import meet_eat.data.location.SphericalLocation;
import meet_eat.data.location.SphericalPosition;
import meet_eat.data.predicate.OfferPredicate;

import static android.view.View.GONE;
import static meet_eat.app.fragment.ListType.STANDARD;
import static meet_eat.app.fragment.ListType.SUBSCRIBED;
import static meet_eat.app.fragment.NavigationArgumentKey.LIST_TYPE;
import static meet_eat.app.fragment.NavigationArgumentKey.SORT_CRITERION;
import static meet_eat.app.fragment.SortCriterion.TIME;

public class OfferListFragment extends Fragment {

    private static final boolean DEBUG = true;

    private FragmentOfferListBinding binding;
    private OfferViewModel offerVM;
    private NavController navController;
    private OfferListAdapter offerListAdapter;
    private ListType type;
    private SortCriterion sortCriterion;

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
            sortCriterion = Objects.isNull(getArguments().getSerializable(SORT_CRITERION.name())) ? TIME :
                    (SortCriterion) getArguments().getSerializable(SORT_CRITERION.name());
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

        if (DEBUG) {
            ArrayList<Offer> offerList = new ArrayList<>();

            offerList.add(new Offer(offerVM.getCurrentUser(), new Set<Tag>() {
                @Override
                public int size() {
                    return 0;
                }

                @Override
                public boolean isEmpty() {
                    return false;
                }

                @Override
                public boolean contains(@Nullable Object o) {
                    return false;
                }

                @NonNull
                @Override
                public Iterator<Tag> iterator() {
                    return null;
                }

                @NonNull
                @Override
                public Object[] toArray() {
                    return new Object[0];
                }

                @NonNull
                @Override
                public <T> T[] toArray(@NonNull T[] a) {
                    return null;
                }

                @Override
                public boolean add(Tag tag) {
                    return false;
                }

                @Override
                public boolean remove(@Nullable Object o) {
                    return false;
                }

                @Override
                public boolean containsAll(@NonNull Collection<?> c) {
                    return false;
                }

                @Override
                public boolean addAll(@NonNull Collection<? extends Tag> c) {
                    return false;
                }

                @Override
                public boolean retainAll(@NonNull Collection<?> c) {
                    return false;
                }

                @Override
                public boolean removeAll(@NonNull Collection<?> c) {
                    return false;
                }

                @Override
                public void clear() {
                }
            }, "EIGENES ANGEBOT", "mmm leker", 2.50, 1, LocalDateTime.now(),
                    () -> new SphericalPosition(48.9305065, 8.4612313)));

            for (int i = 0; i < 100; i++) {
                Localizable localizable = new SphericalLocation(new SphericalPosition(0, 0));
                offerList.add(new Offer(
                        new User(new Email("tester@testi.de"), Password.createHashedPassword("123abcABC!§%"),
                                LocalDate.of(1999, 1, 21), "Tester 2 " + "Testi 2", "+49160304050", "My description",
                                true, localizable), new Set<Tag>() {
                    @Override
                    public int size() {
                        return 0;
                    }

                    @Override
                    public boolean isEmpty() {
                        return false;
                    }

                    @Override
                    public boolean contains(@Nullable Object o) {
                        return false;
                    }

                    @NonNull
                    @Override
                    public Iterator<Tag> iterator() {
                        return null;
                    }

                    @NonNull
                    @Override
                    public Object[] toArray() {
                        return new Object[0];
                    }

                    @NonNull
                    @Override
                    public <T> T[] toArray(@NonNull T[] a) {
                        return null;
                    }

                    @Override
                    public boolean add(Tag tag) {
                        return false;
                    }

                    @Override
                    public boolean remove(@Nullable Object o) {
                        return false;
                    }

                    @Override
                    public boolean containsAll(@NonNull Collection<?> c) {
                        return false;
                    }

                    @Override
                    public boolean addAll(@NonNull Collection<? extends Tag> c) {
                        return false;
                    }

                    @Override
                    public boolean retainAll(@NonNull Collection<?> c) {
                        return false;
                    }

                    @Override
                    public boolean removeAll(@NonNull Collection<?> c) {
                        return false;
                    }

                    @Override
                    public void clear() {
                    }
                }, "lasanje" + i, "mmm leker", 69.88, 1, LocalDateTime.now(),
                        () -> new SphericalPosition(48.9305065, 8.4612313)));
            }
            offerListAdapter.updateOffers(offerList);
            return;
        }

        try {

            switch (type) {
                case OWN:
                    offerListAdapter.updateOffers(offerVM.fetchOffers(offerVM.getCurrentUser()));
                    break;
                case BOOKMARKED:
                    // TODO offerListAdapter.updateOffers(offerVM.fetchBookmarkedOffers());
                    break;
                case SUBSCRIBED:
                    // TODO offerListAdapter.updateOffers(offerVM.fetchSubscribedOffers());
                    break;
                default:
                    offerListAdapter.updateOffers(offerVM.fetchOffers(new ArrayList<>()));
            }

        } catch (RequestHandlerException e) {
            // TODO remove debug toast
            Toast.makeText(getActivity(), "DEBUG OfferListFragment.java -> updateOffers(): " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

    }
}