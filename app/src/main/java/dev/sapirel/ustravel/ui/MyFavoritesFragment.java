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

import dev.sapirel.ustravel.Adapters.FavTripAdapter;
import dev.sapirel.ustravel.Models.Trip;
import dev.sapirel.ustravel.R;
import dev.sapirel.ustravel.Utils.DataManager;

public class MyFavoritesFragment extends Fragment {

    private RecyclerView favTrips_RV_rv;
    private SearchView favorites_SV_search;
    private ArrayList<Trip> favTrips;
    private ArrayList<Trip> searchTrips;
    public MyFavoritesFragment() {
        // Required empty public constructor
    }


    public static MyFavoritesFragment newInstance(String param1, String param2) {
        MyFavoritesFragment fragment = new MyFavoritesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        favTrips = new ArrayList<>();
        searchTrips = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_favorites, container, false);
        findView(view);
        initView(this);


        FavTripAdapter favTripAdapter = new FavTripAdapter(this, DataManager.getFavoriteTrips());

        favTrips_RV_rv.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL,false));
        favTrips_RV_rv.setHasFixedSize(true);
        favTrips_RV_rv.setItemAnimator(new DefaultItemAnimator());
        favTrips_RV_rv.setAdapter(favTripAdapter);


        return view;
    }

    private void initView(Fragment fragment) {

        favTrips = DataManager.getFavoriteTrips();

        favorites_SV_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                FavTripAdapter favTripAdapter = new FavTripAdapter(fragment, DataManager.getTrips());
                favTrips_RV_rv.setAdapter(favTripAdapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!favorites_SV_search.toString().isEmpty()){
                    searchTrips.clear();
                    for(int i = 0 ; i < favTrips.size() ; i++){
                        if (favTrips.get(i).getTripLocation().toLowerCase().contains(favorites_SV_search.getQuery().toString().toLowerCase())){
                            searchTrips.add(favTrips.get(i));
                        }
                    }

                    FavTripAdapter favTripAdapter = new FavTripAdapter(fragment, searchTrips);
                    favTrips_RV_rv.setAdapter(favTripAdapter);
                }
                else{
                    FavTripAdapter favTripAdapter = new FavTripAdapter(fragment, favTrips);
                    favTrips_RV_rv.setAdapter(favTripAdapter);
                }
                return false;
            }
        });
    }

    private void findView(View view) {

        favTrips_RV_rv = view.findViewById(R.id.favTrips_RV_rv);
        favorites_SV_search = view.findViewById(R.id.favorites_SV_search);
    }
}