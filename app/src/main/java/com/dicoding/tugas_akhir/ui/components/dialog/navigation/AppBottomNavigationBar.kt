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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.dicoding.tugas_akhir.R
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dicoding.tugas_akhir.ui.localization.AppStrings
import com.dicoding.tugas_akhir.ui.localization.LocalAppStrings
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
        label = "Tiket Saya",
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
    val colors = MaterialTheme.colorScheme
    val strings = LocalAppStrings.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surface)
            .padding(horizontal = 16.dp),
    ) {
        NavigationBar(
            modifier = Modifier.fillMaxWidth(),
            containerColor = colors.surface,
        ) {
            bottomNavItems.forEach { item ->
                val selected = currentRoute == item.route
                val label = item.localizedLabel(strings)

                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        onItemClick(item)
                    },
                    icon = {
                        val showBadge = item.route == Screens.Notification &&
                                unreadNotificationCount > 0

                        if (showBadge) {
                            BadgedBox(
                                badge = {
                                    Badge(
                                        containerColor = colors.error,
                                    )
                                },
                            ) {
                                Icon(
                                    painter = painterResource(id = item.icon),
                                    contentDescription = label,
                                )
                            }
                        } else {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = label,
                            )
                        }
                    },
                    label = {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            softWrap = false,
                            textAlign = TextAlign.Center,
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colors.primary,
                        selectedTextColor = colors.primary,
                        indicatorColor = colors.primaryContainer,
                        unselectedIconColor = colors.onSurfaceVariant,
                        unselectedTextColor = colors.onSurfaceVariant,
                    ),
                )
            }
        }
    }
}

private fun BottomNavItem.localizedLabel(
    strings: AppStrings,
): String {
    return when (route) {
        Screens.Home -> strings.navHome
        Screens.Schedule -> strings.navSchedule
        Screens.MyTicket -> strings.navMyTicket
        Screens.Notification -> strings.navNotification
        Screens.Profile -> strings.navProfile
        else -> label
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
