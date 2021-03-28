package com.gpeal.tesla

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.tooling.preview.Preview
import com.gpeal.tesla.ui.theme.TeslaTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun TeslaScaffold(
    content: @Composable () -> Unit,
) {
    TeslaTheme {
        Surface(
            modifier = Modifier
                .drawBehind {

                }
        ) {
            content()
        }
    }
}