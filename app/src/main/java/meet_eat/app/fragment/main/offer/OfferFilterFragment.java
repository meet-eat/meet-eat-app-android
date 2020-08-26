package meet_eat.app.fragment.main.offer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
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

import meet_eat.app.MainActivity;
import meet_eat.app.R;
import meet_eat.app.databinding.FragmentOfferFilterBinding;
import meet_eat.app.fragment.ContextFormatter;
import meet_eat.app.fragment.ListType;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.main.OfferViewModel;
import meet_eat.data.comparator.OfferComparableField;
import meet_eat.data.comparator.OfferComparator;
import meet_eat.data.location.UnlocalizableException;
import meet_eat.data.predicate.OfferPredicate;
import meet_eat.data.predicate.chrono.LocalDateTimeOperation;
import meet_eat.data.predicate.chrono.LocalDateTimePredicate;
import meet_eat.data.predicate.localizable.LocalizablePredicate;
import meet_eat.data.predicate.numeric.DoubleOperation;
import meet_eat.data.predicate.numeric.ParticipantsPredicate;
import meet_eat.data.predicate.numeric.PricePredicate;
import meet_eat.data.predicate.numeric.RatingPredicate;

import static meet_eat.app.fragment.NavigationArgumentKey.LIST_TYPE;

/**
 * This is the filter page where the user can set new filters and sort criteria.
 */
public class OfferFilterFragment extends Fragment {

    private static final int M_TO_KM_FACTOR = 1000;

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
        ((MainActivity) requireActivity()).selectMenuItem(1);
        binding = FragmentOfferFilterBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        offerVM = new ViewModelProvider(requireActivity()).get(OfferViewModel.class);
        navController = NavHostFragment.findNavController(this);

        // Checks if the previous page sent a bundle of arguments containing an offer list type
        if (Objects.isNull(getArguments()) || Objects.isNull(getArguments().getSerializable(LIST_TYPE.name()))) {
            navController.navigateUp();
        } else {
            originListType = (ListType) getArguments().getSerializable(LIST_TYPE.name());
        }

        initializeSortSpinner();
        setButtonOnClickListener();
        initUI();
        return binding.getRoot();
    }

    /**
     * Sets various click listener.
     */
    private void setButtonOnClickListener() {
        binding.ibtBack.setOnClickListener(event -> navController.navigateUp());
        binding.tvOfferFilterDateMin.setOnClickListener(this::showDateTimePicker);
        binding.tvOfferFilterDateMax.setOnClickListener(this::showDateTimePicker);
        binding.btOfferFilterSave.setOnClickListener(event -> saveFilters());
    }

    /**
     * Shows a dialog where the user can pick a date for the offer filters. Calls showTimePickerDialog() with the
     * selected date. Calls either showTimePickerDialogMin() or showTimePickerDialogMax().
     *
     * @param view decide which date filter edit text view was clicked
     */
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

    /**
     * Shows a dialog where the user can pick the earliest date and time for an offer to begin, then
     * updates the GUI.
     *
     * @param datePicker the dialog before
     * @param year       the year of the date
     * @param month      the month of the year
     * @param dayOfMonth the day of the month
     */
    private void showTimePickerDialogMin(DatePicker datePicker, int year, int month, int dayOfMonth) {
        ContextFormatter contextFormatter = new ContextFormatter(binding.getRoot().getContext());
        new TimePickerDialog(binding.getRoot().getContext(), (view, hourOfDay, minute) -> {
            minDateTime =
                    LocalDateTime.of(year, month + ContextFormatter.MONTH_CORRECTION, dayOfMonth, hourOfDay, minute);
            binding.tvOfferFilterDateMin.setText(contextFormatter.formatDateTime(minDateTime));
        }, LocalDateTime.now().getHour(), LocalDateTime.now().getMinute(), false).show();
    }

    /**
     * Shows a dialog where the user can pick the latest date and time for an offer to begin, then
     * updates the GUI.
     *
     * @param datePicker the dialog before
     * @param year       the year of the date
     * @param month      the month of the year
     * @param dayOfMonth the day of the month
     */
    private void showTimePickerDialogMax(DatePicker datePicker, int year, int month, int dayOfMonth) {
        ContextFormatter contextFormatter = new ContextFormatter(binding.getRoot().getContext());
        new TimePickerDialog(binding.getRoot().getContext(), (view, hourOfDay, minute) -> {
            maxDateTime =
                    LocalDateTime.of(year, month + ContextFormatter.MONTH_CORRECTION, dayOfMonth, hourOfDay, minute);
            binding.tvOfferFilterDateMax.setText(contextFormatter.formatDateTime(maxDateTime));
        }, LocalDateTime.now().getHour(), LocalDateTime.now().getMinute(), false).show();
    }

    /**
     * Checks the filters for correctness and tries to update the user filters.
     */
    private void saveFilters() {
        Collection<OfferPredicate> predicates = new ArrayList<>();

        if (Objects.nonNull(minDateTime) && Objects.nonNull(maxDateTime)) {
            if (maxDateTime.compareTo(minDateTime) < 0) {
                Toast.makeText(getActivity(), R.string.invalid_date_time_interval, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (Objects.nonNull(minDateTime)) {
            predicates.add(new LocalDateTimePredicate(LocalDateTimeOperation.AFTER, minDateTime));
        }

        if (Objects.nonNull(maxDateTime)) {
            predicates.add(new LocalDateTimePredicate(LocalDateTimeOperation.BEFORE, maxDateTime));
        }

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
                predicates.add(new LocalizablePredicate(DoubleOperation.GREATER,
                        Double.parseDouble(minDistance) * M_TO_KM_FACTOR, offerVM.getCurrentUser().getLocalizable()));
            } catch (UnlocalizableException exception) {
                Toast.makeText(getActivity(), getString(R.string.invalid_location), Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (Objects.nonNull(maxDistance) && !maxDistance.isEmpty()) {
            try {
                predicates.add(new LocalizablePredicate(DoubleOperation.LESS,
                        Double.parseDouble(maxDistance) * M_TO_KM_FACTOR, offerVM.getCurrentUser().getLocalizable()));
            } catch (UnlocalizableException exception) {
                Toast.makeText(getActivity(), getString(R.string.invalid_location), Toast.LENGTH_SHORT).show();
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
            offerVM.getCurrentUser().setOfferComparator(new OfferComparator(OfferComparableField.values()[spinner.getSelectedItemPosition()],
                    offerVM.getCurrentUser().getLocalizable()));
            Bundle bundle = new Bundle();
            // Adds the last offer list type to the arguments bundle
            bundle.putSerializable(LIST_TYPE.name(), originListType);
            offerVM.updatePredicates(predicates);
            navController.navigate(R.id.offerListFragment, bundle);
        } catch (RequestHandlerException exception) {
            Toast.makeText(getActivity(), R.string.toast_error_message, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Initializes the views with their values.
     */
    private void initUI() {
        ContextFormatter contextFormatter = new ContextFormatter(binding.getRoot().getContext());
        spinner.setSelection(offerVM.getCurrentUser().getOfferComparator().getField().ordinal());

        for (OfferPredicate offerPredicate : offerVM.getCurrentUser().getOfferPredicates()) {
            Class<? extends OfferPredicate> predicateClass = offerPredicate.getClass();

            if (predicateClass.equals(LocalDateTimePredicate.class)) {
                LocalDateTime localDateTime = ((LocalDateTimePredicate) offerPredicate).getReferenceValue();
                String localDateTimePredicateString = contextFormatter.formatDateTime(localDateTime);

                if (((LocalDateTimePredicate) offerPredicate).getOperation().equals(LocalDateTimeOperation.AFTER)) {
                    minDateTime = localDateTime;
                    binding.tvOfferFilterDateMin.setText(localDateTimePredicateString);
                } else {
                    maxDateTime = localDateTime;
                    binding.tvOfferFilterDateMax.setText(localDateTimePredicateString);
                }
            } else if (predicateClass.equals(PricePredicate.class)) {
                String price = String.valueOf(((PricePredicate) offerPredicate).getReferenceValue());

                if (((PricePredicate) offerPredicate).getOperation().equals(DoubleOperation.GREATER)) {
                    minPrice = price;
                } else {
                    maxPrice = price;
                }
            } else if (predicateClass.equals(LocalizablePredicate.class)) {
                String distance = String.valueOf(((LocalizablePredicate) offerPredicate).getReferenceValue());

                if (((LocalizablePredicate) offerPredicate).getOperation().equals(DoubleOperation.GREATER)) {
                    minDistance = distance;
                } else {
                    maxDistance = distance;
                }
            } else if (predicateClass.equals(ParticipantsPredicate.class)) {
                String participants =
                        String.valueOf(((ParticipantsPredicate) offerPredicate).getReferenceValue().intValue());

                if (((ParticipantsPredicate) offerPredicate).getOperation().equals(DoubleOperation.GREATER)) {
                    minParticipants = participants;
                } else {
                    maxParticipants = participants;
                }
            } else if (predicateClass.equals(RatingPredicate.class)) {
                String rating = String.valueOf(((RatingPredicate) offerPredicate).getReferenceValue());

                if (((RatingPredicate) offerPredicate).getOperation().equals(DoubleOperation.GREATER)) {
                    minRating = rating;
                } else {
                    maxRating = rating;
                }
            }
        }
    }

    /**
     * Initializes the sort spinner by adding the sort criteria.
     */
    private void initializeSortSpinner() {
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(binding.getRoot().getContext(), R.layout.color_spinner_layout,
                        getResources().getStringArray(R.array.items_offer_filter_spinner));
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