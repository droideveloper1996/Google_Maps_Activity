package tourguide.project.com.google_maps_activity;

import android.Manifest.permission;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Info_Activity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        setTitle("Location Information");
        final TextView velocity = (TextView) findViewById(R.id.speed);
        final TextView add = (TextView) findViewById(R.id.address);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                double height = location.getAltitude();
                int speed=(int) ((location.getSpeed()*3600)/1000);
                velocity.setText(Double.toString(speed)+" Km/hr");
                String address = "";
                Geocoder geocoder = new Geocoder(Info_Activity.this, Locale.getDefault());
                try {
                    List<Address> map = geocoder.getFromLocation(latitude, longitude, 1);
                    if (map != null && map.size() > 0) {
                        Log.i("Place info", map.get(0).toString());
                        if (map.get(0).getSubThoroughfare() != null) {
                            address += map.get(0).getSubThoroughfare() + " ,";
                        }
                        if (map.get(0).getSubLocality() != null) {
                            address += map.get(0).getSubLocality() + " ,";
                        }
                        if (map.get(0).getLocality() != null) {
                            address += map.get(0).getLocality() + " ,";
                        }
                        if (map.get(0).getPostalCode() != null) {
                            address += map.get(0).getPostalCode() + " ,";
                        }
                        if (map.get(0).getSubAdminArea() != null) {
                            address += map.get(0).getSubAdminArea() + " ,";
                        }
                        if (map.get(0).getAdminArea() != null) {
                            address += map.get(0).getAdminArea() + " ,";
                        }
                        if (map.get(0).getCountryName() != null) {
                            address += map.get(0).getCountryName() + " ";
                        }
                        if (map.get(0).getCountryCode() != null) {
                            address += "("+map.get(0).getCountryCode() + ") ";
                        }

                    } else {
                        address = "No Address Found";
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                add.setText(address);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (VERSION.SDK_INT < 23) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4, 4, listener);

        } else {
            if (ContextCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{permission.ACCESS_FINE_LOCATION}, 2);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4, 4, listener);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4, 4, listener);
                }

            }
        }
    }
}
