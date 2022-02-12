package dev.sapirel.ustravel.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import java.util.ArrayList;

import dev.sapirel.ustravel.Models.Trip;
import dev.sapirel.ustravel.R;

import dev.sapirel.ustravel.Utils.DataManager;
import dev.sapirel.ustravel.ui.MyTripsFragmentDirections;

public class TripAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Fragment fragment;
    private ArrayList<Trip> trips = new ArrayList<>();
    private TripItemClickListener tripItemClickListener;


    public TripAdapter(Fragment fragment, ArrayList<Trip> trips) {
        this.fragment = fragment;
        this.trips = trips;
    }

    public TripAdapter setTripItemClickListener(TripItemClickListener tripItemClickListener) {
        this.tripItemClickListener = tripItemClickListener;
        return this;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_trip_item, parent,false);


        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TripViewHolder tripViewHolder = (TripViewHolder) holder;
        Trip trip = getTripItem(position);

        tripViewHolder.setTrip(trip);


        tripViewHolder.trip_TV_userName.setText(trip.getUserName());
        tripViewHolder.trip_TV_location.setText(trip.getTripLocation());
        String likes = String.valueOf(trip.getNumLikes());
        tripViewHolder.trip_TV_numLikes.setText(likes);

        if (DataManager.getInstance().getCurrent_user().getPhotoUrl() != null) {

            Glide.with(fragment)
                    .load(DataManager.getInstance().getCurrent_user().getPhotoUrl())
                    .into(tripViewHolder.trip_IMG_image);
        }


        if(trip.isLiked()) {
            tripViewHolder.trip_IMG_like.setImageResource(R.drawable.ic_baseline_favorite_24);
        }
        else
            tripViewHolder.trip_IMG_like.setImageResource(R.drawable.ic_baseline_favorite_border_24);
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    private Trip getTripItem(int position) {
        return trips.get(position);
    }


    public interface TripItemClickListener{
       // void tripItemClick(Trip trip, int position);
        void likeClicked(Trip trip, int position);
    }

    public class TripViewHolder extends RecyclerView.ViewHolder {

       private MaterialTextView trip_TV_location;
       private MaterialTextView trip_TV_userName;
       private ShapeableImageView trip_IMG_image;
       private AppCompatImageView trip_IMG_like;
       private TextView trip_TV_numLikes;
       private Trip trip;



        public TripViewHolder(@NonNull View itemView) {
            super(itemView);

            trip_TV_location = itemView.findViewById(R.id.trip_TV_location);
            trip_TV_userName = itemView.findViewById(R.id.trip_TV_userName);
            trip_IMG_image = itemView.findViewById(R.id.trip_IMG_image);
            trip_IMG_like = itemView.findViewById(R.id.trip_IMG_like);
            trip_TV_numLikes = itemView.findViewById(R.id.trip_TV_numLikes);


            itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NavController navController =
                                    Navigation.findNavController(fragment.requireActivity(),
                                            fragment.requireParentFragment().getId());

                            MyTripsFragmentDirections.ActionNavMyTripsToTripFragment action
                                    = MyTripsFragmentDirections.actionNavMyTripsToTripFragment(trip);

                            navController.navigate((NavDirections) action);
                        }

            });

            trip_IMG_like.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(trip.isLiked()) {
                                trip.setNumLikes(trip.getNumLikes() - 1);
                                trip.setLiked(false);
                            }
                            else {
                                trip.setNumLikes(trip.getNumLikes() + 1);
                                trip.setLiked(true);
                            }
                            tripItemClickListener.likeClicked(getTripItem(getAdapterPosition()), getAdapterPosition());
                        }

                    });
        }

        public TripViewHolder setTrip(Trip trip) {
            this.trip = trip;
            return this;
        }
    }


}
