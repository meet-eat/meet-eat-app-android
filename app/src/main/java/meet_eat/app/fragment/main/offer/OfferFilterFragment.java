package meet_eat.app.fragment.main.offer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

import meet_eat.app.R;
import meet_eat.app.databinding.FragmentOfferFilterBinding;
import meet_eat.app.viewmodel.main.OfferViewModel;
import meet_eat.data.entity.user.User;
import meet_eat.data.predicate.chrono.ChronoLocalDateTimeOperation;
import meet_eat.data.predicate.chrono.LocalDateTimePredicate;

public class OfferFilterFragment extends Fragment {

    private FragmentOfferFilterBinding binding;
    private OfferViewModel offerVM;
    private NavController navController;
    private Collection<Predicate> predicates;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentOfferFilterBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        offerVM = new ViewModelProvider(requireActivity()).get(OfferViewModel.class);
        navController = NavHostFragment.findNavController(this);
        initializeSortSpinner();
        setButtonOnClickListener();
        return binding.getRoot();
    }

    private void setButtonOnClickListener() {
        binding.btOfferFilterSave.setOnClickListener(event -> saveFilters());
        binding.tvOfferFilterDateMin.setOnClickListener(this::showDateTimePicker);
        binding.tvOfferFilterDateMax.setOnClickListener(this::showDateTimePicker);
        binding.ibtBack.setOnClickListener(event -> navController.navigateUp());
    }

    private void showDateTimePicker(View view) {
        LocalDateTime minDateTime = null;
        LocalDateTime maxDateTime = null;
        if (view.getId() == R.id.tvOfferFilterDateMin) {
            // TODO minDateTime = showDateTimePicker();
        } else {
            // TODO maxDateTime = showDateTimePicker();
        }

        if (maxDateTime.compareTo(minDateTime) < 0) {
            Toast.makeText(getActivity(), "Zeiten überschneiden sich.", Toast.LENGTH_SHORT).show();
            return;
        } else if (maxDateTime.compareTo(minDateTime) == 0) {
            predicates.add(new LocalDateTimePredicate(ChronoLocalDateTimeOperation.EQUAL, maxDateTime));
            return;
        } else {
            predicates.add(new LocalDateTimePredicate(ChronoLocalDateTimeOperation.BEFORE, minDateTime));
            predicates.add(new LocalDateTimePredicate(ChronoLocalDateTimeOperation.AFTER, maxDateTime));

        }
    }

    private void saveFilters() {
        User currentUser = offerVM.getCurrentUser();
        // TODO currentUser.addManyOfferPredicates();
    }

    /* add sorting criteria to view */
    private void initializeSortSpinner() {
        ArrayList<String> spinnerItems = new ArrayList<>();
        spinnerItems.add(getString(R.string.sort_spinner_item_time));
        spinnerItems.add(getString(R.string.sort_spinner_item_price));
        spinnerItems.add(getString(R.string.sort_spinner_item_distance));
        spinnerItems.add(getString(R.string.sort_spinner_item_amount_participants));
        spinnerItems.add(getString(R.string.sort_spinner_item_host_rating));

        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(binding.getRoot().getContext(), android.R.layout.simple_spinner_item, spinnerItems);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.sOfferFilterSort.setAdapter(arrayAdapter);
    }
}