package com.dicoding.tugas_akhir.ui.components.dialog.navigation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.dicoding.tugas_akhir.R
import com.dicoding.tugas_akhir.ui.theme.Neutral500
import com.dicoding.tugas_akhir.ui.theme.Primary2
import com.dicoding.tugas_akhir.ui.theme.Primary3
import com.dicoding.tugas_akhir.ui.theme.White
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dicoding.tugas_akhir.ui.navigation.Screens

data class BottomNavItem(
    val route: String,
    val label: String,
    @DrawableRes val icon: Int
)

val bottomNavItems = listOf(
    BottomNavItem(
        route = Screens.Home,
        label = "Beranda",
        icon = R.drawable.ic_beranda
    ),
    BottomNavItem(
        route = Screens.Schedule,
        label = "Jadwal",
        icon = R.drawable.ic_tiket
    ),
    BottomNavItem(
        route = Screens.MyTicket,
        label = "Pesanan",
        icon = R.drawable.ic_riwayat
    ),
    BottomNavItem(
        route = Screens.Notification,
        label = "Notifikasi",
        icon = R.drawable.ic_notifikasi
    ),
    BottomNavItem(
        route = Screens.Profile,
        label = "Profil",
        icon = R.drawable.ic_person
    )
)

@Composable
fun AppBottomNavigationBar(
    currentRoute: String,
    onItemClick: (BottomNavItem) -> Unit,
    unreadNotificationCount: Int = 0,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp)
    ) {
        NavigationBar(
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            bottomNavItems.forEach { item ->
                val selected = currentRoute == item.route

                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        onItemClick(item)
                    },
                    icon = {
                        val showBadge = item.route == "notification" && unreadNotificationCount > 0

                        if (showBadge) {
                            BadgedBox(
                                badge = {
                                    Badge(
                                        containerColor = Color(0xFFD32F2F)
                                    )
                                }
                            ) {
                                Icon(
                                    painter = painterResource(id = item.icon),
                                    contentDescription = item.label,
                                )
                            }
                        } else {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = item.label,
                            )
                        }
                    },
                    label = {
                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.labelMedium
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
}

@Preview(
    showBackground = true,
    widthDp = 360
)
@Composable
fun AppBottomNavigationBarPreview() {
    AppBottomNavigationBar(
        currentRoute = Screens.Home,
        onItemClick = {}
    )
}