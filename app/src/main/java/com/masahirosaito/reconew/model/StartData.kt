package com.masahirosaito.reconew.model

import android.os.Parcel
import android.os.Parcelable

data class StartData(val time: Long,
                     val longitude: Double,
                     val latitude: Double) : Parcelable {

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<StartData> = object : Parcelable.Creator<StartData> {
            override fun createFromParcel(source: Parcel?): StartData = source!!.run {
                StartData(readLong(), readDouble(), readDouble())
            }

            override fun newArray(size: Int): Array<StartData> = emptyArray()
        }
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.run {
            writeLong(time)
            writeDouble(longitude) // 緯度
            writeDouble(latitude) // 経度
        }
    }

    override fun describeContents(): Int = 0
}