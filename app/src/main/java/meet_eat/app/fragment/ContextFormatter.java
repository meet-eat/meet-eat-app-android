package meet_eat.app.fragment;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import meet_eat.app.R;

public class ContextFormatter {

    private final Context context;

    public ContextFormatter(Context context) {
        this.context = Objects.requireNonNull(context);
    }

    public String formatDateTime(LocalDateTime dateTime) {
        return formatDate(dateTime.toLocalDate()) + ", " + formatTime(dateTime.toLocalTime());
    }

    public String formatDate(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern(context.getResources().getString(R.string.european_date_format)));
    }

    public String formatTime(LocalTime localTime) {
        return localTime.format(DateTimeFormatter.ofPattern(context.getResources().getString(R.string.european_time_format))) + " " + context.getResources().getString(R.string.european_time_calling);
    }

    public String formatPrice(Double price) {
        return String.format(context.getResources().getString(R.string.price_format), price) + context.getResources().getString(R.string.currency);
    }

    public Address getLocationFromString(String location) {
        Geocoder geocoder = new Geocoder(context);
        Address address = null;

        try {

            if (geocoder.getFromLocationName(location, 1) != null) {
                address = geocoder.getFromLocationName(location, 1).get(0);
            }

            return address;
        } catch (IOException e) {
            // TODO remove debug toast
            Toast.makeText(context,
                    "DEBUG RegisterFragment.java -> getLocationFromUI(): " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
            return null;
        }

    }

    public String getStringFromLocation(Address address) {

        if (Objects.requireNonNull(address).getPostalCode() == null) {
            return address.getSubAdminArea();
        }

        return address.getPostalCode() + ", " + address.getSubAdminArea();
    }
}