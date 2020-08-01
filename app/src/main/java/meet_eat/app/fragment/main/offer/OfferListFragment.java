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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import meet_eat.app.R;
import meet_eat.app.databinding.FragmentOfferListBinding;
import meet_eat.app.viewmodel.main.OfferViewModel;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Tag;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.User;
import meet_eat.data.location.SphericalPosition;

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
        offerListAdapter = new OfferListAdapter(offerVM, new ArrayList<>());

        if (getArguments() == null ) {
            navController.navigateUp();
        } else {
            type = getArguments().getInt("type");
        }
        updateUI();

        setButtonOnClickListener();
        return binding.getRoot();
    }

    private void updateUI() {
        updateOffers();
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

    private void updateOffers() {
        ArrayList<Offer> offerList = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            offerList.add(new Offer(new User(new Email("tester@testi.de"), Password
                    .createHashedPassword("123abcABC!§%"), LocalDate.of(1999, 1, 21), "Tester 2 Testi 2",
                    "+49160304050", "My description", true), new Set<Tag>() {
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

        switch (type) {
            case OFFER_LIST_TYPE_STANDARD:
                offerListAdapter.updateOffers(offerList);
                break;
            case OFFER_LIST_TYPE_OWN:
                offerListAdapter.updateOffers(offerList);
                break;
            case OFFER_LIST_TYPE_BOOKMARKED:
                offerListAdapter.updateOffers(offerList);
                break;
            case OFFER_LIST_TYPE_SUBSCRIBED:
                offerListAdapter.updateOffers(offerList);
                break;
            default:
                offerListAdapter.updateOffers(offerList);
                break;
        }

        // TODO switch case for type, offerListAdapter.updateOffers(type filters)
    }
}
