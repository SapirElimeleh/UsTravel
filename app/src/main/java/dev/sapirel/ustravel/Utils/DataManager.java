package dev.sapirel.ustravel.Utils;


import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Hashtable;

import dev.sapirel.ustravel.Models.Day;
import dev.sapirel.ustravel.Models.Trip;

public class DataManager {
    private static DataManager dataManager;
    private FirebaseFirestore database;
    private FirebaseUser current_user;
    public static ArrayList<Trip> trips = new ArrayList<>();
    public static ArrayList<Trip> myTrips = new ArrayList<>();
    public static ArrayList<Day> allDays = new ArrayList<>();
    public static Hashtable<String, String> users = new Hashtable<>();

    public DataManager() {
    }

    public static DataManager getInstance() {
        if (dataManager == null) {
            dataManager = new DataManager();
            dataManager.database = FirebaseFirestore.getInstance();
            dataManager.current_user = FirebaseAuth.getInstance().getCurrentUser();
        }
        return dataManager;
    }

    public void updateTripLikes(String id, int numLikes) {

        DocumentReference tripUpdateLike = database.collection("Trips").document(id);

        tripUpdateLike.update("numLikes", numLikes)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Likes", "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Likes", "Error updating document", e);
                    }
                });
    }

    public void readDataFromFireBase(){
        EventChangeListenerTrips();
        EventChangeListenerDays();
    }

    public void EventChangeListenerTrips() {

        trips.clear();
        myTrips.clear();

            database.collection("Trips").orderBy("userName", Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                            if(error != null){
                                Log.e("Firestore error" , error.getMessage());
                                return;
                            }
                            for (DocumentChange dc : value.getDocumentChanges()){

                                if(dc.getType() == DocumentChange.Type.ADDED){

                                    database.collection("Trips").document(dc.getDocument().getId())
                                            .update("id",dc.getDocument().getId());

                                    Trip temp = dc.getDocument().toObject(Trip.class);


                                    if (temp.getUserId().equals(current_user.getUid())){
                                        myTrips.add(dc.getDocument().toObject(Trip.class));
                                    }
                                    else {
                                        trips.add(dc.getDocument().toObject(Trip.class));
                                    }


                                }
                            }
                        }
                    });
        }

    public void EventChangeListenerDays() {

        allDays.clear();

        database.collection("Days").orderBy("dayName", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error != null){
                            Log.e("Firestore error" , error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()){

                            if(dc.getType() == DocumentChange.Type.ADDED){

                                database.collection("Days").document(dc.getDocument().getId())
                                        .update("dayId",dc.getDocument().getId());

                                allDays.add(dc.getDocument().toObject(Day.class));
                            }
                        }
                    }
                });

    }

    public void deleteDayFromDataBase(String dayID, Fragment fragment){
        Day temp = new Day();
        for (Day day: allDays) {
            if (day.getDayId().equals(dayID)) {
                temp = day;

                database.collection("Days").document(dayID).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(fragment.getContext(), "Day successfully deleted!", Toast.LENGTH_SHORT).show();
                                Log.d("DeleteDay", "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("DeleteDay", "Error deleting document", e);
                            }
                        });
            }
        }

        allDays.remove(temp);

    }


    public void deleteTripFromDataBase(String tripId, Fragment fragment) {

        Trip temp = new Trip();
        for (Trip trip: myTrips) {
            if (trip.getId().equals(tripId)) {
                temp = trip;

                database.collection("Trips").document(tripId).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(fragment.getContext(), "Trip successfully deleted!", Toast.LENGTH_SHORT).show();
                                Log.d("DeleteTrip", "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(fragment.getContext(), "Error deleting Trip", Toast.LENGTH_SHORT).show();
                                Log.w("DeleteTrip", "Error deleting document", e);
                            }
                        });
            }
        }

        myTrips.remove(temp);

    }




    public void addMyTripToDataBase(Trip trip) {

        trip.setUserId(current_user.getUid());
        trip.setUserName(current_user.getDisplayName());

        database.collection("Trips")
                .add(trip)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        readDataFromFireBase();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //
            }
        });

    }


    public void addDayToDataBase(Day day, Trip trip) {

        day.setTripId(trip.getId());

        database.collection("Days")
                .add(day)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        readDataFromFireBase();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });



    }


    public static ArrayList<Trip> getTrips(){
        return trips;
    }

    public static ArrayList<Trip> getFavoriteTrips(){

        ArrayList<Trip> favoritesTrips = new ArrayList<>();

        if(trips == null)
            return favoritesTrips;

        for (int i=0 ; i < trips.size() ; i++) {
            if (trips.get(i).isLiked())
                favoritesTrips.add(trips.get(i));
        }

        return favoritesTrips;
    }


    public static ArrayList<Trip> getMyTrips(){
        return myTrips;
    }


    public static void addToMyTrips(Trip myTrip) {
        myTrips.add(myTrip);
    }



    public ArrayList<Day> getDays(String tripId) {
        ArrayList<Day> days = new ArrayList<>();
        for (Day day : allDays){
            if(day.getTripId().equals(tripId)){
                days.add(day);
            }
        }
        return days;
    }


    public Trip getTripByID(String tripId){
        for(Trip trip : myTrips)
            if(trip.getId().equals(tripId))
                return trip;

        return null;
    }

    public FirebaseUser getCurrent_user(){
        return dataManager.current_user;
    }

    public String getUserImage() {
       String Url = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString();

       return Url;
    }

}
