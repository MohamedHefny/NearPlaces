package com.mohamedhefny.nearplaces.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.mohamedhefny.nearplaces.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homePlacesAdapter: HomePlacesAdapter

    private val LOCATION_CHECHING_INTERVAL: Long = 5000 //ms
    private val LOCATION_PERMISSION_RQ by lazy { 101 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        homePlacesAdapter = HomePlacesAdapter(ArrayList())

        home_recycler.adapter = homePlacesAdapter

        if (homeViewModel.getUpdatingMode(this) == 1)
            toolbar_home_update_mode.setText(R.string.places_mode_single)

        listenForUpdateMode()

        if (isLocationPermissionGranted())
            observeLocation()
        else
            requestLocationPermission()
    }

    /**
     * This method is to observe places data and bind it to recyclerView
     */
    private fun observePlaces(location: Location) {
        homeViewModel.getPlaces(location).observe(this, Observer {

            home_progressbar.visibility = View.GONE
            home_progress_tip.visibility = View.GONE
            home_state_info_img.visibility = View.GONE

            if (it != null)
                if (it.isNotEmpty())
                    homePlacesAdapter.setPlacesList(it)
                else
                    updateUiStatus(R.string.data_not_found, R.drawable.ic_no_data_found)
            else //Places data is null
                updateUiStatus(R.string.something_wrong, R.drawable.ic_cloud_off)
        })
    }

    /**
     * Observe user's current location to update venues in a Realtime mode.
     */
    private fun observeLocation() {
        val locationRequest = LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = LOCATION_CHECHING_INTERVAL
            fastestInterval = LOCATION_CHECHING_INTERVAL.div(2)
        }

        LocationServices.getFusedLocationProviderClient(this)
            .requestLocationUpdates(locationRequest, object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult?) {
                    if (p0?.lastLocation != null) {
                        if (homePlacesAdapter.itemCount == 0)
                            observePlaces(p0.lastLocation)
                        else
                            homeViewModel.newLocation(p0.lastLocation)

                        if (homeViewModel.getUpdatingMode(this@HomeActivity) == 1) //If single update mode
                            stopObserveLocation(this)
                    }
                }
            }, Looper.myLooper())
    }

    private fun stopObserveLocation(locationCallback: LocationCallback) =
        LocationServices.getFusedLocationProviderClient(this)
            .removeLocationUpdates(locationCallback)

    /**
     *Listen for user preferred mode changes.
     */
    private fun listenForUpdateMode() =
        toolbar_home_update_mode.setOnClickListener {
            when (homeViewModel.getUpdatingMode(this)) {
                0 -> {//Switch to Single update mode.
                    homeViewModel.changeUpdatingMode(this, 1)
                    toolbar_home_update_mode.setText(R.string.places_mode_single)
                }
                1 -> {//Switch to Realtime mode.
                    homeViewModel.changeUpdatingMode(this, 0)
                    toolbar_home_update_mode.setText(R.string.places_mode_realtime)
                    observeLocation()
                }
            }
        }

    /**
     * Handel error, and update a view to the user.
     *
     * @param statusMsg is a string resource id of error message
     * @param statusImg is a drawable resource id of error image
     */
    private fun updateUiStatus(statusMsg: Int, statusImg: Int) {
        home_progress_tip.setText(statusMsg)
        home_state_info_img.setImageResource(statusImg)
        home_progress_tip.visibility = View.VISIBLE
        home_state_info_img.visibility = View.VISIBLE
    }

    /**
     * @return True if the location permissions granted and False otherwise.
     */
    private fun isLocationPermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        )
            return false

        return true
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            LOCATION_PERMISSION_RQ
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_RQ)
            if (isLocationPermissionGranted())
                observeLocation()
            else {
                home_progressbar.visibility = View.GONE
                updateUiStatus(R.string.something_wrong, R.drawable.ic_cloud_off)
                Toast.makeText(
                    this, R.string.permission_required_location, Toast.LENGTH_LONG
                ).show()
            }
    }
}