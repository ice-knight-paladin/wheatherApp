package org.dc.arseniy.weatherapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import org.dc.arseniy.weatherapp.databinding.ActivityMainBinding
import java.util.Date


class MainActivity : AppCompatActivity() {
    private val INITIAL_PERMS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_CONTACTS
    )
    private val INITIAL_REQUEST = 1337

    private lateinit var binding: ActivityMainBinding

    private var locationManager: LocationManager? = null
    var sbGPS: StringBuilder = StringBuilder()
    var sbNet: StringBuilder = StringBuilder()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
        requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
    }

    override fun onResume() {
        super.onResume()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        locationManager?.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000 * 10,
            10f,
            locationListener
        )
        locationManager?.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            1000 * 10,
            10f,
            locationListener
        )
        checkenabled()
    }

    override fun onPause() {
        super.onPause()
        locationManager?.removeUpdates(locationListener)
    }

    private val locationListener: LocationListener = LocationListener {
        fun onLocationChanged(location: Location) {
            showLocation(location)
        }

        fun onProviderDisabled(provide: String) {
            checkenabled()
        }

//        fun onProviderEnabled(provide : String){
//            checkenabled()
//            showLocation(locationManager?.getLastKnownLocation(provide))
//        }
        fun onStatusChanged(provide: String, status: Int, extras: Bundle) {
            if (provide.equals(LocationManager.GPS_PROVIDER)) {
                binding.tvStatusGPS.setText("Status: " + status.toString())
            } else if (provide.equals(LocationManager.NETWORK_PROVIDER)) {
                binding.tvStatusNet.setText("Status: " + status.toString())
            }
        }
    }

    private fun showLocation(location: Location?) {
        if (location == null) {
            return
        }
        if (location.provider.equals(LocationManager.GPS_PROVIDER)) {
            binding.tvLocationGPS.setText(formatLocation(location))
        } else if (location.provider.equals(LocationManager.NETWORK_PROVIDER)) {
            binding.tvLocationNet.setText(formatLocation(location))
        }
    }

    private fun formatLocation(location: Location): String {
        if (location == null) {
            return ""
        }
        return String.format(
            "Coord: lat = %1$.4f, lon = %2$.4f, time =%3$ %3$",
            location.latitude, location.longitude, Date(location.time)
        )
    }

    private fun checkenabled() {
        binding.tvEnabledGPS.setText(
            "Enabled: " + (locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)
                ?: "")
        )
        binding.tvEnabledNet.setText(
            "Enabled: " + (locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                ?: "")
        )
    }

    fun onClickLocationSettings(view: View) {
        startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }
}