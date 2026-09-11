package com.example.assignment2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.assignment2.ui.theme.Assignment2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assignment2Theme {
                MainScreen()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "My name is Jacob Moyer" +
                "\n My student id is 1325669",
        modifier = modifier
    )
}

@Composable
fun GreetingPreview() {
    Assignment2Theme {
        Greeting("Android")
    }
}

@Composable
fun explicitButton(){
    val context= LocalContext.current
    Button(onClick = {
        val intent = Intent(context, SecondActivity::class.java)
        context.startActivity(intent)
    }) {
        Text("Explicit")
    }
}

@Composable
fun implicitButton(){
    val context= LocalContext.current
    Button(onClick = {
        val intent = Intent("com.example.assignment2.SECOND_ACTIVITY")
        intent.setPackage(context.packageName)
        context.startActivity(intent)
    }){
        Text("Implicit")
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally , verticalArrangement=Arrangement.Center){
        Greeting("Jacob")
        Spacer(modifier = Modifier.height(32.dp))
        explicitButton()
        Spacer(modifier = Modifier.height(32.dp))
        implicitButton()
    }
}
