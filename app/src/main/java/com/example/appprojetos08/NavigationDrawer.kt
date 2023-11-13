package com.example.appprojetos08

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch
import androidx.compose.ui.tooling.preview.Preview
import com.example.appprojetos08.ui.theme.AppProjetos08Theme
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.appprojetos08.controllers.GroupController
import com.example.appprojetos08.models.group.Group
import com.example.appprojetos08.services.group.GroupService

class NavigationDrawer : ComponentActivity() {
    private val groupController = GroupController()

    private val groupsList = mutableStateOf(emptyList<Group>())

    private fun getGroup() {
        lifecycleScope.launch {
            groupsList.value = GroupService().getAll()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getGroup()
        setContent {
            NavigationDrawerFun(groupsList)
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawerFun (groupsList: State<List<Group>>){

    val addGroupItem = NavigationItem(
        title = "Adicionar Grupo",
        id = 100,
        selectedIcon = Icons.Default.Add, // Ícone para adicionar
        unselectedIcon = Icons.Default.Add, // Ícone para adicionar
    )

    val clearFilters = NavigationItem(
        title = "Limpar Filtros",
        id = 101,
        selectedIcon = Icons.Default.Clear, // Ícone para adicionar
        unselectedIcon = Icons.Default.Clear, // Ícone para adicionar
    )

    val items = groupsList.value.map { group ->
        NavigationItem(
            title = group.groupName, // Assuming groupName is the property you want to use
            id = group.groupId,
            selectedIcon = Icons.Filled.Star, // Replace with appropriate icons
            unselectedIcon = Icons.Outlined.Star, // Replace with appropriate icons
        )
    }

    // Adicione o item "Adicionar Grupo" à lista de items
    val allItems = items + addGroupItem + clearFilters

    Surface(
        modifier = Modifier.fillMaxSize()
                    .background(Color.DarkGray),
        color = Color.DarkGray
    ) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        var selectedItemIndex by rememberSaveable {
            mutableStateOf(0)
        }
        ModalNavigationDrawer(
            modifier = Modifier.background(Color.DarkGray),
            drawerContent = {
                ModalDrawerSheet {
                    Spacer(modifier = Modifier.height(64.dp)
                        .background(Color.DarkGray))
                    allItems.forEachIndexed { index, item ->
                        NavigationDrawerItem(
                            label = {
                                Text(text = item.title, style = TextStyle(fontSize = 20.sp))
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
                                    Text(text = item.badgeCount.toString())
                                }
                            },
                            modifier = Modifier
                                .padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            },
            drawerState = drawerState
        ) {
            TopAppBar(
                title = {
                    Text(text = "Smart House", color = Color.DarkGray, style = TextStyle(fontSize = 20.sp))
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
        }
    }
}