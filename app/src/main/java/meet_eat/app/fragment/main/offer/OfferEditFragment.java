package meet_eat.app.fragment.main.offer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.location.Address;
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

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Set;

import meet_eat.app.R;
import meet_eat.app.databinding.FragmentOfferEditBinding;
import meet_eat.app.fragment.ContextFormatter;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.main.OfferViewModel;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Tag;
import meet_eat.data.location.Localizable;
import meet_eat.data.location.SphericalPosition;

import static android.view.View.GONE;
import static meet_eat.app.fragment.NavigationArgumentKey.OFFER;
import static meet_eat.app.fragment.NavigationArgumentKey.TYPE;
import static meet_eat.app.fragment.OfferListType.STANDARD;

public class OfferEditFragment extends Fragment {

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
        binding.btOfferEditSave.setOnClickListener(event -> editOffer());
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
                                hourOfDay, minute);
                        ContextFormatter contextFormatter =
                                new ContextFormatter(binding.getRoot().getContext());
                        binding.tvOfferEditDate.setText(contextFormatter.formatDateTime(dateTime));
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

    private void editOffer() {
        ContextFormatter contextFormatter = new ContextFormatter(binding.getRoot().getContext());
        Address address = contextFormatter.getAddressFromString(city);

        if (address == null) {
            Toast.makeText(getActivity(), R.string.invalid_location, Toast.LENGTH_SHORT).show();
            return;
        }

        offer.setLocation(() -> new SphericalPosition(address.getLatitude(),
                address.getLongitude()));

        try {
            offer.setPrice(Double.parseDouble(price));
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), R.string.invalid_price, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            offer.setMaxParticipants(Integer.parseInt(participants));
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), R.string.invalid_max_participants, Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO offer image
        offer.setName(title);
        offer.setDateTime(LocalDateTime.from(dateTime));
        offer.setDescription(description);
        // TODO tags

        try {
            offerVM.edit(offer);
            Toast.makeText(getActivity(), R.string.request_send, Toast.LENGTH_SHORT).show();
            navController.navigateUp();
        } catch (RequestHandlerException e) {
            // TODO resolve error code
            Toast.makeText(getActivity(),
                    "DEBUG OfferEditFragment.java -> deleteOffer(): " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void publishOffer() {
        ContextFormatter contextFormatter = new ContextFormatter(binding.getRoot().getContext());
        Address address = contextFormatter.getAddressFromString(city);

        if (address == null) {
            Toast.makeText(getActivity(), R.string.invalid_location, Toast.LENGTH_SHORT).show();
            return;
        }

        Localizable location = () -> new SphericalPosition(address.getLatitude(),
                address.getLongitude());
        double price;

        try {
            price = Double.parseDouble(this.price);
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), R.string.invalid_price, Toast.LENGTH_SHORT).show();
            return;
        }

        int maxParticipants;

        try {
            maxParticipants = Integer.parseInt(participants);
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), R.string.invalid_max_participants, Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO offer image
        // TODO tags
        offer = new Offer(offerVM.getCurrentUser(), new Set<Tag>() {
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
        }, title, description, price, maxParticipants, LocalDateTime.from(dateTime), location);

        try {
            offerVM.add(offer);
            Toast.makeText(getActivity(), R.string.request_send, Toast.LENGTH_SHORT).show();
            Bundle bundle = new Bundle();
            bundle.putSerializable(OFFER.name(), offer);
            navController.navigate(R.id.offerDetailedFragment, bundle);
        } catch (RequestHandlerException e) {
            // TODO resolve error code
            Toast.makeText(getActivity(),
                    "DEBUG OfferEditFragment.java -> deleteOffer(): " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

    }

    private void initUI() {

        if (isNewOffer) {
            binding.btOfferEditSave.setVisibility(GONE);
            binding.btOfferEditDelete.setVisibility(GONE);
        } else {
            binding.btOfferEditPublish.setVisibility(GONE);
            title = offer.getName();
            ContextFormatter contextFormatter =
                    new ContextFormatter(binding.getRoot().getContext());
            dateTime = offer.getDateTime();
            binding.tvOfferEditDate.setText(contextFormatter.formatDateTime(dateTime));
            city = contextFormatter.getStringFromLocalizable(offer.getLocation());
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