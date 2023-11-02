package com.example.appprojetos08

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalDrawer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

  private val groupController = GroupController()

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

  private val groupsList = mutableStateOf(emptyList<Group>())

  private fun getGroup() {
    lifecycleScope.launch {
      groupsList.value = GroupService().getAll()
    }
  }
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    getGroup()

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
      Box{
        //NavigationDrawerFun(groupsList)
        HomeScreenStructure(groupsList)
      }

    }
  }


  @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
  @OptIn(ExperimentalMaterial3Api::class)
  @Composable
  fun HomeScreenStructure(groupsList: State<List<Group>>) {
    val addGroupItem = NavigationItem(
      title = "Adicionar Grupo",
      id = 100,
      selectedIcon = Icons.Default.Add,
      unselectedIcon = Icons.Default.Add,
    )

    val items = groupsList.value.map { group ->
      NavigationItem(
        title = group.groupName,
        id = group.groupId,
        selectedIcon = Icons.Filled.Star,
        unselectedIcon = Icons.Outlined.Star,
      )
    }

    // Adicione o item "Adicionar Grupo" à lista de items
    val allItems = items + addGroupItem


    androidx.compose.material.Surface(
      modifier = Modifier.fillMaxSize(),
      color = Color.LightGray
    ) {
      val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
      val scope = rememberCoroutineScope()
      var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
      }
      ModalNavigationDrawer(

        drawerContent = {
          Surface(
            modifier = Modifier
              .fillMaxSize()
              .background(Color.LightGray) // Cor de fundo do menu lateral
          ){
            ModalDrawerSheet {
              Spacer(modifier = Modifier.height(64.dp))
              allItems.forEachIndexed { index, item ->
                NavigationDrawerItem(
                  label = {
                    androidx.compose.material.Text(text = item.title, style = TextStyle(fontSize = 20.sp))
                  },
                  selected = index == selectedItemIndex,
                  onClick = {
//                                            navController.navigate(item.route)

                    selectedItemIndex = index
                    scope.launch {
                      drawerState.close()
                    }
                  },
                  icon = {
                    Icon(
                      imageVector = if (index == selectedItemIndex) {
                        item.selectedIcon
                      } else item.unselectedIcon,
                      contentDescription = item.title
                    )
                  },
                  badge = {
                    item.badgeCount?.let {
                      androidx.compose.material.Text(text = item.badgeCount.toString())
                    }
                  },
                  modifier = Modifier
                    .padding(NavigationDrawerItemDefaults.ItemPadding)
                )


              }
            }
          }

        },
        drawerState = drawerState
      ) {
        Scaffold(
          topBar = {
            TopAppBar(
              title = {
                androidx.compose.material.Text(
                  text = "Smart House",
                  color = Color.White,
                  style = TextStyle(fontSize = 20.sp)
                )
              },
              colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.DarkGray),
              navigationIcon = {
                IconButton(onClick = {
                  scope.launch {
                    drawerState.open()
                  }
                }) {
                  Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Color.White
                  )
                }
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

        ){
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
