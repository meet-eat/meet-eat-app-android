package meet_eat.app.fragment.main.offer;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import meet_eat.app.databinding.ItemOfferParticipantBinding;
import meet_eat.app.viewmodel.main.OfferViewModel;
import meet_eat.data.entity.user.User;

public class OfferParticipantsAdapter extends RecyclerView.Adapter<OfferParticipantsAdapter.ViewHolder> {

    private OfferViewModel offerVM;
    private ArrayList<User> currentParticipants;

    public OfferParticipantsAdapter(OfferViewModel offerVM, ArrayList<User> participants) {
        this.offerVM = offerVM;
        currentParticipants = participants;
    }

    public void updateParticipants(ArrayList<User> participants) {
        currentParticipants = participants;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemOfferParticipantBinding binding = ItemOfferParticipantBinding.inflate(inflater,
                parent, false);
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

    class ViewHolder extends RecyclerView.ViewHolder {

        private ItemOfferParticipantBinding binding;

        public ViewHolder(@NonNull ItemOfferParticipantBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(User user) {
        }
    }
}