package com.example.appprojetos08.models.sensorGroup
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class SensorGroup(
  var sensorGroupId: Int,
  var sensorId: Int,
  var groupId: Int,
  var setPointId: Int,
  var isActive: Boolean,
  var createdAt: Date,
  var updatedAt: Date,
) : Parcelable
