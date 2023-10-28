package com.example.appprojetos08.services.sensor

import android.util.Log
import com.example.appprojetos08.models.sensor.Sensor
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await

class SensorService {
  val db = Firebase.firestore

  suspend fun getAll(): MutableList<Sensor> = coroutineScope {
    val sensorList = mutableListOf<Sensor>()

    try {
      val result = db.collection("sensor").get().await()
      for (document in result) {
        val sensorData = document.data
        val sensor = Sensor(sensorData)
        sensorList.add(sensor)
      }
      Log.d("Log", "Lista de sensores: $sensorList.")
    } catch (e: Exception) {
      Log.w("Log", "Erro ao buscar no banco de dados.", e)
    }
    sensorList
  }
}