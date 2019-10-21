package guidetec.com.guidetec.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import guidetec.com.guidetec.R;
import guidetec.com.guidetec.account.LoginActivity;
import guidetec.com.guidetec.fragments.ChatFragment;
import guidetec.com.guidetec.fragments.MapFragment;
import guidetec.com.guidetec.fragments.MessageFragment;
import guidetec.com.guidetec.fragments.CommerceFragment;
import guidetec.com.guidetec.fragments.MoreFragment;
import guidetec.com.guidetec.fragments.RouteFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth auth;
    TextView tv;
    Button btn_logout;

    BottomNavigationView bottomNavigationView;

    private static final String TAG = "MainActivity";

    //Fragments
    MapFragment mapFragment2 = new MapFragment();
    CommerceFragment commerceFragment = new CommerceFragment();
    RouteFragment routeFragment = new RouteFragment();
    ChatFragment chatFragment = new ChatFragment();
    MoreFragment moreFragment = new MoreFragment();

    // Create fragment
    final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        //Events
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        bottomNavigationView.setSelectedItemId(R.id.navigation_map);

        auth = FirebaseAuth.getInstance();
        //get current user
/*
        //Get instance of mapbox
        Mapbox.getInstance(this, getString(R.string.mapboxToken));


        Location location = getLastKnownLocation();

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
*/
        //mapFragment.getMapAsync(this);
        //loadCommerce();
    }

    // this listener will be called when there is change in firebase user session
    FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
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
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.navigation_map:
                // Add map fragment to parent container
                //transaction.add(R.id.main_container, mapFragment, "com.mapbox.map");
                //transaction.commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, mapFragment2).commit();
                return true;
            case R.id.navigation_route:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, routeFragment).commit();
                return true;
            case R.id.navigation_commerce:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, commerceFragment).commit();
                return true;
            case R.id.navigation_chat:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, chatFragment).commit();
                return true;
            case R.id.navigation_more:
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, moreFragment).commit();
                return true;
        }
        return false;
    }

}
