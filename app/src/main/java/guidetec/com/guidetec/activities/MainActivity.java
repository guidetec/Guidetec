package guidetec.com.guidetec.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.SupportMapFragment;

import java.util.HashMap;

import guidetec.com.guidetec.R;
import guidetec.com.guidetec.account.LoginActivity;
import guidetec.com.guidetec.fragments.ARFragment;
import guidetec.com.guidetec.fragments.MapFragment;
import guidetec.com.guidetec.fragments.MessageFragment;
import guidetec.com.guidetec.fragments.PlaceFragment;
import guidetec.com.guidetec.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private FirebaseAuth auth;
    TextView tv;
    Button btn_logout;

    BottomNavigationView bottomNavigationView;

    private static final String TAG = "MainActivity";

    //Fragments
    MapFragment mapFragment2 = new MapFragment();
    PlaceFragment placeFragment = new PlaceFragment();
    ARFragment arFragment = new ARFragment();
    MessageFragment messageFragment = new MessageFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    // Create fragment
    final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

    // Create supportMapFragment
    SupportMapFragment mapFragment;

    //Location services
    protected LocationManager locationManager;
    protected LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        btn_logout = (Button) findViewById(R.id.btn_logout);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        //Events
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        bottomNavigationView.setSelectedItemId(R.id.navigation_augmented);

        auth = FirebaseAuth.getInstance();
        //get current user

        FirebaseUser currentUser = auth.getCurrentUser();
        tv = (TextView) findViewById(R.id.tv);
        if (currentUser != null) {
        }//tv.setText(currentUser.getEmail());
        else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        //Get instance of mapbox
        Mapbox.getInstance(this, getString(R.string.mapboxToken));

        //Get the location
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (savedInstanceState == null) {
            LatLng currenteLocation = new LatLng(location.getLatitude(), location.getLongitude());
            //LatLng patagonia = new LatLng(-52.6885, -70.1395);
            // Build mapboxMap
            MapboxMapOptions options = new MapboxMapOptions();
            options.styleUrl(Style.LIGHT);
            options.camera(new CameraPosition.Builder()
                    .target(currenteLocation)
                    .zoom(13)
                    .build());

            // Create map fragment
            mapFragment = SupportMapFragment.newInstance(options);
            //mapFragment.get

        } else {
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentByTag("com.mapbox.map");
        }

        mapFragment.getMapAsync(this);
        //loadCommerce();
    }
    public void loadPlace(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap=new HashMap<>();
        hashMap.put("id","PLC000007");
        hashMap.put("nombre","Plaza San Agustín");
        hashMap.put("resena","Centro");
        hashMap.put("lat","28.665766");
        hashMap.put("lon","-106.091027");
        hashMap.put("gratis",1);
        hashMap.put("interactuar",0);

        reference.child("lugares").push().setValue(hashMap);
    }
    public void loadCommerce(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap=new HashMap<>();
        hashMap.put("id","COM000003");
        hashMap.put("nombre","Dionisio");
        hashMap.put("resena","Un bar informal");
        hashMap.put("lat","28.628692");
        hashMap.put("lon","-106.099499");
        hashMap.put("open","07:00");
        hashMap.put("close","24:00");
        hashMap.put("calif",3);
        hashMap.put("dom","Blvd. Antonio Ortiz Mena 3403, Int. 22, Puerta del Sol, Campestre-Lomas, 31060 Chihuahua, Chihuahua");

        reference.child("comercios").push().setValue(hashMap);
    }

    // this listener will be called when there is change in firebase user session
    FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                // user auth state is changed - user is null
                // launch login activity
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            } else {
                //setDataToView(user);
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();

        //auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        //if (authListener != null) {
        //  auth.removeAuthStateListener(authListener);
        //}
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.navigation_map:
                // Add map fragment to parent container
                //transaction.add(R.id.main_container, mapFragment, "com.mapbox.map");
                //transaction.commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, mapFragment2).commit();
                return true;
            case R.id.navigation_place:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, placeFragment).commit();
                return true;
            case R.id.navigation_augmented:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, arFragment).commit();
                return true;
            case R.id.navigation_message:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, messageFragment).commit();
                return true;
            case R.id.navigation_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, profileFragment).commit();
                return true;
        }
        return false;
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        LocationComponent locationComponent = mapboxMap.getLocationComponent();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationComponent.activateLocationComponent(this);
        locationComponent.setLocationComponentEnabled(true);
        locationComponent.setRenderMode(RenderMode.COMPASS);
        Toast.makeText(this,"Se ha puesto la marca",Toast.LENGTH_LONG).show();

    }
}
