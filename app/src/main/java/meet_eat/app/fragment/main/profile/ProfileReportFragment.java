package meet_eat.app.fragment.main.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import meet_eat.app.R;
import meet_eat.app.databinding.FragmentProfileReportBinding;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.main.UserViewModel;
import meet_eat.data.Report;
import meet_eat.data.entity.user.User;

public class ProfileReportFragment extends Fragment {

    private FragmentProfileReportBinding binding;
    private UserViewModel userVM;
    private User user;
    private String reportMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileReportBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        userVM = new ViewModelProvider(this).get(UserViewModel.class);

        if (getArguments() == null)
            Navigation.findNavController(binding.getRoot()).popBackStack();
        user = (User) getArguments().get("user");

        setButtonOnClickListener();

        return binding.getRoot();
    }

    private void setButtonOnClickListener() {
        binding.btProfileReport.setOnClickListener(event -> reportUser());
    }

    private void reportUser() {

        Report report = new Report(userVM.getCurrentUser(), reportMessage != null ? reportMessage : "");

        try {
            userVM.report(user, report);
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
