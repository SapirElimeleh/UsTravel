package dev.sapirel.ustravel.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import dev.sapirel.ustravel.Adapters.DayAdapter;
import dev.sapirel.ustravel.Models.Day;
import dev.sapirel.ustravel.Models.Trip;
import dev.sapirel.ustravel.R;
import dev.sapirel.ustravel.Utils.DataManager;

public class TripFragment extends Fragment {

    private MaterialTextView trip_TV_name;
    private MaterialTextView trip_TV_userName;
    private ImageButton trip_BTN_addDay;
    private ImageButton trip_BTN_deleteTrip;
    private RecyclerView trip_RV_rv;
    private TextView trip_TV_time;
    private Trip trip;
    private ArrayList<Day> days;


    public TripFragment() {
        // Required empty public constructor
    }

    public static TripFragment newInstance(String param1, String param2) {
        TripFragment fragment = new TripFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trip, container, false);

        if(getArguments() != null) {
            TripFragmentArgs tripFragmentArgs = TripFragmentArgs.fromBundle(getArguments());
            this.trip = tripFragmentArgs.getTrip();
        }
        days = DataManager.getInstance().getDays(trip.getId());

        DayAdapter dayAdapter = new DayAdapter(this, days);

        findView(view);
        initView(this);



        trip_TV_name.setText(trip.getTripLocation());
        trip_TV_userName.setText(trip.getUserName());
        String time = (trip.getStartDate() + " - " + trip.getEndDate());
        Log.d("Date", time);
        trip_TV_time.setText(time);

        if(!trip.isMyTripVal())
            trip_BTN_addDay.setVisibility(View.INVISIBLE);





        trip_RV_rv.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        trip_RV_rv.setHasFixedSize(true);
        trip_RV_rv.setItemAnimator(new DefaultItemAnimator());
        trip_RV_rv.setAdapter(dayAdapter);

        return view;
    }

    private void initView(Fragment fragment) {

        trip_BTN_addDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavController navController =
                        Navigation.findNavController(fragment.requireActivity(),
                                fragment.requireParentFragment().getId());


                TripFragmentDirections.ActionTripFragmentToAddDayFragment action
                        = TripFragmentDirections.actionTripFragmentToAddDayFragment(trip);

                navController.navigate((NavDirections) action);

            }
        });

        trip_BTN_deleteTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager.getInstance().deleteTripFromDataBase(trip.getId(), fragment);

                NavController navController =
                        Navigation.findNavController(fragment.requireActivity(),
                                fragment.requireParentFragment().getId());

                navController.navigate((NavDirections) TripFragmentDirections.actionTripFragmentToNavMyTrips2());
            }
        });
    }

    private void findView(View view) {

        trip_TV_name = view.findViewById(R.id.trip_TV_name);
        trip_RV_rv = view.findViewById(R.id.trip_RV_rv);
        trip_BTN_addDay = view.findViewById(R.id.trip_BTN_addDay);
        trip_TV_userName = view.findViewById(R.id.trip_TV_userName);
        trip_BTN_deleteTrip = view.findViewById(R.id.trip_BTN_deleteTrip);
        trip_TV_time = view.findViewById(R.id.trip_TV_time);

    }






}