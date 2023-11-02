package com.example.appprojetos08

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val title: String,
    val id : Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null
)