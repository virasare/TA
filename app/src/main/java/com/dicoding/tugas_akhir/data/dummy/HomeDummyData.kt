package com.dicoding.tugas_akhir.data.dummy

data class Port(
    val name: String,
    val city: String
)

data class PopularRoute(
    val route: String,
    val price: String,
    val date: String
)

val dummyPorts = listOf(
    Port(
        name = "Pelabuhan Ende",
        city = "Ende"
    ),
    Port(
        name = "Pelabuhan Tenau Kupang",
        city = "Kupang"
    ),
    Port(
        name = "Pelabuhan Labuan Bajo",
        city = "Labuan Bajo"
    ),
    Port(
        name = "Pelabuhan Benoa",
        city = "Bali"
    ),
    Port(
        name = "Pelabuhan Tanjung Perak",
        city = "Surabaya"
    )
)

val popularRoutes = listOf(
    PopularRoute(
        route = "Ende → Surabaya",
        price = "Mulai dari Rp350.000",
        date = "Jadwal terdekat: 12 Jun"
    ),
    PopularRoute(
        route = "Ende → Denpasar",
        price = "Mulai dari Rp420.000",
        date = "Jadwal terdekat: 15 Jun"
    ),
    PopularRoute(
        route = "Kupang → Surabaya",
        price = "Mulai dari Rp500.000",
        date = "Jadwal terdekat: 18 Jun"
    )
)