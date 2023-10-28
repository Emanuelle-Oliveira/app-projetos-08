package com.example.appprojetos08.models.sensor
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Sensor(
  var sensorId: Int,
  var databaseUrl: String,
  var dataType: String,
  var measurementUnit: String,
  var currentValueFloat: Float,
  var currentValueBoolean: Boolean
) : Parcelable

fun Sensor.toHashMap(): HashMap<String, Any> {
  return hashMapOf(
    "sensorId" to sensorId,
    "databaseUrl" to databaseUrl,
    "dataType" to dataType,
    "measurementUnit" to measurementUnit,
    "currentValueFloat" to currentValueFloat,
    "currentValueBoolean" to currentValueBoolean
  )
}