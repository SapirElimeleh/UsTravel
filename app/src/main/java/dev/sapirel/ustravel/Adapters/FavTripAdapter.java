package dev.sapirel.ustravel.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import dev.sapirel.ustravel.Models.Trip;
import dev.sapirel.ustravel.R;
import dev.sapirel.ustravel.Utils.DataManager;
import dev.sapirel.ustravel.ui.MyFavoritesFragmentDirections;

public class FavTripAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Fragment fragment;
    private ArrayList<Trip> trips = new ArrayList<>();
    private TripItemClickListener tripItemClickListener;

    public FavTripAdapter(Fragment fragment, ArrayList<Trip> trips) {
        this.fragment = fragment;
        this.trips = trips;
    }

    public FavTripAdapter setTripItemClickListener(TripItemClickListener tripItemClickListener) {
        this.tripItemClickListener = tripItemClickListener;
        return this;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_trip_item, parent, false);


        return new FavTripAdapter.FavTripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        FavTripViewHolder favTripViewHolder = (FavTripAdapter.FavTripViewHolder) holder;

        Trip trip = getTripItem(position);

        favTripViewHolder.setTrip(trip);

        favTripViewHolder.trip_TV_userName.setText(trip.getUserName());
        favTripViewHolder.trip_TV_location.setText(trip.getTripLocation());
        String likes = String.valueOf(trip.getNumLikes());
        favTripViewHolder.trip_TV_numLikes.setText(likes);


        if(trip.isLiked())
            favTripViewHolder.trip_IMG_like.setImageResource(R.drawable.ic_baseline_favorite_24);
        else
            favTripViewHolder.trip_IMG_like.setImageResource(R.drawable.ic_baseline_favorite_border_24);
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


    public class FavTripViewHolder extends RecyclerView.ViewHolder {

        private MaterialTextView trip_TV_location;
        private MaterialTextView trip_TV_userName;
        private AppCompatImageView trip_IMG_image;
        private AppCompatImageView trip_IMG_like;
        private TextView trip_TV_numLikes;
        private Trip trip;

        public FavTripViewHolder(@NonNull View itemView) {
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

                            MyFavoritesFragmentDirections.ActionNavMyFavToTripFragment action
                                    = MyFavoritesFragmentDirections.actionNavMyFavToTripFragment(trip);

                            navController.navigate(action);
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

        public FavTripViewHolder setTrip(Trip trip) {
            this.trip = trip;
            return this;
        }
    }
    }

