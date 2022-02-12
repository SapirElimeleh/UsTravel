package dev.sapirel.ustravel.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import dev.sapirel.ustravel.Models.Trip;
import dev.sapirel.ustravel.R;
import dev.sapirel.ustravel.Utils.DataManager;


public class AddTripFragment extends Fragment {

    private TextInputLayout addTrip_TI_destination;
    private EditText        addTrip_StartDate;
    private EditText        addTrip_EndDate;
    private MaterialButton  addTrip_BTN_create;
    private Calendar myCalendar;


    public AddTripFragment() {
        // Required empty public constructor
    }


    public static AddTripFragment newInstance(String param1, String param2) {
        AddTripFragment fragment = new AddTripFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_trip, container, false);

        findView(view);
        initView(this);


        return view;
    }


    private String getTripDestination(){
        return addTrip_TI_destination.getEditText().getText().toString();
    }

    private String getTripStartDate(){
        return addTrip_StartDate.getText().toString();
    }

    private String getTripEndDate(){
        return addTrip_EndDate.getText().toString();
    }


    private void initView(Fragment fragment) {

        openDatePickerDialogForStartDate(fragment);
        openDatePickerDialogForEndDate(fragment);


        addTrip_BTN_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Trip myTrip = new Trip();
                myTrip.setTripLocation(getTripDestination());
                myTrip.setStartDate(getTripStartDate());
                myTrip.setEndDate(getTripEndDate());
                myTrip.setImage(DataManager.getInstance().getUserImage());
                myTrip.setMyTripVal(true);

                DataManager.getInstance().addMyTripToDataBase(myTrip);

                Toast.makeText(fragment.getContext(), "Trip Added Successfully!", Toast.LENGTH_SHORT).show();


                NavController navController =
                        Navigation.findNavController(fragment.requireActivity(),
                                fragment.requireParentFragment().getId());

                navController.navigate((NavDirections) AddTripFragmentDirections.actionAddTripFragmentToNavMyTrips());

                //navController.navigate(R.id.action_addTripFragment_to_nav_my_trips);

            }
        });
    }

    public void openDatePickerDialogForStartDate(Fragment fragment) {
        // Get Current Date

        myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateCalendar();
            }

            private void updateCalendar(){
                String Format = "dd/MM/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(Format, Locale.US);

                addTrip_StartDate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        addTrip_StartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(fragment.getContext(), date, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    public void openDatePickerDialogForEndDate(Fragment fragment) {
        // Get Current Date

        myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateCalendar();
            }

            private void updateCalendar(){
                String Format = "dd/MM/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(Format, Locale.US);

                addTrip_EndDate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        addTrip_EndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(fragment.getContext(), date, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void findView(View view) {

        addTrip_TI_destination = view.findViewById(R.id.addTrip_TI_destination);
        addTrip_StartDate = view.findViewById(R.id.addTrip_StartDate);
        addTrip_EndDate = view.findViewById(R.id.addTrip_EndDate);
        addTrip_BTN_create = view.findViewById(R.id.addTrip_BTN_create);

    }
}