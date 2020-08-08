package meet_eat.app.fragment.main.offer;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Stream;

import meet_eat.app.R;
import meet_eat.app.databinding.ItemOfferCardBinding;
import meet_eat.app.fragment.ContextFormatter;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.main.OfferViewModel;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.user.User;
import meet_eat.data.location.UnlocalizableException;

import static android.view.View.GONE;
import static meet_eat.app.fragment.NavigationArgumentKey.OFFER;

public class OfferListAdapter extends RecyclerView.Adapter<OfferListAdapter.ViewHolder> {

    private OfferViewModel offerVM;
    private ArrayList<Offer> currentOffers;

    public OfferListAdapter(OfferViewModel offerVM, ArrayList<Offer> offers) {
        this.offerVM = offerVM;
        currentOffers = offers;
    }

    public void updateOffers(Iterable<Offer> offers) {
        if (Objects.isNull(offers)) {
            return;
        }
        offers.forEach(currentOffers::add);
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

        public ViewHolder(@NonNull ItemOfferCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(Offer offer) {
            ContextFormatter contextFormatter = new ContextFormatter(binding.getRoot().getContext());
            binding.tvOfferCardTitle.setText(offer.getName());
            binding.tvOfferCardDescription.setText(offer.getDescription());
            binding.tvOfferCardDate.setText(contextFormatter.formatDateTime(offer.getDateTime()));
            binding.tvOfferCardPrice.setText(contextFormatter.formatPrice(offer.getPrice()));

            try {
                binding.tvOfferCardDistance.setText(contextFormatter
                        .formatDistance(offerVM.getCurrentUser().getLocalizable().getDistance(offer.getLocation())));
            } catch (UnlocalizableException e) {
                Toast.makeText(binding.getRoot().getContext(),
                        binding.getRoot().getResources().getString(R.string.invalid_location), Toast.LENGTH_SHORT)
                        .show();
                Log.i("DEBUG", "In OfferListAdapter.setData: " + e.getMessage());
                return;
            }

            binding.tvOfferCardRating.setText(String.valueOf(offer.getCreator().getHostRating()));
            // add offer image
            binding.ivOfferCardPicture.setOnClickListener(event -> navigateToOfferDetailed(offer));

            if (!offerVM.isCreator(offer) && !offerVM.isParticipating(offer)) {

                if (offerVM.isBookmarked(offer)) {
                    binding.ibtOfferCardBookmark
                            .setColorFilter(ContextCompat.getColor(binding.getRoot().getContext(), R.color.bookmarked));
                }

                binding.ibtOfferCardBookmark.setOnClickListener(event -> changeBookmark(offer));
            } else {
                binding.ibtOfferCardBookmark.setVisibility(GONE);
            }

            setColorOfOfferCard(offer);

        }

        private void setColorOfOfferCard(Offer offer) {

            if (offerVM.isCreator(offer)) {
                binding.ivOfferCardPicture
                        .setColorFilter(ContextCompat.getColor(binding.getRoot().getContext(), R.color.ownOffer),
                                PorterDuff.Mode.SRC_IN);
            } else if (offerVM.isParticipating(offer)) {
                binding.ivOfferCardPicture.setColorFilter(
                        ContextCompat.getColor(binding.getRoot().getContext(), R.color.participatingOffer),
                        PorterDuff.Mode.SRC_IN);
            }

        }

        private void changeBookmark(Offer offer) {

            try {

                if (offerVM.isBookmarked(offer)) {
                    offerVM.removeBookmark(offer);
                    binding.ibtOfferCardBookmark
                            .setColorFilter(ContextCompat.getColor(binding.getRoot().getContext(), R.color.symbol),
                                    PorterDuff.Mode.SRC_IN);
                } else {
                    offerVM.addBookmark(offer);
                    binding.ibtOfferCardBookmark
                            .setColorFilter(ContextCompat.getColor(binding.getRoot().getContext(), R.color.bookmarked),
                                    PorterDuff.Mode.SRC_IN);
                }

                notifyDataSetChanged();
            } catch (RequestHandlerException e) {
                Toast.makeText(binding.getRoot().getContext(), R.string.request_handler_exception_toast_error_message,
                        Toast.LENGTH_LONG).show();
                Log.i("DEBUG", "In OfferListAdapter.changeBookmark: " + e.getMessage());
            }

        }

        private void navigateToOfferDetailed(Offer offer) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(OFFER.name(), offer);
            Navigation.findNavController(binding.getRoot()).navigate(R.id.offerDetailedFragment, bundle);
        }
    }
}