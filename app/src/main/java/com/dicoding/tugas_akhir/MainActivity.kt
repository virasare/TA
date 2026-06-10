package com.dicoding.tugas_akhir

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.dicoding.tugas_akhir.ui.navigation.AppNavigation
import com.dicoding.tugas_akhir.ui.theme.Tugas_AkhirTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tugas_AkhirTheme {
                AppNavigation()
            }
        }
    }
}

@Preview(
    name = "Main App Preview",
    showBackground = true,
    showSystemUi = true,
    widthDp = 412,
    heightDp = 917
)
@Composable
fun MainAppPreview() {
    Tugas_AkhirTheme {
        AppNavigation()
    }
}