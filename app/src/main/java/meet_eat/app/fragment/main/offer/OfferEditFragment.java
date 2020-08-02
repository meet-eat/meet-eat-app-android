package meet_eat.app.fragment.main.offer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;

import meet_eat.app.R;
import meet_eat.app.databinding.FragmentOfferEditBinding;
import meet_eat.app.fragment.FormatFragment;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.main.OfferViewModel;
import meet_eat.data.entity.Offer;
import meet_eat.data.location.UnlocalizableException;

import static android.view.View.GONE;
import static meet_eat.app.fragment.Key.*;
import static meet_eat.app.fragment.OfferListType.STANDARD;

public class OfferEditFragment extends FormatFragment {

    private static final int MONTH_CORRECTION = 1;

    private FragmentOfferEditBinding binding;
    private OfferViewModel offerVM;
    private NavController navController;
    private Offer offer;
    private LocalDateTime dateTime;
    private String title;
    private String city;
    private String price;
    private String participants;
    private String description;
    private boolean isNewOffer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentOfferEditBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        offerVM = new ViewModelProvider(requireActivity()).get(OfferViewModel.class);
        navController = NavHostFragment.findNavController(this);

        if (getArguments() == null || getArguments().getSerializable(OFFER.name()) == null) {
            isNewOffer = true;
        } else {
            isNewOffer = false;
            offer = (Offer) getArguments().getSerializable(OFFER.name());
        }

        initUI();
        setButtonOnClickListener();
        return binding.getRoot();
    }

    private void setButtonOnClickListener() {
        binding.ibtBack.setOnClickListener(event -> navController.navigateUp());
        binding.btOfferEditPublish.setOnClickListener(event -> publishOffer());
        binding.btOfferEditSave.setOnClickListener(event -> updateOffer());
        binding.btOfferEditDelete.setOnClickListener(event -> deleteOffer());
        binding.tvOfferEditDate.setOnClickListener(event -> showDateTimePicker());
    }

    private void showDateTimePicker() {
        Calendar cal = new GregorianCalendar();
        new DatePickerDialog(binding.getRoot().getContext(), (datePicker, year, month,
                                                              dayOfMonth) -> {
            new TimePickerDialog(binding.getRoot().getContext(),
                    (view, hourOfDay, minute) -> {
                        dateTime = LocalDateTime.of(year, month + MONTH_CORRECTION, dayOfMonth,
                                hourOfDay
                                , minute);
                        binding.tvOfferEditDate.setText(formatDateTime(dateTime));
                    }, LocalDateTime.now().getHour(), LocalDateTime.now().getMinute(), false).show();
        }, cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void deleteOffer() {

        try {
            offerVM.delete(offer);
            Toast.makeText(getActivity(), R.string.request_send, Toast.LENGTH_SHORT).show();
            Bundle bundle = new Bundle();
            bundle.putSerializable(TYPE.name(), STANDARD);
            navController.navigate(R.id.offerListFragment, bundle);
        } catch (RequestHandlerException e) {
            // TODO resolve error code
            Toast.makeText(getActivity(),
                    "DEBUG OfferEditFragment.java -> deleteOffer(): " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

    }

    private void updateOffer() {
        offer.setName(title);
        offer.setDateTime(LocalDateTime.from(dateTime));
    }

    private void publishOffer() {
    }

    private void initUI() {

        if (isNewOffer) {
            binding.btOfferEditSave.setVisibility(GONE);
            binding.btOfferEditDelete.setVisibility(GONE);
        } else {
            binding.btOfferEditPublish.setVisibility(GONE);
            title = offer.getName();
            binding.tvOfferEditDate.setText(formatDateTime(dateTime));

            try {
                // TODO distance
                city =
                        new Geocoder(binding.getRoot().getContext()).getFromLocation(offer.getLocation().getSphericalPosition().getLatitude(),
                                offer.getLocation().getSphericalPosition().getLongitude(), 1).get(0).getSubAdminArea();
            } catch (IOException | UnlocalizableException e) {
                // TODO remove debug toast
                Toast.makeText(getActivity(),
                        "DEBUG OfferEditFragment.java -> initUI(): " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }

            price = String.valueOf(offer.getPrice());
            participants = String.valueOf(offer.getMaxParticipants());
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}