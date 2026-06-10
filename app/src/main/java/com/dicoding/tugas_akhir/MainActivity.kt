package com.dicoding.tugas_akhir

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.dicoding.tugas_akhir.ui.navigation.AppNavigation
import com.dicoding.tugas_akhir.ui.theme.Background
import com.dicoding.tugas_akhir.ui.theme.Tugas_AkhirTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Tugas_AkhirTheme {
                Surface(
                    color = Background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Preview(
    name = "Main App Preview",
    showBackground = true,
    showSystemUi = true,
    widthDp = 380,
    heightDp = 800
)
@Composable
fun MainAppPreview() {
    Tugas_AkhirTheme {
        AppNavigation()
    }
}