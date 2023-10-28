package com.example.appprojetos08.models.setPoint
import android.os.Parcelable
import com.example.appprojetos08.models.sensor.Sensor
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class SetPoint(
  var setPointId: Int,
  var valueFloat: Float?,
  var valueBoolean: Boolean?,
  var groupId: Int,
  var sensorId: Int,
) : Parcelable {
  constructor(map: Map<String, Any>) : this(
    setPointId = (map["setPointId"] as Number).toInt(),
    valueFloat = (map["valueFloat"] as? Number)?.toFloat(),
    valueBoolean = map["valueBoolean"] as? Boolean,
    groupId = (map["groupId"] as Number).toInt(),
    sensorId = (map["sensorId"] as Number).toInt()
  )
}

fun SetPoint.toHashMap(): HashMap<String, Any> {
  return hashMapOf<String, Any>().apply {
    put("setPointId", setPointId)
    valueFloat?.let { put("valueFloat", it) }
    valueBoolean?.let { put("valueBoolean", it) }
    put("groupId", groupId)
    put("sensorId", sensorId)
  }
}