package meet_eat.app.fragment.main.offer;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import meet_eat.app.R;
import meet_eat.app.databinding.ItemOfferCardBinding;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.main.OfferViewModel;
import meet_eat.data.entity.Offer;

import static meet_eat.app.fragment.Key.OFFER;

public class OfferListAdapter extends RecyclerView.Adapter<OfferListAdapter.ViewHolder> {

    private OfferViewModel offerVM;
    private ArrayList<Offer> currentOffers;

    public OfferListAdapter(OfferViewModel offerVM, ArrayList<Offer> offers) {
        this.offerVM = offerVM;
        currentOffers = offers;
    }

    public void updateOffers(ArrayList<Offer> offers) {
        currentOffers = offers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemOfferCardBinding binding = ItemOfferCardBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(currentOffers.get(position));
    }

    @Override
    public int getItemCount() {
        return currentOffers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ItemOfferCardBinding binding;
        private String dateFormatPattern;
        private String timeFormatPattern;
        private String timeCalling;
        private String pricePattern;
        private String currency;

        public ViewHolder(@NonNull ItemOfferCardBinding binding) {
            super(binding.getRoot());
            dateFormatPattern = binding.getRoot().getResources().getString(R.string.european_date_format);
            timeFormatPattern = binding.getRoot().getResources().getString(R.string.european_time_format);
            timeCalling = binding.getRoot().getResources().getString(R.string.european_time_calling);
            pricePattern = binding.getRoot().getResources().getString(R.string.price_format);
            currency = binding.getRoot().getResources().getString(R.string.currency);
            this.binding = binding;
        }

        public void setData(Offer offer) {
            binding.tvOfferCardTitle.setText(offer.getName());
            binding.tvOfferCardDescription.setText(offer.getDescription());
            binding.tvOfferCardDate.setText(formatDateTime(offer.getDateTime()));
            binding.tvPrice.setText(formatPrice(offer.getPrice()));
            // TODO binding.tvDistance.setText(Haversine.applyHaversineFormula(...));
            binding.tvOfferCardRating.setText(String.valueOf(offer.getCreator().getHostRating()));
            // TODO offer image
            binding.ivOfferCardPicture.setOnClickListener(event -> navigateToOfferDetailed(offer));

            if (offerVM.isBookmarked(offer)) {
                binding.ibtOfferCardBookmark.setColorFilter(ContextCompat.getColor(binding.getRoot().getContext(), R.color.bookmarked));
            }

            binding.ibtOfferCardBookmark.setOnClickListener(event -> changeBookmark(offer));
        }

        private void changeBookmark(Offer offer) {
            try {

                if (offerVM.isBookmarked(offer)) {
                    offerVM.removeBookmark(offer);
                    binding.ibtOfferCardBookmark.setColorFilter(ContextCompat.getColor(binding.getRoot().getContext(),
                            R.color.bookmarked), PorterDuff.Mode.SRC_IN);
                } else {
                    offerVM.addBookmark(offer);
                    binding.ibtOfferCardBookmark.setColorFilter(ContextCompat.getColor(binding.getRoot().getContext(),
                            R.color.symbol), PorterDuff.Mode.SRC_IN);
                }

            } catch (RequestHandlerException e) {
                Toast.makeText(binding.getRoot().getContext(), "Exception " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }

        private void navigateToOfferDetailed(Offer offer) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(OFFER.name(), offer);
            Navigation.findNavController(binding.getRoot()).navigate(R.id.offerDetailedFragment,
                    bundle);
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
}