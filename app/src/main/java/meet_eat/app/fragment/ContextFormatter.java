package meet_eat.app.fragment;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import meet_eat.app.R;
import meet_eat.data.location.Localizable;
import meet_eat.data.location.UnlocalizableException;

/**
 * This is a helper class. It contains various methods to format values and objects.
 */
public class ContextFormatter {

    /**
     * Needed to correct the months value, because the months start with 0.
     */
    public static final int MONTH_CORRECTION = 1;

    private static final int M_TO_KM_FACTOR = 1000;
    private static final int MAX_RESULTS = 1;

    private final Context context;

    /**
     * Initializes the context.
     *
     * @param context the context
     */
    public ContextFormatter(Context context) {
        this.context = Objects.requireNonNull(context);
    }

    /**
     * Formats a {@link LocalDateTime} object to a string by calling the formatDate() and formatTime() methods.
     *
     * @param dateTime the {@link LocalDateTime} object to be formatted
     * @return the formatted {@link LocalDateTime} object
     */
    public String formatDateTime(LocalDateTime dateTime) {
        return formatDate(dateTime.toLocalDate()) + ", " + formatTime(dateTime.toLocalTime());
    }

    /**
     * Formats a {@link LocalDate} object to a string.
     *
     * @param localDate the {@link LocalDate} object to be formatted
     * @return the formatted {@link LocalDate} object
     */
    public String formatDate(LocalDate localDate) {
        return localDate
                .format(DateTimeFormatter.ofPattern(context.getResources().getString(R.string.european_date_format)));
    }

    /**
     * Formats a {@link LocalTime} object to a string.
     *
     * @param localTime the {@link LocalTime} object to be formatted
     * @return the formatted {@link LocalTime} object
     */
    public String formatTime(LocalTime localTime) {
        return localTime
                .format(DateTimeFormatter.ofPattern(context.getResources().getString(R.string.european_time_format))) +
                " " + context.getResources().getString(R.string.european_time_calling);
    }

    /**
     * Formats a double to represent a price string.
     *
     * @param price the double to be formatted
     * @return the formatted double
     */
    public String formatPrice(Double price) {
        return String.format(context.getResources().getString(R.string.decimal_format), price) +
                context.getResources().getString(R.string.currency);
    }

    /**
     * Formats a double to represent a distance string.
     *
     * @param distance the double to be formatted
     * @return the formatted double
     */
    public String formatDistance(Double distance) {
        return (int) (distance / M_TO_KM_FACTOR) + context.getResources().getString(R.string.distance_unit);
    }

    /**
     * Formats a string to an {@link Address} object by using a {@link Geocoder} object.
     *
     * @param location the string which contains the location
     * @return the {@link Address} object of the location name, or null on error
     */
    public Address formatAddressFromString(String location) {
        location = Objects.toString(location, "");
        Geocoder geocoder = new Geocoder(context);
        Address address = null;

        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocationName(location, MAX_RESULTS);
        } catch (IOException e) {
            return null;
        }
        if (Objects.nonNull(addressList) && addressList.size() > 0) {
            return addressList.get(0);
        }

        return address;
    }

    /**
     * Formats an {@link Address} object to a string.
     *
     * @param address the {@link Address} object to be formatted
     * @return the string representation of the addresses sub-admin area and if available, also the postal code
     */
    public String formatStringFromAddress(Address address) {
        return address.getAddressLine(0);
    }

    /**
     * Formats a {@link Localizable} object to a string using a {@link Geocoder} object and by calling the
     * formatStringFromAddress()
     * method.
     *
     * @param localizable the {@link Localizable} object to be formatted
     * @return the string representation of the addresses sub-admin area and if available, also the postal code
     * @throws IOException            when the address couldn't be found
     * @throws UnlocalizableException if the {@link Localizable} object is invalid
     */
    public String formatStringFromLocalizable(Localizable localizable) throws IOException, UnlocalizableException {
        Address address;
        Geocoder geocoder = new Geocoder(context);
        double lat = localizable.getSphericalPosition().getLatitude();
        double lng = localizable.getSphericalPosition().getLongitude();

        if (Objects.nonNull(geocoder.getFromLocation(lat, lng, MAX_RESULTS)) &&
                geocoder.getFromLocation(lat, lng, MAX_RESULTS).size() > 0) {
            address = geocoder.getFromLocation(lat, lng, MAX_RESULTS).get(0);
        } else {
            return context.getResources().getString(R.string.invalid_location);
        }

        return formatStringFromAddress(address);
    }
}