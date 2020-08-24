package meet_eat.app.fragment.main.offer;

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
import meet_eat.app.databinding.FragmentOfferReportBinding;
import meet_eat.app.repository.RequestHandlerException;
import meet_eat.app.viewmodel.main.OfferViewModel;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.relation.Report;

import static meet_eat.app.fragment.NavigationArgumentKey.OFFER;

/**
 * This is the offer report page. Here the user can send a report of the previously displayed offer.
 */
public class OfferReportFragment extends Fragment {

    private FragmentOfferReportBinding binding;
    private NavController navController;
    private OfferViewModel offerVM;
    private Offer offer;
    private String reportMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).selectMenuItem(1);
        binding = FragmentOfferReportBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        offerVM = new ViewModelProvider(this).get(OfferViewModel.class);
        navController = NavHostFragment.findNavController(this);

        if (Objects.isNull(getArguments()) || Objects.isNull(getArguments().getSerializable(OFFER.name()))) {
            Toast.makeText(getActivity(), R.string.toast_error_message, Toast.LENGTH_SHORT).show();
            navController.navigateUp();
        } else {
            offer = (Offer) getArguments().getSerializable(OFFER.name());
        }

        initUI();
        setButtonOnClickListener();
        return binding.getRoot();
    }

    private void setButtonOnClickListener() {
        binding.btOfferReport.setOnClickListener(event -> reportOffer());
    }

    private void initUI() {
        binding.tvOfferReportInfo.setText(getResources().getString(R.string.report_info_offer) + " " + offer.getName());
    }

    private void reportOffer() {
        Report report = new Report(offerVM.getCurrentUser(), offer, Strings.nullToEmpty(reportMessage));

        try {
            offerVM.report(report);
            navController.navigateUp();
            Toast.makeText(getActivity(),
                    offer.getName() + " " + getResources().getString(R.string.reported_toast_text), Toast.LENGTH_SHORT)
                    .show();
        } catch (RequestHandlerException e) {
            Toast.makeText(getActivity(), R.string.toast_error_message, Toast.LENGTH_SHORT).show();
        }
    }

    public String getReportMessage() {
        return reportMessage;
    }

    public void setReportMessage(String reportMessage) {
        this.reportMessage = reportMessage;
    }
}