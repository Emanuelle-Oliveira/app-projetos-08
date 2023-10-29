package com.example.appprojetos08.controllers.output

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.appprojetos08.models.output.Output
import com.example.appprojetos08.services.output.OutputService
import com.example.appprojetos08.services.sensor.SensorService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class OutputController {

    var outputList by mutableStateOf(listOf<Output>())
    var outputListByGroupId by mutableStateOf(listOf<Output>())
    lateinit var updatedOutput: Output

    init {
        getOutputs()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getOutputs() {
        GlobalScope.launch {
            outputList = OutputService().getAll()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getOutputsByGroupId(id: Int) {
        GlobalScope.launch {
            outputListByGroupId = OutputService().getByGroupId(id)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun updateOutput(output: Output) {
        GlobalScope.launch {
            updatedOutput = OutputService().update(output)
        }
    }
}