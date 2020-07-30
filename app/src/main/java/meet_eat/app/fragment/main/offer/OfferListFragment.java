package meet_eat.app.fragment.main.offer;

import android.app.ActionBar;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import meet_eat.app.R;
import meet_eat.app.databinding.FragmentOfferListBinding;
import meet_eat.app.viewmodel.main.OfferViewModel;

public class OfferListFragment extends Fragment {
    private FragmentOfferListBinding binding;
    private OfferViewModel offerVM;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentOfferListBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        offerVM = new ViewModelProvider(this).get(OfferViewModel.class);
        setButtonOnClickListener();
        return binding.getRoot();
    }

    private void setButtonOnClickListener() {
        binding.ibtCreate.setOnClickListener(event -> addOffer());
    }

    private void addOffer() {
        binding.llLinearLayout.addView(createNewOfferCard());
    }

    private View createNewOfferCard() {
        ConstraintLayout clOfferLayout = new ConstraintLayout(getActivity());
        ViewGroup.MarginLayoutParams clOfferLayoutParams =
                new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT,
                (int) getResources().getDimension(R.dimen.offer_card_height));
        clOfferLayoutParams.setMargins((int) getResources().getDimension(R.dimen.std_margin),
                (int) getResources().getDimension(R.dimen.std_margin),
                (int) getResources().getDimension(R.dimen.std_margin),
                (int) getResources().getDimension(R.dimen.std_margin));
        clOfferLayout.setLayoutParams(clOfferLayoutParams);
        clOfferLayout.setElevation(10);

        ImageView ivOfferPicture = new ImageView(getActivity());
        ViewGroup.MarginLayoutParams ivOfferPictureParams =
                new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT,
                (int) getResources().getDimension(R.dimen.offer_card_height));
        ivOfferPicture.setLayoutParams(ivOfferPictureParams);
        ivOfferPicture.setAdjustViewBounds(true);
        ivOfferPicture.setScaleType(ImageView.ScaleType.CENTER);
        ivOfferPicture.setImageResource(R.drawable.bg_button);
        ivOfferPicture.setColorFilter(getContext().getResources().getColor(R.color.black));
        /*
        <ImageView
                        android:id="@+id/ivOfferPicture"
                        android:layout_width="match_parent"
                        android:layout_height="300dp"
                        android:adjustViewBounds="true"
                        android:scaleType="fitCenter"
                        android:src="@android:color/black"
                        app:layout_constraintStart_toStartOf="parent"
                        app:layout_constraintTop_toTopOf="parent" />
         */
        clOfferLayout.addView(ivOfferPicture);
        return clOfferLayout;
    }
}