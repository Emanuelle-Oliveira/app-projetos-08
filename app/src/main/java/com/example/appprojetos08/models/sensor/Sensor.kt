package com.example.appprojetos08.models.sensor
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Sensor(
  var sensorId: Int,
  var databaseUrl: String,
  var dataType: String,
  var measurementUnit: String?,
  var currentValueFloat: Float?,
  var currentValueBoolean: Boolean?
) : Parcelable {
  constructor(map: Map<String, Any>) : this(
    sensorId = (map["sensorId"] as Number).toInt(),
    databaseUrl = map["databaseUrl"] as String,
    dataType = map["dataType"] as String,
    measurementUnit = map["measurementUnit"] as? String,
    currentValueFloat = (map["currentValueFloat"] as? Number)?.toFloat(),
    currentValueBoolean = map["currentValueBoolean"] as? Boolean
  )
}

fun Sensor.toHashMap(): HashMap<String, Any> {
  return hashMapOf<String, Any>().apply {
    put("sensorId", sensorId)
    put("databaseUrl", databaseUrl)
    put("dataType", dataType)
    measurementUnit?.let { put("measurementUnit", it) }
    currentValueFloat?.let { put("currentValueFloat", it) }
    currentValueBoolean?.let { put("currentValueBoolean", it) }
  }
}