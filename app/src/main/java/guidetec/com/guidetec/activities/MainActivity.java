package guidetec.com.guidetec.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.SupportMapFragment;

import guidetec.com.guidetec.R;
import guidetec.com.guidetec.account.LoginActivity;
import guidetec.com.guidetec.fragments.ARFragment;
import guidetec.com.guidetec.fragments.MapFragment;
import guidetec.com.guidetec.fragments.MessageFragment;
import guidetec.com.guidetec.fragments.PlaceFragment;
import guidetec.com.guidetec.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth auth;
    TextView tv;
    Button btn_logout;

    BottomNavigationView bottomNavigationView;

    private static final String TAG="MainActivity";

    //Fragments
    MapFragment mapFragment2=new MapFragment();
    PlaceFragment placeFragment=new PlaceFragment();
    ARFragment arFragment=new ARFragment();
    MessageFragment messageFragment=new MessageFragment();
    ProfileFragment profileFragment=new ProfileFragment();

    // Create fragment
    final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

    // Create supportMapFragment
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        btn_logout=(Button)findViewById(R.id.btn_logout);
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_navigation);

        //Events
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        bottomNavigationView.setSelectedItemId(R.id.navigation_augmented);

        auth = FirebaseAuth.getInstance();
        //get current user

        FirebaseUser currentUser = auth.getCurrentUser();
        tv=(TextView)findViewById(R.id.tv);
        if(currentUser!=null)
        {}//tv.setText(currentUser.getEmail());
        else{
            Intent intent =new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

        //Get instance of mapbox
        Mapbox.getInstance(this, getString(R.string.mapboxToken));

        if (savedInstanceState == null) {
            LatLng patagonia = new LatLng(-52.6885, -70.1395);
            // Build mapboxMap
            MapboxMapOptions options = new MapboxMapOptions();
            options.styleUrl(Style.SATELLITE);
            options.camera(new CameraPosition.Builder()
                    .target(patagonia)
                    .zoom(9)
                    .build());

            // Create map fragment
            mapFragment = SupportMapFragment.newInstance(options);
            //mapFragment.get


        } else {
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentByTag("com.mapbox.map");
        }


        /*
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };*/

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

        switch (item.getItemId()){
            case R.id.navigation_map:
                // Add map fragment to parent container
                //transaction.add(R.id.main_container, mapFragment, "com.mapbox.map");
                //transaction.commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container,mapFragment).commit();
                return true;
            case R.id.navigation_place:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container,placeFragment).commit();
                return true;
            case R.id.navigation_augmented:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container,arFragment).commit();
                return true;
            case R.id.navigation_message:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container,messageFragment).commit();
                return true;
            case R.id.navigation_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container,profileFragment).commit();
                return true;
        }
        return false;
    }
}
