package com.example.appprojetos08.services.setPoint

import android.util.Log
import com.example.appprojetos08.models.group.Group
import com.example.appprojetos08.models.group.toHashMap
import com.example.appprojetos08.models.setPoint.SetPoint
import com.example.appprojetos08.models.setPoint.toHashMap
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await

class SetPointService {
    private val db = Firebase.firestore

    suspend fun getByGroupId(id: Int): MutableList<SetPoint> = coroutineScope {
        val setPointList = mutableListOf<SetPoint>()

        try {
            val result = db.collection("setPoint").whereEqualTo("groupId", id).get().await()
            for (document in result) {
                val setPointData = document.data
                val setPoint = SetPoint(setPointData)
                setPointList.add(setPoint)
            }
            Log.d("Log", "Lista de set points: $setPointList do grupo de id $id")
        } catch (e: Exception) {
            Log.w("Log", "Erro ao buscar no banco de dados.", e)
        }
        setPointList
    }

    suspend fun create(value: Any, groupId: Int, sensorId: Int): SetPoint? = coroutineScope {

        lateinit var setPoint: SetPoint

        try {
            val result = db.collection("lastSetPointId").get().await()
            val lastId = result.first().data["lastSetPointId"]
            val id = (lastId as Number).toInt() + 1

            if (value is Float) {
                setPoint = SetPoint(id, value, null, groupId, sensorId)
            }

            if (value is Boolean) {
                setPoint = SetPoint(id, null, value, groupId, sensorId)
            }

            db.collection("setPoint")
                .document(id.toString())
                .set(setPoint.toHashMap())

            db.collection("lastSetPointId")
                .document("lastSetPointId")
                .set(hashMapOf(
                    "lastSetPointId" to id
                ))

            Log.d("Log", "Set point criado com sucesso.")
            setPoint
        } catch (e: Exception) {
            Log.w("Log", "Erro ao criar no banco de dados.", e)
            null
        }
    }

    suspend fun update(setPoint: SetPoint): SetPoint? = coroutineScope {
        try {
            val result = db.collection("lastSetPointId").get().await()
            val lastId = result.first().data["lastSetPointId"]
            val id = (lastId as Number).toInt() + 1

            db.collection("setPoint")
                .document(id.toString())
                .set(setPoint.toHashMap())

            db.collection("lastSetPointId")
                .document("lastSetPointId")
                .set(hashMapOf(
                    "lastSetPointId" to id
                ))

            Log.d("Log", "Set point criado com sucesso.")
            setPoint
        } catch (e: Exception) {
            Log.w("Log", "Erro ao criar no banco de dados.", e)
            null
        }
    }

    suspend fun delete(id: Int): Int? = coroutineScope {
        try {
            db.collection("setPoint").document(id.toString()).delete().await()
            Log.d("Log", "Set point deletado com sucesso.")
            id
        } catch (e: Exception) {
            Log.w("Log", "Erro ao deletar no banco de dados.", e)
            null
        }
    }
}