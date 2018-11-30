package guidetec.com.guidetec.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.optimization.v1.MapboxOptimization;
import com.mapbox.api.optimization.v1.models.OptimizationResponse;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

// classes to calculate a route
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;

import guidetec.com.guidetec.activities.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import guidetec.com.guidetec.R;
import guidetec.com.guidetec.database.MarkerPlace;

import static android.support.constraint.Constraints.TAG;
import static com.mapbox.core.constants.Constants.PRECISION_6;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, PermissionsListener,
        MapboxMap.OnMapClickListener, MapboxMap.OnMapLongClickListener {

    private MapView mapView;
    private Button fab_controls_ar;

    //For optimized route
    private DirectionsRoute optimizedRoute;
    private MapboxOptimization optimizedClient;
    private Polyline optimizedPolyline;
    private List<Point> stops;
    private Point origin;

    private static final String FIRST = "first";
    private static final String ANY = "any";
    private static final String TEAL_COLOR = "#23D2BE";
    private static final int POLYLINE_WIDTH = 5;

    // variables for adding a marker
    private Marker destinationMarker;
    private LatLng originCoord;
    private LatLng destinationCoord;

    // variables for calculating and drawing a route
    private Point originPosition;
    private Point destinationPosition;
    private DirectionsRoute currentRoute;
    private static final String TAG = "DirectionsActivity";
    private NavigationMapRoute navigationMapRoute;

    //For marker
    private static final String MARKER_SOURCE = "markers-source";
    private static final String MARKER_STYLE_LAYER = "markers-style-layer";
    private static final String MARKER_IMAGE = "custom-marker";

    // variables for adding location layer
    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private Location originLocation;

    ArrayList<MarkerPlace> listaLugares;
    ArrayList<String> idLugares;

    DatabaseReference reference;
    DatabaseReference reference_route;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        Mapbox.getInstance(getActivity(), "pk.eyJ1IjoiZ3VpZGV0ZWMiLCJhIjoiY2ptbWo0aTUyMDI5ZjNrbHVxYnFob29uaSJ9.DVPv3_Q8H_2-oDr784OYug");
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        fab_controls_ar = (Button) view.findViewById(R.id.fab_controls_ar);
        fab_controls_ar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetFragmentAr fragment = new BottomSheetFragmentAr();
                fragment.show(getActivity().getSupportFragmentManager(), TAG);
            }
        });
        mapView.getMapAsync(this::onMapReady);

        stops = new ArrayList<>();

        //Start
        reference = FirebaseDatabase.getInstance().getReference("lugares");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MarkerPlace markerPlace = snapshot.getValue(MarkerPlace.class);
                    listaLugares.add(markerPlace);
                    mapboxMap.addMarker(new MarkerOptions().title(markerPlace.getNombre())
                            .position(new LatLng(Double.parseDouble(markerPlace.getLat()), Double.parseDouble(markerPlace.getLon()))));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //End

        //Start
        idLugares=new ArrayList<String>();

        reference_route = FirebaseDatabase.getInstance().getReference("routes");
        reference_route.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //ciclo para las rutas
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //Para las ubicaciones
                    for (DataSnapshot locationSnapshot : snapshot.child("locations").getChildren()) {
                        //Toast.makeText(getActivity(), locationSnapshot.getKey() + " " + locationSnapshot.getValue(), Toast.LENGTH_LONG).show();
                        idLugares.add(locationSnapshot.getValue().toString());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Toast.makeText(getContext(),"Actual",Toast.LENGTH_LONG).show();
        DatabaseReference local=FirebaseDatabase.getInstance().getReference("lugares");
        local.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    MarkerPlace mp=snapshot.getValue(MarkerPlace.class);
                    for(String id:idLugares){
                        if(id.equals(mp.getId())){
                            if (alreadyTwelveMarkersOnMap()) {
                                Toast.makeText(getContext(), "Se supero el máximo", Toast.LENGTH_LONG).show();
                            } else {
                                LatLng p=new LatLng(Double.parseDouble(mp.getLat()),Double.parseDouble(mp.getLon()));
                                addDestinationMarker(p);
                                addPointToStopsList(p);
                                getOptimizedRoute(stops);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Add the origin Point to the list


        return view;
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.LIGHT);
        enableLocationComponent();

        /* Image: An image is loaded and added to the map. */
        Bitmap icon = BitmapFactory.decodeResource(
                MapFragment.this.getResources(), R.drawable.custom_marker);
        mapboxMap.addImage(MARKER_IMAGE, icon);

        listaLugares=new ArrayList<MarkerPlace>();
        addFirstStopToStopsList();

        // Load and Draw the GeoJSON
        new DrawGeoJson().execute();
        //addMarkers();
        //addRoutes();
    }

    private void addMarkers() {
        // Create an Icon object for the marker to use
        IconFactory iconFactory = IconFactory.getInstance(getActivity());
        Icon icon = iconFactory.fromResource(R.drawable.custom_marker);

        //Add markers of the db
        //chats=new ArrayList<>();
        //listaLugares.clear();
        reference = FirebaseDatabase.getInstance().getReference("lugares");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MarkerPlace markerPlace = snapshot.getValue(MarkerPlace.class);
                    listaLugares.add(markerPlace);
                    mapboxMap.addMarker(new MarkerOptions().title(markerPlace.getNombre())
                            .position(new LatLng(Double.parseDouble(markerPlace.getLat()), Double.parseDouble(markerPlace.getLon()))));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void addRoutes() {

        ArrayList<String> idLugares=new ArrayList<String>();

        reference_route = FirebaseDatabase.getInstance().getReference("routes");
        reference_route.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //ciclo para las rutas
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //Para las ubicaciones
                    for (DataSnapshot locationSnapshot : snapshot.child("locations").getChildren()) {
                        //Toast.makeText(getActivity(), locationSnapshot.getKey() + " " + locationSnapshot.getValue(), Toast.LENGTH_LONG).show();
                        idLugares.add(locationSnapshot.getValue().toString());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Toast.makeText(getContext(),"Actual",Toast.LENGTH_LONG).show();
        DatabaseReference local=FirebaseDatabase.getInstance().getReference("lugares");
        local.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    MarkerPlace mp=snapshot.getValue(MarkerPlace.class);
                    for(String id:idLugares){
                        if(id.equals(mp.getId())){
                            if (alreadyTwelveMarkersOnMap()) {
                                Toast.makeText(getContext(), "Se supero el máximo", Toast.LENGTH_LONG).show();
                            } else {
                                LatLng p=new LatLng(Double.parseDouble(mp.getLat()),Double.parseDouble(mp.getLon()));
                                addDestinationMarker(p);
                                addPointToStopsList(p);
                                getOptimizedRoute(stops);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static Bitmap createCustomMarker(Context context, @DrawableRes int resource, String _name) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);

        CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);
        markerImage.setImageResource(resource);
        TextView txt_name = (TextView) marker.findViewById(R.id.name);
        txt_name.setText(_name);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);

        return bitmap;
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent() {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this.getActivity())) {
            // Activate the MapboxMap LocationComponent to show user location
            // Adding in LocationComponentOptions is also an optional parameter
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(this.getActivity());
            locationComponent.setLocationComponentEnabled(true);
            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            //Set render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
            originLocation = locationComponent.getLastKnownLocation();

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this.getActivity());
        }
    }

    private Location getLastKnownLocation() {
        return originLocation;
    }
    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this.getActivity(), "Permiso concedido", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationComponent();
        } else {
            Toast.makeText(this.getActivity(), "Permiso denegado", Toast.LENGTH_LONG).show();
            this.getActivity().finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void getRoute(Point origin, Point destination) {
        NavigationRoute.builder(this.getContext())
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        // You can get the generic HTTP info about the response
                        Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;
                        }

                        currentRoute = response.body().routes().get(0);

                        // Draw the route on the map
                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
    }

    @Override
    public void onMapClick(@NonNull LatLng point) {

    }

    @Override
    public void onMapLongClick(@NonNull LatLng point) {

    }

    private void addFirstStopToStopsList() {
// Set first stop
        Location l=getLastKnownLocation();
        if(l!=null){
            origin = Point.fromLngLat(l.getLongitude(), l.getLatitude());
            stops.add(origin);
            Toast.makeText(this.getContext(), "Correct", Toast.LENGTH_SHORT).show();
        }

    }
    private boolean alreadyTwelveMarkersOnMap() {
        if (stops.size() == 12) {
            return true;
        } else {
            return false;
        }
    }
    private void addDestinationMarker(LatLng point) {
      /*  mapboxMap.addMarker(new MarkerOptions()
                .position(new LatLng(point.getLatitude(), point.getLongitude()))
                .title(getString(R.string.destination)));*/
    }

    private void addPointToStopsList(LatLng point) {
        stops.add(Point.fromLngLat(point.getLongitude(), point.getLatitude()));
    }

    private void getOptimizedRoute(List<Point> coordinates) {
        optimizedClient = MapboxOptimization.builder()
                .source(FIRST)
                .destination(ANY)
                .coordinates(coordinates)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .accessToken(Mapbox.getAccessToken())
                .build();

        optimizedClient.enqueueCall(new Callback<OptimizationResponse>() {
            @Override
            public void onResponse(Call<OptimizationResponse> call, Response<OptimizationResponse> response) {
                if (!response.isSuccessful()) {
                    Log.d("DirectionsActivity", "Algo ha salido mal");
                    Toast.makeText(getContext(), "Algo ha salido mal", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (response.body().trips().isEmpty()) {
                        Log.d("DirectionsActivity", "Exitoso pero sin rutas" + " size = "
                                + response.body().trips().size());
                        Toast.makeText(getContext(), "No se han encontrado rutas",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

// Get most optimized route from API response
                optimizedRoute = response.body().trips().get(0);
                drawOptimizedRoute(optimizedRoute);
            }

            @Override
            public void onFailure(Call<OptimizationResponse> call, Throwable throwable) {
                Log.d("DirectionsActivity", "Error: " + throwable.getMessage());
            }
        });
    }

    private void drawOptimizedRoute(DirectionsRoute route) {
// Remove old polyline
        if (optimizedPolyline != null) {
            mapboxMap.removePolyline(optimizedPolyline);
        }
// Draw points on MapView
        LatLng[] pointsToDraw = convertLineStringToLatLng(route);
        optimizedPolyline = mapboxMap.addPolyline(new PolylineOptions()
                .add(pointsToDraw)
                .color(Color.parseColor(TEAL_COLOR))
                .width(POLYLINE_WIDTH));
    }
    private LatLng[] convertLineStringToLatLng(DirectionsRoute route) {
// Convert LineString coordinates into LatLng[]
        LineString lineString = LineString.fromPolyline(route.geometry(), PRECISION_6);
        List<Point> coordinates = lineString.coordinates();
        LatLng[] points = new LatLng[coordinates.size()];
        for (int i = 0; i < coordinates.size(); i++) {
            points[i] = new LatLng(
                    coordinates.get(i).latitude(),
                    coordinates.get(i).longitude());
        }
        return points;
    }
/*
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Cancel the directions API request
        if (optimizedClient != null) {
            optimizedClient.cancelCall();
        }
        if (mapboxMap != null) {
            mapboxMap.removeOnMapClickListener(this);
        }
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }*/
private class DrawGeoJson extends AsyncTask<Void, Void, List<LatLng>> {
    @Override
    protected List<LatLng> doInBackground(Void... voids) {

        ArrayList<LatLng> points = new ArrayList<>();

        try {
            // Load GeoJSON file
            InputStream inputStream = getActivity().getAssets().open("example.geojson");
            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }

            inputStream.close();

            // Parse JSON
            JSONObject json = new JSONObject(sb.toString());
            JSONArray features = json.getJSONArray("features");
            JSONObject feature = features.getJSONObject(0);
            JSONObject geometry = feature.getJSONObject("geometry");
            if (geometry != null) {
                String type = geometry.getString("type");

                // Our GeoJSON only has one feature: a line string
                if (!TextUtils.isEmpty(type) && type.equalsIgnoreCase("LineString")) {

                    // Get the Coordinates
                    JSONArray coords = geometry.getJSONArray("coordinates");
                    for (int lc = 0; lc < coords.length(); lc++) {
                        JSONArray coord = coords.getJSONArray(lc);
                        LatLng latLng = new LatLng(coord.getDouble(1), coord.getDouble(0));
                        points.add(latLng);
                    }
                }
            }
        } catch (Exception exception) {
            Log.e(TAG, "Exception Loading GeoJSON: " + exception.toString());
        }

        return points;
    }

    @Override
    protected void onPostExecute(List<LatLng> points) {
        super.onPostExecute(points);

        if (points.size() > 0) {

            // Draw polyline on map
            mapboxMap.addPolyline(new PolylineOptions()
                    .addAll(points)
                    .color(Color.parseColor("#3bb2d0"))
                    .width(2));
        }
    }
}
}
