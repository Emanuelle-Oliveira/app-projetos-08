package com.example.appprojetos08.services.output

import android.util.Log
import com.example.appprojetos08.models.output.Output
import com.example.appprojetos08.models.output.toHashMap
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await

class OutputService {
  val db = Firebase.firestore

  suspend fun getAll(): MutableList<Output> = coroutineScope {
    val outputList = mutableListOf<Output>()

    try {
      val result = db.collection("output").get().await()
      for (document in result) {
        val outputData = document.data
        val output = Output(outputData)
        outputList.add(output)
      }
      Log.d("Log", "Lista de saídas: $outputList.")
    } catch (e: Exception) {
      Log.w("Log", "Erro ao buscar no banco de dados.", e)
    }
    outputList
  }

  suspend fun getByGroupId(id: Int): MutableList<Output> = coroutineScope {
    val outputList = mutableListOf<Output>()

    try {
      val result = db.collection("output").whereEqualTo("groupId", id).get().await()
      for (document in result) {
        val outputData = document.data
        val output = Output(outputData)
        outputList.add(output)
      }
      Log.d("Log", "Lista de saídas: $outputList do grupo de id $id")
    } catch (e: Exception) {
      Log.w("Log", "Erro ao buscar no banco de dados.", e)
    }
    outputList
  }

  suspend fun update(output: Output): Output = coroutineScope {
    try {
      db.collection("output")
        .document(output.outputId.toString())
        .set(output.toHashMap())
      Log.d("Log", "Saída atualizada com sucesso.")
    } catch (e: Exception) {
      Log.w("Log", "Erro ao atualizar no banco de dados.", e)
    }
    output
  }
}