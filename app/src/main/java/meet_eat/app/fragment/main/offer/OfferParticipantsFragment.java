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
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import meet_eat.app.databinding.FragmentOfferParticipantsBinding;
import meet_eat.app.viewmodel.main.OfferViewModel;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.user.User;

public class OfferParticipantsFragment extends Fragment {

    private FragmentOfferParticipantsBinding binding;
    private OfferViewModel offerVM;
    private OfferParticipantsAdapter offerParticipantsAdapter;
    private NavController navController;
    private Offer offer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentOfferParticipantsBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        offerVM = new ViewModelProvider(this).get(OfferViewModel.class);
        binding.rvOfferParticipants.setAdapter(offerParticipantsAdapter);
        binding.rvOfferParticipants.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        offerParticipantsAdapter = new OfferParticipantsAdapter(offerVM, new ArrayList<User>());
        navController = NavHostFragment.findNavController(this);

        if (getArguments() == null || getArguments().getSerializable("offer") == null) {
            Toast.makeText(getActivity(), "DEBUG: Offer not given", Toast.LENGTH_SHORT).show();
            navController.navigateUp();
        } else {
            offer = (Offer) getArguments().getSerializable("offer");
        }

        setButtonOnClickListener();
        return binding.getRoot();
    }

    private void setButtonOnClickListener() {
    }
}