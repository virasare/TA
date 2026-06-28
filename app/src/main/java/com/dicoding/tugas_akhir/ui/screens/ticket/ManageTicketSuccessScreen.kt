package com.dicoding.tugas_akhir.ui.screens.ticket

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.ui.components.dialog.buttons.PrimaryButton
import com.dicoding.tugas_akhir.ui.components.ticket.ManageTicketSuccessCard

@Composable
fun ManageTicketSuccessScreen(
    title: String,
    description: String,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        ManageTicketSuccessCard(
            title = title,
            description = description,
        )

        PrimaryButton(
            text = "Lanjut",
            onClick = onContinueClick,
            modifier = Modifier
                .padding(top = 16.dp)
                .navigationBarsPadding(),
        )
    }
}
