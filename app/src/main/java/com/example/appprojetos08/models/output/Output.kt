package com.example.appprojetos08.models.output
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Output(
  var outputId: Int,
  var outputName: String,
  var databaseUrl: String,
  var isActive: Boolean,
  var groupId: Int
) : Parcelable

fun Output.toHashMap(): HashMap<String, Any> {
  return hashMapOf(
    "outputId" to outputId,
    "outputName" to outputName,
    "databaseUrl" to databaseUrl,
    "isActive" to isActive,
    "groupId" to groupId
  )
}