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
import com.example.appprojetos08.controllers.sensor.SensorController
import com.example.appprojetos08.controllers.group.GroupController
import com.example.appprojetos08.controllers.output.OutputController
import com.example.appprojetos08.models.output.Output
import kotlinx.coroutines.*


class MainActivity : ComponentActivity() {

  private val sensorController = SensorController()

  private val groupController = GroupController();

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

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

