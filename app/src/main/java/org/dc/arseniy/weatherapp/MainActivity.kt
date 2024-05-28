package org.dc.arseniy.weatherapp

import android.Manifest
import android.annotation.SuppressLint
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.dc.arseniy.weatherapp.databinding.ActivityMainBinding
import java.util.Date


class MainActivity : AppCompatActivity() {
    private val INITIAL_PERMS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_CONTACTS
    )
    private val INITIAL_REQUEST = 1337

    private lateinit var binding: ActivityMainBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var latitude :String
    private lateinit var longitude :String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestPermissions(INITIAL_PERMS, INITIAL_REQUEST)
        binding.btnLocationSettings.setOnClickListener { object : View.OnClickListener{
            override fun onClick(v: View?) {
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@MainActivity)
            }
            @SuppressLint("MissingPermission")
            private fun obtieneLocalizacion(){
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        latitude =  location?.latitude.toString()
                        longitude = location?.longitude.toString()
                    }
            }
        } }
        binding.tvLocationGPS.text = latitude+" " + longitude
    }

}