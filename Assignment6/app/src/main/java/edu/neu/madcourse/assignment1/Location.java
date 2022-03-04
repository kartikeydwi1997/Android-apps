package edu.neu.madcourse.assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.location.SettingInjectorService;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.Locale;

public class Location extends AppCompatActivity {
TextView currLocation,locationAddress;
FusedLocationProviderClient fusedLocationProviderClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        currLocation=findViewById(R.id.textLocation);
        locationAddress=findViewById(R.id.locAddress);
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(Location.this);

        if(ActivityCompat.checkSelfPermission(Location.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(Location.this,Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            getCurrentLocation();
        }
        else{
             ActivityCompat.requestPermissions(Location.this,new String[]{
                     Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},100);

}
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100&& grantResults.length>0 && (grantResults[0]+grantResults[1]
        ==PackageManager.PERMISSION_GRANTED)){
            getCurrentLocation();
        }else{
            Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        LocationManager manager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER)||
        manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<android.location.Location>() {
                @Override
                public void onComplete(@NonNull Task<android.location.Location> task) {
                     android.location.Location location=task.getResult();
                     if(location!=null){
                         currLocation.setText("Lat:"+String.valueOf(location.getLatitude()+"," +
                                 ""+"Long:"+String.valueOf(location.getLongitude())));
                         try{
                             Geocoder geocoder=new Geocoder(Location.this, Locale.getDefault());
                             List<Address> addressList=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                             String address=addressList.get(0).getAddressLine(0);
                             locationAddress.setText(address);
                         }catch (Exception e){
                             e.printStackTrace();
                         }
                     }else{
                         LocationRequest locationRequest=new LocationRequest()
                                 .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                 .setInterval(10000)
                                 .setFastestInterval(1000)
                                 .setNumUpdates(1);
                        LocationCallback locationCallback=new LocationCallback(){
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                super.onLocationResult(locationResult);
                                android.location.Location location1=locationResult.getLastLocation();
                                currLocation.setText("Lat:"+String.valueOf(location1.getLatitude()+"," +
                                        ""+"Long:"+String.valueOf(location1.getLongitude())));
                            }
                        };
fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
                     }
                }
            });
        }else{
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}