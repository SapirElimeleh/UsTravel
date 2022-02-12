package dev.sapirel.ustravel.ui;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;

import dev.sapirel.ustravel.Models.Day;
import dev.sapirel.ustravel.Models.Trip;
import dev.sapirel.ustravel.R;
import dev.sapirel.ustravel.Utils.DataManager;


public class DayFragment extends Fragment {

    private AppCompatImageButton day_BTN_delete;
    private MaterialTextView     day_TV_name;
    private MaterialTextView     day_TV_date;
    private MaterialTextView     day_TV_location;
    private AppCompatImageView   day_IV_img;
    private Day day;
    private Trip trip;


    public DayFragment() {
        // Required empty public constructor
    }

    public static DayFragment newInstance(String param1, String param2) {
        DayFragment fragment = new DayFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        trip = new Trip();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_day, container, false);


        if(getArguments() != null) {
            DayFragmentArgs dayFragmentArgs = DayFragmentArgs.fromBundle(getArguments());
            this.day = dayFragmentArgs.getDay();

        }

        trip = DataManager.getInstance().getTripByIDFromTrips(day.getTripId());

        if(trip == null)
            trip = DataManager.getInstance().getTripByID(day.getTripId());


        findView(view);

            if (!trip.getUserId().equals(DataManager.getInstance().getCurrent_user().getUid()))
                day_BTN_delete.setVisibility(View.INVISIBLE);

        initView(this);



        setDayName(day.getDayName());
        setDayDate(day.getDate());
        setDayLocation(day.getTripLocation());

        //Image
        Glide.with(this).load(day.getImage()).into(day_IV_img);

        return view;
    }

    private void initView(Fragment fragment) {

        day_BTN_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               DataManager.getInstance().deleteDayFromDataBase(day.getDayId(), fragment);

                NavController navController =
                        Navigation.findNavController(fragment.requireActivity(),
                                fragment.requireParentFragment().getId());


                DayFragmentDirections.ActionDayFragmentToTripFragment action
                        = DayFragmentDirections.actionDayFragmentToTripFragment
                        (DataManager.getInstance().getTripByID(day.getTripId()));

                navController.navigate((NavDirections) action);
            }
        });
    }

    private void findView(View view) {

        day_BTN_delete = view.findViewById(R.id.day_BTN_delete);
        day_TV_name = view.findViewById(R.id.day_TV_name);
        day_TV_date = view.findViewById(R.id.day_TV_date);
        day_TV_location = view.findViewById(R.id.day_TV_location);
        day_IV_img = view.findViewById(R.id.day_IV_img);

    }

    public void setDayName(String dayName) {
        this.day_TV_name.setText(dayName);
    }

    public void setDayDate(String dayDate) {
        this.day_TV_date.setText(dayDate);
    }

    public void setDayLocation(String dayLocation) {
        this.day_TV_location.setText(dayLocation);
    }

    public String getDayName() {
        return day_TV_name.getEditableText().toString();
    }

    public String getTripDate() {
        return day_TV_date.getEditableText().toString();
    }

    public String getDayLocation() {
        return day_TV_location.getEditableText().toString();
    }

}