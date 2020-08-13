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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import meet_eat.app.R;
import meet_eat.app.databinding.ItemOfferCardBinding;
import meet_eat.app.fragment.ContextFormatter;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.main.OfferViewModel;
import meet_eat.data.entity.Offer;
import meet_eat.data.location.UnlocalizableException;

import static android.view.View.GONE;
import static meet_eat.app.fragment.NavigationArgumentKey.OFFER;

/**
 * Contains the various offer summary cards displayed in the offer list pages.
 */
public class OfferListAdapter extends RecyclerView.Adapter<OfferListAdapter.ViewHolder> {

    private final OfferViewModel offerVM;
    private final List<Offer> currentOffers;

    /**
     * Initializing fields.
     *
     * @param offerVM the offer view model
     * @param offers  the fetched offers list
     */
    public OfferListAdapter(OfferViewModel offerVM, ArrayList<Offer> offers) {
        this.offerVM = offerVM;
        currentOffers = offers;
    }

    /**
     * Updates the offer list.
     *
     * @param offers the fetched offers list
     */
    public void updateOffers(Iterable<Offer> offers) {
        if (Objects.isNull(offers)) {
            return;
        }
        currentOffers.clear();
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

    /**
     * Holds the individual offers.
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        private ItemOfferCardBinding binding;

        /**
         * Initializes the binding.
         *
         * @param binding the binding
         */
        public ViewHolder(@NonNull ItemOfferCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        /**
         * Initializes the GUI of an offer summary card and sets its click listeners.
         *
         * @param offer the offer to be shown in this offer summary card
         */
        public void setData(Offer offer) {
            ContextFormatter contextFormatter = new ContextFormatter(binding.getRoot().getContext());
            binding.tvOfferCardTitle.setText(offer.getName());
            binding.tvOfferCardTitle.setSelected(true);
            binding.tvOfferCardDescription.setText(offer.getDescription());
            binding.tvOfferCardDate.setText(contextFormatter.formatDateTime(offer.getDateTime()));
            binding.tvOfferCardPrice.setText(contextFormatter.formatPrice(offer.getPrice()));

            try {
                binding.tvOfferCardDistance.setText(contextFormatter
                        .formatDistance(offerVM.getCurrentUser().getLocalizable().getDistance(offer.getLocation())));
            } catch (UnlocalizableException exception) {
                Toast.makeText(binding.getRoot().getContext(),
                        binding.getRoot().getResources().getString(R.string.invalid_location), Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            binding.tvOfferCardRating.setText(String.valueOf(offer.getCreator().getHostRating()));
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

        /**
         * Changes the offer summary card color depending on the relation between the user and the offer. The offer
         * summary card is displayed red if the offer is the users offer, blue if it is an offer in which the user
         * participates and black otherwise.
         *
         * @param offer the offer to be shown in this offer summary card
         */
        private void setColorOfOfferCard(Offer offer) {
            if (offerVM.isCreator(offer)) {
                binding.ivOfferCardPictureBackground
                        .setColorFilter(ContextCompat.getColor(binding.getRoot().getContext(), R.color.ownOffer),
                                PorterDuff.Mode.SRC_IN);
            } else if (offerVM.isParticipating(offer)) {
                binding.ivOfferCardPictureBackground.setColorFilter(
                        ContextCompat.getColor(binding.getRoot().getContext(), R.color.participatingOffer),
                        PorterDuff.Mode.SRC_IN);
            }
        }

        /**
         * Changes the bookmark state and change its display settings.
         *
         * @param offer the offer to be changed
         */
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
            } catch (RequestHandlerException exception) {
                Toast.makeText(binding.getRoot().getContext(), R.string.toast_error_message, Toast.LENGTH_LONG).show();
            }
        }

        /**
         * Navigates to the detailed offer page.
         *
         * @param offer the offer to be displayed in detail
         */
        private void navigateToOfferDetailed(Offer offer) {
            Bundle bundle = new Bundle();
            // Adds the offer to be displayed in detail to the arguments bundle
            bundle.putSerializable(OFFER.name(), offer);
            Navigation.findNavController(binding.getRoot()).navigate(R.id.offerDetailedFragment, bundle);
        }
    }
}