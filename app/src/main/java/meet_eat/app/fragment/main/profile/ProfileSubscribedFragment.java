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
import meet_eat.app.viewmodel.main.UserViewModel;
import meet_eat.data.entity.user.User;

public class ProfileSubscribedFragment extends Fragment {

    private FragmentProfileSubscribedBinding binding;
    private UserViewModel userVM;
    private ProfileSubscribedAdapter profileSubscribedAdapter;
    private NavController navController;

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

    private void displaySubscriberList() {
        profileSubscribedAdapter.updateSubscriptions(new ArrayList<>(userVM.getCurrentUser().getSubscriptions()));
    }

    private void setButtonOnClickListener() {
        binding.ibtBack.setOnClickListener(event -> navController.navigateUp());
    }

}
