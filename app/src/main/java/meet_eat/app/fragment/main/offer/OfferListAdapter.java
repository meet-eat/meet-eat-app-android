package meet_eat.app.fragment.main.offer;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import meet_eat.app.R;
import meet_eat.app.databinding.ItemOfferCardBinding;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.main.OfferViewModel;
import meet_eat.data.entity.Offer;

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

        public ViewHolder(@NonNull ItemOfferCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(Offer offer) {
            binding.tvOfferCardTitle.setText(offer.getName());
            binding.tvOfferCardDescription.setText(offer.getDescription());
            binding.tvOfferCardDate.setText(offer.getDateTime().toString());
            binding.tvPrice.setText(String.valueOf(offer.getPrice()) + binding.getRoot()
                    .getResources().getString(R.string.currency));
            // TODO binding.tvDistance.setText(Haversine.applyHaversineFormula(...));
            binding.tvOfferCardRating.setText(String.valueOf(offer.getCreator().getHostRating()));
            // TODO binding.ivOfferPicture.setImageResource(...);
            binding.ivOfferCardPicture.setOnClickListener(event -> {
                Navigation.findNavController(binding.getRoot()).navigate(OfferListFragmentDirections
                        .actionOfferListFragmentToOfferDetailedFragment());
            });

            if (offerVM.isBookmarked(offer))
                binding.ibtOfferCardBookmark.setColorFilter(ContextCompat.getColor(binding.getRoot().getContext(), R.color.bookmarked));

            binding.ibtOfferCardBookmark.setOnClickListener(event -> {
                try {
                    if (offerVM.isBookmarked(offer)) {
                        offerVM.removeBookmark(offer);
                        binding.ibtOfferCardBookmark.setColorFilter(ContextCompat.getColor(binding.getRoot().getContext(), R.color.symbol));
                    } else {
                        offerVM.addBookmark(offer);
                        binding.ibtOfferCardBookmark.setColorFilter(ContextCompat.getColor(binding.getRoot().getContext(), R.color.bookmarked));
                    }
                } catch (RequestHandlerException e) {
                    Toast.makeText(binding.getRoot().getContext(), e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}