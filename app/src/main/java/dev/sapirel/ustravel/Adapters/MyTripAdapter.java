package dev.sapirel.ustravel.Adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import dev.sapirel.ustravel.Models.Trip;
import dev.sapirel.ustravel.R;
import dev.sapirel.ustravel.ui.MyTripsFragmentDirections;

public class MyTripAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Fragment fragment;
    private ArrayList<Trip> myTrips = new ArrayList<>();

    public MyTripAdapter(Fragment fragment, ArrayList<Trip> myTrips) {
        this.fragment = fragment;
        this.myTrips = myTrips;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_my_trip_item, parent,false);

        return new MyTripAdapter.MyTripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyTripViewHolder myTripViewHolder = (MyTripViewHolder) holder;
        Trip myTrip = getMyTripItem(position);

        myTripViewHolder.setTrip(myTrip);

        myTripViewHolder.myTrip_TV_location.setText(myTrip.getTripLocation());


    }

    @Override
    public int getItemCount() {
        if (myTrips == null)
            return 0;
        else
            return myTrips.size();
    }

    private Trip getMyTripItem(int position){
        return  myTrips.get(position);
    }



    public class MyTripViewHolder extends RecyclerView.ViewHolder {

        private MaterialTextView myTrip_TV_location;
        private Trip myTrip;

        public MyTripViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavController navController =
                            Navigation.findNavController(fragment.requireActivity(),
                                    fragment.requireParentFragment().getId());

                    MyTripsFragmentDirections.ActionNavMyTripsToTripFragment action
                            = MyTripsFragmentDirections.actionNavMyTripsToTripFragment(myTrip);

                    navController.navigate((NavDirections) action);

                }
            });


            myTrip_TV_location = itemView.findViewById(R.id.myTrip_TV_location);
        }

        public MyTripViewHolder setTrip(Trip myTrip) {
            this.myTrip = myTrip;
            return this;
        }


    }

}

