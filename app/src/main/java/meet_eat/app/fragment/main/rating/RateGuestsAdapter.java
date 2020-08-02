package meet_eat.app.fragment.main.rating;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;

import meet_eat.app.databinding.ItemRateGuestBinding;
import meet_eat.app.viewmodel.main.RatingViewModel;
import meet_eat.data.entity.user.User;

public class RateGuestsAdapter extends RecyclerView.Adapter<RateGuestsAdapter.ViewHolder> {

    private RatingViewModel ratingVM;
    private ArrayList<User> currentGuests;

    public RateGuestsAdapter(RatingViewModel ratingVM, ArrayList<User> guests) {
        this.ratingVM = ratingVM;
        currentGuests = guests;
    }

    public void updateGuests(Collection<User> participants) {
        currentGuests = new ArrayList<>(participants);
        notifyDataSetChanged();
    }

    public void sendRatings() {
        // TODO create ratings, then: ratingVM.send(ratings);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemRateGuestBinding binding = ItemRateGuestBinding.inflate(inflater,
                parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(currentGuests.get(position));
    }

    @Override
    public int getItemCount() {
        return currentGuests.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ItemRateGuestBinding binding;

        public ViewHolder(@NonNull ItemRateGuestBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(User user) {
        }
    }
}