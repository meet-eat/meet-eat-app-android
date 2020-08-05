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
        return String.format(context.getResources().getString(R.string.decimal_format), price) +
                context.getResources().getString(R.string.currency);
    }

    public String formatDistance(Double distance) {
        return String.format(context.getResources().getString(R.string.decimal_format), distance / 1000) +
                context.getResources().getString(R.string.distance_unit);
    }

    public Address formatAddressFromString(String location) throws IOException {
        Geocoder geocoder = new Geocoder(context);
        Address address = null;

        if (Objects.nonNull(geocoder.getFromLocationName(location, 1))) {
            address = geocoder.getFromLocationName(location, 1).get(0);
        }

        return address;
    }

    public String formatStringFromAddress(Address address) {

        if (Objects.isNull(Objects.requireNonNull(address).getPostalCode())) {
            return address.getSubAdminArea();
        }

        return address.getPostalCode() + ", " + address.getSubAdminArea();
    }

    public String formatStringFromLocalizable(Localizable localizable) throws IOException, UnlocalizableException {
        return formatStringFromAddress(new Geocoder(context)
                .getFromLocation(localizable.getSphericalPosition().getLatitude(),
                        localizable.getSphericalPosition().getLongitude(), 1).get(0));
    }
}