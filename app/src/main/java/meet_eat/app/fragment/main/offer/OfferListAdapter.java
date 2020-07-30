package meet_eat.app.fragment.main.offer;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import meet_eat.app.databinding.ItemOfferCardBinding;
import meet_eat.data.entity.Offer;

public class OfferListAdapter extends RecyclerView.Adapter<OfferListAdapter.ViewHolder> {

    private ArrayList<Offer> currentOffers;

    public OfferListAdapter(ArrayList<Offer> offers) {
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
            binding.tvOfferTitle.setText(offer.getName());
        }
    }
}