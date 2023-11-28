package com.example.appprojetos08.ui.activities.output

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.example.appprojetos08.ui.activities.group.ItemCreate
import kotlinx.coroutines.launch

class UpdateOutputActivity : ComponentActivity(){
    private val outputController = OutputController()

    private val groupsList = mutableStateOf(emptyList<Group>())
    private val setPointsList = mutableStateOf(emptyList<SetPoint>())
    private val sensorsList = mutableStateOf(emptyList<Sensor>())
    private val outputsList = mutableStateOf(emptyList<Output>())
    var outputList = mutableListOf<Output>()
    var groupList = mutableListOf<Group>()

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

    private fun updateOutput(output: Output) {
        lifecycleScope.launch {
            var output = OutputService().update(output)
        }
    }

    private fun updateSetPoint(setPoint: SetPoint) {
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
        val outputUpdate = intent.extras?.getParcelable<Output>("outputAux")
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
            UpdateOutputScreen(groupUpdate,outputUpdate)
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun UpdateOutputScreen(groupUpdate: Group?, outputUpdate: Output?) {
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
            //var selectedGroupId by remember { mutableStateOf<Int?>(null) }

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
                    }
                ) {
                    // Fundo preto da tela
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        color = Color.DarkGray,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        ElementsUpdateOutput(outputUpdate)
                    }
                }
            }
        }
    }
    @Composable
    fun ElementsUpdateOutput(outputUpdate: Output?) {
        val textFieldNameOutputValue = remember { mutableStateOf(outputUpdate?.outputName ?: "") }

        // Função para customizar a cor do text field
        val customTextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.White,
            disabledBorderColor = Color.Magenta,
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
                OutlinedTextField(
                    value = textFieldNameOutputValue.value,
                    onValueChange = { textFieldNameOutputValue.value = it },
                    label = { Text("Nome da Saida", color = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = customTextFieldColors
                )
            }
            item {
                Button(
                    onClick = {
                        outputUpdate?.let {
                            val updatedOutput = Output(
                                it.outputId,
                                textFieldNameOutputValue.value,
                                it.databaseUrl,
                                it.isActive,
                                it.isManual,
                                it.groupId
                            )
                            updateOutput(updatedOutput)
                        }
                        context.startActivity(Intent(context, MainActivity::class.java))
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        backgroundColor = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(16.dp)
                ) {
                    Text(text = "Salvar", color = Color.White)
                }
            }
            item {
                Button(
                    onClick = {
                        finish() // or handle navigation as needed
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        backgroundColor = Color.Black
                    ),
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