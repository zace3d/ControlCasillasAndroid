package mx.citydevs.denunciaelectoral.utils;

import android.content.Context;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by zace3d on 6/02/15.
 */
public class LocationUtils {
    public static final String TAG_CLASS = LocationUtils.class.getSimpleName();

    public static boolean isGpsOrNetworkProviderEnabled(Context context) {
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Do what you need if enabled...
            return true;
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //Do what you need if not enabled...
            return true;
        } else {
            return false;
        }
    }

    public static LatLng getLatLngFromLocation(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public static LatLng getLatLngFromLocation(double latitude, double longitude) {
        return new LatLng(latitude, longitude);
    }

    public static String getStateFromLatLong(Context context, double latitude, double longitude) {
        Geocoder geocoder;
        List<android.location.Address> addresses;
        geocoder = new Geocoder(context, new Locale("es", "MX"));

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            if (addresses != null && addresses.size() > 0) {
                return addresses.get(0).getAdminArea();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
