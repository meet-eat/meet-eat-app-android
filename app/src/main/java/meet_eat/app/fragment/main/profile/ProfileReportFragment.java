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
import androidx.navigation.fragment.NavHostFragment;

import com.google.common.base.Strings;

import java.util.Objects;

import meet_eat.app.MainActivity;
import meet_eat.app.R;
import meet_eat.app.databinding.FragmentProfileReportBinding;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.main.UserViewModel;
import meet_eat.data.entity.relation.Report;
import meet_eat.data.entity.user.User;

import static meet_eat.app.fragment.NavigationArgumentKey.USER;

/**
 * This is the profile report page. Here the user can send a report to the previously displayed user.
 */
public class ProfileReportFragment extends Fragment {

    private FragmentProfileReportBinding binding;
    private NavController navController;
    private UserViewModel userVM;
    private User user;
    private String reportMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).selectMenuItem(0);
        binding = FragmentProfileReportBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        userVM = new ViewModelProvider(this).get(UserViewModel.class);
        navController = NavHostFragment.findNavController(this);

        if (Objects.isNull(getArguments()) || Objects.isNull(getArguments().getSerializable(USER.name()))) {
            Toast.makeText(getActivity(), R.string.toast_error_message, Toast.LENGTH_SHORT).show();
            navController.navigateUp();
        } else {
            user = (User) getArguments().getSerializable(USER.name());
        }

        initUI();
        setButtonOnClickListener();
        return binding.getRoot();
    }

    /**
     * Sets various click listeners.
     */
    private void setButtonOnClickListener() {
        binding.btProfileReport.setOnClickListener(event -> reportUser());
    }

    private void initUI() {
        binding.tvProfileReportInfo.setText(getResources().getString(R.string.report_info_user) + " " + user.getName());
    }

    private void reportUser() {
        Report report = new Report(userVM.getCurrentUser(), user, Strings.nullToEmpty(reportMessage));

        try {
            userVM.report(report);
            navController.navigateUp();
            Toast.makeText(getActivity(), user.getName() + " " + getResources().getString(R.string.reported_toast_text),
                    Toast.LENGTH_SHORT).show();
        } catch (RequestHandlerException exception) {
            Toast.makeText(getActivity(), R.string.toast_error_message, Toast.LENGTH_LONG).show();
        }
    }

    public String getReportMessage() {
        return reportMessage;
    }

    public void setReportMessage(String reportMessage) {
        this.reportMessage = reportMessage;
    }
}