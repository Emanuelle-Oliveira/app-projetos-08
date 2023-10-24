package com.example.appprojetos08.models.setPoint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class SetPoint(
  var setPointId: Int,
  var value: Float,
  var sensorGroupId: Int,
  var createdAt: Date,
  var updatedAt: Date,
) : Parcelable
