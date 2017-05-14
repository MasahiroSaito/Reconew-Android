package com.masahirosaito.reconew

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.masahirosaito.reconew.model.StartData
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng

class MainActivity : AppCompatActivity(), LocationListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMyLocationButtonClickListener, LocationSource {

    val locationManager: LocationManager by lazy {
        getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    val supportMapFragment: SupportMapFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
    }

    val mGoogleApiClient: GoogleApiClient by lazy {
        GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build()
    }

    var map: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startButton = findViewById(R.id.start_button)
        startButton.setOnClickListener {
            val now = getNowDate()
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val startData = StartData(now, location.longitude, location.latitude)
            RunActivity.intent(this, startData).let { startActivity(it) }
        }

        supportMapFragment.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()
        mGoogleApiClient.connect()
        locationStart()
        supportMapFragment.getMapAsync { it.isMyLocationEnabled = true }
    }

    override fun onPause() {
        super.onPause()
        mGoogleApiClient.disconnect()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap
        map?.setLocationSource(this)
        map?.isMyLocationEnabled = true
        map?.setOnMyLocationButtonClickListener(this)
    }

    fun getNowDate() = System.currentTimeMillis()

    fun locationStart() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0f, this)
    }

    override fun onLocationChanged(location: Location?) {
        val newLocation = LatLng(location!!.latitude, location.longitude)
        map?.addMarker(MarkerOptions().position(newLocation))
        map?.moveCamera(CameraUpdateFactory.newLatLng(newLocation))
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }

    override fun onConnected(p0: Bundle?) {
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun deactivate() {
    }

    override fun activate(p0: LocationSource.OnLocationChangedListener?) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onMyLocationButtonClick(): Boolean = false
}
