package com.example.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.databinding.FragmentSearchBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: SearchViewModel
    private val REQUEST_CODE_COARSE_LOCATION: Int = 1234
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var latitude: Float? = null
    private var longitude: Float? = null
    private lateinit var serviceIntent : Intent
    private var IsNotificationActive : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.MINUTES.toMillis(30)
            fastestInterval = TimeUnit.SECONDS.toMillis(30)
            maxWaitTime = TimeUnit.MINUTES.toMillis(1)
            priority = PRIORITY_HIGH_ACCURACY
        }
        locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.locations.forEach{
                    Log.d(TAG,"1) Callback Latitude: " + it.latitude.toString() + ", Longitude: " + it.longitude.toString())
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = "Search"
        binding = FragmentSearchBinding.bind(view)
        viewModel = SearchViewModel()
        createNotificationChannel()
        serviceIntent = Intent(requireActivity().applicationContext, NotificationService::class.java)
    }

    override fun onResume() {
        super.onResume()
        viewModel.enableButton.observe(this){
            enable -> binding.searchButton.isEnabled = enable
        }
        binding.searchButton.setOnClickListener {
            val action= SearchFragmentDirections.actionSearchFragmentToCurrentConditionsFragment(viewModel.getZipCode())
            findNavController().navigate(action)
        }
        binding.zipCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
               p0?.toString()?.let { viewModel.updateZipCode(it) }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.locationButton.setOnClickListener {
            if(ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                getLocation()
            } else {
               requestPermission()
           }
        }

        binding.notificationBtn.setOnClickListener {
            if(ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                getLocation()
            } else {
                requestPermission()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            Log.d(TAG,"2) Latitude: " + location.latitude.toString() + ", Longitude: " + location.longitude.toString())
            latitude = location.latitude.toFloat()
            longitude = location.longitude.toFloat()
            Log.d(TAG,"3) Latitude: " + latitude + ", Longitude: " + longitude)
            createNotificationChannel()
            if(latitude != null && longitude != null) {
                val action = SearchFragmentDirections.actionSearchFragmentToCurrentConditionsFragment("", latitude!!, longitude!!)
                findNavController().navigate(action)
            }
        }.addOnFailureListener {
            Log.d(TAG, "Failed getting current location")
        }
    }

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
            AlertDialog.Builder(requireContext())
                .setMessage("We need permission to give accurate weather data")
                .setCancelable(false)
                .setPositiveButton("Ok") { dialog: DialogInterface, which: Int ->
                    ActivityCompat.requestPermissions(requireActivity(),arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_CODE_COARSE_LOCATION)
                }
                .setNegativeButton("Cancel") { dialog: DialogInterface, which: Int ->
                    dialog.dismiss()
                }
                .create().show();
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_CODE_COARSE_LOCATION)
        }
        runBlocking {
            delay(4000)
            getLocation()
        }
    }

    private fun notificationCondition(){
        if (!IsNotificationActive){
            IsNotificationActive = true
            serviceIntent.putExtra(ELAPSED_TIME, 0)
            requireActivity().startService(serviceIntent)
            binding.notificationBtn.text = getString(R.string.turn_notification_off)
        } else {
            IsNotificationActive = false
            requireActivity().stopService(serviceIntent)
            with(NotificationManagerCompat.from(requireContext())) {
                cancel(1)
            }
            binding.notificationBtn.text = getString(R.string.turn_notification_on)
        }
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = "Weather Channel"
            }
            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object{
        const val CHANNEL_ID = "Weather_Channel"
        const val CHANNEL_NAME = "Weather_Man"
        const val NOTIFICATION_ID = 1
        const val ELAPSED_TIME = "TIME_ELAPSED"
    }

}