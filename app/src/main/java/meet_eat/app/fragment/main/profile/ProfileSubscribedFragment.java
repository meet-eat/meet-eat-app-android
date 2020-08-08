package meet_eat.app.fragment.main.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import meet_eat.app.databinding.FragmentProfileSubscribedBinding;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.main.UserViewModel;
import meet_eat.data.entity.user.User;

/**
 * This is the subscriber list page. Here the user can see his subscriptions, click on their names for more
 * information, or remove them from his subscriber list.
 */
public class ProfileSubscribedFragment extends Fragment {

    private FragmentProfileSubscribedBinding binding;
    private UserViewModel userVM;
    private NavController navController;
    private ProfileSubscribedAdapter profileSubscribedAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileSubscribedBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        userVM = new ViewModelProvider(this).get(UserViewModel.class);
        profileSubscribedAdapter = new ProfileSubscribedAdapter(userVM, new ArrayList<User>());
        binding.rvProfileSubscriptions.setAdapter(profileSubscribedAdapter);
        binding.rvProfileSubscriptions
                .setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        navController = NavHostFragment.findNavController(this);
        displaySubscriberList();
        setButtonOnClickListener();
        return binding.getRoot();
    }

    /**
     * Updates the subscriber list by giving the adapter the {@link java.util.Set Set} of subscribers.
     */
    private void displaySubscriberList() {
        ArrayList<User> subscribedUsers = new ArrayList<>();
        try {
            subscribedUsers = new ArrayList<>(userVM.getSubscribedUsers());
        } catch (RequestHandlerException exception) {
            // TODO Toast if needed
        }
        profileSubscribedAdapter.updateSubscriptions(subscribedUsers);
    }

    /**
     * Sets the click listener for the back button
     */
    private void setButtonOnClickListener() {
        binding.ibtBack.setOnClickListener(event -> navController.navigateUp());
    }
}