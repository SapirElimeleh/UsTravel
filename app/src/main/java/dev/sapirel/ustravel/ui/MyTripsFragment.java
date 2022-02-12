package dev.sapirel.ustravel.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;

import dev.sapirel.ustravel.Adapters.MyTripAdapter;
import dev.sapirel.ustravel.Models.Trip;
import dev.sapirel.ustravel.R;
import dev.sapirel.ustravel.Utils.DataManager;

public class MyTripsFragment extends Fragment {

    private RecyclerView myTrips_RV_myTrips;
    private ImageButton myTrips_BTN_add;
    private ArrayList<Trip> temp;

    public MyTripsFragment() {
        // Required empty public constructor
    }

    public static MyTripsFragment newInstance(String param1, String param2) {
        MyTripsFragment fragment = new MyTripsFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_trips, container, false);
        temp = DataManager.getMyTrips();

        findView(view);
        initView(this);



        MyTripAdapter myTripAdapter = new MyTripAdapter(this, temp);
        Log.d("ptt", temp.toString());

        myTrips_RV_myTrips.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL,false));
        myTrips_RV_myTrips.setHasFixedSize(true);
        myTrips_RV_myTrips.setItemAnimator(new DefaultItemAnimator());
        myTrips_RV_myTrips.setAdapter(myTripAdapter);

        return view;
    }

    private void initView(Fragment fragment) {

        myTrips_BTN_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavController navController = Navigation.findNavController(fragment.requireActivity(), fragment.requireParentFragment().getId());

                navController.navigate(R.id.action_nav_my_trips_to_addTripFragment);


            }
        });

    }

    private void findView(View view) {
        myTrips_BTN_add  = view.findViewById(R.id.myTrips_BTN_add);
        myTrips_RV_myTrips = view.findViewById(R.id.myTrips_RV_myTrips);
    }
}