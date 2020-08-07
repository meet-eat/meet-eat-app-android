package meet_eat.app.fragment.main.offer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Objects;

import meet_eat.app.R;
import meet_eat.app.databinding.FragmentOfferFilterBinding;
import meet_eat.app.fragment.ContextFormatter;
import meet_eat.app.fragment.ListType;
import meet_eat.app.fragment.SortCriterion;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.main.OfferViewModel;
import meet_eat.data.location.UnlocalizableException;
import meet_eat.data.predicate.OfferPredicate;
import meet_eat.data.predicate.chrono.ChronoLocalDateTimeOperation;
import meet_eat.data.predicate.chrono.LocalDateTimePredicate;
import meet_eat.data.predicate.localizable.LocalizablePredicate;
import meet_eat.data.predicate.numeric.DoubleOperation;
import meet_eat.data.predicate.numeric.ParticipantsPredicate;
import meet_eat.data.predicate.numeric.PricePredicate;
import meet_eat.data.predicate.numeric.RatingPredicate;

import static meet_eat.app.fragment.NavigationArgumentKey.LIST_TYPE;
import static meet_eat.app.fragment.NavigationArgumentKey.SORT_CRITERION;

public class OfferFilterFragment extends Fragment {

    private static final int MONTH_CORRECTION = 1;

    private FragmentOfferFilterBinding binding;
    private OfferViewModel offerVM;
    private NavController navController;
    private LocalDateTime minDateTime;
    private LocalDateTime maxDateTime;
    private Spinner spinner;
    private ListType originListType;
    private String minPrice;
    private String maxPrice;
    private String minDistance;
    private String maxDistance;
    private String minParticipants;
    private String maxParticipants;
    private String minRating;
    private String maxRating;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentOfferFilterBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        offerVM = new ViewModelProvider(requireActivity()).get(OfferViewModel.class);
        navController = NavHostFragment.findNavController(this);

        if (Objects.isNull(getArguments()) || Objects.isNull(getArguments().getSerializable(LIST_TYPE.name()))) {
            Log.i("DEBUG", "In OfferContactFragment.getArguments: " + "getArguments() null or getArguments()" +
                    ".getSerializable() null");
            navController.navigateUp();
        } else {
            originListType = (ListType) getArguments().getSerializable(LIST_TYPE.name());
        }

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
        Calendar cal = new GregorianCalendar();

        if (view.getId() == R.id.tvOfferFilterDateMin) {
            new DatePickerDialog(binding.getRoot().getContext(), this::showTimePickerDialogMin, cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        } else {
            new DatePickerDialog(binding.getRoot().getContext(), this::showTimePickerDialogMax, cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        }

    }

    private void showTimePickerDialogMin(DatePicker datePicker, int year, int month, int dayOfMonth) {
        ContextFormatter contextFormatter = new ContextFormatter(binding.getRoot().getContext());
        new TimePickerDialog(binding.getRoot().getContext(), (view, hourOfDay, minute) -> {
            minDateTime = LocalDateTime.of(year, month + MONTH_CORRECTION, dayOfMonth, hourOfDay, minute);
            binding.tvOfferFilterDateMin.setText(contextFormatter.formatDateTime(minDateTime));
        }, LocalDateTime.now().getHour(), LocalDateTime.now().getMinute(), false).show();
    }

    private void showTimePickerDialogMax(DatePicker datePicker, int year, int month, int dayOfMonth) {
        ContextFormatter contextFormatter = new ContextFormatter(binding.getRoot().getContext());
        new TimePickerDialog(binding.getRoot().getContext(), (view, hourOfDay, minute) -> {
            maxDateTime = LocalDateTime.of(year, month + MONTH_CORRECTION, dayOfMonth, hourOfDay, minute);
            binding.tvOfferFilterDateMax.setText(contextFormatter.formatDateTime(maxDateTime));
        }, LocalDateTime.now().getHour(), LocalDateTime.now().getMinute(), false).show();
    }

    private void saveFilters() {
        Collection<OfferPredicate> predicates = new ArrayList<>();

        if (Objects.nonNull(minDateTime) && Objects.nonNull(maxDateTime)) {

            if (maxDateTime.compareTo(minDateTime) < 0) {
                Toast.makeText(getActivity(), R.string.invalid_date_time_interval, Toast.LENGTH_SHORT).show();
                return;
            }

        }

        if (Objects.nonNull(minDateTime)) {
            predicates.add(new LocalDateTimePredicate(ChronoLocalDateTimeOperation.AFTER, minDateTime));
        }

        if (Objects.nonNull(maxDateTime)) {
            predicates.add(new LocalDateTimePredicate(ChronoLocalDateTimeOperation.BEFORE, maxDateTime));
        }

        // TODO possible parse errors for all following

        if (Objects.nonNull(minPrice) && Objects.nonNull(maxPrice) && !minPrice.isEmpty() && !maxPrice.isEmpty()) {

            if (Double.parseDouble(minPrice) > Double.parseDouble(maxPrice)) {
                Toast.makeText(getActivity(), R.string.invalid_price_interval, Toast.LENGTH_SHORT).show();
                return;
            }

        }

        if (Objects.nonNull(minPrice) && !minPrice.isEmpty()) {
            predicates.add(new PricePredicate(DoubleOperation.GREATER, Double.parseDouble(minPrice)));
        }

        if (Objects.nonNull(maxPrice) && !maxPrice.isEmpty()) {
            predicates.add(new PricePredicate(DoubleOperation.LESS, Double.parseDouble(maxPrice)));
        }

        if (Objects.nonNull(minDistance) && Objects.nonNull(maxDistance) && !minDistance.isEmpty() &&
                !maxDistance.isEmpty()) {

            if (Double.parseDouble(minDistance) > Double.parseDouble(maxDistance)) {
                Toast.makeText(getActivity(), R.string.invalid_distance_interval, Toast.LENGTH_SHORT).show();
                return;
            }

        }

        if (Objects.nonNull(minDistance) && !minDistance.isEmpty()) {

            try {
                predicates.add(new LocalizablePredicate(DoubleOperation.GREATER, Double.parseDouble(minDistance),
                        offerVM.getCurrentUser().getLocalizable()));
            } catch (UnlocalizableException e) {
                // TODO remove debug toast
                Log.i("DEBUG", "In OfferFilterFragment.saveFilters.182: " + e.getMessage());
                return;
            }

        }

        if (Objects.nonNull(maxDistance) && !maxDistance.isEmpty()) {

            try {
                predicates.add(new LocalizablePredicate(DoubleOperation.LESS, Double.parseDouble(maxDistance),
                        offerVM.getCurrentUser().getLocalizable()));
            } catch (UnlocalizableException e) {
                // TODO remove debug toast
                Log.i("DEBUG", "In OfferFilterFragment.saveFilters.195: " + e.getMessage());
                return;
            }

        }

        if (Objects.nonNull(minParticipants) && Objects.nonNull(maxParticipants) && !minParticipants.isEmpty() &&
                !maxParticipants.isEmpty()) {

            if (Integer.parseInt(minParticipants) > Integer.parseInt(maxParticipants)) {
                Toast.makeText(getActivity(), R.string.invalid_participants_interval, Toast.LENGTH_SHORT).show();
                return;
            }

        }

        if (Objects.nonNull(minParticipants) && !minParticipants.isEmpty()) {
            predicates.add(new ParticipantsPredicate(DoubleOperation.GREATER, Double.parseDouble(minParticipants)));
        }

        if (Objects.nonNull(maxParticipants) && !maxParticipants.isEmpty()) {
            predicates.add(new ParticipantsPredicate(DoubleOperation.LESS, Double.parseDouble(maxParticipants)));
        }

        if (Objects.nonNull(minRating) && Objects.nonNull(maxRating) && !minRating.isEmpty() && !maxRating.isEmpty()) {

            if (Double.parseDouble(minRating) > Double.parseDouble(maxRating)) {
                Toast.makeText(getActivity(), R.string.invalid_rating_interval, Toast.LENGTH_SHORT).show();
                return;
            }

        }

        if (Objects.nonNull(minRating) && !minRating.isEmpty()) {
            predicates.add(new RatingPredicate(DoubleOperation.GREATER, Double.parseDouble(minRating)));
        }

        if (Objects.nonNull(maxRating) && !maxRating.isEmpty()) {
            predicates.add(new RatingPredicate(DoubleOperation.LESS, Double.parseDouble(maxRating)));
        }

        try {
            offerVM.updatePredicates(predicates);
            Bundle bundle = new Bundle();
            bundle.putSerializable(SORT_CRITERION.name(), SortCriterion.values()[spinner.getSelectedItemPosition()]);
            bundle.putSerializable(LIST_TYPE.name(), originListType);
            navController.navigate(R.id.offerListFragment, bundle);
        } catch (RequestHandlerException e) {
            // TODO resolve error code
            Log.i("DEBUG", "In OfferFilterFragment.saveFilters.243: " + e.getMessage());
        }
    }

    // add sorting criteria to view
    private void initializeSortSpinner() {
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(binding.getRoot().getContext(), R.layout.color_spinner_layout,
                        getResources().getStringArray(R.array.items_offer_filter_spinner));

        /*ArrayAdapter arrayAdapter = ArrayAdapter
                .createFromResource(binding.getRoot().getContext(), R.array.items_offer_filter_spinner,
                        R.layout.color_spinner_layout);*/
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.sOfferFilterSort.setAdapter(arrayAdapter);
        spinner = binding.sOfferFilterSort;
        spinner.setAdapter(arrayAdapter);
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(String minDistance) {
        this.minDistance = minDistance;
    }

    public String getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(String maxDistance) {
        this.maxDistance = maxDistance;
    }

    public String getMinParticipants() {
        return minParticipants;
    }

    public void setMinParticipants(String minParticipants) {
        this.minParticipants = minParticipants;
    }

    public String getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(String maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public String getMinRating() {
        return minRating;
    }

    public void setMinRating(String minRating) {
        this.minRating = minRating;
    }

    public String getMaxRating() {
        return maxRating;
    }

    public void setMaxRating(String maxRating) {
        this.maxRating = maxRating;
    }
}