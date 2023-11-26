package com.example.appprojetos08.ui.activities.group

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.appprojetos08.MainActivity
import com.example.appprojetos08.NavigationItem
import com.example.appprojetos08.controllers.OutputController
import com.example.appprojetos08.models.group.Group
import com.example.appprojetos08.models.output.Output
import com.example.appprojetos08.models.sensor.Sensor
import com.example.appprojetos08.models.setPoint.SetPoint
import com.example.appprojetos08.services.group.GroupService
import com.example.appprojetos08.services.output.OutputService
import com.example.appprojetos08.services.sensor.SensorService
import com.example.appprojetos08.services.setPoint.SetPointService
import kotlinx.coroutines.launch

class UpdateGroupActivity : ComponentActivity() {
    private val outputController = OutputController()

    private val groupsList = mutableStateOf(emptyList<Group>())
    private val setPointsList = mutableStateOf(emptyList<SetPoint>())
    private val sensorsList = mutableStateOf(emptyList<Sensor>())
    private val outputsList = mutableStateOf(emptyList<Output>())
    var outputList = mutableListOf<Output>()

    private fun getGroup() {
        lifecycleScope.launch {
            groupsList.value = GroupService().getAll()
        }
    }

    private fun getOutputByID(selectedGroupId: Int) {
        lifecycleScope.launch {
            //outputController.getByGroupID(selectedGroupId)
            outputsList.value = OutputService().getByGroupId(selectedGroupId)
        }
    }

    private fun getByGroupId(selectedGroupId: Int) {
        lifecycleScope.launch {
            setPointsList.value = SetPointService().getByGroupId(selectedGroupId)
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

    private fun updateGroup(group: Group) {
        lifecycleScope.launch {
            val updatedGroup = GroupService().update(group)
        }
    }

    private var nextId: Int? = null
    private fun getNextGroupId() {
        lifecycleScope.launch {
            nextId = GroupService().getNextId()
        }
    }

    private fun updateOutput(output:Output) {
        lifecycleScope.launch {
            var output = OutputService().update(output)
        }
    }

    private fun updateSetPoint(setPoint:SetPoint) {
        lifecycleScope.launch {
            var setPoint = SetPointService().update(setPoint)
        }
    }

    private var createdSetPoint: SetPoint? = null
    private fun createSetPoint(value: Any, groupId: Int, sensorId: Int) {
        lifecycleScope.launch {
            createdSetPoint = SetPointService().create(value, groupId, sensorId)
        }
    }

    private fun deleteSetPoint(id: Int) {
        lifecycleScope.launch {
            SetPointService().delete(id)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val groupUpdate = intent.extras?.getParcelable<Group>("groupAux")
        Log.i("Teste", groupUpdate.toString())
        if (groupUpdate != null) {
            getByGroupId(groupUpdate.groupId)
        }
        if (groupUpdate != null) {
            getOutputByID(groupUpdate.groupId)
        }
        getGroup()
        getSensor()
        getOutputs()
        getNextGroupId()
        setContent {
            UpdateGroupScreen(groupUpdate)
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun UpdateGroupScreen(groupUpdate: Group?) {
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
                        ElementsUpdateGroup(groupUpdate)
                    }
                }
            }
        }
    }

    @Composable
    fun ElementsUpdateGroup(groupUpdate: Group?) {
        val textValueNameGroup = remember { groupUpdate?.let { mutableStateOf(it.groupName) } }
        val textField1Value = remember { mutableStateOf("") }
        val textField2Value = remember { mutableStateOf("") }

        var selectedSensorId by remember { mutableStateOf<Int?>(null) }
        var selectedOutputId by remember { mutableStateOf<Int?>(null) }

        //val outputElements = remember { mutableStateListOf<OutputElement>() }

        var isSwitchOn by remember { mutableStateOf(false) }

        var quantity: Float = 0.0F
        val setPointList = remember { mutableStateListOf<ItemCreate>() }
        val dropdownOutputList = remember { mutableStateListOf<ItemOutput>() }

        setPointsList.value.forEach{ setPoint ->
            if(setPoint.sensorId == 3){
                setPointList.add(ItemCreate(setPoint.sensorId , "Movimento" , setPoint.valueBoolean.toString()))
            }
            if(setPoint.sensorId == 2){
                setPointList.add(ItemCreate(setPoint.sensorId , "Umidade" , setPoint.valueFloat.toString()))
            }
            if(setPoint.sensorId == 1){
                setPointList.add(ItemCreate(setPoint.sensorId , "Temperatura" , setPoint.valueFloat.toString()))
            }
        }

        outputsList.value.forEach{output ->
            dropdownOutputList.add(ItemOutput(output.outputId , output.outputName))
        }



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
            item{
                if (textValueNameGroup != null) {
                    androidx.compose.material.OutlinedTextField(
                        value = textValueNameGroup.value,
                        onValueChange = { textValueNameGroup.value = it },
                        label = { Text("Nome", color = Color.White) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = customTextFieldColors
                    )
                }
            }
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
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = "Saída: ${output.outputName}",
                                color = Color.Black,
                                fontSize = 16.sp
                            )

                            IconButton(
                                onClick = {
                                    dropdownOutputList.remove(output)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Excluir",
                                    tint = Color.Red
                                )
                            }
                        }
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
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(top = 10.dp),
                            text = "Sensor: ${setPoint.sensorName}, Valor: ${setPoint.value}",
                            color = Color.Black,
                            fontSize = 16.sp
                        )

                        IconButton(
                            onClick = {
                                setPointList.remove(setPoint)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Excluir",
                                tint = Color.Red
                            )
                        }
                    }
                }
            }

            item{
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ){
                    androidx.compose.material.Button(
                        onClick = {
                            if (textValueNameGroup != null) {
                                if (groupUpdate != null) {
                                    Log.i("nometeste", groupUpdate.groupId.toString() )
                                    val attGroup  = Group(
                                        groupUpdate.groupId ,
                                        textValueNameGroup.value,
                                        groupUpdate.isActive
                                    )
                                    updateGroup(attGroup)
                                }
                            }


                            /*setPointList.forEach { item ->
                                val matchingSetPoint = setPointsList.value.find { it.sensorId == item.sensorId }

                                if (matchingSetPoint != null) {
                                    // SetPoint já existe, então atualize-o apenas se o valor for diferente
                                    if (item.sensorId == 3 && item.value.toBoolean() != matchingSetPoint.valueBoolean) {
                                        val updatedSetPoint = SetPoint(
                                            matchingSetPoint.setPointId,
                                            null,
                                            item.value.toBoolean(),
                                            matchingSetPoint.groupId,
                                            item.sensorId
                                        )
                                        updateSetPoint(updatedSetPoint)
                                    } else if (item.sensorId != 3 && item.value.toFloat() != matchingSetPoint.valueFloat) {
                                        val updatedSetPoint = SetPoint(
                                            matchingSetPoint.setPointId,
                                            item.value.toFloat(),
                                            null,
                                            matchingSetPoint.groupId,
                                            item.sensorId
                                        )
                                        updateSetPoint(updatedSetPoint)
                                    }
                                } else {
                                    // SetPoint não existe, então crie um novo
                                    if (item.sensorId == 3) {
                                        createSetPoint(item.value.toBoolean(), nextId ?: 0, item.sensorId)
                                    } else {
                                        createSetPoint(item.value.toFloat(), nextId ?: 0, item.sensorId)
                                    }
                                }
                            }

                            setPointsList.value.forEach { existingSetPoint ->
                                val matchingItem = setPointList.find { it.sensorId == existingSetPoint.sensorId }
                                if (matchingItem == null) {
                                    // O SetPoint não está presente em setPointList, então exclua-o
                                    deleteSetPoint(existingSetPoint.setPointId)
                                }
                            }*/

                            dropdownOutputList.forEach{ output ->
                                groupUpdate?.let {
                                    Output(output.outputId ,output.outputName , "url" + output.outputId , false ,
                                        it.groupId
                                    )
                                }?.let { updateOutput(it) }
                            }
                            // Limpar os campos de texto
                            textField1Value.value = ""
                            textField2Value.value = ""
                            if (textValueNameGroup != null) {
                                textValueNameGroup.value = ""
                            }
                            context.startActivity(Intent(context, MainActivity::class.java))

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
                    androidx.compose.material.Button(
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
