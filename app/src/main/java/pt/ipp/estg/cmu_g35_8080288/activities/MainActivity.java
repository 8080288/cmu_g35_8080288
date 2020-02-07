package pt.ipp.estg.cmu_g35_8080288.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import pt.ipp.estg.cmu_g35_8080288.R;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final int REQUEST_FINE_LOCATION = 100;
    private FusedLocationProviderClient fusedLocationProviderClient;
    //pharmacies map variables
    private SupportMapFragment pharmaciesMapFragment;
    private GoogleMap googleMap;
    public static final String TAG = "MapActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.main_activity_drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getLastLocation();

        this.pharmaciesMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.pharmacies_map);
        this.pharmaciesMapFragment.getMapAsync(this);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.pharmacies:
                FragmentTransaction pharmaciesMap = getSupportFragmentManager().beginTransaction();
                this.pharmaciesMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.pharmacies_map);
                this.pharmaciesMapFragment.getMapAsync(this);
                pharmaciesMap.replace(R.id.main_content, this.pharmaciesMapFragment);
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        this.googleMap = googleMap;
    }

   private void getLastLocation(){

        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

       if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
               !=
          PackageManager.PERMISSION_GRANTED
       ){
           requestPermissions();
           return;
       } else{
           final Task location = this.fusedLocationProviderClient.getLastLocation();
           location.addOnCompleteListener(new OnCompleteListener() {
               @Override
               public void onComplete(@NonNull Task task) {
                   if (task.isSuccessful()){
                       Log.d(TAG, "location founded!");
                       Location currentLocation = (Location) task.getResult();

                       moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));

                   } else{
                       Log.d(TAG, "onComplete: current location is null");
                   }
               }
           });
       }
   }

   private void requestPermissions(){
       ActivityCompat.requestPermissions(
               this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
               REQUEST_FINE_LOCATION
       );
   }

   private void moveCamera(LatLng latLng){
//       Log.d(TAG, "Latlon:" + latLng.latitude + latLng.longitude + latLng);

       LatLng latLng1 = new LatLng(latLng.latitude, latLng.longitude);
//       Log.d(TAG, "Latlon:" + latLng1.latitude + latLng1.longitude + latLng);
       this.googleMap.addMarker(new MarkerOptions().position(new LatLng(latLng1.latitude, latLng1.longitude)).title("Você está aqui"));
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

   }
}