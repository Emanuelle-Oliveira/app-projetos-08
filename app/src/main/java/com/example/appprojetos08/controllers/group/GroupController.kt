package com.example.appprojetos08.controllers.group

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.appprojetos08.models.group.Group
import com.example.appprojetos08.services.group.GroupService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GroupController {

    var groupList by mutableStateOf(listOf<Group>())
    /*var group: Group? = null
    var createdGroup: Group? = null
    var updatedGroup: Group? = null*/

    init {
        getGroups()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getGroups() {
        GlobalScope.launch {
            groupList = GroupService().getAll()
        }
    }

    /*@OptIn(DelicateCoroutinesApi::class)
    fun getGroup(id: Int) {
        GlobalScope.launch {
            group = GroupService().getOne(id)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun createGroup(name: String) {
        GlobalScope.launch {
            createdGroup = GroupService().create(name)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun updateGroup(group: Group) {
        GlobalScope.launch {
            updatedGroup = GroupService().update(group)
        }
    }*/
}