package dev.sapirel.ustravel.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import dev.sapirel.ustravel.Models.Day;
import dev.sapirel.ustravel.Models.Trip;
import dev.sapirel.ustravel.R;
import dev.sapirel.ustravel.Utils.DataManager;


public class AddDayFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private TextInputLayout addDay_TI_name;
    private EditText addDay_Date;
    private TextInputLayout addDay_TI_destination;
    private AppCompatImageButton addDay_BTN_addPhotos;
    private MaterialButton addDay_BTN_create;
    private TextView addDay_TV_photos;
    private Trip trip;
    private ImageView addDay_IV_img;
    private ProgressBar addDay_PB_pb;
    private String URL;
    private Calendar myCalendar;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference;
    private Uri mImageUri;

    public AddDayFragment() {
        // Required empty public constructor
    }


    public static AddDayFragment newInstance(String param1, String param2) {
        AddDayFragment fragment = new AddDayFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_day, container, false);
        findView(view);
        initView(this);


        if (getArguments() != null) {
            AddDayFragmentArgs addDayFragmentArgs = AddDayFragmentArgs.fromBundle(getArguments());
            this.trip = addDayFragmentArgs.getTrip();
        }

        return view;
    }

    private void initView(Fragment fragment) {

        openDatePickerDialog(fragment);

        addDay_BTN_addPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChoose();
                addDay_TV_photos.setVisibility(View.INVISIBLE);


            }
        });

        //create day clicked
        addDay_BTN_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Day day = new Day();
                day.setDayName(getDayName())
                        .setDate(getDayDate())
                        .setTripLocation(getDayDestination());

                uploadImageToFireBase(fragment, day, trip);

            }
        });
    }

    public void openDatePickerDialog(Fragment fragment) {
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

                addDay_Date.setText(sdf.format(myCalendar.getTime()));
            }
        };

        addDay_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(fragment.getContext(), date, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        addDay_Date.setText(sdf.format(myCalendar.getTime()));
    }

    private void uploadImageToFireBase(Fragment fragment, Day day, Trip trip) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
        Date now = new Date();
        String fileName = formatter.format(now);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("images/" + fileName);

        if(mImageUri == null)
            Toast.makeText(fragment.getContext(), "Please choose photo", Toast.LENGTH_SHORT).show();
        else {
            storageReference.putFile(mImageUri).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(fragment.getContext(), "Failed to add Day", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            addDay_PB_pb.setProgress(0);
                        }
                    }, 500);

                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            URL = uri.toString();
                            addDay_IV_img.setImageURI(uri);
                            day.setImage(URL);

                            DataManager.getInstance().addDayToDataBase(day, trip);
                            Toast.makeText(fragment.getContext(), "Day Added Successfully!", Toast.LENGTH_SHORT).show();


                            NavController navController = Navigation.findNavController(fragment.requireActivity(),
                                    fragment.requireParentFragment().getId());

                            AddDayFragmentDirections.ActionAddDayFragmentToTripFragment action
                                    = AddDayFragmentDirections.actionAddDayFragmentToTripFragment(trip);

                            navController.navigate((NavDirections) action);


                        }
                    });


                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    addDay_PB_pb.setProgress((int) progress);
                }
            });


        }
    }


    public String getDayName() {
        return addDay_TI_name.getEditText().getText().toString();
    }

    public String getDayDate() {
        return addDay_Date.getEditableText().toString();
    }

    public String getDayDestination() {
        return addDay_TI_destination.getEditText().getText().toString();
    }

    private void findView(View view) {

        addDay_TI_name = view.findViewById(R.id.addDay_TI_name);
        addDay_Date = view.findViewById(R.id.addDay_Date);
        addDay_TI_destination = view.findViewById(R.id.addDay_TI_destination);
        addDay_BTN_addPhotos = view.findViewById(R.id.addDay_BTN_addPhotos);
        addDay_BTN_create = view.findViewById(R.id.addDay_BTN_create);
        addDay_TV_photos = view.findViewById(R.id.addDay_TV_photos);
        addDay_IV_img = view.findViewById(R.id.addDay_IV_img);
        addDay_PB_pb = view.findViewById(R.id.addDay_PB_pb);

    }


    private void openFileChoose() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        mImageUri = data.getData();
                        addDay_IV_img.setImageURI(mImageUri);
                    }
                }
            });


}