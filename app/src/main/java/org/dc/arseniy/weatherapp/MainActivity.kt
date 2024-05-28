package org.dc.arseniy.weatherapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermissions(INITIAL_PERMS, INITIAL_REQUEST)

        binding.btnLocationSettings.setOnClickListener(object : View.OnClickListener{
            @RequiresApi(Build.VERSION_CODES.P)
            override fun onClick(v: View?) {
                locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
                if (ActivityCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this@MainActivity,
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
                binding.tvTitleGPS.text =
                    locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.latitude.toString() + " " +
                            locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.longitude.toString()

                binding.tvTitleNet.text =
                    locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)?.latitude.toString() + " " +
                        locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)?.longitude.toString()
                binding.tvEnabledGPS.text = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER).toString()
                binding.tvEnabledNet.text = locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER).toString()
                System.out.println(String.equals(LocationManager.NETWORK_PROVIDER))
            }
        })
    }

}