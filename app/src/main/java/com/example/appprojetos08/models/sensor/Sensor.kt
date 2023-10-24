package com.example.appprojetos08.models.sensor
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Sensor(
  var sensorId : Int,
  var databaseUrl: String,
  var measurementUnit: String,
  var isActive: Boolean
) : Parcelable