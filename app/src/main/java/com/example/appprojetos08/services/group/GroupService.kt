package com.example.appprojetos08.services.group

import android.util.Log
import com.example.appprojetos08.models.group.Group
import com.example.appprojetos08.models.group.toHashMap
import com.example.appprojetos08.models.output.toHashMap
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await

class GroupService {
  private val db = Firebase.firestore

  suspend fun getAll(): MutableList<Group> = coroutineScope {
    val groupList = mutableListOf<Group>()

    try {
      val result = db.collection("group").get().await()
      for (document in result) {
        val groupData = document.data
        val group = Group(groupData)
        groupList.add(group)
      }
      Log.d("Log", "Lista de grupos: $groupList.")
    } catch (e: Exception) {
      Log.w("Log", "Erro ao buscar no banco de dados.", e)
    }
    groupList
  }

  suspend fun getOne(id: Int): Group? = coroutineScope {
    var group: Group? = null
    try {
      val result = db.collection("group").get().await()
      group = Group(result.first().data)
      Log.d("Log", "Grupo: $group.")
    } catch (e: Exception) {
      Log.w("Log", "Erro ao buscar no banco de dados.", e)
    }
    group
  }

  suspend fun create(name: String): Group? = coroutineScope {
    var group: Group? = null
    try {
      val result = db.collection("lastGroupId").get().await()
      val lastId = result.first().data["lastGroupId"]
      val id = (lastId as Number).toInt() + 1

      group = Group(id, name, false)
      db.collection("group")
        .document(id.toString())
        .set(group.toHashMap())

      db.collection("lastGroupId")
        .document("lastGroupId")
        .set(hashMapOf(
          "lastGroupId" to id
        ))

      Log.d("Log", "Grupo criado com sucesso.")
    } catch (e: Exception) {
      Log.w("Log", "Erro ao criar no banco de dados.", e)
    }
    group
  }
}