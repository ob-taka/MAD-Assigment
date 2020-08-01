package com.health.anytime;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class User_home extends AppCompatActivity{

    private MAdaptor adaptor; // adaptor refer
    String receriveIntent;
    private String name;
    private String scheduleID;
    private String patientPic;
    private ArrayList<String> medicinePic;
    private FirebaseAuth auth;
    private DatabaseReference userReference;// change to recieveintent
    private final DatabaseReference medicineReference = FirebaseDatabase.getInstance().getReference().child("med_list"); // get the reference of the medicine list base on schedule ID from user
    ImageView imageBtn;
    private TextView username, greating;
    private Button opt;
    public static Activity fa;
    private String day, meal;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // ensure that no title is displayed
        setContentView(R.layout.activity_user_home);
        fa=this;
        imageBtn = findViewById(R.id.imageButton); // when clicked go to patient profile detail
        username = findViewById(R.id.label_Name);
        greating = findViewById(R.id.greating);
        medicinePic = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        //receriveIntent = getIntent().getStringExtra("Uid"); // get Uid intent form SignIn
        receriveIntent = auth.getCurrentUser().getUid();
        userReference = FirebaseDatabase.getInstance().getReference().child("User").child(receriveIntent);// get reference of the current using the uid pass in form login
        opt = findViewById(R.id.menu_btn);
        // set btn for menu
        registerForContextMenu(opt);
        Calendar calendar  = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date currentdate = calendar.getTime();
        day = dateFormat.format(currentdate);

        meal = setmeal();
        initUser(day, meal);



        //Glide.with(this).load(auth.getCurrentUser().getPhotoUrl()).into(imageBtn);
        // set onClickListenr on the image of the user profile to got into their profile page
        imageBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User_home.this, Profile_Patient.class);
                intent.putExtra("UID", receriveIntent);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.options_menu, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.chat_button:
                startActivity(new Intent(User_home.this, ChatHome.class));
                break;
            case R.id.find_location:
                getLocationpermission();
                break;
            case R.id.logout_button:
                auth.signOut();
                startActivity(new Intent(User_home.this, SignIn.class));
                break;
            case R.id.alarm:
                startActivity(new Intent(User_home.this, SignIn.class));
                break;
            default:
                break;

        }
        return super.onContextItemSelected(item);
    }

    /**
     * fetch the user information form the firebase and get the specific data that was needed
     * to prepare to be display on the screen and pass as a intent to the next activity
     */
    private void initUser(final String day, final String meal) {

        userReference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                scheduleID = dataSnapshot.child("medid").getValue(String.class);
                name = dataSnapshot.child("patientName").getValue(String.class);
                patientPic = dataSnapshot.child("patientProfilepic").getValue(String.class);
                username.setText(name);
                CollectionReference userRef = db.collection("Medicines_hardcode").document("med_list_ID")
                        .collection("Day").document("1-8-2020") //string.valueof(day)
                        .collection(meal);
                setUpRecyclerView(userRef);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    /**
     * change the greeting base on the time of the time
     */
    private String  setmeal() {
        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        if (timeOfDay >= 0 && timeOfDay < 12) {
            greating.setText("Good Morning");
            return "Breakfast";
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            greating.setText("Good Afternoon");
            return "Lunch";
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            greating.setText("Good Evening");
            return "Dinner";
        }
        return null;
    }


    /**
     * Using this method to set up the recycler view using the information fetched form the database
     * adding in OnClickListener to the object to allow the user to interact with the recycler view items to expand of close to show the details of the medicament
     */
    private void setUpRecyclerView(CollectionReference ref) {
        Query query = ref.orderBy("title", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Modle> options = new  FirestoreRecyclerOptions.Builder<Modle>()
                .setQuery(query, Modle.class)
                .build();

        // passing data into adaptor
        adaptor = new MAdaptor(options);
        RecyclerView recyclerView = findViewById(R.id.mRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptor);

        adaptor.startListening();
        fetchPatientPic();

        // o`verrides the interface created in the adaptor class to customise the even of the click
        adaptor.setOnItemClickListener(new MAdaptor.OnItemClickListener(){
            @Override
            public void onItemClick(DocumentSnapshot dataSnapshot, int position) {
                Modle modle = adaptor.getItem(position);
                modle.setExpanded(!modle.isExpanded());
                adaptor.notifyItemChanged(position);
            }
        });
    }

    /**
     * fetch image view from firebase Storage (file hosting service)
     */
    private void fetchPatientPic() {
        // finds image and download image from firebase storage by image path and binds it to view holder
        FirebaseStorage storage = FirebaseStorage.getInstance(
                "gs://quickmad-e4016.appspot.com/"
        );
        StorageReference storageRef = storage
                .getReference()
                .child("ProfilePicture/" + patientPic);
        storageRef
                .getDownloadUrl()
                .addOnSuccessListener(
                        new OnSuccessListener<Uri>(){

                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(User_home.this).load(uri).into(imageBtn); // uses Gilde , a framework to load and download files in android
                            }
                        }
                );
    }

    //to get location permission from user
    private void getLocationpermission() {
        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(User_home.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);

        } else {
            getLocation();
        }
    }
    //get location permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(this, "Please enable location permissions to use this feature", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //get location of user latitude and longitude
    private void getLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, new LocationCallback(){
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(User_home.this).removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int lastestlocationindex = locationResult.getLocations().size() - 1;
                            double latitude = locationResult.getLocations().get(lastestlocationindex).getLatitude();
                            double longitude = locationResult.getLocations().get(lastestlocationindex).getLongitude();
                            Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=clinic");
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
                        }
                    }
                }, Looper.getMainLooper());
    }



    @Override
    protected void onStop() {
        super.onStop();
        adaptor.stopListening();
    }
}
