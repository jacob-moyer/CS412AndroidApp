package com.example.assignment2

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontVariation.width
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding

class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent{
            SecondScreen()
        }
    }

}

@Preview(showBackground=true)
@Composable
fun SecondScreen() {
    Column(Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
        )
    {
        Challenges()
        Spacer(modifier= Modifier.height(100.dp))
        MainButton()
    }
}


@Composable
fun Challenges(){
    Column {
        Text("Challenges:")
        Text("1) Support for older client versions must be maintained")
        Text("2) Mobile devices come in different sizes/hardware")
        Text("3) Need to have access to features/data even in unstable environments")
        Text("4) Developers need to develop stable and reliable apps to keep users")
        Text("5) OS versions are constantly being updated, meaning developers need to keep up with changes")
    }
}

@Composable
fun MainButton(){
    val context = LocalContext.current
    Button(onClick={
        val intent= Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }) {
        Text("Back to Main Activity")
    }

}