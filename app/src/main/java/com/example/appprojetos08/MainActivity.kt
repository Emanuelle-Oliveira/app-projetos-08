package com.example.appprojetos08

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.appprojetos08.controllers.GroupController
import com.example.appprojetos08.controllers.OutputController
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
  private val groupController = GroupController();
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
      HomeScreenStructure()
    }
  }
  @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
  @OptIn(ExperimentalMaterial3Api::class)
  @Composable
  fun HomeScreenStructure() {
    val outputController = OutputController();
    val groupController = GroupController();
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
        defaultElevation = 4.dp
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

