package meet_eat.app.fragment.main.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import meet_eat.app.databinding.FragmentProfileReportBinding;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.main.UserViewModel;
import meet_eat.data.Report;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.user.User;

public class ProfileReportFragment extends Fragment {

    private FragmentProfileReportBinding binding;
    private UserViewModel userVM;
    private User user;
    private String reportMessage;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileReportBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        userVM = new ViewModelProvider(this).get(UserViewModel.class);
        navController = NavHostFragment.findNavController(this);

        if (getArguments() == null || getArguments().getSerializable("user") == null) {
            Toast.makeText(getActivity(), "DEBUG: User not given", Toast.LENGTH_SHORT).show();
            navController.navigateUp();
        } else {
            user = (User) getArguments().getSerializable("user");
        }

        setButtonOnClickListener();
        return binding.getRoot();
    }

    private void setButtonOnClickListener() {
        binding.btProfileReport.setOnClickListener(event -> reportUser());
    }

    private void reportUser() {
        Report report = new Report(userVM.getCurrentUser(), reportMessage != null ?
                reportMessage : "");

        try {
            userVM.report(user, report);
            navController.navigateUp();
            // TODO toast?
        } catch (RequestHandlerException e) {
            // TODO timeout etc
        }

    }

    public String getReportMessage() {
        return reportMessage;
    }

    public void setReportMessage(String reportMessage) {
        this.reportMessage = reportMessage;
    }
}