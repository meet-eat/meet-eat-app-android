package meet_eat.app.fragment.main.offer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import meet_eat.app.R;
import meet_eat.app.databinding.ItemOfferParticipantBinding;
import meet_eat.app.viewmodel.main.OfferViewModel;
import meet_eat.data.entity.user.User;

import static meet_eat.app.fragment.NavigationArgumentKey.USER;

/**
 * Contains the various participants displayed in the participants page.
 */
public class OfferParticipantsAdapter extends RecyclerView.Adapter<OfferParticipantsAdapter.ViewHolder> {

    private ArrayList<User> currentParticipants;

    /**
     * Initialize fields.
     *
     * @param offerVM      the offer view model
     * @param participants the participants list
     */
    public OfferParticipantsAdapter(OfferViewModel offerVM, ArrayList<User> participants) {
        currentParticipants = participants;
    }

    /**
     * Updates the participants list.
     *
     * @param participants the updated participants list
     */
    public void updateParticipants(ArrayList<User> participants) {
        currentParticipants = participants;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemOfferParticipantBinding binding = ItemOfferParticipantBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(currentParticipants.get(position));
    }

    @Override
    public int getItemCount() {
        return currentParticipants.size();
    }

    /**
     * Holds the individual participants.
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        private ItemOfferParticipantBinding binding;

        /**
         * Initializes the binding.
         *
         * @param binding the binding
         */
        public ViewHolder(@NonNull ItemOfferParticipantBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        /**
         * Initializes the GUI for an participant and set its click listeners.
         *
         * @param user the participant
         */
        public void setData(User user) {
            binding.tvOfferParticipantUsername.setText(user.getName());
            binding.ivOfferParticipantProfile.setOnClickListener(event -> navigateToProfile(user));
            binding.tvOfferParticipantUsername.setOnClickListener(event -> navigateToProfile(user));
        }

        /**
         * Navigates to the profile of the participant.
         *
         * @param user the participant
         */
        private void navigateToProfile(User user) {
            Bundle bundle = new Bundle();
            // Adds the participant to the arguments bundle
            bundle.putSerializable(USER.name(), user);
            Navigation.findNavController(binding.getRoot()).navigate(R.id.profileFragment, bundle);
        }
    }
}