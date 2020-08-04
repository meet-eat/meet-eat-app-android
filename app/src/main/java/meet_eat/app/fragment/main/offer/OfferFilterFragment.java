package meet_eat.app.fragment.main.offer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import meet_eat.app.R;
import meet_eat.app.databinding.FragmentOfferFilterBinding;

public class OfferFilterFragment extends Fragment {

    private FragmentOfferFilterBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentOfferFilterBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        initializeSortSpinner();
        return binding.getRoot();
    }

    /* add sorting criteria to view */
    private void initializeSortSpinner() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(getString(R.string.sort_spinner_item_time));
        arrayList.add(getString(R.string.sort_spinner_item_price));
        arrayList.add(getString(R.string.sort_spinner_item_distance));
        arrayList.add(getString(R.string.sort_spinner_item_amount_participants));
        arrayList.add(getString(R.string.sort_spinner_item_host_rating));

        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(binding.getRoot().getContext(), android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.sOfferFilterSort.setAdapter(arrayAdapter);
    }
}