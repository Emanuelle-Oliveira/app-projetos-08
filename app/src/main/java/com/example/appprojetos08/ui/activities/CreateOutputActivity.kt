package com.example.appprojetos08.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material3.*
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.material3.DropdownMenu
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope

import com.example.appprojetos08.models.group.Group
import com.example.appprojetos08.services.group.GroupService
import com.example.appprojetos08.NavigationItem
import com.example.appprojetos08.controllers.GroupController
import com.example.appprojetos08.controllers.OutputController
import com.example.appprojetos08.controllers.sensor.SensorController

import com.example.appprojetos08.models.output.Output
import com.example.appprojetos08.models.sensor.Sensor
import com.example.appprojetos08.models.setPoint.SetPoint
import com.example.appprojetos08.services.output.OutputService
import com.example.appprojetos08.services.sensor.SensorService
import com.example.appprojetos08.services.setPoint.SetPointService
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction0
import kotlin.reflect.KFunction1
import kotlin.reflect.KFunction4
import kotlin.reflect.KFunction3



class CreateOutputActivity : ComponentActivity() {
    private val outputController = OutputController()
    private val sensorController = SensorController()
    private val groupController = GroupController()
    private val outputService = OutputService()


    private val db = Firebase.firestore

    private val groupsList = mutableStateOf(emptyList<Group>())
    private val outputsList = mutableStateOf(outputController.outputList)

    private val sensorList = mutableStateOf(emptyList<Sensor>())

    private fun getGroup() {
        lifecycleScope.launch {
            groupsList.value = GroupService().getAll()
        }
    }

    private fun getSensor() {
        lifecycleScope.launch {
            sensorList.value = SensorService().getAll()
        }
    }

    private var createdSetPoint: SetPoint? = null
    private fun createSetPoint(value: Any, groupId: Int, sensorId: Int) {
        lifecycleScope.launch {
            createdSetPoint = SetPointService().create(value, groupId, sensorId)
            Log.d("Log", "SetPoint: $createdSetPoint")
        }
    }

    private var updatedOutput: Output? = null
    private fun updateOutput(output: Output) {
        lifecycleScope.launch {
            updatedOutput = OutputService().update(output)
            Log.d("Log", "Saída atualizada: $updatedOutput")
        }
    }

    private fun getOutputByID(selectedGroupId: Int) {
        lifecycleScope.launch {
            outputController.getByGroupID(selectedGroupId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getGroup()
        getSensor()
        setContent {
            NewOutputScreen(sensorList)
        }
    }


    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun NewOutputScreen(
        sensorsList: State<List<Sensor>>
    ) {
        val scope = rememberCoroutineScope()
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        var selectedItemIndex by remember { mutableStateOf(0) }

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
                            .offset(x = (-20).dp)
                            .background(Color.DarkGray) // Cor de fundo do menu lateral
                    ) {
                        ModalDrawerSheet(modifier = Modifier.background(Color.DarkGray)) {
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
                                        if (item.id == 101) {
                                            outputController.getOutputs()
                                            scope.launch {
                                                drawerState.close()
                                            }
                                        } else {
                                            selectedItemIndex = index
                                            selectedGroupId = item.id
                                            getOutputByID(selectedGroupId)
                                            scope.launch {
                                                drawerState.close()
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
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Adicionar Saída",
                                tint = Color.DarkGray
                            )
                        }
                    }
                ) {
                    // Fundo preto da tela
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        color = Color.DarkGray,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        InputsCreateOutput(sensorsList)
                    }
                }
            }
        }
    }

    @Composable
    fun InputsCreateOutput(
        sensorsList: State<List<Sensor>>
    ) {
        val textValueNameOutput = remember { mutableStateOf("") }
        var selectedSensor by remember { mutableStateOf("") }
        // var selectedSensorId by remember { mutableStateOf(null) }
        val textField1Value = remember { mutableStateOf("") }
        val textField2Value = remember { mutableStateOf("") }
        var selectedSensorId by remember { mutableStateOf<Int?>(null) }

        //val outputElements = remember { mutableStateListOf<OutputElement>() }

        var isSwitchOn by remember { mutableStateOf(false) }

        var quantity: Float = 0.0F
        //val setPointList = remember { mutableStateListOf<ItemCreate>() }


        //função para customizar a cor do text field
        val customTextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.White, // Define a cor da borda quando o campo está em foco
            unfocusedBorderColor = Color.White, // Define a cor da borda quando o campo não está em foco
            disabledBorderColor = Color.Magenta, // Define a cor da borda quando o campo está desativado
            backgroundColor = Color.Black,
            leadingIconColor = Color.LightGray,
            placeholderColor = Color.White,
            textColor = Color.White
        )

        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {


            OutlinedTextField(
                value = textValueNameOutput.value,
                onValueChange = { textValueNameOutput.value = it },
                label = { Text("Nome", color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                colors = customTextFieldColors
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {


            }

            // Lista de itens adicionados
            /*LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {


                items(setPointList) { setPoint ->
                    // Exibir informações do item adicionado (nome do sensor e valor)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .height(60.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White,
                            contentColor = Color.DarkGray
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 100.dp
                        ),
                        content = {
                            Text(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(top = 10.dp),
                                text = "Sensor: ${setPoint.sensorName}, Valor: ${setPoint.value}",
                                color = Color.Black,
                                fontSize = 16.sp
                            )
                        }
                    )
                }


            }*/

            Button(
                onClick = {
                    /*setPointList.forEach{ item ->
                        var item =  createSetPoint( item.value, 0 , item.sensorId  )
                    }*/




                },
                colors = ButtonDefaults.buttonColors(contentColor = Color.White, backgroundColor = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally) // Corrige o tipo para Alignment.Horizontal
                    .padding(16.dp)

            ) {
                Text(text = "Salvar", color = Color.White)
            }
        }
    }


}