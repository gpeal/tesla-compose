package com.gpeal.tesla

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradient
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.tooling.preview.Preview
import com.gpeal.tesla.ui.theme.TeslaTheme
import com.gpeal.tesla.ui.theme.scaffoldEndColor
import com.gpeal.tesla.ui.theme.scaffoldStartColor

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
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            content()
        }
    }
}