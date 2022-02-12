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

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import dev.sapirel.ustravel.Models.Day;
import dev.sapirel.ustravel.R;
import dev.sapirel.ustravel.ui.TripFragmentDirections;

public class DayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private Fragment fragment;
    private ArrayList<Day> days = new ArrayList<>();

    public DayAdapter(Fragment fragment, ArrayList<Day> days) {
        this.fragment = fragment;
        this.days = days;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_day_item, parent,false);

        return new DayAdapter.DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DayViewHolder dayViewHolder = (DayViewHolder) holder;
        Day day = getDayItem(position);

        dayViewHolder.setDay(day);

        dayViewHolder.day_TV_name.setText(day.getDayName());
        dayViewHolder.day_TV_location.setText(day.getTripLocation());

        Glide.with(fragment)
                .load(day.getImage())
                .into(dayViewHolder.day_IMG_img);


    }

    @Override
    public int getItemCount() {
        if(days != null)
            return days.size();
        else
            return 0;
    }

    private Day getDayItem(int position) {
        return days.get(position);
    }


    public class DayViewHolder extends RecyclerView.ViewHolder {


        private ShapeableImageView day_IMG_img;
        private MaterialTextView day_TV_location;
        private MaterialTextView day_TV_name;
        private Day day;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    NavController navController =
                            Navigation.findNavController(fragment.requireActivity(),
                                    fragment.requireParentFragment().getId());

                    TripFragmentDirections.ActionTripFragmentToDayFragment action
                            = TripFragmentDirections.actionTripFragmentToDayFragment(day);

                    navController.navigate((NavDirections) action);

                }
            });



            day_IMG_img = itemView.findViewById(R.id.day_IMG_img);
            day_TV_location = itemView.findViewById(R.id.day_TV_location);
            day_TV_name = itemView.findViewById(R.id.day_TV_name);

        }

        public DayViewHolder setDay(Day day) {
            this.day = day;
            return this;
        }
    }
}
