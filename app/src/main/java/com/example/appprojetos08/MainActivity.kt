package com.example.appprojetos08

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.appprojetos08.models.output.Output
import com.example.appprojetos08.models.sensor.Sensor
import com.example.appprojetos08.services.output.OutputService
import com.example.appprojetos08.ui.theme.AppProjetos08Theme
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {
  /*var outputList = mutableListOf<Output>()

  private fun getOutputs() {
    lifecycleScope.launch {
      outputList = OutputService().getAll()
    }
  }

  private fun getOutputsByGroupId(id: Int) {
    lifecycleScope.launch {
      outputList = OutputService().getByGroupId(id)
    }
  }*/

   /*
   TESTES DAS FUNÇÔES PARA EXEMPLO

   OUTPUT:
   - get all
   lifecycleScope.launch {
     val outputList = OutputService().getAll()
     Log.d("Log", "Lista de saídas: $outputList")
   }
   - getByGroupId
   lifecycleScope.launch {
     val outputList = OutputService().getByGroupId(0)
     Log.d("Log", "Lista de saídas: $outputList")
   }
   */

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    //getOutputs()
    //getOutputsByGroupId(1)

    setContent {
      AppProjetos08Theme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          Greeting("Android")
        }
      }
    }
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(
    text = "Hello $name!",
    modifier = modifier
  )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  AppProjetos08Theme {
    Greeting("Android")
  }
}