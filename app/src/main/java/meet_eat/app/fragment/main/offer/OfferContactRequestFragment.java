package meet_eat.app.fragment.main.offer;

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

import meet_eat.app.MainActivity;
import meet_eat.app.databinding.FragmentOfferContactRequestBinding;
import meet_eat.app.viewmodel.main.OfferViewModel;

/**
 * This is the contact request page. Here the user can accept a contact request and choose which information he wants
 * to share with the requester.
 */
public class OfferContactRequestFragment extends Fragment {

    private FragmentOfferContactRequestBinding binding;
    private OfferViewModel offerVM;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).selectMenuItem(1);
        binding = FragmentOfferContactRequestBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        offerVM = new ViewModelProvider(this).get(OfferViewModel.class);
        navController = NavHostFragment.findNavController(this);
        return binding.getRoot();
    }
}