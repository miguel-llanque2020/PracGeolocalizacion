package com.example.pracgeolocalizacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.icu.text.DecimalFormat;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.StreetViewPanoramaOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;

import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "Estilo del mapa";
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private GoogleMap mMap;
    private LatLng mjapon, malemania, mitalia, mfrancia;

    private LocationManager locManager;
    private Location loc;

    private double tvLatitud = 0, tvLongitud = 0;
    private double tvLatitudEND = 0, tvLongitudEND = 0;

    DecimalFormat formatoKm = new DecimalFormat("#.00");
    DecimalFormat formatom = new DecimalFormat("#");
    double distance;

    public MapsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mapFragment).commit();
        mapFragment.getMapAsync(this);

        ActivityCompat.requestPermissions(MapsActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        } else {
        }
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        tvLatitud = loc.getLatitude();
        tvLongitud = loc.getLongitude();
    }

    private void setInfoWindowClickToPanorama(GoogleMap map) {
        map.setOnInfoWindowClickListener(
                new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        if(marker.getTag() == "poi"){
                            StreetViewPanoramaOptions options =
                                    new StreetViewPanoramaOptions().position(
                                            marker.getPosition());
                            SupportStreetViewPanoramaFragment streetViewFragment
                                    = SupportStreetViewPanoramaFragment
                                    .newInstance(options);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container,
                                            streetViewFragment)
                                    .addToBackStack(null).commit();
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Change the map type based on the user's selection.
        mjapon = new LatLng(35.680513,139.769051);
        malemania = new LatLng(52.516934,13.403190);
        mitalia = new LatLng(41.902609,12.494847);
        mfrancia = new LatLng(48.843489,2.355331);

        Location location = new Location("Punto A");
        location.setLatitude(tvLatitud);  //latitud
        location.setLongitude(tvLongitud); //longitud
        Location location2 = new Location("Punto B");

        switch (item.getItemId()) {
            case R.id.normal_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.addMarker(new MarkerOptions().position(mjapon).title("Japon"));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(mjapon));
                tvLatitudEND = 35.680513;
                tvLongitudEND = 139.769051;
                location2.setLatitude(tvLatitudEND);  //latitud
                location2.setLongitude(tvLongitudEND); //longitud
                distance = location.distanceTo(location2)/1000;
                Toast.makeText(getApplicationContext(), "Japon a "+String.valueOf(formatoKm.format(distance))+" Km",Toast.LENGTH_LONG).show();
                return true;
            case R.id.hybrid_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                mMap.addMarker(new MarkerOptions().position(mitalia).title("Italia"));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(mitalia));
                tvLatitudEND = 41.902609;
                tvLongitudEND = 12.494847;
                location2.setLatitude(tvLatitudEND);  //latitud
                location2.setLongitude(tvLongitudEND); //longitud
                distance = location.distanceTo(location2)/1000;
                Toast.makeText(getApplicationContext(), "Italia a "+String.valueOf(formatoKm.format(distance))+" Km",Toast.LENGTH_LONG).show();
                return true;
            case R.id.satellite_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                mMap.addMarker(new MarkerOptions().position(malemania).title("Alemania"));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(malemania));
                tvLatitudEND = 52.516934;
                tvLongitudEND = 13.403190;
                location2.setLatitude(tvLatitudEND);  //latitud
                location2.setLongitude(tvLongitudEND); //longitud
                distance = location.distanceTo(location2)/1000;
                Toast.makeText(getApplicationContext(), "Alemania a "+String.valueOf(formatoKm.format(distance))+" Km",Toast.LENGTH_LONG).show();
                return true;
            case R.id.terrain_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                mMap.addMarker(new MarkerOptions().position(mfrancia).title("Francia"));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(mfrancia));
                tvLatitudEND = 48.843489;
                tvLongitudEND = 2.355331;
                location2.setLatitude(tvLatitudEND);  //latitud
                location2.setLongitude(tvLongitudEND); //longitud
                distance = location.distanceTo(location2)/1000;
                Toast.makeText(getApplicationContext(), "Francia a "+String.valueOf(formatoKm.format(distance))+" Km",Toast.LENGTH_LONG).show();
                return true;
            case R.id.calculo_map:
                location2.setLatitude(tvLatitudEND);  //latitud
                location2.setLongitude(tvLongitudEND); //longitud
                distance = location.distanceTo(location2);
                if (distance > 1000){
                    Toast.makeText(getApplicationContext(), "Distancia de "+String.valueOf(formatoKm.format(distance/1000))+" Km",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Distancia de "+String.valueOf(formatom.format(distance))+" m",Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation();
                    break;
                }
        }
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

        } else {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map_style));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        setOnMapClick(mMap);
        setMapLongClick(mMap);
        setPoiClick(mMap);
        setInfoWindowClickToPanorama(mMap);
        enableMyLocation();
    }

    private void setOnMapClick(final GoogleMap map){
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener(){

            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
            }
        });
    }

    private void setMapLongClick(final GoogleMap map) {
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mMap.clear();
                String snippet = String.format(Locale.getDefault(),
                        "Lat: %1$.5f, Long: %2$.5f",
                        latLng.latitude,
                        latLng.longitude);
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(getString(R.string.app_name))
                        .snippet(snippet)
                        .icon(BitmapDescriptorFactory.defaultMarker
                                (BitmapDescriptorFactory.HUE_RED)));
                tvLatitudEND = latLng.latitude;
                tvLongitudEND = latLng.longitude;
            }
        });
    }

    private void setPoiClick(final GoogleMap mMap) {
        mMap.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
            @Override
            public void onPoiClick(PointOfInterest poi) {
                mMap.clear();
                Marker poiMarker = mMap.addMarker(new MarkerOptions().position(poi.latLng).title(poi.name));
                poiMarker.showInfoWindow();
                poiMarker.setTag("poi");
            }
        });
    }
}
