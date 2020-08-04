package meet_eat.app.fragment;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import meet_eat.app.R;
import meet_eat.data.location.Localizable;
import meet_eat.data.location.UnlocalizableException;

public class ContextFormatter {

    private final Context context;

    public ContextFormatter(Context context) {
        this.context = Objects.requireNonNull(context);
    }

    public String formatDateTime(LocalDateTime dateTime) {
        return formatDate(dateTime.toLocalDate()) + ", " + formatTime(dateTime.toLocalTime());
    }

    public String formatDate(LocalDate localDate) {
        return localDate
                .format(DateTimeFormatter.ofPattern(context.getResources().getString(R.string.european_date_format)));
    }

    public String formatTime(LocalTime localTime) {
        return localTime
                .format(DateTimeFormatter.ofPattern(context.getResources().getString(R.string.european_time_format))) +
                " " + context.getResources().getString(R.string.european_time_calling);
    }

    public String formatPrice(Double price) {
        return String.format(context.getResources().getString(R.string.price_format), price) +
                context.getResources().getString(R.string.currency);
    }

    public Address getAddressFromString(String location) throws IOException {
        Geocoder geocoder = new Geocoder(context);
        Address address = null;

        if (geocoder.getFromLocationName(location, 1) != null) {
            address = geocoder.getFromLocationName(location, 1).get(0);
        }

        return address;
    }

    public String getStringFromAddress(Address address) {

        if (Objects.requireNonNull(address).getPostalCode() == null) {
            return address.getSubAdminArea();
        }

        return address.getPostalCode() + ", " + address.getSubAdminArea();
    }

    public String getStringFromLocalizable(Localizable localizable) throws IOException, UnlocalizableException {
        return getStringFromAddress(new Geocoder(context)
                .getFromLocation(localizable.getSphericalPosition().getLatitude(),
                        localizable.getSphericalPosition().getLongitude(), 1).get(0));
    }
}