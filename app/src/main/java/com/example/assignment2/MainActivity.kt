package com.example.assignment2


import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.annotation.SuppressLint
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.assignment2.ui.theme.Assignment2Theme
import java.util.jar.Manifest

class MainActivity : ComponentActivity() {

    private val myBroadcastReceiver = MyBroadcastReceiver()

    private val notificationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
        setContent {
            Assignment2Theme {
                MainScreen()
            }
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(MyBroadcastReceiver.ACTION_MY_BROADCAST)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(myBroadcastReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(myBroadcastReceiver, filter)
        }
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(myBroadcastReceiver)
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

@Composable
fun startServiceButton(){
    val context = LocalContext.current
    Button(onClick = {
        val intent = Intent(context, MyService::class.java)
        ContextCompat.startForegroundService(context, intent)
    })
    {
        Text("Start Service")
    }
}

@Composable
fun bindServiceButton(){
    val context = LocalContext.current
    var grade by remember {mutableStateOf("")}

    val connection = remember {
        object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as MyService.MyBinder
                grade = binder.getService().getMyGrade()
            }

            override fun onServiceDisconnected(name: ComponentName?){
                grade = ""
            }
        }
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Button(onClick = {
            val intent = Intent(context, MyService::class.java)
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }) {
            Text("Bind Service")
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(grade)
    }
}

@Composable
fun sendBroadcastButton(){
    val context = LocalContext.current
    Button(onClick = {
        val intent = Intent(MyBroadcastReceiver.ACTION_MY_BROADCAST)
        intent.setPackage(context.packageName)
        context.sendBroadcast(intent)
    }) {
        Text("Send Broadcast")
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally , verticalArrangement=Arrangement.Center){
        Greeting("Jacob")
        Spacer(modifier = Modifier.height(32.dp))
        explicitButton()
        Spacer(modifier = Modifier.height(32.dp))
        implicitButton()
        Spacer(modifier = Modifier.height(32.dp))
        startServiceButton()
        Spacer(modifier = Modifier.height(32.dp))
        bindServiceButton()
        Spacer(modifier = Modifier.height(32.dp))
        sendBroadcastButton()
    }
}
