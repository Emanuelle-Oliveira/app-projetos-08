package com.example.appprojetos08.models.output
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Output(
  var outputId: Int,
  var outputName: String,
  var databaseUrl: String,
  var isActive: Boolean,
  var isManual: Boolean,
  var groupId: Int
) : Parcelable {
  constructor(map: Map<String, Any>) : this(
    outputId = (map["outputId"] as Number).toInt(),
    outputName = map["outputName"] as String,
    databaseUrl = map["databaseUrl"] as String,
    isActive = map["isActive"] as Boolean,
    isManual = map["isManual"] as Boolean,
    groupId = (map["groupId"] as Number).toInt()
  )
}

fun Output.toHashMap(): HashMap<String, Any> {
  return hashMapOf(
    "outputId" to outputId,
    "outputName" to outputName,
    "databaseUrl" to databaseUrl,
    "isActive" to isActive,
    "isManual" to isManual,
    "groupId" to groupId
  )
}