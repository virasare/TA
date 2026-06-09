package com.dicoding.tugas_akhir

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.ui.components.cards.ShipScheduleCard
import com.dicoding.tugas_akhir.ui.components.cards.ShipScheduleStatus
import com.dicoding.tugas_akhir.ui.components.feedback.InfoBox
import com.dicoding.tugas_akhir.ui.components.feedback.InfoBoxVariant
import com.dicoding.tugas_akhir.ui.components.navigation.AppBottomNavigationBar
import com.dicoding.tugas_akhir.ui.components.navigation.AppTopBar
import com.dicoding.tugas_akhir.ui.theme.Tugas_AkhirTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tugas_AkhirTheme {
                Scaffold(
                    topBar = {
                        AppTopBar(title = "Beranda")
                    },
                    bottomBar = {
                        AppBottomNavigationBar(
                            currentRoute = "home",
                            onItemClick = {}
                        )
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        InfoBox(
                            title = "Informasi",
                            description = "Cari jadwal kapal sesuai pelabuhan asal, tujuan, dan tanggal keberangkatan.",
                            variant = InfoBoxVariant.Info
                        )

                        ShipScheduleCard(
                            shipName = "KM Nusa Bahari",
                            route = "Ende → Surabaya",
                            departureDate = "12 Jul 2026",
                            departureTime = "18.00 WITA",
                            arrivalTime = "08.00 WIB",
                            duration = "±38 jam",
                            price = "Rp350.000",
                            quota = "24 kursi",
                            status = ShipScheduleStatus.Available,
                            onClick = {}
                        )
                    }
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
    Tugas_AkhirTheme {
        Greeting("Android")
    }
}