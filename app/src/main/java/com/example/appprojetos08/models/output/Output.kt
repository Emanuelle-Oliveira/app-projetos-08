package com.example.appprojetos08.models.output
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Output(
  var outputId: Int,
  var outputName: String,
  var isActive: Boolean,
  var groupId: Int
) : Parcelable
