package com.example.appprojetos08

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.appprojetos08.models.group.Group
import com.example.appprojetos08.models.output.Output
import com.example.appprojetos08.models.sensor.Sensor
import com.example.appprojetos08.services.group.GroupService
import com.example.appprojetos08.services.output.OutputService
import com.example.appprojetos08.services.sensor.SensorService
import com.example.appprojetos08.ui.theme.AppProjetos08Theme
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {
  /*
  TESTES DAS FUNÇÔES PARA EXEMPLO

  OUTPUT:
  var outputList = mutableListOf<Output>()

  private fun getOutputs() {
    lifecycleScope.launch {
      outputList = OutputService().getAll()
      Log.d("Log", "Lista de saídas: $outputList")
    }
  }

  private fun getOutputsByGroupId(id: Int) {
    lifecycleScope.launch {
      outputList = OutputService().getByGroupId(id)
      Log.d("Log", "Lista de saídas: $outputList")
    }
  }

  private fun updateOutput(output:Output) {
    lifecycleScope.launch {
      var output = OutputService().update(output)
      Log.d("Log", "Saída atualizada: $output")
    }
  }

  SENSOR:
  var sensorList = mutableListOf<Sensor>()

  private fun getSensors() {
    lifecycleScope.launch {
      sensorList = SensorService().getAll()
      Log.d("Log", "Lista de sensores: $sensorList")
    }
  }

  GROUP:
  var groupList = mutableListOf<Group>()
  private fun getGroups() {
    lifecycleScope.launch {
      groupList = GroupService().getAll()
      Log.d("Log", "Lista de grupos: $groupList")
    }
  }

  var group: Group? = null
  private fun getGroup(id: Int) {
    lifecycleScope.launch {
      group = GroupService().getOne(id)
      Log.d("Log", "Grupo: $group")
    }
  }

  private fun createGroup(name: String) {
    lifecycleScope.launch {
      group = GroupService().create(name)
      Log.d("Log", "Grupo: $group")
    }
  }

  private fun updateGroup(group: Group) {
    lifecycleScope.launch {

      val updatedGroup = GroupService().update(group)
      Log.d("Log", "Grupo: updatedGroup")
    }
  }

   private fun deleteGroup(id: Int) {
    lifecycleScope.launch {

      GroupService().delete(id)
      Log.d("Log", "Grupo deletado")
    }
  }
  */

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    //getOutputs()
    //getOutputsByGroupId(1)
    //updateOutput(Output(1, "Lâmpada", "url1", true, 1))

    //getSensors()

    //getGroups()
    //getGroup(0)
    //createGroup("Cozinha")
    //updateGroup(Group(3, "Quarto", true))
    //deleteGroup(2)

    setContent {
      AppProjetos08Theme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          Greeting("Android")
        }
      }
    }
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(
    text = "Hello $name!",
    modifier = modifier
  )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  AppProjetos08Theme {
    Greeting("Android")
  }
}