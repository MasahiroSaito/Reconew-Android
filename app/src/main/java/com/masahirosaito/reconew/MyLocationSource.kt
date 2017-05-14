package com.masahirosaito.reconew

import android.location.Location
import com.google.android.gms.maps.LocationSource

class MyLocationSource(val location: Location): LocationSource {

    override fun deactivate() {
    }

    override fun activate(p0: LocationSource.OnLocationChangedListener?) {
        p0!!.onLocationChanged(location)
    }
}