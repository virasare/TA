package com.dicoding.tugas_akhir.data.dummy

data class Port(
    val name: String,
    val city: String
)

data class PopularRoute(
    val id: Int,
    val originCity: String,
    val destinationCity: String,
    val route: String,
    val price: String,
    val date: String,
    val popularityScore: Int
)

val dummyPorts = listOf(
    Port("Pelabuhan Tanjung Perak", "Surabaya"),
    Port("Pelabuhan Benoa", "Denpasar"),
    Port("Pelabuhan Gilimanuk", "Jembrana"),
    Port("Pelabuhan Ketapang", "Banyuwangi"),
    Port("Pelabuhan Makassar", "Makassar"),
    Port("Pelabuhan Soekarno-Hatta", "Makassar"),
    Port("Pelabuhan Tenau", "Kupang"),
    Port("Pelabuhan Ende", "Ende"),
    Port("Pelabuhan Maumere", "Maumere"),
    Port("Pelabuhan Labuan Bajo", "Labuan Bajo"),
    Port("Pelabuhan Lembar", "Lombok Barat"),
    Port("Pelabuhan Kayangan", "Lombok Timur"),
    Port("Pelabuhan Balikpapan", "Balikpapan"),
    Port("Pelabuhan Samarinda", "Samarinda"),
    Port("Pelabuhan Bitung", "Bitung"),
    Port("Pelabuhan Ambon", "Ambon"),
    Port("Pelabuhan Sorong", "Sorong"),
    Port("Pelabuhan Jayapura", "Jayapura"),
)

val popularRoutes = listOf(
    PopularRoute(
        id = 1,
        originCity = "Ende",
        destinationCity = "Surabaya",
        route = "Ende → Surabaya",
        price = "Mulai dari Rp350.000",
        date = "Jadwal terdekat: 12 Jun",
        popularityScore = 95
    ),
    PopularRoute(
        id = 2,
        originCity = "Ende",
        destinationCity = "Denpasar",
        route = "Ende → Denpasar",
        price = "Mulai dari Rp420.000",
        date = "Jadwal terdekat: 15 Jun",
        popularityScore = 88
    ),
    PopularRoute(
        id = 3,
        originCity = "Kupang",
        destinationCity = "Surabaya",
        route = "Kupang → Surabaya",
        price = "Mulai dari Rp500.000",
        date = "Jadwal terdekat: 18 Jun",
        popularityScore = 82
    )
)