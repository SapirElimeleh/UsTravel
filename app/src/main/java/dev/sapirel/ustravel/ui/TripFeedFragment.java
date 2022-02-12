package dev.sapirel.ustravel.ui;

import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import dev.sapirel.ustravel.Adapters.TripFeedAdapter;
import dev.sapirel.ustravel.Models.Trip;
import dev.sapirel.ustravel.R;
import dev.sapirel.ustravel.Utils.DataManager;

public class TripFeedFragment extends Fragment {

    private RecyclerView tripFeed_RV_rv;
    private SearchView feed_SV_search;
    private ArrayList<Trip> allTrips;
    private ArrayList<Trip> searchTrips;


    public TripFeedFragment() {
        // Required empty public constructor
    }

    public static TripFeedFragment newInstance(String param1, String param2) {
        TripFeedFragment fragment = new TripFeedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        allTrips = new ArrayList<>();
        searchTrips = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trip_feed, container, false);

        TripFeedAdapter tripFeedAdapter = new TripFeedAdapter(this, DataManager.getTrips());

        findView(view);
        initView(this);



        tripFeedAdapter.setTripItemClickListener(new TripFeedAdapter.TripItemClickListener() {

            @Override
            public void likeClicked(Trip trip, int position) {
                DataManager.getInstance().updateTripLikes(trip.getId(), trip.getNumLikes());
                tripFeed_RV_rv.getAdapter().notifyItemChanged(position);
            }
        });

        tripFeed_RV_rv.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL,false));
        tripFeed_RV_rv.setHasFixedSize(true);
        tripFeed_RV_rv.setItemAnimator(new DefaultItemAnimator());
        tripFeed_RV_rv.setAdapter(tripFeedAdapter);

        return view;

    }

    private void initView(Fragment fragment) {

        allTrips = DataManager.getTrips();

        feed_SV_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                TripFeedAdapter tripFeedAdapter = new TripFeedAdapter(fragment, DataManager.getTrips());
                tripFeed_RV_rv.setAdapter(tripFeedAdapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!feed_SV_search.toString().isEmpty()){
                    searchTrips.clear();
                    for(int i = 0 ; i < allTrips.size() ; i++){
                        if (allTrips.get(i).getTripLocation().toLowerCase().contains(feed_SV_search.getQuery().toString().toLowerCase())){
                            searchTrips.add(allTrips.get(i));
                        }
                    }

                    TripFeedAdapter tripFeedAdapter = new TripFeedAdapter(fragment, searchTrips);
                    tripFeed_RV_rv.setAdapter(tripFeedAdapter);
                }
                else{
                    TripFeedAdapter tripFeedAdapter = new TripFeedAdapter(fragment, allTrips);
                    tripFeed_RV_rv.setAdapter(tripFeedAdapter);
                }
                return false;
            }
        });
    }

    private void findView(View view) {

        tripFeed_RV_rv = view.findViewById(R.id.tripFeed_RV_rv);
        feed_SV_search = view.findViewById(R.id.feed_SV_search);

    }
}