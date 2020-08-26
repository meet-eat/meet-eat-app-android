package meet_eat.app.fragment.main.offer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import meet_eat.app.MainActivity;
import meet_eat.app.databinding.FragmentOfferContactRequestBinding;
import meet_eat.app.fragment.MenuSection;

/**
 * This is the contact request page. Here the user can accept a contact request and choose which information he wants
 * to share with the requester.
 */
public class OfferContactRequestFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((MainActivity) requireActivity()).selectMenuItem(MenuSection.MAIN_OFFERS.ordinal());
        meet_eat.app.databinding.FragmentOfferContactRequestBinding binding =
                FragmentOfferContactRequestBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        return binding.getRoot();
    }
}