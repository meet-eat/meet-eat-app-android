package meet_eat.app.fragment;

import androidx.fragment.app.Fragment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import meet_eat.app.R;

public abstract class FormatFragment extends Fragment {

    private String dateFormatPattern;
    private String timeFormatPattern;
    private String timeCalling;
    private String pricePattern;
    private String currency;

    public void setPatterns() {
        dateFormatPattern = getResources().getString(R.string.european_date_format);
        timeFormatPattern = getResources().getString(R.string.european_time_format);
        timeCalling = getResources().getString(R.string.european_time_calling);
        pricePattern = getResources().getString(R.string.price_format);
        currency = getResources().getString(R.string.currency);
    }

    public String formatDateTime(LocalDateTime dateTime) {
        return formatDate(dateTime.toLocalDate()) + ", " + formatTime(dateTime.toLocalTime());
    }

    public String formatDate(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern(dateFormatPattern));
    }

    public String formatTime(LocalTime localTime) {
        return localTime.format(DateTimeFormatter.ofPattern(timeFormatPattern)) + " " + timeCalling;
    }

    public String formatPrice(Double price) {
        return String.format(pricePattern, price) + currency;
    }
}