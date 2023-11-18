package com.example.appprojetos08.ui.activities.group

import android.annotation.SuppressLint
import android.content.Intent
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.appprojetos08.NavigationItem
import com.example.appprojetos08.controllers.GroupController
import com.example.appprojetos08.controllers.OutputController
import com.example.appprojetos08.controllers.sensor.SensorController
import com.example.appprojetos08.models.group.Group
import com.example.appprojetos08.services.group.GroupService
import com.example.appprojetos08.services.output.OutputService
import com.example.appprojetos08.ui.activities.group.ui.theme.AppProjetos08Theme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material3.*
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.example.appprojetos08.MainActivity
import com.example.appprojetos08.models.output.Output
import com.example.appprojetos08.models.sensor.Sensor
import com.example.appprojetos08.models.setPoint.SetPoint
import com.example.appprojetos08.services.sensor.SensorService
import com.example.appprojetos08.services.setPoint.SetPointService
import kotlin.text.Typography.section


data class ItemCreate (
    val sensorId: Int,
    val sensorName: String,
    val value: String
)

data class ItemOutput (
    val outputId: Int,
    val outputName: String
)

class CreateGroupActivity : ComponentActivity() {
    private val outputController = OutputController()
    private val sensorController = SensorController()
    private val groupController = GroupController()
    private val outputService = OutputService()


    private val db = Firebase.firestore

    private val groupsList = mutableStateOf(emptyList<Group>())
    private val sensorsList = mutableStateOf(emptyList<Sensor>())
    var outputList = mutableListOf<Output>()

    private fun getGroup() {
        lifecycleScope.launch {
            groupsList.value = GroupService().getAll()
        }
    }

    private fun getOutputByID(selectedGroupId: Int) {
        lifecycleScope.launch {
            outputController.getByGroupID(selectedGroupId)
        }
    }

    private fun getSensor() {
        lifecycleScope.launch {
            sensorsList.value = SensorService().getAll()
        }
    }

    private fun getOutputs() {
        lifecycleScope.launch {
            outputList = OutputService().getAll()
        }
    }

    private var createdSetPoint: SetPoint? = null
    private fun createSetPoint(value: Any, groupId: Int, sensorId: Int) {
        lifecycleScope.launch {
            createdSetPoint = SetPointService().create(value, groupId, sensorId)
        }
    }

    var group: Group? = null
    private fun createGroup(name: String) {
        lifecycleScope.launch {
            group = GroupService().create(name)
        }
    }

    private fun updateOutput(output:Output) {
        lifecycleScope.launch {
            var output = OutputService().update(output)
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getGroup()
        getSensor()
        getOutputs()
        setContent {
            NewGroupScreen()
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun NewGroupScreen(){
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
                    androidx.compose.material.Surface(
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
                    }
                ) {
                    // Fundo preto da tela
                    Spacer(modifier = Modifier.height(8.dp))
                    androidx.compose.material.Surface(
                        color = Color.DarkGray,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        ElementsCreateGroup()

                    }
                }
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    @Composable
    fun ElementsCreateGroup(){
        val textField1Value = remember { mutableStateOf("") }
        val textField2Value = remember { mutableStateOf("") }
        var selectedSensorId by remember { mutableStateOf<Int?>(null) }
        var selectedOutputId by remember { mutableStateOf<Int?>(null) }

        //val outputElements = remember { mutableStateListOf<OutputElement>() }

        var isSwitchOn by remember { mutableStateOf(false) }

        var quantity: Float = 0.0F
        val setPointList = remember { mutableStateListOf<ItemCreate>() }
        val dropdownOutputList = remember { mutableStateListOf<ItemOutput>() }


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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Box(
                        modifier = Modifier
                            //.weight(2f)
                            .width(200.dp)
                            .background(color = Color.Black)
                            .border(1.dp, Color.White, RoundedCornerShape(4.dp))
                    ){
                        OutputDropdown(
                            selectedOutputId = selectedOutputId,
                            outputList = outputList,
                            onOutputSelected = { outputId ->
                                selectedOutputId = outputId
                            }
                        )
                    }


                    androidx.compose.material.Button(
                        onClick = {
                            selectedOutputId?.let { outputId ->
                                // Verificar se já existe um item para o output selecionado
                                if (dropdownOutputList.none { it.outputId == outputId }) {
                                    outputList.find { it.outputId == outputId }?.let { output ->
                                        val outputName = output.outputName

                                        dropdownOutputList.add(ItemOutput(outputId, outputName))
                                    }
                                }
                            }


                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .padding(start = 8.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)

                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "Add FAB",
                            tint = Color.White,
                        )

                    }
                }

            }

            items(dropdownOutputList) { output ->
                // Exibir informações do item adicionado (nome da saida)
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
                            text = "Saída: ${output.outputName}",
                            color = Color.Black,
                            fontSize = 16.sp
                        )
                    }
                )
            }

            item{
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(130.dp)
                            .background(color = Color.Black)
                            .border(1.dp, Color.White, RoundedCornerShape(4.dp))
                    ) {
                        SensorDropdown(
                            selectedSensorId = selectedSensorId,
                            sensorsList = sensorsList.value,
                            onSensorSelected = { sensorId ->
                                selectedSensorId = sensorId
                            }
                        )
                    }

                    if (selectedSensorId == 1) {
                        androidx.compose.material.OutlinedTextField(
                            value = textField1Value.value,
                            onValueChange = { textField1Value.value = it },
                            label = { Text("ºC", color = Color.White) },
                            modifier = Modifier
                                .width(100.dp)
                                .padding(start = 16.dp),
                            colors = customTextFieldColors
                        )
                    }

                    if (selectedSensorId == 2) {
                        androidx.compose.material.OutlinedTextField(
                            value = textField2Value.value,
                            onValueChange = { textField2Value.value = it },
                            label = { Text("0% - 100%", color = Color.White) },
                            modifier = Modifier
                                .width(100.dp)
                                .padding(start = 10.dp),
                            colors = customTextFieldColors
                        )
                    }

                    if (selectedSensorId === 3) {
                        androidx.compose.material.Switch(
                            checked = isSwitchOn,
                            onCheckedChange = { isSwitchOn = it },
                            modifier = Modifier
                                .padding(start = 8.dp)
                        )
                    }

                    androidx.compose.material.Button(
                        onClick = {
                            selectedSensorId?.let { sensorId ->
                                // Verificar se já existe um item para o sensor selecionado
                                if (setPointList.none { it.sensorId == sensorId }) {
                                    sensorsList.value.find { it.sensorId == sensorId }?.let { sensor ->
                                        val sensorName = sensor.dataType
                                        val value = when (sensorId) {
                                            1 -> textField1Value.value
                                            2 -> textField2Value.value
                                            3 -> isSwitchOn.toString()
                                            else -> ""
                                        }

                                        setPointList.add(ItemCreate(sensorId, sensorName, value))
                                    }
                                }
                            }

                            // Limpar os campos de texto
                            textField1Value.value = ""
                            textField2Value.value = ""

                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .padding(start = 8.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)

                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "Add FAB",
                            tint = Color.White,
                        )

                    }
                }

            }

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

            item{
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ){
                    Button(
                        onClick = {
                            /*setPointList.forEach{ item ->
                                var item =  createSetPoint( item.value, 0 , item.sensorId  )
                            }*/




                        },
                        colors = ButtonDefaults.buttonColors(contentColor = Color.White, backgroundColor = Color.Black),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .padding(16.dp)

                    ) {
                        Text(text = "Salvar", color = Color.White)
                    }
                }
            }
            item{
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ){
                    Button(
                        onClick = {
                            context.startActivity(Intent(context, MainActivity::class.java))
                        },
                        colors = ButtonDefaults.buttonColors(contentColor = Color.White, backgroundColor = Color.Black),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .padding(16.dp)

                    ) {
                        Text(text = "Voltar", color = Color.White)
                    }
                }
            }
        }
    }


    @Composable
    fun SensorDropdown(
        selectedSensorId: Int?,
        sensorsList: List<Sensor>,
        onSensorSelected: (Int?) -> Unit
    ) {
        var expanded by remember { mutableStateOf(false) }

        Column {
            Text(
                text = sensorsList.find { it.sensorId == selectedSensorId }?.dataType ?: "Sensor",
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { expanded = true }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .wrapContentHeight()
                    .border(1.dp, Color.Black)
            ) {
                sensorsList.forEach { sensor ->
                    androidx.compose.material.DropdownMenuItem(
                        onClick = {
                            onSensorSelected(sensor.sensorId)
                            expanded = false
                        }
                    ) {
                        Text(text = sensor.dataType)
                    }
                }
            }
        }
    }

    @Composable
    fun OutputDropdown(
        selectedOutputId: Int?,
        outputList: List<Output>,
        onOutputSelected: (Int?) -> Unit
    ){
        var expanded by remember { mutableStateOf(false) }

        Column {
            Text(
                text = outputList.find { it.outputId == selectedOutputId }?.outputName ?: "Output",
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { expanded = true }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .wrapContentHeight()
                    .border(1.dp, Color.Black)
            ) {
                outputList.forEach { output ->
                    androidx.compose.material.DropdownMenuItem(
                        onClick = {
                            onOutputSelected(output.outputId)
                            expanded = false
                        }
                    ) {
                        Text(text = output.outputName)
                    }
                }
            }
        }

    }
}