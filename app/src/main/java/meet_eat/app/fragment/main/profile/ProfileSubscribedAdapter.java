package meet_eat.app.fragment.main.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import meet_eat.app.R;
import meet_eat.app.databinding.ItemProfileSubscriptionBinding;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.main.UserViewModel;
import meet_eat.data.entity.user.User;

import static meet_eat.app.fragment.NavigationArgumentKey.USER;

public class ProfileSubscribedAdapter extends RecyclerView.Adapter<ProfileSubscribedAdapter.ViewHolder> {

    private UserViewModel userVM;
    private ArrayList<User> currentSubscriptions;

    public ProfileSubscribedAdapter(UserViewModel userVM, ArrayList<User> subscriptions) {
        this.userVM = userVM;
        currentSubscriptions = subscriptions;
    }

    public void updateSubscriptions(ArrayList<User> subscriptions) {
        currentSubscriptions = subscriptions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemProfileSubscriptionBinding binding = ItemProfileSubscriptionBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(currentSubscriptions.get(position));
    }

    @Override
    public int getItemCount() {
        return currentSubscriptions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ItemProfileSubscriptionBinding binding;

        public ViewHolder(@NonNull ItemProfileSubscriptionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(User user) {
            binding.tvOfferSubscriptionUsername.setText(user.getName());
            binding.tvOfferSubscriptionUsername.setOnClickListener(event -> navigateToProfile(user));
            binding.ibtOfferSubscriptionRemove.setOnClickListener(event -> unsubscribe(user));
            binding.ivOfferSubscriptionProfile.setOnClickListener(event -> navigateToProfile(user));
        }

        private void navigateToProfile(User user) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(USER.name(), user);
            Navigation.findNavController(binding.getRoot()).navigate(R.id.profileFragment, bundle);
        }

        private void unsubscribe(User user) {
            int position = currentSubscriptions.indexOf(user);
            currentSubscriptions.remove(user);
            notifyItemRemoved(position);

            try {
                userVM.unsubscribe(user);
            } catch (RequestHandlerException e) {
                // TODO resolve error code
                Toast.makeText(binding.getRoot().getContext(),
                        "DEBUG ProfileSubscribedAdapter.java -> unsubscribe(): " + e.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }

        }
    }
}