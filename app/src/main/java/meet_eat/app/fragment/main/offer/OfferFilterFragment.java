package meet_eat.app.fragment.main.offer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

import meet_eat.app.R;
import meet_eat.app.databinding.FragmentOfferFilterBinding;
import meet_eat.app.fragment.ContextFormatter;
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

public class OfferFilterFragment extends Fragment {

    private static final int MONTH_CORRECTION = 1;

    private FragmentOfferFilterBinding binding;
    private OfferViewModel offerVM;
    private NavController navController;
    private Collection<OfferPredicate> predicates;
    private LocalDateTime minDateTime;
    private LocalDateTime maxDateTime;
    private Spinner spinner;
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
        initializeSortSpinner();
        initUI();
        setButtonOnClickListener();
        return binding.getRoot();
    }

    private void initUI() {
        // TODO
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
        // TODO sort

        if (minDateTime != null && maxDateTime != null) {

            if (maxDateTime.compareTo(minDateTime) < 0) {
                Toast.makeText(getActivity(), R.string.invalid_date_time_interval, Toast.LENGTH_SHORT).show();
                return;
            } else {
                predicates.add(new LocalDateTimePredicate(ChronoLocalDateTimeOperation.AFTER, minDateTime));
                predicates.add(new LocalDateTimePredicate(ChronoLocalDateTimeOperation.BEFORE, maxDateTime));
            }

        } else if (minDateTime != null) {
            predicates.add(new LocalDateTimePredicate(ChronoLocalDateTimeOperation.AFTER, minDateTime));
        } else if (maxDateTime != null) {
            predicates.add(new LocalDateTimePredicate(ChronoLocalDateTimeOperation.BEFORE, maxDateTime));
        } // no else statement, because no predicates must be added

        // TODO possible error for all following

        if (minPrice != null) {
            predicates.add(new PricePredicate(DoubleOperation.GREATER, Double.parseDouble(minPrice)));
        }

        if (maxPrice != null) {
            predicates.add(new PricePredicate(DoubleOperation.LESS, Double.parseDouble(maxPrice)));
        }

        if (minDistance != null) {

            try {
                predicates.add(new LocalizablePredicate(DoubleOperation.GREATER,
                        Integer.parseInt(minDistance),
                        offerVM.getCurrentUser().getLocalizable()));
            } catch (UnlocalizableException e) {
                // TODO remove debug toast
                Toast.makeText(getActivity(), "DEBUG OfferFilterFragment.java -> saveFilters(): " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
                return;
            }

        }

        if (maxDistance != null) {

            try {
                predicates.add(new LocalizablePredicate(DoubleOperation.LESS, Integer.parseInt(maxDistance),
                        offerVM.getCurrentUser().getLocalizable()));
            } catch (UnlocalizableException e) {
                // TODO remove debug toast
                Toast.makeText(getActivity(), "DEBUG OfferFilterFragment.java -> saveFilters(): " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
                return;
            }

        }

        if (minParticipants != null) {
            predicates.add(new ParticipantsPredicate(DoubleOperation.GREATER, Integer.parseInt(minParticipants)));
        }

        if (maxParticipants != null) {
            predicates.add(new ParticipantsPredicate(DoubleOperation.LESS, Integer.parseInt(maxParticipants)));
        }

        if (minRating != null) {
            predicates.add(new RatingPredicate(DoubleOperation.GREATER, Double.parseDouble(minRating)));
        }

        if (maxRating != null) {
            predicates.add(new RatingPredicate(DoubleOperation.LESS, Double.parseDouble(maxRating)));
        }

        try {
            offerVM.updatePredicates(predicates);
            navController.navigateUp();
        } catch (RequestHandlerException e) {
            // TODO resolve error code
            Toast.makeText(getActivity(), "DEBUG OfferEditFragment.java -> deleteOffer(): " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

    }

    // add sorting criteria to view
    private void initializeSortSpinner() {
        ArrayAdapter arrayAdapter = ArrayAdapter
                .createFromResource(binding.getRoot().getContext(), R.array.items_offer_filter_spinner,
                        R.layout.color_spinner_layout);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.sOfferFilterSort.setAdapter(arrayAdapter);
        spinner = binding.sOfferFilterSort;
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO sort
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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