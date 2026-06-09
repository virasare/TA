package com.dicoding.tugas_akhir.ui.components.navigation

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.dicoding.tugas_akhir.R
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Primary2
import com.dicoding.tugas_akhir.ui.theme.Primary3
import com.dicoding.tugas_akhir.ui.theme.White
import androidx.compose.ui.tooling.preview.Preview

data class BottomNavItem(
    val route: String,
    val label: String,
    @DrawableRes val icon: Int
)

val bottomNavItems = listOf(
    BottomNavItem(
        route = "home",
        label = "Beranda",
        icon = R.drawable.ic_beranda
    ),
    BottomNavItem(
        route = "schedule",
        label = "Jadwal",
        icon = R.drawable.ic_tiket
    ),
    BottomNavItem(
        route = "orders",
        label = "Pesanan",
        icon = R.drawable.ic_riwayat
    ),
    BottomNavItem(
        route = "notifications",
        label = "Notifikasi",
        icon = R.drawable.ic_notifikasi
    ),
    BottomNavItem(
        route = "profile",
        label = "Profil",
        icon = R.drawable.ic_person
    )
)

@Composable
fun AppBottomNavigationBar(
    currentRoute: String,
    onItemClick: (BottomNavItem) -> Unit
) {
    NavigationBar(
        containerColor = White
    ) {
        bottomNavItems.forEach { item ->
            val selected = currentRoute == item.route

            NavigationBarItem(
                selected = selected,
                onClick = {
                    onItemClick(item)
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Primary2,
                    selectedTextColor = Primary2,
                    indicatorColor = Primary3,
                    unselectedIconColor = Neutral500,
                    unselectedTextColor = Neutral500
                )
            )
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 360
)
@Composable
fun AppBottomNavigationBarPreview() {
    AppBottomNavigationBar(
        currentRoute = "home",
        onItemClick = {}
    )
}