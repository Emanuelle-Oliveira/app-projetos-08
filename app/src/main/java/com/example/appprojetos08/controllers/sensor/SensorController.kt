package com.example.appprojetos08.controllers.sensor

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.appprojetos08.models.sensor.Sensor
import com.example.appprojetos08.services.sensor.SensorService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SensorController {

    var sensorList by mutableStateOf(listOf<Sensor>())

    init {
        getSensors()
    }

    private fun getSensors() {
        GlobalScope.launch {
            sensorList = SensorService().getAll()
        }
    }
}
