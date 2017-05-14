package com.masahirosaito.reconew

import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.masahirosaito.reconew.model.StartData
import java.lang.Thread.sleep
import java.text.SimpleDateFormat
import java.util.*

class RunActivity : AppCompatActivity(), LocationListener, OnMapReadyCallback {
    val locationManager: LocationManager by lazy {
        getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    val supportMapFragment: SupportMapFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
    }

    var map: GoogleMap? = null
    var isRun = false
    var timer: Thread? = null
    val handler: Handler = Handler()

    companion object {
        private const val START_DATA: String = "start_data"
        fun intent(context: Context, startData: StartData): Intent =
                Intent(context, RunActivity::class.java)
                        .putExtra(START_DATA, startData)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_run)

        findViewById(R.id.stop_button).apply {
            setOnClickListener {
                isRun = false
                timer?.interrupt()
            }
        }

        val startTimeText = findViewById(R.id.start_time) as TextView
        val startData: StartData = intent.getParcelableExtra(START_DATA)

        startTimeText.text = SimpleDateFormat("hh:mm:ss").format(Date(startData.time))

        timer = Thread {
            kotlin.run {
                try {
                    val nowTimeText = findViewById(R.id.now_time) as TextView
                    val old = startData.time
                    isRun = true

                    while (isRun) {
                        sleep(1000)
                        handler.post {
                            val now = getNowDate()
                            val time = now - old

                            nowTimeText.text = SimpleDateFormat("mm:ss").format(time)
                        }
                    }
                } catch (e: InterruptedException) {
                    isRun = false
                }
            }
        }

        map?.addMarker(MarkerOptions().apply {
            position(LatLng(startData.longitude, startData.latitude))
            title("START")
        })
    }

    override fun onResume() {
        super.onResume()
        locationStart()
        timer?.start()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap
        map!!.isMyLocationEnabled = true
    }

    fun getNowDate() = System.currentTimeMillis()

    fun locationStart() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0f, this)
    }

    override fun onLocationChanged(location: Location?) {
        map!!.setLocationSource(MyLocationSource(location!!))
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }
}
