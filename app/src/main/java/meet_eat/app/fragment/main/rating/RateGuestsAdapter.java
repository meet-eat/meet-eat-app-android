package meet_eat.app.fragment.main.rating;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import meet_eat.app.databinding.ItemRateGuestBinding;
import meet_eat.app.viewmodel.main.RatingViewModel;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.relation.rating.Rating;
import meet_eat.data.entity.relation.rating.RatingValue;
import meet_eat.data.entity.user.User;

/**
 * Contains the various offer guests displayed in the guest rating page.
 */
public class RateGuestsAdapter extends RecyclerView.Adapter<RateGuestsAdapter.ViewHolder> {

    private static final float RATING_STEP_SIZE = 1;
    private static final int DEFAULT_NUM_STARS = 3;

    private final Offer offer;
    private final RatingViewModel ratingVM;
    private List<User> currentGuests;

    /**
     * Initializing fields.
     *
     * @param ratingVM the rating view model
     * @param guests   the guests to the offer
     */
    public RateGuestsAdapter(RatingViewModel ratingVM, ArrayList<User> guests, Offer offer) {
        this.ratingVM = ratingVM;
        currentGuests = guests;
        this.offer = offer;
    }

    /**
     * Updates the guest list.
     *
     * @param participants the participants which are rated
     */
    public void updateGuests(Collection<User> participants) {
        currentGuests = new ArrayList<>(participants);
        notifyDataSetChanged();
    }

    /**
     * Tries to send the guest ratings.
     */
    public void sendRatings() {
        // create ratings, then: ratingVM.send(ratings);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemRateGuestBinding binding = ItemRateGuestBinding.inflate(inflater, parent, false);
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

    /**
     * Holds the individual guests.
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        private ItemRateGuestBinding binding;

        /**
         * Initializes the binding.
         *
         * @param binding the binding
         */
        public ViewHolder(@NonNull ItemRateGuestBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        /**
         * Initializes the GUI for a guest and sets various click listeners.
         *
         * @param user the guest
         */
        public void setData(User user) {
            binding.tvRateGuestUsername.setText(user.getName());
            binding.rbRateGuest.setNumStars(DEFAULT_NUM_STARS);
            binding.rbRateGuest.setStepSize(RATING_STEP_SIZE);
        }

        /**
         * Gets the guest rating from the GUI by calling the createRating() method.
         *
         * @return a rating to a guest
         */
        public Rating getRatingFromUI() {
            return createRating((int) binding.rbRateGuest.getRating());
        }

        /**
         * Creates the guest rating from the rating amount.
         *
         * @param ratingAmount number of stars the user selected
         * @return a new guest rating
         */
        private Rating createRating(int ratingAmount) {
            return Rating.createGuestRating(ratingVM.getCurrentUser(), offer,
                    RatingValue.getRatingValueByInteger(ratingAmount));
        }
    }
}