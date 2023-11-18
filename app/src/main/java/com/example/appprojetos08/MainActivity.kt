package com.example.appprojetos08

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.TopCenter
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import com.example.appprojetos08.controllers.sensor.SensorController
import com.example.appprojetos08.services.group.GroupService
import com.example.appprojetos08.services.output.OutputService
import com.example.appprojetos08.services.sensor.SensorService
import com.example.appprojetos08.ui.activities.CreateOutputActivity
import com.example.appprojetos08.ui.activities.group.CreateGroupActivity
import com.example.appprojetos08.ui.theme.AppProjetos08Theme
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.*


class MainActivity : ComponentActivity() {
  private val outputController = OutputController()
  private val sensorController = SensorController()
  private val groupController = GroupController()
  private val outputService = OutputService()
  private val db = Firebase.firestore

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
  private val groupsList = mutableStateOf(emptyList<Group>())
  private val outputsList = mutableStateOf(outputController.outputList)
  private fun getGroup() {
    lifecycleScope.launch {
      groupsList.value = GroupService().getAll()
    }
  }
  private fun getOutputByID(selectedGroupId : Int) {
    lifecycleScope.launch {
      outputController.getByGroupID(selectedGroupId)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    getGroup()
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
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var selectedItemIndex by remember { mutableStateOf(0) }

    val context = LocalContext.current


    // 2. Crie a função para o conteúdo do drawer
    val addGroupItem = NavigationItem(
      title = "Adicionar Grupo",
      id = 100,
      selectedIcon = Icons.Default.Add,
      unselectedIcon = Icons.Default.Add,
    )

    val clearFilters = NavigationItem(
      title = "Limpar Filtros",
      id = 101,
      selectedIcon = Icons.Default.Clear, // Ícone para adicionar
      unselectedIcon = Icons.Default.Clear, // Ícone para adicionar
    )

    val items = groupsList.value.map { group ->
      NavigationItem(
        title = group.groupName,
        id = group.groupId,
        selectedIcon = Icons.Filled.Star,
        unselectedIcon = Icons.Outlined.Star,
      )
    }
    val allItems = items + addGroupItem + clearFilters

    androidx.compose.material.Surface(
      modifier = Modifier.fillMaxSize()
        .background(Color.DarkGray),
      color = Color.DarkGray
    ) {
      val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
      val scope = rememberCoroutineScope()
      var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
      }
      var selectedGroupId by rememberSaveable { mutableStateOf(0) }

      ModalNavigationDrawer(
        modifier = Modifier.background(Color.DarkGray),
        drawerContent = {
          Surface(
            modifier = Modifier
              .offset(x= (-20).dp)
              .background(Color.DarkGray) // Cor de fundo do menu lateral
          ) {
            ModalDrawerSheet (modifier = Modifier.background(Color.DarkGray)){
              Spacer(modifier = Modifier.height(64.dp))
              allItems.forEachIndexed { index, item ->
                NavigationDrawerItem(
                  label = {
                    androidx.compose.material.Text(
                      text = item.title,
                      style = TextStyle(fontSize = 20.sp)
                    )
                  },
                  selected = index == selectedItemIndex,
                  onClick = {
                    when (item.id) {
                      101 -> {
                        outputController.getOutputs()
                        scope.launch {
                          drawerState.close()
                        }
                      }
                      100 -> {
                        context.startActivity(Intent(context, CreateGroupActivity::class.java))
                        scope.launch {
                          drawerState.close()
                        }
                      }
                      else -> {
                        selectedItemIndex = index
                        selectedGroupId = item.id
                        getOutputByID(selectedGroupId)
                        scope.launch {
                          drawerState.close()
                        }
                      }
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
              title = {},
              modifier = Modifier.height(48.dp),
              colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent),
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
        ) {
          // Fundo preto da tela
          Spacer(modifier = Modifier.height(8.dp))
          Surface(
            color = Color.DarkGray,
            modifier = Modifier.fillMaxSize()
          ) {
            LazyColumn(
              contentPadding = PaddingValues(18.dp)
            ) {
              // Divide os cards em duas colunas
              items(outputController.outputList.chunked(2)) { chunkedOutputs ->
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, bottom = 12.dp)
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
    var isActive by remember { mutableStateOf(output.isActive) }
    Log.i("1", "groupController: ${groupController.groupList}")

    groupController.groupList.forEach { group ->
      Log.i("1", "groupId output: ${output.groupId} e groupId Group: ${group.groupId}")
      if (output.groupId == group.groupId) {
        Log.i("1", "Entrou if")
        groupOfOutputCard = group.groupName
      }
    }
    val docRef = db.collection("outputs").document(output.outputId.toString())
    val outputState = remember { mutableStateOf(output) }

    LaunchedEffect(Unit) {
      val listener = docRef.addSnapshotListener { documentSnapshot, e ->
        if (e != null) {
          Log.w(TAG, "listen:error", e)
          return@addSnapshotListener
        }
        if (documentSnapshot != null && documentSnapshot.exists()) {
          val updatedOutput = documentSnapshot.toObject(Output::class.java)
          if (updatedOutput != null) {
            outputState.value = updatedOutput
          }
        }
      }
//      onDispose {
//        listener.remove()
//      }
    }
    Card(
      modifier = Modifier
        .width(185.dp)
        .padding(10.dp)
        .height(200.dp)
        .clickable {
          isActive = !isActive
          lifecycleScope.launch {
            OutputService().toggleOutputActiveStatus(output.outputId.toString().toInt(), isActive)
          }
        },
      shape = RoundedCornerShape(15.dp),
      colors = CardDefaults.cardColors(
        containerColor = Color.White,
        contentColor = Color.DarkGray
      ),
      elevation = CardDefaults.cardElevation(
        defaultElevation = 100.dp
      )
    ) {
      Box(
        modifier = Modifier
          .padding(start = 120.dp)
        //.align(Alignment.TopEnd)
      ){
        MenuTresPontos()
      }
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(12.dp)
          //.height(10.dp)
          .absoluteOffset(x=0.dp, y= (-20).dp)
      ) {
        Icon(
          painter = painterResource(R.drawable.power_settings_new),
          contentDescription = "Icone desligar/ligar saída",
          modifier = Modifier
            .size(89.dp)
            .padding(8.dp)
            .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(1.dp))
        Text(
          text = "${output.outputName}", // Substitua pelo nome do dispositivo
        )
        Spacer(modifier = Modifier.height(1.dp))
        Text(
          text = "${groupOfOutputCard}",
        )
        Canvas(
          modifier = Modifier.fillMaxSize()
        ) {
          val radius = 18f
          val x = size.width - radius * 1.5f
          val y = (radius * 1.5f)
          val color = if (isActive) Color.Green else Color.Red
          drawCircle(color, center = Offset(x + 6.dp.toPx(), y), radius = radius)
        }
      }
    }
  }
}

@Composable
fun MenuTresPontos() {
  val contexto: Context = LocalContext.current
  var isOpened: Boolean by remember { mutableStateOf(false) }

  Box(modifier = Modifier
    .wrapContentSize(Alignment.TopEnd)
    .padding(start = 16.dp)
  ) {
    IconButton(onClick = { isOpened = !isOpened }) {
      Icon(
        imageVector = Icons.Default.MoreVert,
        contentDescription = "More vert"
      )
    }
    DropdownMenu(expanded = isOpened, onDismissRequest = { isOpened = false }) {
      DropdownMenuItem(text = { Text(text = "Editar Saída") }, onClick = {
        Toast.makeText(contexto, "Folhas Secas", Toast.LENGTH_LONG).show()
        isOpened = !isOpened
      })
      DropdownMenuItem(text = { Text(text = "Editar grupo") }, onClick = {
        Toast.makeText(contexto, "Em breve.....", Toast.LENGTH_LONG).show()
        isOpened = !isOpened
      })
    }
  }
}