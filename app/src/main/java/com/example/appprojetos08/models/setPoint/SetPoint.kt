package com.example.appprojetos08.models.setPoint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class SetPoint(
  var setPointId: Int,
  var valueFloat: Float,
  var valueBoolean: Boolean,
  var groupId: Int,
  var sensorId: Int,
) : Parcelable
