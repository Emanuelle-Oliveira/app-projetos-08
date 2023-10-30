package com.example.appprojetos08

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.appprojetos08.controllers.sensor.SensorController
import com.example.appprojetos08.controllers.group.GroupController
import com.example.appprojetos08.controllers.output.OutputController
import com.example.appprojetos08.models.group.Group
import com.example.appprojetos08.models.output.Output
import com.example.appprojetos08.models.setPoint.SetPoint
import com.example.appprojetos08.services.group.GroupService
import com.example.appprojetos08.services.output.OutputService
import com.example.appprojetos08.services.setPoint.SetPointService
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {

  private val outputController = OutputController()

  private val sensorController = SensorController()

  private val groupController = GroupController();

  /*
  OUTPUT:

  private var outputList = mutableListOf<Output>()
  private fun getOutputsByGroupId(id: Int) {
    lifecycleScope.launch {
      outputList = OutputService().getByGroupId(id)
      Log.d("Log", "Lista de saídas: $outputList")
    }
  }

  private var updatedOutput : Output? = null
  private fun updateOutput(output:Output) {
    lifecycleScope.launch {
      updatedOutput = OutputService().update(output)
      Log.d("Log", "Saída atualizada: $updatedOutput")
    }
  }
  */
  /*
  GROUP:

  private var group: Group? = null
  private fun getGroup(id: Int) {
    lifecycleScope.launch {
      group = GroupService().getOne(id)
      Log.d("Log", "Grupo: $group")
    }
  }

  private var createdGroup: Group? = null
  private fun createGroup(name: String) {
    lifecycleScope.launch {
      createdGroup = GroupService().create(name)
      Log.d("Log", "Grupo: $group")
    }
  }

  private var updatedGroup: Group? = null
  private fun updateGroup(group: Group) {
    lifecycleScope.launch {
      updatedGroup = GroupService().update(group)
      Log.d("Log", "Grupo: $updatedGroup")
    }
  }

  private var deletedId: Int? = null
  private fun deleteGroup(id: Int) {
    lifecycleScope.launch {
      deletedId = GroupService().delete(id)
      Log.d("Log", "Grupo deletado")
    }
  }
  */
  /*
  SET POINT:

  private var setPointList = mutableListOf<SetPoint>()
  private fun getSetPointsByGroupId(id: Int) {
    lifecycleScope.launch {
      setPointList = SetPointService().getByGroupId(id)
      Log.d("Log", "Lista de set points: $setPointList")
    }
  }

  private var createdSetPoint: SetPoint? = null
  private fun createSetPoint(value: Any, groupId: Int, sensorId: Int) {
    lifecycleScope.launch {
      createdSetPoint = SetPointService().create(value, groupId, sensorId)
      Log.d("Log", "SetPoint: $createdSetPoint")
    }
  }

  private var updatedSetPoint: SetPoint? = null
  private fun updateSetPoint(setPoint: SetPoint) {
    lifecycleScope.launch {
      updatedSetPoint = SetPointService().update(setPoint)
      Log.d("Log", "SetPoint: $updatedSetPoint")
    }
  }

  private var deletedId: Int? = null
  private fun deleteSetPoint(id: Int) {
    lifecycleScope.launch {
      deletedId = SetPointService().delete(id)
      Log.d("Log", "SetPoint deletado")
    }
  }
  */

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    //getOutputsByGroupId(1)
    //updateOutput(Output(1, "Lâmpada", "url1", true, 1))

    //getGroup(0)
    //createGroup("Cozinha")
    //updateGroup(Group(3, "Quarto", true))
    //deleteGroup(2)

    //getSetPointsByGroupId(1)
    //createSetPoint(30F, 0, 0)
    //updateSetPoint(SetPoint(4, 30F, null, 0, 0))
    //deleteSetPoint(0)

    setContent {
      HomeScreenStructure()
    }
  }


  @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
  @OptIn(ExperimentalMaterial3Api::class)
  @Composable
  fun HomeScreenStructure() {
    Scaffold(
      topBar = {
        TopAppBar(
          title = {},
          colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.DarkGray),
          navigationIcon = {
            Icon(
              imageVector = Icons.Default.Menu,
              contentDescription = "Menu",
              tint = Color.White
            )
          }
        )
      },
      floatingActionButton = {
        FloatingActionButton(
          onClick = {

          },
          containerColor = Color.White,
          contentColor = Color.DarkGray,
        ) {
          Icon(imageVector = Icons.Default.Add, contentDescription = "Adicionar Saída", tint = Color.DarkGray)
        }
      }
    ) {
      // Fundo preto da tela
      Surface(
        color = Color.DarkGray,
        modifier = Modifier.fillMaxSize()
      ) {
        LazyColumn(
          contentPadding = PaddingValues(16.dp)
        ) {
          // Divide os cards em duas colunas
          items(outputController.outputList.chunked(2)) { chunkedOutputs ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
            ) {
              for (output in chunkedOutputs) {
                OutputCard(output = output)
              }
            }
          }
        }
      }
    }
  }
  @Composable
  fun OutputCard(output: Output) {

    var groupOfOutputCard = "Sem grupo"
    Log.i("1", "groupController: ${groupController.groupList}")

    groupController.groupList.forEach { group ->
      Log.i("1", "groupId output: ${output.groupId} e groupId Group: ${group.groupId}")
      if(output.groupId == group.groupId){
        Log.i("1", "Entrou if")
        groupOfOutputCard = group.groupName
      }
    }
    Card(
      modifier = Modifier
        .width(185.dp)
        .padding(10.dp)
        .height(200.dp),
      shape = RoundedCornerShape(10.dp),
      colors = CardDefaults.cardColors(
        containerColor = Color.White,
        contentColor = Color.DarkGray
      ),
      elevation = CardDefaults.cardElevation(
        defaultElevation = 100.dp
      )
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(16.dp),

      ) {
        Icon(
          painter = painterResource(R.drawable.power_settings_new),
          contentDescription = "Icone desligar/ligar saída",
          modifier = Modifier
            .size(110.dp)
            .padding(10.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "${output.outputName}", // Substitua pelo nome do dispositivo
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "${groupOfOutputCard}", // Substitua pelo status do dispositivo
        )
      }
    }
  }
}

