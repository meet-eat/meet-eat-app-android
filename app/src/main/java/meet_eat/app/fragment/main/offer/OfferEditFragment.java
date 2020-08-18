package meet_eat.app.fragment.main.offer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.location.Address;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Objects;

import meet_eat.app.MainActivity;
import meet_eat.app.R;
import meet_eat.app.databinding.FragmentOfferEditBinding;
import meet_eat.app.fragment.ContextFormatter;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.main.OfferViewModel;
import meet_eat.data.entity.Offer;
import meet_eat.data.location.SphericalLocation;
import meet_eat.data.location.SphericalPosition;
import meet_eat.data.location.UnlocalizableException;

import static android.view.View.GONE;
import static meet_eat.app.fragment.ListType.STANDARD;
import static meet_eat.app.fragment.NavigationArgumentKey.LIST_TYPE;
import static meet_eat.app.fragment.NavigationArgumentKey.OFFER;

/**
 * This is the offer editing page. Here the user can edit his own offer. This is also the page where the user can
 * create a new offer.
 */
public class OfferEditFragment extends Fragment {

    private FragmentOfferEditBinding binding;
    private OfferViewModel offerVM;
    private NavController navController;
    private Bundle bundle;
    private Offer offer;
    private LocalDateTime dateTime;
    private Address address;
    private String title;
    private String city;
    private String priceString;
    private String participantsString;
    private String description;
    private double price;
    private int participants;
    private boolean isNewOffer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).selectMenuItem(1);
        binding = FragmentOfferEditBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        offerVM = new ViewModelProvider(requireActivity()).get(OfferViewModel.class);
        navController = NavHostFragment.findNavController(this);

        // Checks if the previous page sent a bundle of arguments containing an offer
        if (Objects.isNull(getArguments()) || Objects.isNull(getArguments().getSerializable(OFFER.name()))) {
            isNewOffer = true;
        } else {
            isNewOffer = false;
            offer = (Offer) getArguments().getSerializable(OFFER.name());
        }

        bundle = new Bundle();
        initUI();
        setButtonOnClickListener();
        return binding.getRoot();
    }

    /**
     * Sets various click listeners.
     */
    private void setButtonOnClickListener() {
        binding.ibtBack.setOnClickListener(event -> navController.navigateUp());
        binding.btOfferEditPublish.setOnClickListener(event -> saveOffer());
        binding.btOfferEditSave.setOnClickListener(event -> saveOffer());
        binding.btOfferEditDelete.setOnClickListener(event -> deleteOffer());
        binding.tvOfferEditDate.setOnClickListener(event -> showDateTimePicker());
    }

    /**
     * Shows a dialog where the user can pick the date for the offer. Calls showTimePickerDialog() with the selected
     * date.
     */
    private void showDateTimePicker() {
        Calendar cal = new GregorianCalendar();
        new DatePickerDialog(binding.getRoot().getContext(), this::showTimePickerDialog, cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * Shows a dialog where the user can pick a time for the offer, then updates the GUI.
     *
     * @param datePicker the dialog before
     * @param year       the year of the date
     * @param month      the month of the year
     * @param dayOfMonth the day of the month
     */
    private void showTimePickerDialog(DatePicker datePicker, int year, int month, int dayOfMonth) {
        ContextFormatter contextFormatter = new ContextFormatter(binding.getRoot().getContext());
        new TimePickerDialog(binding.getRoot().getContext(), (view, hourOfDay, minute) -> {
            dateTime = LocalDateTime.of(year, month + ContextFormatter.MONTH_CORRECTION, dayOfMonth, hourOfDay, minute);
            binding.tvOfferEditDate.setText(contextFormatter.formatDateTime(dateTime));
        }, LocalDateTime.now().getHour(), LocalDateTime.now().getMinute(), false).show();
    }

    /**
     * Tries to delete the users offer and navigate back to the offer list page on success.
     */
    private void deleteOffer() {
        try {
            offerVM.delete(offer);
            Toast.makeText(getActivity(), R.string.request_sent, Toast.LENGTH_SHORT).show();
            bundle.putSerializable(LIST_TYPE.name(), STANDARD);
            navController.navigate(R.id.offerListFragment, bundle);
        } catch (RequestHandlerException exception) {
            Toast.makeText(getActivity(), R.string.toast_error_message, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Gets the offer details from the GUI, checks them for correctness and saves the values in the global fields.
     *
     * @return true, if everything went fine
     */
    private boolean setOfferDetails() {
        ContextFormatter contextFormatter = new ContextFormatter(binding.getRoot().getContext());

        try {
            address = contextFormatter.formatAddressFromString(city);
        } catch (IOException exception) {
            Toast.makeText(getActivity(), R.string.missing_location, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (Objects.isNull(address)) {
            Toast.makeText(getActivity(), R.string.invalid_location, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (Objects.isNull(priceString)) {
            Toast.makeText(getActivity(), R.string.missing_price, Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            price = Double.parseDouble(priceString);
        } catch (NumberFormatException exception) {
            Toast.makeText(getActivity(), R.string.invalid_price, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (Objects.isNull(participantsString)) {
            Toast.makeText(getActivity(), R.string.missing_participants, Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            participants = Integer.parseInt(participantsString);
        } catch (NumberFormatException exception) {
            Toast.makeText(getActivity(), R.string.invalid_max_participants, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (participants < 1) {
            Toast.makeText(getActivity(), R.string.invalid_max_participants, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (Objects.isNull(title)) {
            Toast.makeText(getActivity(), R.string.missing_title, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (Objects.isNull(description)) {
            Toast.makeText(getActivity(), R.string.missing_description, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (Objects.isNull(dateTime)) {
            Toast.makeText(getActivity(), R.string.missing_date_time, Toast.LENGTH_SHORT).show();
            return false;
        } else if (dateTime.isBefore(LocalDateTime.now())) {
            Toast.makeText(getActivity(), R.string.invalid_date_time_past, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    /**
     * Gets the offer details and tries to creates a new offer with the details if the user wants to create a new offer
     * or tries to update the offer with the details.
     */
    private void saveOffer() {
        if (setOfferDetails()) {
            if (isNewOffer) {
                offer = new Offer(offerVM.getCurrentUser(), new HashSet<>(), title, description, price, participants,
                        dateTime,
                        new SphericalLocation(new SphericalPosition(address.getLatitude(), address.getLongitude())));

                try {
                    offerVM.add(offer);
                    Toast.makeText(getActivity(), R.string.request_sent, Toast.LENGTH_SHORT).show();
                    bundle.putSerializable(OFFER.name(), offer);
                    // Prevent the user to navigate back to the offer creation page
                    navController.navigateUp();
                    // navController.navigate(R.id.offerDetailedFragment, bundle);
                } catch (RequestHandlerException exception) {
                    Toast.makeText(getActivity(), R.string.toast_error_message, Toast.LENGTH_LONG).show();
                }
            } else {
                offer.setLocation(
                        new SphericalLocation(new SphericalPosition(address.getLatitude(), address.getLongitude())));
                offer.setPrice(price);
                offer.setMaxParticipants(participants);
                offer.setName(title);
                offer.setDescription(description);
                offer.setDateTime(LocalDateTime.from(dateTime));

                try {
                    offerVM.edit(offer);
                    Toast.makeText(getActivity(), R.string.request_sent, Toast.LENGTH_SHORT).show();
                    navController.navigateUp();
                } catch (RequestHandlerException exception) {
                    Toast.makeText(getActivity(), R.string.toast_error_message, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * Initializes the GUI by filling the edit text views with the current offer details. If a new offer is to be
     * created, the edit text views remain empty.
     */
    private void initUI() {
        if (isNewOffer) {
            binding.btOfferEditSave.setVisibility(GONE);
            binding.btOfferEditDelete.setVisibility(GONE);
        } else {
            binding.btOfferEditPublish.setVisibility(GONE);
            title = offer.getName();
            ContextFormatter contextFormatter = new ContextFormatter(binding.getRoot().getContext());
            dateTime = offer.getDateTime();
            binding.tvOfferEditDate.setText(contextFormatter.formatDateTime(dateTime));

            try {
                city = contextFormatter.formatStringFromLocalizable(offer.getLocation());
            } catch (IOException | UnlocalizableException exception) {
                Toast.makeText(getActivity(), getString(R.string.invalid_location), Toast.LENGTH_SHORT).show();
                return;
            }

            priceString = String.valueOf(offer.getPrice());
            participantsString = String.valueOf(offer.getMaxParticipants());
            description = offer.getDescription();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPriceString() {
        return priceString;
    }

    public void setPriceString(String priceString) {
        this.priceString = priceString;
    }

    public String getParticipantsString() {
        return participantsString;
    }

    public void setParticipantsString(String participantsString) {
        this.participantsString = participantsString;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}