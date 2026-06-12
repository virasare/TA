package com.dicoding.tugas_akhir.ui.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.R
import com.dicoding.tugas_akhir.ui.components.dialog.buttons.PrimaryButton
import com.dicoding.tugas_akhir.ui.components.dialog.buttons.SecondaryButton
import com.dicoding.tugas_akhir.ui.theme.Background
import com.dicoding.tugas_akhir.ui.theme.Neutral200
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Neutral700
import com.dicoding.tugas_akhir.ui.theme.Primary2

private data class OnboardingItem(
    val title: String,
    val description: String,
    val imageRes: Int
)

@Composable
fun OnboardingScreen(
    onFinishClick: () -> Unit
) {
    val items = listOf(
        OnboardingItem(
            title = "Cari Jadwal Kapal",
            description = "Temukan jadwal kapal berdasarkan rute keberangkatan dan tujuan.",
            imageRes = R.drawable.onboarding1
        ),
        OnboardingItem(
            title = "Pesan dan Bayar Tiket",
            description = "Pilih jadwal, isi data penumpang, dan lakukan pembayaran dengan metode yang tersedia.",
            imageRes = R.drawable.onboarding2
        ),
        OnboardingItem(
            title = "Pantau Status Tiket",
            description = "Lihat tiket, status pembayaran, serta pengajuan refund dan reschedule dengan lebih jelas.",
            imageRes = R.drawable.onboarding3
        )
    )

    var currentPage by remember {
        mutableIntStateOf(0)
    }

    val item = items[currentPage]
    val isFirstPage = currentPage == 0
    val isLastPage = currentPage == items.lastIndex

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(24.dp)
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = item.title,
            color = Neutral700,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )

        Image(
            painter = painterResource(id = item.imageRes),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 36.dp, bottom = 28.dp)
                .size(220.dp)
        )

        Text(
            text = item.description,
            color = Neutral500,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        Row(
            modifier = Modifier.padding(top = 36.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items.forEachIndexed { index, _ ->
                Surface(
                    modifier = Modifier.size(width = 36.dp, height = 4.dp),
                    shape = RoundedCornerShape(50.dp),
                    color = if (index <= currentPage) Primary2 else Neutral200,
                    content = {}
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (isFirstPage) {
                SecondaryButton(
                    text = "Lewati",
                    onClick = onFinishClick,
                    modifier = Modifier.weight(1f)
                )
            } else {
                SecondaryButton(
                    text = "Kembali",
                    onClick = {
                        currentPage--
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            PrimaryButton(
                text = if (isLastPage) "Mulai" else "Lanjut",
                onClick = {
                    if (isLastPage) {
                        onFinishClick()
                    } else {
                        currentPage++
                    }
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}