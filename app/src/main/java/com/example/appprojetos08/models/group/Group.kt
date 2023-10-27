package com.example.appprojetos08.models.group
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class Group(
  var groupId: Int,
  var groupName: String,
  var isActive: Boolean
) : Parcelable
